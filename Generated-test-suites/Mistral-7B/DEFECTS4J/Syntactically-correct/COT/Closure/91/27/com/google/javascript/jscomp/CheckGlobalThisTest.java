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
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node node = createMockNode("function foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    Node node = createMockNode("function Foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    JSDocInfo jsDoc = createMockJSDocInfo("constructor");
    when(node.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node node = createMockNode("function foo() { @this }");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node node = createMockNode("function foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, null));
  }

  @Test
  public void testShouldTraverseFunctionWithParentBlock() {
    Node node = createMockNode("function foo() {}");
    Node parent = createMockNode("{ foo: function foo() {} }");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithParentScript() {
    Node node = createMockNode("function foo() {}");
    Node parent = createMockNode("function main() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithParentName() {
    Node node = createMockNode("function foo() {}");
    Node parent = createMockNode("var a = function foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithParentAssignment() {
    Node node = createMockNode("function foo() {}");
    Node parent = createMockNode("var a = {}; a.foo = function foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithParentGetProp() {
    Node node = createMockNode("function foo() {}");
    Node parent = createMockNode("var a = {}; a.prop = function foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeGetProp() {
    Node node = createMockNode("function foo() {}");
    Node parent = createMockNode("function Foo() {} Foo.prototype.foo = function foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeGetPropChain() {
    Node node = createMockNode("function foo() {}");
    Node parent = createMockNode("function Foo() {} Foo.prototype.prop.prop = function foo() {}");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), node, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node node = createMockNode("var a = this;");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(node, eq(CheckLevel.WARNING), eq(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenReturn(mock(Node.class));

    checker.visit(traversal, node, null);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node node = createMockNode("var a = {}; a.a = this;");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(node, eq(CheckLevel.WARNING), eq(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenReturn(mock(Node.class));

    checker.visit(traversal, node, null);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithAncestorAssignment() {
    Node node = createMockNode("(a = this).b = c;");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.makeError(node, eq(CheckLevel.WARNING), eq(DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object"))))
        .thenReturn(mock(Node.class));

    checker.visit(traversal, node, null);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node node = createMockNode("var a = this; a = b;");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    Node node = createMockNode("var a = {}; a.a = this.b;");
    CheckGlobalThis checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  private Node createMockNode(String source) {
    Node node = mock(Node.class);
    when(node.getFirstChild()).thenReturn(null);
    when(node.getLastChild()).thenReturn(null);
    when(node.getString()).thenReturn(source);

    return node;
  }

  private JSDocInfo createMockJSDocInfo(String... annotations) {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(annotations.contains("constructor"));
    when(jsDoc.isInterface()).thenReturn(annotations.contains("interface"));
    when(jsDoc.hasThisType()).thenReturn(annotations.contains("@this"));
    when(jsDoc.isOverride()).thenReturn(annotations.contains("@override"));

    return jsDoc;
  }
}