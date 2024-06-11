import static com.google.javascript.jscomp.NodeTraversal.javalink;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private JSErrorReporter reporter;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.setErrorReporter(new JSErrorReporter());
  }

  @Test
  public void testEmptyBlock() {
    String code = "function foo() { }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertEquals(0, rootNode.getChildCount());
  }

  @Test
  public void testSimpleReturn() {
    String code = "function foo() { return 42; }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.RETURN.toInt32());
    assertEquals(42, rootNode.getFirstChild().getFirstChild().getInt32Value());
  }

  @Test
  public void testSimpleNoOp() {
    String code = "function foo() { true; }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertNull(rootNode.getFirstChild());
  }

  @Test
  public void testSimpleNoOpWithSideEffect() {
    String code = "function foo() { document.write('Hello World'); }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.ASSIGN.toInt32());
    assertEquals("Hello World", compiler.getOutputText());
  }

  @Test
  public void testReturnAfterIf() {
    String code = "function foo() { if (true) { return 42; } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.RETURN.toInt32());
    assertEquals(42, rootNode.getFirstChild().getFirstChild().getInt32Value());
  }

  @Test
  public void testReturnAfterIfElse() {
    String code = "function foo() { if (true) { return 42; } else { return 43; } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.RETURN.toInt32());
    assertEquals(42, rootNode.getFirstChild().getFirstChild().getInt32Value());
  }

  @Test
  public void testIfElseReturn() {
    String code = "function foo() { if (true) { return 42; } else { return 43; } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.RETURN.toInt32());
    assertEquals(42, rootNode.getFirstChild().getFirstChild().getInt32Value());
  }

  @Test
  public void testIfReturn() {
    String code = "function foo() { if (true) { return; } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertNull(rootNode.getFirstChild());
  }

  @Test
  public void testBreak() {
    String code = "function foo() { for (var i = 0; i < 10; i++) { if (i == 5) { break; } } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertNull(rootNode.getFirstChild());
  }

  @Test
  public void testContinue() {
    String code = "function foo() { for (var i = 0; i < 10; i++) { if (i % 2 == 0) { continue; } } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertNull(rootNode.getFirstChild());
  }

  @Test
  public void testComplexExample() {
    String code = "function foo() { if (x) { return; alert('unreachable'); } " +
        "else if (y) { break; } " +
        "else if (z) { continue; } " +
        "else { document.write('Hello World'); } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertNull(rootNode.getFirstChild());
  }

  @Test
  public void testComplexExampleWithSideEffect() {
    String code = "function foo() { if (x) { return; document.write('unreachable'); } " +
        "else if (y) { break; } " +
        "else if (z) { continue; } " +
        "else { document.write('Hello World'); } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.ASSIGN.toInt32());
    assertEquals("unreachable", compiler.getOutputText());
  }

  @Test
  public void testComplexExampleWithSideEffectInConditional() {
    String code = "function foo() { if (x) { document.write('unreachable'); return; } " +
        "else if (y) { break; } " +
        "else if (z) { continue; } " +
        "else { document.write('Hello World'); } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.ASSIGN.toInt32());
    assertEquals("unreachable", compiler.getOutputText());
  }

  @Test
  public void testComplexExampleWithSideEffectInConditionalElse() {
    String code = "function foo() { if (x) { document.write('unreachable'); return; } " +
        "else if (y) { break; } " +
        "else if (z) { continue; } " +
        "else { document.write('Hello World'); return; } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(rootNode.getFirstChild().getNodeType() == Token.ASSIGN.toInt32());
    assertEquals("Hello World", compiler.getOutputText());
  }

  @Test
  public void testComplexExampleWithSideEffectInConditionalElseIf() {
    String code = "function foo() { if (x) { document.write('unreachable'); return; } " +
        "else if (y) { document.write('y'); break; } " +
        "else if (z) { continue; } " +
        "else { document.write('Hello World'); return; } }";
    Node rootNode = NodeUtil.parse(code, null, null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, pass);
    traversal.traverse();
    assertTrue(compiler.getErrorCount() > 0);
  }
}