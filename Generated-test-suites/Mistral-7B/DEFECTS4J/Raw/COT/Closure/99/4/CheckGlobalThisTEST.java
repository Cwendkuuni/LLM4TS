import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  CheckGlobalThis checkGlobalThis;
  AbstractCompiler compilerMock;

  @org.junit.Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
  }

  @org.junit.Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newFunctionNode();
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructorFunction() {
    Node functionNode = Node.newFunctionNode();
    functionNode.setIsConstructor(true);
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode();
    Node jsDocNode = Node.newNameNode("@this");
    functionNode.setJSDocInfo(jsDocNode);
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionOutsideBlockContext() {
    Node functionNode = Node.newFunctionNode();
    Node parentNode = Node.newNameNode("function");
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @org.junit.Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssignNode();
    Node lhsNode = Node.newNameNode("a");
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, rhsNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseLeftSideOfAssignmentWithPrototypeProperty() {
    Node assignNode = Node.newAssignNode();
    Node lhsNode = Node.newGetPropNode(Node.newNameNode("a"), Node.newNameNode("prototype"));
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, rhsNode, parentNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseLeftSideOfAssignmentWithQualifiedPrototypeProperty() {
    Node assignNode = Node.newAssignNode();
    Node lhsNode = Node.newGetPropNode(Node.newNameNode("a"), Node.newNameNode("prototype.x"));
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, rhsNode, parentNode));
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssignNode();
    Node lhsNode = Node.newNameNode("a");
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    checkGlobalThis.visit(traversal, rhsNode, parentNode);

    assertTrue(compilerMock.hasError());
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfGetProp() {
    Node assignNode = Node.newAssignNode();
    Node lhsNode = Node.newGetPropNode(Node.newNameNode("a"), Node.newNameNode("prototype"));
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    checkGlobalThis.visit(traversal, rhsNode, parentNode);

    assertTrue(compilerMock.hasError());
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Node.newAssignNode();
    Node lhsNode = Node.newNameNode("a");
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    checkGlobalThis.visit(traversal, lhsNode, parentNode);
    checkGlobalThis.visit(traversal, rhsNode, parentNode);

    assertFalse(compilerMock.hasError());
  }

  @org.junit.Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentWithAssignLhsChild() {
    Node assignNode = Node.newAssignNode();
    Node lhsNode = Node.newNameNode("a");
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    checkGlobalThis.visit(traversal, lhsNode, parentNode);
    checkGlobalThis.assignLhsChild = lhsNode;
    checkGlobalThis.visit(traversal, rhsNode, parentNode);

    assertFalse(compilerMock.hasError());
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfGetPropWithAssignLhsChild() {
    Node assignNode = Node.newAssignNode();
    Node assignLhsNode = Node.newNameNode("a");
    Node getPropNode = Node.newGetPropNode(assignLhsNode, Node.newNameNode("prototype"));
    Node rhsNode = Node.newNameNode("this");
    Node parentNode = Node.newBlockNode();
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compilerMock.getTraverser()).thenReturn(traversal);

    checkGlobalThis.visit(traversal, assignLhsNode, parentNode);
    checkGlobalThis.visit(traversal, getPropNode, parentNode);
    checkGlobalThis.assignLhsChild = assignLhsNode;
    checkGlobalThis.visit(traversal, rhsNode, parentNode);

    assertTrue(compilerMock.hasError());
  }
}