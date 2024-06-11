import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldTraverse_functionWithConstructorAnnotation_returnFalse() {
    Node functionNode = createFunctionNode();
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, null);
    
    verify(jsDoc).isConstructor();
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_functionWithThisAnnotation_returnFalse() {
    Node functionNode = createFunctionNode();
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.hasThisType()).thenReturn(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    NodeTraversal traversal = mock(NodeTraversal.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, null);
    
    verify(jsDoc).hasThisType();
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_functionWithLogicalPlaceForThisAnnotation_returnTrue() {
    Node functionNode = createFunctionNode();

    NodeTraversal traversal = mock(NodeTraversal.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, functionNode, null);
    
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_assignmentLeftSide_returnTrue() {
    Node assignmentNode = createAssignmentNode();

    NodeTraversal traversal = mock(NodeTraversal.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, assignmentNode.getFirstChild(), assignmentNode);
    
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_assignmentRightSideToPrototype_returnFalse() {
    Node assignmentNode = createAssignmentNode();
    Node leftSide = assignmentNode.getFirstChild();
    Node rightSide = leftSide.getNext();
    Node prototypeNode = createPrototypeNode();

    leftSide.addChildToFront(prototypeNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, rightSide, assignmentNode);
    
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_assignmentRightSideToSubpropertyOfPrototype_returnFalse() {
    Node assignmentNode = createAssignmentNode();
    Node leftSide = assignmentNode.getFirstChild();
    Node rightSide = leftSide.getNext();
    Node prototypeNode = createPrototypeNode();
    Node subPropertyNode = createSubPropertyNode();

    leftSide.addChildToFront(prototypeNode);
    prototypeNode.addChildToBack(subPropertyNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, rightSide, assignmentNode);
    
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_otherAssignmentRightSide_returnTrue() {
    Node assignmentNode = createAssignmentNode();
    Node leftSide = assignmentNode.getFirstChild();
    Node rightSide = leftSide.getNext();
    Node otherNode = createOtherNode();

    rightSide.addChildToFront(otherNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    boolean result = checkGlobalThis.shouldTraverse(traversal, rightSide, assignmentNode);
    
    assertEquals(true, result);
  }

  @Test
  public void testVisit_thisNode_shouldReportGlobalThis() {
    Node thisNode = new Node(Token.THIS);
    NodeTraversal traversal = mock(NodeTraversal.class);
    
    checkGlobalThis.visit(traversal, thisNode, null);

    verify(compiler).report(eq(traversal.makeError(eq(thisNode), eq(level), eq(CheckGlobalThis.GLOBAL_THIS))));
  }

  @Test
  public void testVisit_assignmentLeftSideNode_resetAssignLhsChild() {
    Node assignmentNode = createAssignmentNode();
    Node leftSide = assignmentNode.getFirstChild();

    NodeTraversal traversal = mock(NodeTraversal.class);
    checkGlobalThis.visit(traversal, leftSide, assignmentNode);
    
    assertEquals(null, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_otherNode_doNothing() {
    Node otherNode = new Node(Token.NAME, "other");
    NodeTraversal traversal = mock(NodeTraversal.class);
    
    checkGlobalThis.visit(traversal, otherNode, null);

    verifyNoInteractions(compiler);
  }

  private Node createFunctionNode() {
    return new Node(Token.FUNCTION);
  }

  private Node createAssignmentNode() {
    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.NAME, "a");
    Node rightSide = new Node(Token.NAME, "this");

    assignNode.addChildToBack(leftSide);
    assignNode.addChildToBack(rightSide);

    return assignNode;
  }

  private Node createPrototypeNode() {
    Node prototypeNode = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    Node prototypePropertyNode = new Node(Token.STRING, "prototype");

    prototypeNode.addChildToBack(thisNode);
    prototypeNode.addChildToBack(prototypePropertyNode);

    return prototypeNode;
  }

  private Node createSubPropertyNode() {
    Node subPropertyNode = new Node(Token.GETPROP);
    Node prototypeNode = new Node(Token.NAME, "prototype");
    Node propertyNode = new Node(Token.STRING, "property");

    subPropertyNode.addChildToBack(prototypeNode);
    subPropertyNode.addChildToBack(propertyNode);

    return subPropertyNode;
  }

  private Node createOtherNode() {
    return new Node(Token.NAME, "other");
  }
}