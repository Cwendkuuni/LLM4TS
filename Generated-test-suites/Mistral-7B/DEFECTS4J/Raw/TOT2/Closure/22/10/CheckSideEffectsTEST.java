import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects_NormalCase() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Compiler compiler = new Compiler();
    Node root = null;
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffects_EmptyCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Compiler compiler = new Compiler();
    Node root = new Node(Token.BLOCK, null);
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffects_SingleStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Compiler compiler = new Compiler();
    Node root = new Node(Token.BLOCK, null);
    root.addChildToBack(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 10)));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffects_SingleExpression() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Compiler compiler = new Compiler();
    Node root = new Node(Token.BLOCK, null);
    root.addChildToBack(new Node(Token.EXPR_RESULT, null, new Node(Token.CALL, null, new Node(Token.NAME, "Math"), new Node(Token.NUMBER, 2))));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffects_SingleStatementWithSideEffect() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Compiler compiler = new Compiler();
    Node root = new Node(Token.BLOCK, null);
    root.addChildToBack(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"))); // Side effect
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffects_MultipleStatements() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Compiler compiler = new Compiler();
    Node root = new Node(Token.BLOCK, null);
    root.addChildToBack(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 10)));
    root.addChildToBack(new Node(Token.ASSIGN, null, new Node(Token.NAME, "y"), new Node(Token.NUMBER, 20)));
    checkSideEffects.process(null, root);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCheckSideEffects_ProtectedCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, true);
    Compiler compiler = new Compiler();
    Node root = new Node(Token.BLOCK, null);
    root.addChildToBack(new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 10)));
    checkSideEffects.process(null, root);
    assertEquals(1, checkSideEffects.problemNodes.size());
  }
}