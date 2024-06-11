import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = NodeTraversal.create(new Parser(), checker);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node root = createNode("function global() {}");
    Node parent = root.getFirstChild();

    assertTrue(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node root = createNode("function MyConstructor() {}");
    Node parent = root.getFirstChild();

    assertFalse(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisType() {
    Node root = createNode("function MyClass() { @this MyClass }");
    Node parent = root.getFirstChild();

    assertFalse(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithThis() {
    Node root = createNode("function MyClass() { this.x = 0; }");
    Node parent = root.getFirstChild();

    assertFalse(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThis() {
    Node root = createNode("function MyClass() { this.x = 0; } var a = function() {};");
    Node parent = root.getLastChild();

    assertFalse(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldTraverseAssignment() {
    Node root = createNode("var a = this;");
    Node parent = root.getParent();

    assertTrue(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node root = createNode("var a = { get x() { return this; } }; a.prototype.x = this;");
    Node parent = root.getLastChild();

    assertFalse(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubproperty() {
    Node root = createNode("var a = { get x() { return this; } }; a.prototype.p = { get y() { return this; } }; a.prototype.p.y = this;");
    Node parent = root.getLastChild();

    assertFalse(checker.shouldTraverse(traversal, root, parent));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node root = createNode("var a = this;");
    Node n = root.getFirstChild();

    when(compiler.report(traversal.makeError(n, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(true);

    checker.visit(traversal, null, null);
    traversal.traverse(root);

    assertTrue(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignment() {
    Node root = createNode("var a = this; this = 5;");
    Node n = root.getFirstChildren()[0];

    when(compiler.report(traversal.makeError(n, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(false);

    checker.visit(traversal, null, null);
    traversal.traverse(root);

    assertFalse(compiler.reportWasCalled());
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfPropertyAccess() {
    Node root = createNode("var a = { get x() { return this; } }; a.x = this;");
    Node n = root.getLastChild();

    when(compiler.report(traversal.makeError(n, CheckLevel.WARNING, DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object")))).thenReturn(false);

    checker.visit(traversal, null, null);
    traversal.traverse(root);

    assertFalse(compiler.reportWasCalled());
  }

  private Node createNode(String jsCode) {
    return new Parser().parseCompilationUnit(jsCode, null, null).getTree();
  }
}