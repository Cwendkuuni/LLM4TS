import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checker;

  @Before
  public void setUp() {
    CheckerFactory factory = new CheckerFactory();
    compiler = factory.newCompiler();
    checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testSimpleStatement() {
    Node root = Node.createText("x = 5;");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVoidStatement() {
    Node root = Node.createText(";");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testSimpleOperator() {
    Node root = Node.createText("x = 5 + 10;");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testUselessAssignment() {
    Node root = Node.createText("x = x;");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testMissingPlus() {
    Node root = Node.createText("var s = \"this string is \";");
    NodeTraversal.traverse(compiler, root, checker);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.STRING_LITERAL, problemNode.getType());
    assertEquals("Is there a missing '+' on the previous line?", problemNode.getErrorMessage());
  }

  @Test
  public void testCallWithSideEffect() {
    Node root = Node.createText("console.log('Hello World');");
    NodeTraversal.traverse(compiler, root, checker);
    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.CALL_EXPR, problemNode.getType());
    assertEquals("Suspicious code. console.log('Hello World')", problemNode.getErrorMessage());
  }

  @Test(expected = AssertionError.class)
  public void testProtectedCode() {
    Node root = Node.createText("JSCOMPILER_PRESERVE(1 + 1);");
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();
    NodeTraversal.traverse(compiler, root, new CheckSideEffects.StripProtection(compiler));
    NodeUtil.runToCompletion(compiler);
  }

  @Test
  public void testStatementInsideBlock() {
    Node root = Node.createText("if (true) {} x = 5;");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testStatementInsideFor() {
    Node root = Node.createText("for (int i = 0; i < 10; i++) {}; x = 5;");
    NodeTraversal.traverse(compiler, root, checker);
    assertTrue(checker.problemNodes.isEmpty());
  }

  private void assertEquals(int expected, int actual) {
    assertEquals(Integer.toString(expected), Integer.toString(actual));
  }

  private void assertEquals(String expected, String actual) {
    assertEquals(expected, actual);
  }

  private String getErrorMessage(Node node) {
    return checker.level.getErrorMessage(node, compiler.getDiagnostics());
  }

  private boolean isStatement(Node node) {
    return node.isStmt();
  }
}