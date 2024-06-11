import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterType.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.ExitType.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalUtils;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
  }

  @Test
  public void testSimpleUnreachableCode() {
    String jsCode = "function test() { if (true) { return; } alert('unreachable'); }";

    Node root = ParserTestUtil.parseSingleStatementsToRoot(jsCode);
    ImmutableList<CompilerPass> passes = ImmutableList.of(
        new UnreachableCodeElimination(compiler, true));
    Node result = TraversalUtils.traverse(compiler, root, passes);

    assertNotNull(result);
    assertEquals("function test() { }", result.toSource());
  }

  @Test
  public void testReturnStatementWithSideEffects() {
    String jsCode = "function test() { if (true) { return ++i; } alert('unreachable'); i = 0; }";

    Node root = ParserTestUtil.parseSingleStatementsToRoot(jsCode);
    ImmutableList<CompilerPass> passes = ImmutableList.of(
        new UnreachableCodeElimination(compiler, true));
    Node result = TraversalUtils.traverse(compiler, root, passes);

    assertNotNull(result);
    assertEquals("function test() { return ++i; }", result.toSource());
  }

  @Test
  public void testPrototypePropertyWithNoInitialization() {
    String jsCode = "function Foo() {} Foo.prototype.bar = null;";

    Scope scope = Scope.createRootScope(parser, compiler);
    Node root = parser.parse(jsCode, null, scope);
    ImmutableList<CompilerPass> passes = ImmutableList.of(
        new UnreachableCodeElimination(compiler, true));
    Node result = TraversalUtils.traverse(compiler, root, passes);

    assertNotNull(result);
    assertEquals("function Foo() {}\n" +
        "Foo.prototype = {\n" +
        "  bar: null\n" +
        "}", result.toSource());
  }

  @Test
  public void testReturnStatementWithCondition() {
    String jsCode = "function test() { return condition ? x : test(); }";

    Node root = ParserTestUtil.parseSingleStatementsToRoot(jsCode);
    ImmutableList<CompilerPass> passes = ImmutableList.of(
        new UnreachableCodeElimination(compiler, true));
    Node result = TraversalUtils.traverse(compiler, root, passes);

    assertNotNull(result);
    assertEquals("function test() { return condition ? x : undefined; }", result.toSource());
  }

  @Test(expected = RuntimeException.class)
  public void testDeadCodeEliminationException() {
    String jsCode = "function test() { throw new Error('test error'); return; }";

    Node root = ParserTestUtil.parseSingleStatementsToRoot(jsCode);
    ImmutableList<CompilerPass> passes = ImmutableList.of(
        new UnreachableCodeElimination(compiler, true));
    TraversalUtils.traverse(compiler, root, passes);
  }

  @Test
  public void testDeadCodeEliminationWithNestedFunction() {
    String jsCode = "function test() {" +
        "  function nestedFunction() {}" +
        "  return; nestedFunction(); }";

    Node root = ParserTestUtil.parseSingleStatementsToRoot(jsCode);
    ImmutableList<CompilerPass> passes = ImmutableList.of(
        new UnreachableCodeElimination(compiler, true));
    Node result = TraversalUtils.traverse(compiler, root, passes);

    assertNotNull(result);
    assertEquals("function test() {}", result.toSource());
  }

  @Test
  public void testDeadCodeEliminationWithFunctionDeclaration() {
    String jsCode = "function test() {" +
        "  function nestedFunction() {}" +
        "  nestedFunction();" +
        "}";

    Node root = ParserTestUtil.parseSingleStatementsToRoot(jsCode);
    ImmutableList<CompilerPass> passes = ImmutableList.of(
        new UnreachableCodeElimination(compiler, true));
    Node result = TraversalUtils.traverse(compiler, root, passes);

    assertNotNull(result);
    assertEquals("function test() {}\n" +
        "function nestedFunction() {}\n", result.toSource());
  }
}