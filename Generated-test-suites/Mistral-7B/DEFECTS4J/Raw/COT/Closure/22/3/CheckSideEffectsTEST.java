import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {
  private CheckLevel level;
  private CheckSideEffects checkSideEffects;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    level = new CheckLevel(WARNING);
    compiler = new Compiler(new CompilerOptions());
    checkSideEffects = new CheckSideEffects(compiler, level, true);
  }

  @Test
  public void testProcess_RootIsEmpty() {
    Node root = Node.EMPTY();
    checkSideEffects.process(Node.toCompilationUnit(root), root);
    assertFalse("No nodes to process", checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_RootIsVoidStatement() {
    Node root = Node.newExpr(Token.VOID);
    checkSideEffects.process(Node.toCompilationUnit(root), root);
    assertFalse("Void statement is not a problem", checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_RootIsCallToEval() {
    String expression = "function foo() { return 42; } foo();";
    Node root = Node.newBlock(2, ImmutableList.of(
        Node.newexpr(Token.CALL, Node.newexpr(Token.NAME, "eval"), ImmutableList.of(
            Node.newexpr(Token.STRING_LITERAL, expression)
        ))));
    checkSideEffects.process(Node.toCompilationUnit(root), root);
    assertFalse("Call to eval is not a problem", checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_RootIsCommaOperator() {
    Node root = Node.newexpr(Token.COMMA, Node.newexpr(Token.NUMBER, 42), Node.newexpr(Token.NUMBER, 13));
    checkSideEffects.process(Node.toCompilationUnit(root), root);
    assertFalse("Comma operator is not a problem", checkSideEffects.problemNodes.isEmpty());
  }

  @Test
  public void testProcess_RootIsStatementBlockWithOneEmptyStatement() {
    String code = "var x = 42;\n";
    Node root = Node.newBlock(code.length(), parseStatements(code));
    checkSideEffects.process(Node.toCompilationUnit(root), root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testProcess_RootIsStatementBlockWithOneStatementThatLacksSideEffects() {
    String code = "var x = 42;\n" +
        "var y = x + 42;";
    Node root = Node.newBlock(code.length(), parseStatements(code));
    checkSideEffects.process(Node.toCompilationUnit(root), root);
    assertEquals(1, checkSideEffects.problemNodes.size());
    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(Token.PLUS, problemNode.getType());
  }

  @Test(expected = AssertionError.class)
  public void testProcess_RootIsStatementBlockWithOneStatementThatHasSideEffects() {
    String code = "var x = 42;\n" +
        "document.write(\"Hello world\");";
    Node root = Node.newBlock(code.length(), parseStatements(code));
    checkSideEffects.process(Node.toCompilationUnit(root), root);
  }

  @Test
  public void testProtectSideEffects() {
    String code = "function foo() { return 42; }";
    Node root = Node.newBlock(code.length(), parseStatements(code));
    checkSideEffects.process(Node.toCompilationUnit(root), root);
    checkSideEffects.protectSideEffects();
    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertNotNull("Protected node is null", problemNode);
    Node protectedNode = problemNode.getParent().getLastChild();
    Node expectedProtector = Node.newexpr(Token.CALL, Node.newexpr(Token.NAME, "JSCOMPILER_PRESERVE"), ImmutableList.of(protectedNode));
    assertEquals("Protected node is not a call to JSCOMPILER_PRESERVE", expectedProtector, protectedNode);
  }

  private List<Node> parseStatements(String code) {
    Node externs = Node.toCompilationUnit(Node.EMPTY());
    Node root = Node.parse(code, null, externs, null);
    return NodeUtil.statementsToList(root);
  }
}