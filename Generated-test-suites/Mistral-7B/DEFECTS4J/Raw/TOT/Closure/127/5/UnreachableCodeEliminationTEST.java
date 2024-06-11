import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  CompilerOptions options;
  AbstractCompiler compiler;
  UnreachableCodeElimination elimination;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    elimination = new UnreachableCodeElimination(compiler, true);
  }

  @Test
  public void testSimpleReturn() {
    Node root = Node.newBlock(1, "function foo() { return; }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeAfterReturn() {
    Node root = Node.newBlock(1, "function foo() { return; alert('unreachable'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
    assertTrue(root.getLastChild().getNext() == null);
  }

  @Test
  public void testUselessCodeWithSideEffects() {
    Node root = Node.newBlock(1, "function foo() { alert('useless'); }");
    elimination.process(null, root);
    assertFalse(root.getLastChild().isEnd());
  }

  @Test
  public void testUselessCodeChangeControlFlow() {
    Node root = Node.newBlock(1, "function foo() { break; break; }");
    elimination.process(null, root);
    Node lastChild = root.getLastChild();
    assertTrue(lastChild.isEnd());
    assertTrue(lastChild.getNext() == null);
  }

  @Test
  public void testBlockWithNoChildren() {
    Node root = Node.newBlock(1, "function foo() { }");
    elimination.process(null, root);
    assertTrue(root.isEnd());
  }

  @Test
  public void testFunctionWithDeadCode() {
    Node root = Node.newBlock(1, "function foo() { return; alert('dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
    assertTrue(root.getLastChild().getNext() == null);
  }

  @Test
  public void testNestedFunctionWithDeadCode() {
    Node root = Node.newBlock(1,
        "function foo() {" +
            "function bar() { return; alert('dead'); }" +
            "return bar(); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isFunction());
    assertTrue(root.getLastChild().getLastChild().isEnd());
    assertTrue(root.getLastChild().getLastChild().getNext() == null);
  }

  @Test
  public void testDeadCodeInForLoop() {
    Node root = Node.newBlock(1, "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
            "if (i == 5) { return; }" +
            "alert('dead'); }" +
        "}");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeInSwitchStatement() {
    Node root = Node.newBlock(1,
        "function foo() {" +
            "var x = 1;" +
            "switch (x) {" +
                "case 0:" +
                    "return;" +
                "case 1:" +
                    "alert('dead');" +
                "default:" +
                    "alert('default');" +
            "}" +
        "}");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeInIfStatement() {
    Node root = Node.newBlock(1, "function foo() {" +
        "if (true) {" +
            "return;" +
            "alert('dead');" +
        "}" +
        "alert('not dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeInConditionalExpression() {
    Node root = Node.newBlock(1, "function foo() {" +
        "return (true ? (return; alert('dead')) : 1) + 1;" +
        "alert('not dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isNumber());
    assertTrue(root.getLastChild().getInt() == 2);
  }

  @Test
  public void testDeadCodeInTryStatement() {
    Node root = Node.newBlock(1, "function foo() {" +
        "try {" +
            "return;" +
            "alert('dead');" +
        "} catch (e) {" +
            "return;" +
        "}" +
        "alert('not dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeInCatchStatement() {
    Node root = Node.newBlock(1, "function foo() {" +
        "try {" +
            "return;" +
        "} catch (e) {" +
            "alert('dead');" +
            "return;" +
        "}" +
        "alert('not dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeInForInStatement() {
    Node root = Node.newBlock(1, "function foo() {" +
        "for (var x in []) {" +
            "alert('dead');" +
            "return;" +
        "}" +
        "alert('not dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeInWhileStatement() {
    Node root = Node.newBlock(1, "function foo() {" +
        "while (true) {" +
            "return;" +
            "alert('dead');" +
        "}" +
        "alert('not dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }

  @Test
  public void testDeadCodeInDoWhileStatement() {
    Node root = Node.newBlock(1, "function foo() {" +
        "do {" +
            "return;" +
            "alert('dead');" +
        "} while (false);" +
        "alert('not dead'); }");
    elimination.process(null, root);
    assertTrue(root.getLastChild().isEnd());
  }
}