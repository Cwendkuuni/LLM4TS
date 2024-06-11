import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn_WithForInLoop_ReturnTrue() {
    Node forInLoop = new Node(Token.FOR);
    forInLoop.addChildToBack(new Node(Token.EMPTY));
    forInLoop.addChildToBack(new Node(Token.EMPTY));
    forInLoop.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(forInLoop);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsForIn_WithNonForInLoop_ReturnFalse() {
    Node nonForInLoop = new Node(Token.EMPTY);

    boolean result = NodeUtil.isForIn(nonForInLoop);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement_WithStatementNode_ReturnTrue() {
    Node parent = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.EMPTY);
    parent.addChildToBack(statementNode);

    boolean result = NodeUtil.isStatement(statementNode);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsStatement_WithNonStatementNode_ReturnFalse() {
    Node parent = new Node(Token.EMPTY);
    Node nonStatementNode = new Node(Token.EMPTY);
    parent.addChildToBack(nonStatementNode);

    boolean result = NodeUtil.isStatement(nonStatementNode);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_WithFunctionDeclaration_ReturnTrue() {
    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isFunctionDeclaration(functionDeclaration);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_WithNonFunctionDeclaration_ReturnFalse() {
    Node nonFunctionDeclaration = new Node(Token.EMPTY);

    boolean result = NodeUtil.isFunctionDeclaration(nonFunctionDeclaration);

    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithHoistedFunctionDeclaration_ReturnTrue() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
    hoistedFunctionDeclaration.addChildToBack(new Node(Token.EMPTY));
    hoistedFunctionDeclaration.getParent().addChildToBack(new Node(Token.SCRIPT));

    boolean result = NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration);

    Assert.assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_WithNonHoistedFunctionDeclaration_ReturnFalse() {
    Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION);
    nonHoistedFunctionDeclaration.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration);

    Assert.assertFalse(result);
  }
}