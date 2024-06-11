import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  private static NodeUtil nodeUtil;

  @BeforeClass
  public static void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn_True() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.FOR));
    node.addChildToFront(new Node(Token.FOR));
    boolean result = nodeUtil.isForIn(node);
    assertTrue(result);
  }

  @Test
  public void testIsForIn_False() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.EXPR_RESULT));
    node.addChildToFront(new Node(Token.FOR));
    boolean result = nodeUtil.isForIn(node);
    assertFalse(result);
  }

  @Test
  public void testIsStatement_True() {
    Node node = new Node(Token.SCRIPT);
    boolean result = nodeUtil.isStatement(node);
    assertTrue(result);
  }

  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.NAME);
    boolean result = nodeUtil.isStatement(node);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    boolean result = nodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.SCRIPT);
    boolean result = nodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    node.getParent().setType(Token.SCRIPT);
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    node.getParent().getParent().setType(Token.SCRIPT);
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }
}