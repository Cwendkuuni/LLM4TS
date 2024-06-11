import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Callback checkGlobalThis;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunctionsWithNoAnnotation() {
    Node node = Node.newNode(Token.FUNCTION);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    JSDocInfo jsDoc = Node.newJsDocInfo("constructor");
    Node node = Node.newNode(Token.FUNCTION, jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    JSDocInfo jsDoc = Node.newJsDocInfo("this");
    Node node = Node.newNode(Token.FUNCTION, jsDoc);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node node = Node.newNode(Token.FUNCTION);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithParentBlock() {
    Node parent = Node.newNode(Token.BLOCK);
    Node node = Node.newNode(Token.FUNCTION);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithParentName() {
    Node parent = Node.newNode(Token.NAME, "a");
    Node node = Node.newNode(Token.FUNCTION);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithParentAssign() {
    Node parentAssign = Node.newNode(Token.ASSIGN);
    Node parent = Node.newNode(Token.NAME, "a");
    Node node = Node.newNode(Token.FUNCTION);
    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parentAssign));
    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeProperty() {
    Node parent = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    Node node = Node.newNode(Token.FUNCTION);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeSubProperty() {
    Node parent = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    Node prop = Node.newNode(Token.GETPROP, Node.newName("subProperty"));
    Node node = Node.newNode(Token.FUNCTION);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, prop));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node parentAssign = Node.newNode(Token.ASSIGN);
    Node lhs = Node.newNode(Token.THIS);
    Node rhs = Node.newNode(Token.NAME, "a");
    Node node = Node.newNode(Token.ASSIGN);
    node.setFirstChild(lhs);
    node.setNext(rhs);
    parentAssign.setFirstChild(node);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(true);

    checkGlobalThis.visit(traversal, rhs, parentAssign);
    assertTrue(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node parentAssign = Node.newNode(Token.ASSIGN);
    Node lhs = Node.newNode(Token.NAME, "a");
    Node rhs = Node.newNode(Token.THIS);
    Node node = Node.newNode(Token.ASSIGN);
    node.setFirstChild(lhs);
    node.setNext(rhs);
    parentAssign.setFirstChild(node);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(false);

    checkGlobalThis.visit(traversal, rhs, parentAssign);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node parentGetProp = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    Node node = Node.newNode(Token.THIS);
    Node parent = Node.newNode(Token.NAME, "a");
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    getProp.setParent(parent);
    parentGetProp.setFirstChild(getProp);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(true);

    checkGlobalThis.visit(traversal, node, parentGetProp);
    assertTrue(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithNoPrototype() {
    Node parent = Node.newNode(Token.NAME, "a");
    Node node = Node.newNode(Token.THIS);
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("x"));
    Node parentGetProp = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    getProp.setParent(parent);
    parentGetProp.setFirstChild(getProp);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(false);

    checkGlobalThis.visit(traversal, node, parentGetProp);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithNoPrototypeProperty() {
    Node parent = Node.newNode(Token.NAME, "a");
    Node node = Node.newNode(Token.THIS);
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("x"));
    Node parentGetProp = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    parentGetProp.setFirstChild(getProp);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(false);

    checkGlobalThis.visit(traversal, node, parentGetProp);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessWithNonPrototypeProperty() {
    Node parent = Node.newNode(Token.NAME, "a");
    Node node = Node.newNode(Token.THIS);
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("x"));
    Node parentGetProp = Node.newNode(Token.GETPROP, Node.newName("x"));
    getProp.setParent(parent);
    parentGetProp.setFirstChild(getProp);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(false);

    checkGlobalThis.visit(traversal, node, parentGetProp);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnGetPropWithNoParent() {
    Node node = Node.newNode(Token.THIS);
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("x"));

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(false);

    checkGlobalThis.visit(traversal, node, null);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnGetPropWithNoPrototypePropertyName() {
    Node node = Node.newNode(Token.THIS);
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("x"));
    Node propName = Node.newName("x");
    getProp.setParent(node);
    getProp.setLastChild(propName);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(false);

    checkGlobalThis.visit(traversal, node, null);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnGetPropWithPrototypePropertyName() {
    Node node = Node.newNode(Token.THIS);
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    Node propName = Node.newName("prototype");
    getProp.setParent(node);
    getProp.setLastChild(propName);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(false);

    checkGlobalThis.visit(traversal, node, null);
    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldReportThisOnGetPropWithPrototypeProperty() {
    Node node = Node.newNode(Token.THIS);
    Node getProp = Node.newNode(Token.GETPROP, Node.newName("prototype"));
    Node propName = Node.newName("prototype");
    Node prototype = Node.newName("prototype");
    getProp.setParent(node);
    getProp.setLastChild(propName);
    Node prototypeNode = Node.newNode(Token.NAME, "prototype");
    prototypeNode.setFirstChild(getProp);

    when(compiler.report(Mockito.<NodeTraversal>any(), Mockito.<DiagnosticType>any(), Mockito.<Node>any()))
        .thenReturn(true);

    checkGlobalThis.visit(traversal, node, prototypeNode);
    assertTrue(compiler.reportWasCalled());
  }

  private boolean compilerReportWasCalled() {
    return compiler.reportWasCalled;
  }
}