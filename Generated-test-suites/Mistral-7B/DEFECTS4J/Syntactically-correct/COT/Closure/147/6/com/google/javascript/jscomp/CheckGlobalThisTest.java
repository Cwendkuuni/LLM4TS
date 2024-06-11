import static org.junit.Assert.assertFalse;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newFunction(null, null, null);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newFunction(null, null, null);
    JSDocInfo jsDocInfo = Node.newJSDocInfo("constructor");
    functionNode.setJSDocInfo(jsDocInfo);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunction(null, null, null);
    JSDocInfo jsDocInfo = Node.newJSDocInfo("this");
    functionNode.setJSDocInfo(jsDocInfo);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newFunction(null, null, null);
    JSDocInfo jsDocInfo = Node.newJSDocInfo("override");
    functionNode.setJSDocInfo(jsDocInfo);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionInNonGlobalContext() {
    Node functionNode = Node.newFunction(null, null, null);
    Node parentNode = Node.newName("localVariable");
    parentNode.setType(Token.NAME);
    parentNode.setJSDocInfo(Node.newJSDocInfo("localVariable"));
    functionNode.setParent(parentNode);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(null, null, null);
    Node leftSideNode = Node.newName("globalVariable");
    leftSideNode.setType(Token.NAME);
    leftSideNode.setJSDocInfo(Node.newJSDocInfo("globalVariable"));
    assignNode.setParent(Node.newBlock(null));
    assignNode.setLhs(leftSideNode);
    leftSideNode.setParent(assignNode);

    Node rightSideNode = Node.newName("rightSideVariable");
    rightSideNode.setType(Token.NAME);
    rightSideNode.setJSDocInfo(Node.newJSDocInfo("rightSideVariable"));
    assignNode.setRhs(rightSideNode);
    rightSideNode.setParent(assignNode);

    assertTrue(checkGlobalThis.shouldTraverse(traversal, null, assignNode));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssign(null, null, null);
    Node leftSideNode = Node.newGetProp(null, Node.newName("prototype"), null);
    leftSideNode.setType(Token.GETPROP);
    leftSideNode.setLastChild(Node.newName("propertyName"));
    assignNode.setParent(Node.newBlock(null));
    assignNode.setLhs(leftSideNode);
    leftSideNode.setParent(assignNode);

    Node rightSideNode = Node.newName("rightSideVariable");
    rightSideNode.setType(Token.NAME);
    rightSideNode.setJSDocInfo(Node.newJSDocInfo("rightSideVariable"));
    assignNode.setRhs(rightSideNode);
    rightSideNode.setParent(assignNode);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, null, assignNode));
  }

  @Test
  public void testShouldNotTraverseLeftSideOfAssignmentToPrototypeSubProperty() {
    Node assignNode = Node.newAssign(null, null, null);
    Node leftSideNode = Node.newGetProp(null, Node.newName("prototype"), null);
    leftSideNode.setType(Token.GETPROP);
    Node getPropNode = Node.newGetProp(null, Node.newName("propertyName"), null);
    leftSideNode.setLastChild(getPropNode);
    getPropNode.setType(Token.GETPROP);
    getPropNode.setLastChild(Node.newName("subPropertyName"));
    assignNode.setParent(Node.newBlock(null));
    assignNode.setLhs(leftSideNode);
    leftSideNode.setParent(assignNode);

    Node rightSideNode = Node.newName("rightSideVariable");
    rightSideNode.setType(Token.NAME);
    rightSideNode.setJSDocInfo(Node.newJSDocInfo("rightSideVariable"));
    assignNode.setRhs(rightSideNode);
    rightSideNode.setParent(assignNode);

    assertFalse(checkGlobalThis.shouldTraverse(traversal, null, assignNode));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(null, null, null);
    Node thisNode = Node.newThis();
    thisNode.setType(Token.THIS);
    assignNode.setParent(Node.newBlock(null));
    assignNode.setLhs(thisNode);
    thisNode.setParent(assignNode);

    ArgumentCaptor<NodeTraversal.Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(NodeTraversal.Diagnostic.class);

    checkGlobalThis.visit(traversal, null, assignNode);

    when(compiler.report(diagnosticCaptor.capture())).thenReturn(true);
    checkGlobalThis.visit(traversal, null, assignNode);

    assertTrue(diagnosticCaptor.getValue().getType().equals(DiagnosticType.WARNING));
    assertTrue(diagnosticCaptor.getValue().getMessage().contains("dangerous use of the global 'this' object"));
  }

  @Test
  public void testShouldNotReportThisKeywordOnRightSideOfAssignment() {
    Node assignNode = Node.newAssign(null, null, null);
    Node leftSideNode = Node.newName("leftSideVariable");
    leftSideNode.setType(Token.NAME);
    leftSideNode.setJSDocInfo(Node.newJSDocInfo("leftSideVariable"));
    assignNode.setParent(Node.newBlock(null));
    assignNode.setLhs(leftSideNode);
    leftSideNode.setParent(assignNode);

    Node thisNode = Node.newThis();
    thisNode.setType(Token.THIS);
    Node rightSideNode = Node.newName("rightSideVariable");
    rightSideNode.setType(Token.NAME);
    rightSideNode.setJSDocInfo(Node.newJSDocInfo("rightSideVariable"));
    assignNode.setRhs(thisNode);
    thisNode.setParent(assignNode);
    rightSideNode.setParent(assignNode);

    ArgumentCaptor<NodeTraversal.Diagnostic> diagnosticCaptor =
        ArgumentCaptor.forClass(NodeTraversal.Diagnostic.class);

    checkGlobalThis.visit(traversal, null, assignNode);

    assertFalse(diagnosticCaptor.getValue() == null);
  }
}