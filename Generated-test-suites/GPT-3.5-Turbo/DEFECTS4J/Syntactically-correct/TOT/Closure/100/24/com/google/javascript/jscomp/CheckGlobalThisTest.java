import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
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
  public void shouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createFunctionNodeWithConstructorAnnotation();
    Node parent = mock(Node.class);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(false, result);
  }
  
  @Test
  public void shouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createFunctionNodeWithThisAnnotation();
    Node parent = mock(Node.class);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(false, result);
  }
  
  @Test
  public void shouldTraverse_FunctionWithoutAnnotations_ReturnsTrue() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createFunctionNodeWithoutAnnotations();
    Node parent = mock(Node.class);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(true, result);
  }
  
  @Test
  public void shouldTraverse_LeftSideOfAssignment_ReturnsTrue() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createAssignmentLeftNode();
    Node parent = createAssignmentNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(true, result);
  }
  
  @Test
  public void shouldTraverse_RightSideOfAssignmentToPrototypeProperty_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createAssignmentRightNodeToPrototypeProperty();
    Node parent = createAssignmentNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(false, result);
  }
  
  @Test
  public void shouldTraverse_RightSideOfAssignmentToNestedProperty_ReturnsFalse() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createAssignmentRightNodeToNestedProperty();
    Node parent = createAssignmentNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(false, result);
  }
  
  @Test
  public void shouldTraverse_RightSideOfAssignmentNotToPrototypeProperty_ReturnsTrue() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createAssignmentRightNodeNotToPrototypeProperty();
    Node parent = createAssignmentNode();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertEquals(true, result);
  }
  
  @Test
  public void visit_NodeIsThisAndShouldReport_ReturnsReportedError() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createThisNode();
    Node parent = mock(Node.class);
    
    checkGlobalThis.visit(t, n, parent);
    
    verify(compiler).report(t.makeError(n, level, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void visit_NodeIsThisAndShouldNotReport_DoesNotReportError() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createThisNode();
    Node parent = mock(Node.class);
    checkGlobalThis.shouldReportThis(n, parent);
    
    checkGlobalThis.visit(t, n, parent);
    
    verify(compiler, never()).report(t.makeError(n, level, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void visit_NotLeftSideOfAssignment_ClearsAssignLhsChild() {
    NodeTraversal t = mock(NodeTraversal.class);
    Node n = createNode();
    Node parent = mock(Node.class);
    checkGlobalThis.assignLhsChild = createAssignmentLeftNode();
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(null, checkGlobalThis.assignLhsChild);
  }

  private Node createFunctionNodeWithConstructorAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(createJsDocInfoWithConstructorAnnotation());
    return functionNode;
  }
  
  private Node createFunctionNodeWithThisAnnotation() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(createJsDocInfoWithThisAnnotation());
    return functionNode;
  }
  
  private Node createFunctionNodeWithoutAnnotations() {
    Node functionNode = new Node(Token.FUNCTION);
    return functionNode;
  }
  
  private Node createAssignmentLeftNode() {
    Node assignmentLeftNode = createNode();
    assignmentLeftNode.setType(Token.THIS);
    return assignmentLeftNode;
  }
  
  private Node createAssignmentRightNodeToPrototypeProperty() {
    Node getPropNode = createNode();
    getPropNode.setType(Token.GETPROP);
    Node prototypeNode = createNode();
    prototypeNode.setString("prototype");
    getPropNode.addChildToBack(prototypeNode);
    return getPropNode;
  }
  
  private Node createAssignmentRightNodeToNestedProperty() {
    Node getPropNode = createNode();
    getPropNode.setType(Token.GETPROP);
    Node nestedNode = createNode();
    nestedNode.setString(".prototype.");
    getPropNode.addChildToBack(nestedNode);
    return getPropNode;
  }
  
  private Node createAssignmentRightNodeNotToPrototypeProperty() {
    Node assignmentRightNode = createNode();
    assignmentRightNode.setType(Token.THIS);
    return assignmentRightNode;
  }
  
  private Node createThisNode() {
    Node thisNode = createNode();
    thisNode.setType(Token.THIS);
    return thisNode;
  }
  
  private Node createAssignmentNode() {
    Node assignNode = createNode();
    assignNode.setType(Token.ASSIGN);
    assignNode.addChildToFront(createNode());
    assignNode.addChildToBack(createNode());
    return assignNode;
  }
  
  private Node createNode() {
    return new Node(Token.EMPTY);
  }

  private JSDocInfo createJsDocInfoWithConstructorAnnotation() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    when(jsDocInfo.isConstructor()).thenReturn(true);
    return jsDocInfo;
  }
  
  private JSDocInfo createJsDocInfoWithThisAnnotation() {
    JSDocInfo jsDocInfo = mock(JSDocInfo.class);
    when(jsDocInfo.hasThisType()).thenReturn(true);
    return jsDocInfo;
  }
}