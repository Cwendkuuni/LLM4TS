import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.visit;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractShallowCallback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal.FunctionCallback;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.jscomp.UnreachableCodeElimination.EliminationPass;

public class UnreachableCodeEliminationTest {

  @Test
  public void testEmptyFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newFunction(null, null, new Node[0], null);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testReturnWithNoChildren() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Node.newReturn(null);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testFunctionWithDeadCodeAfterReturn() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code =
        "function foo() {" +
            "if (true) {" +
                "return;" +
            "}" +
            "alert('unreachable');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertTrue(root.getChild(0).isFunction());
    assertEquals(0, root.getChild(0).getLastChild().getChildCount());
  }

  @Test
  public void testDeadCodeInsideFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "alert('deadCode');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testDeadCodeWithSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "var x = 1;" +
            "alert('deadCode');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertTrue(root.getChild(0).isFunction());
    assertEquals(1, root.getChild(0).getFirstChild().getValue().getInt());
  }

  @Test
  public void testDeadCodeWithAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "/**\n" +
        " * @preserve\n" +
        " */\n" +
        "function foo() {" +
            "alert('deadCode');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testBreakStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "for (var i = 0; i < 1; i++) {" +
                "if (true) {" +
                    "break;" +
                "}" +
            "}" +
            "alert('unreachable');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testContinueStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "for (var i = 0; i < 1; i++) {" +
                "if (true) {" +
                    "continue;" +
                "}" +
            "}" +
            "alert('unreachable');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testReturnStatementWithChildren() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "return {" +
                "a: 1," +
                "b: 2" +
            "};" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertTrue(root.getChild(0).isObject());
    assertEquals(2, root.getChild(0).getChildCount());
  }

  @Test
  public void testTryCatchBlock() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "try {" +
                "throw new Error('Error');" +
            "}" +
            "catch (e) {" +
                "alert('Caught exception: ' + e.message);" +
                "return;" +
            "}" +
            "alert('unreachable');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    assertTrue(root.getChild(0).isFunction());
    assertEquals(0, root.getChild(0).getLastChild().getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeWithFoldConstants() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "if (1 + 1 == 2) {" +
                "alert('unreachable');" +
            "}" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    fail("Expected an AssertionError");
  }

  @Test
  public void testControlFlowGraph() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "if (true) {" +
                "if (true) {" +
                    "return;" +
                "}" +
                "alert('A');" +
            "}" +
            "else {" +
                "alert('B');" +
            "}" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    EliminationPass eliminationPass = untested.new EliminationPass(
        untested.getControlFlowGraph());
    visit(null, root, eliminationPass);

    Node expected = Node.newBlock(null, null);
    expected.setLineInfo(2, 3, 2);
    assertEquals(expected, root);
  }

  @Test
  public void testControlFlowGraphWithBreakStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "for (var i = 0; i < 1; i++) {" +
                "if (true) {" +
                    "break;" +
                "}" +
            "}" +
            "alert('unreachable');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    EliminationPass eliminationPass = untested.new EliminationPass(
        untested.getControlFlowGraph());
    visit(null, root, eliminationPass);

    Node expected = Node.newBlock(null, null);
    expected.setLineInfo(2, 3, 2);
    assertEquals(expected, root);
  }

  @Test
  public void testControlFlowGraphWithReturnStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "if (true) {" +
                "return;" +
            "}" +
            "alert('unreachable');" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    EliminationPass eliminationPass = untested.new EliminationPass(
        untested.getControlFlowGraph());
    visit(null, root, eliminationPass);

    Node expected = Node.newFunction(null, null, null, null);
    expected.setLineInfo(1, 1, 1);
    assertEquals(expected, root);
  }

  @Test
  public void testControlFlowGraphWithUnconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "if (true) {" +
                "return 1;" +
            "}" +
            "else {" +
                "return 2;" +
            "}" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    EliminationPass eliminationPass = untested.new EliminationPass(
        untested.getControlFlowGraph());
    visit(null, root, eliminationPass);

    Node expected = Node.newFunction(null, null, null, null);
    expected.setLineInfo(1, 1, 1);
    assertEquals(expected, root);
  }

  @Test
  public void testControlFlowGraphWithNestedUnconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "if (true) {" +
                "if (true) {" +
                    "return 1;" +
                "}" +
                "else {" +
                    "return 2;" +
                "}" +
            "}" +
            "else {" +
                "return 3;" +
            "}" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    EliminationPass eliminationPass = untested.new EliminationPass(
        untested.getControlFlowGraph());
    visit(null, root, eliminationPass);

    Node expected = Node.newFunction(null, null, null, null);
    expected.setLineInfo(1, 1, 1);
    assertEquals(expected, root);
  }

  @Test
  public void testControlFlowGraphWithConditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    String code = "function foo() {" +
            "if (true) {" +
                "if (1 + 1 == 2) {" +
                    "return 1;" +
                "}" +
                "else {" +
                    "return 2;" +
                "}" +
            "}" +
            "else {" +
                "if (1 + 1 == 3) {" +
                    "return 3;" +
                "}" +
                "else {" +
                    "return 4;" +
                "}" +
            "}" +
        "}";
    Node root = ParserTestUtil.parse(code);
    UnreachableCodeElimination untested = new UnreachableCodeElimination(compiler, false);
    untested.process(null, root);
    EliminationPass eliminationPass = untested.new EliminationPass(
        untested.getControlFlowGraph());
    visit(null, root, eliminationPass);

    Node expected = Node.newFunction(null, null, null, null);
    expected.setLineInfo(1, 1, 1);
    assertEquals(expected, root);
  }
}