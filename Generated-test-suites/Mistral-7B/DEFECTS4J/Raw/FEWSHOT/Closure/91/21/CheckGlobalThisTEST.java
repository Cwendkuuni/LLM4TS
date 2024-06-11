package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(new Node());
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = createNode("function global() { this.property = 'global'; }");
    Node traverserParent = new Node(Token.SCRIPT, null, root);
    when(compiler.getRootNode()).thenReturn(root);

    assertTrue(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.any(Node.class), ArgumentMatchers.any(Node.class)));
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    Node root = createNode("function outer() { function inner() { this.property = 'inner'; } }");

    Node traverserParent = new Node(Token.NAME, "outer", root);
    Node functionNode = new Node(Token.FUNCTION, null, traverserParent);
    Node functionParent = new Node(Token.NAME, "outer", root);

    when(compiler.getRootNode()).thenReturn(root);

    assertFalse(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.any(Node.class), ArgumentMatchers.any(Node.class)));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node root = createNode("var a = this;");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node assignNode = new Node(Token.ASSIGN, null, root);
    Node varNode = new Node(Token.VAR, null, assignNode);

    Node thisNode = new Node(Token.THIS, null, root);

    when(compiler.getRootNode()).thenReturn(root);

    checker.traverse(traversal, root, null);
    Node error = traversal.errors.get(0);

    assertEquals("dangerous use of the global 'this' object", error.getMessage());
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node root = createNode("var a = { property: this };");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node objectNode = new Node(Token.OBJECT, null, traverserParent);
    Node propertyNode = new Node(Token.SETPROP, null, objectNode, "property");
    Node thisNode = new Node(Token.THIS, null, root);

    when(compiler.getRootNode()).thenReturn(root);

    checker.traverse(traversal, root, null);
    Node error = traversal.errors.get(0);

    assertEquals("dangerous use of the global 'this' object", error.getMessage());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node root = createNode("var a = { property: this }; this = { property: 'newthis' };");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node objectNode1 = new Node(Token.OBJECT, null, traverserParent);
    Node setProp1 = new Node(Token.SETPROP, null, objectNode1, "property");
    Node thisNode1 = new Node(Token.THIS, null, root);

    Node objectNode2 = new Node(Token.OBJECT, null, root);
    Node assignNode = new Node(Token.ASSIGN, null, root);
    Node setProp2 = new Node(Token.SETPROP, null, objectNode2, "property");
    Node thisNode2 = new Node(Token.THIS, null, root);

    when(compiler.getRootNode()).thenReturn(root);

    checker.traverse(traversal, root, null);
    Node error = traversal.errors.get(0);

    assertNull(error);
  }

  @Test
  public void testShouldReportThisInPrototypeMethod() {
    Node root = createNode("function outer() { function inner() { this.property = 'inner'; } } outer.prototype.method = inner; }");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node outerFunctionNode = new Node(Token.FUNCTION, null, traverserParent);
    Node prototypeNode = new Node(Token.GETPROP, null, outerFunctionNode, "prototype");
    Node innerFunctionNode = new Node(Token.FUNCTION, null, prototypeNode);
    Node thisNode = new Node(Token.THIS, null, innerFunctionNode);

    when(compiler.getRootNode()).thenReturn(root);

    assertFalse(checker.shouldReportThis(innerFunctionNode, prototypeNode));
  }

  @Test
  public void testShouldNotReportThisInConstructor() {
    Node root = createNode("function MyClass() { this.property = 'MyClass'; }");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node constructorNode = new Node(Token.FUNCTION, null, traverserParent);
    Node thisNode = new Node(Token.THIS, null, constructorNode);

    when(compiler.getRootNode()).thenReturn(root);

    assertFalse(checker.shouldReportThis(constructorNode, null));
  }

  @Test
  public void testShouldReportThisInAnnotatedFunction() {
    Node root = createNode("@this function MyClass() { this.property = 'MyClass'; }");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node constructorNode = new Node(Token.FUNCTION, null, traverserParent);
    Node thisNode = new Node(Token.THIS, null, constructorNode);

    when(compiler.getRootNode()).thenReturn(root);

    assertTrue(checker.shouldReportThis(constructorNode, null));
  }

  @Test
  public void testShouldReportThisInAnnotatedConstructor() {
    Node root = createNode("@constructor function MyClass() { this.property = 'MyClass'; }");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node constructorNode = new Node(Token.FUNCTION, null, traverserParent, "MyClass");
    Node thisNode = new Node(Token.THIS, null, constructorNode);

    when(compiler.getRootNode()).thenReturn(root);

    assertTrue(checker.shouldReportThis(constructorNode, null));
  }

  @Test
  public void testShouldReportThisInFunctionWithNoAnnotation() {
    Node root = createNode("function MyClass() { this.property = 'MyClass'; }");

    Node traverserParent = new Node(Token.SCRIPT, null, root);
    Node constructorNode = new Node(Token.FUNCTION, null, traverserParent, "MyClass");
    Node thisNode = new Node(Token.THIS, null, constructorNode);

    when(compiler.getRootNode()).thenReturn(root);

    assertTrue(checker.shouldReportThis(constructorNode, null));
  }

  private Node createNode(String code) {
    return (Node) RhinoEvaluator.compileToNode(code);
  }

  private void assertTrue(boolean condition) {
    assertThat(condition).isTrue();
  }

  private void assertFalse(boolean condition) {
    assertThat(condition).isFalse();
  }

  private static class RhinoEvaluator {
    public static Object compileAndRun(String code, AbstractCompiler compiler) throws Exception {
      Node rootNode = compiler.compile(code, null, null);
      RhinoInterpreter interpreter = new RhinoInterpreter();
      interpreter.interpret(rootNode);
      return interpreter.getResult();
    }

    public static Node compileToNode(String code) throws Exception {
      AbstractCompiler compiler = new GjsCompiler();
      return (Node) compileAndRun(code, compiler);
    }
  }
}