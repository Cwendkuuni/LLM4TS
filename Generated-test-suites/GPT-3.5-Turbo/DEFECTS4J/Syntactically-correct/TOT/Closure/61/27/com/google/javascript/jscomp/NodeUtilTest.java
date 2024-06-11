import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_withForInLoop_shouldReturnTrue() {
    Node forInLoop = new Node(Token.FOR);
    forInLoop.addChildToBack(new Node(Token.EMPTY));
    forInLoop.addChildToBack(new Node(Token.EMPTY));
    forInLoop.addChildToBack(new Node(Token.EMPTY));

    boolean result = nodeUtil.isForIn(forInLoop);

    assertEquals(true, result);
  }

  @Test
  public void testIsForIn_withNonForInLoop_shouldReturnFalse() {
    Node nonForInLoop = new Node(Token.WHILE);

    boolean result = nodeUtil.isForIn(nonForInLoop);

    assertEquals(false, result);
  }

  @Test
  public void testIsStatement_withStatementNode_shouldReturnTrue() {
    Node statementNode = new Node(Token.SCRIPT);

    boolean result = nodeUtil.isStatement(statementNode);

    assertEquals(true, result);
  }

  @Test
  public void testIsStatement_withNonStatementNode_shouldReturnFalse() {
    Node nonStatementNode = new Node(Token.GETPROP);

    boolean result = nodeUtil.isStatement(nonStatementNode);

    assertEquals(false, result);
  }

  @Test
  public void testIsFunctionDeclaration_withFunctionDeclaration_shouldReturnTrue() {
    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.addChildToBack(new Node(Token.NAME));

    boolean result = nodeUtil.isFunctionDeclaration(functionDeclaration);

    assertEquals(true, result);
  }

  @Test
  public void testIsFunctionDeclaration_withNonFunctionDeclaration_shouldReturnFalse() {
    Node nonFunctionDeclaration = new Node(Token.EXPR_RESULT);

    boolean result = nodeUtil.isFunctionDeclaration(nonFunctionDeclaration);

    assertEquals(false, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withHoistedFunctionDeclaration_shouldReturnTrue() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
    hoistedFunctionDeclaration.addChildToBack(new Node(Token.NAME));
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(hoistedFunctionDeclaration);

    boolean result = nodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration);

    assertEquals(true, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_withNonHoistedFunctionDeclaration_shouldReturnFalse() {
    Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION);
    nonHoistedFunctionDeclaration.addChildToBack(new Node(Token.NAME));

    boolean result = nodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration);

    assertEquals(false, result);
  }
}