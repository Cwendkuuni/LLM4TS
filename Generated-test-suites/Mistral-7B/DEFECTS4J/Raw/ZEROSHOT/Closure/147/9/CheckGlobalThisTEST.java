import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private Diagnostics diagnostics;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    diagnostics = new Diagnostics();
    traversal = new NodeTraversal(compiler, diagnostics, null);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node node = Node.newFunction("functionName");
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node node = Node.newFunction("functionName", null, Node.newBlock(null));
    JSDocInfo jsDoc = Node.newJsDocInfo(null, "constructor");
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node node = Node.newFunction("functionName", null, Node.newBlock(null));
    JSDocInfo jsDoc = Node.newJsDocInfo(null, null, "@this");
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node node = Node.newFunction("functionName", null, Node.newBlock(null));
    JSDocInfo jsDoc = Node.newJsDocInfo(null, null, "@override");
    when(node.getJSDocInfo()).thenReturn(jsDoc);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node node = Node.newFunction("functionName");
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithBlockParent() {
    Node parent = Node.newBlock(null);
    Node node = Node.newFunction("functionName", null, parent);
    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithAssignParent() {
    Node parent = Node.newAssign(Node.newName("a"), Node.newName("this"));
    Node node = Node.newFunction("functionName", null, parent);
    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithGetPropertyParent() {
    Node parent = Node.newGetProp(Node.newName("a"), Node.newString("prototype"));
    Node node = Node.newFunction("functionName", null, parent);
    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithGetPropertyGrandParent() {
    Node grandParent = Node.newGetProp(Node.newName("a"), Node.newString("prototype"));
    Node parent = Node.newGetProp(Node.newName("b"), Node.newName("prototype"));
    Node node = Node.newFunction("functionName", null, parent);
    assertTrue(checker.shouldTraverse(traversal, node, grandParent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node parent = Node.newAssign(Node.newName("a"), Node.newName("this"));
    Node node = Node.newAssign(Node.newName("a"), Node.newName("this"));
    checker.visit(traversal, node, parent);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfFunctionCall() {
    Node parent = Node.newCall(Node.newName("callFunction"), Node.newArray(), null);
    Node node = Node.newName("this");
    checker.visit(traversal, node, parent);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node parent = Node.newAssign(Node.newName("a"), Node.newName("this"));
    Node node = Node.newName("this");
    checker.visit(traversal, node, parent);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfFunctionCall() {
    Node parent = Node.newCall(Node.newName("callFunction"), Node.newArray(), null);
    Node node = Node.newName("this");
    checker.visit(traversal, node, parent);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    Node parent = Node.newGetProp(Node.newName("a"), Node.newString("prototype"));
    Node node = Node.newName("this");
    checker.visit(traversal, node, parent);
    assertFalse(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldReportThisOnRightSideOfPropertyAccessWithPrototype() {
    Node parent = Node.newGetProp(Node.newName("a"), Node.newString("prototype"));
    Node node = Node.newGetProp(Node.newName("prototype"), Node.newString("this"));
    checker.visit(traversal, node, parent);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldReportThisOnRightSideOfPropertyAccessWithPrototypeGrandParent() {
    Node grandParent = Node.newGetProp(Node.newName("a"), Node.newString("prototype"));
    Node parent = Node.newGetProp(Node.newName("b"), Node.newName("prototype"));
    Node node = Node.newGetProp(Node.newName("prototype"), Node.newString("this"));
    checker.visit(traversal, node, grandParent);
    assertTrue(diagnostics.hasError(CheckGlobalThis.GLOBAL_THIS));
  }
}