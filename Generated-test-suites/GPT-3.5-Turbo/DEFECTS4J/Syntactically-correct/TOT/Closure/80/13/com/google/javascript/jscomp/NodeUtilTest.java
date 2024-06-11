import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalUseCase() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node(Token.FOR));
    node.addChildToBack(new Node(Token.FOR));
    node.addChildToBack(new Node(Token.FOR));

    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_edgeCase() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.FOR));
    node.addChildToBack(new Node(Token.FOR));
    node.addChildToFront(new Node(Token.FOR));

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_errorScenario() {
    Node node = new Node(Token.IN);

    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_typicalUseCase() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.FOR));
    node.addChildToBack(new Node(Token.BLOCK));

    assertTrue(NodeUtil.isStatement(node.getChildAtIndex(0)));
  }

  @Test
  public void testIsStatement_edgeCase() {
    Node node = new Node(Token.BLOCK);

    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_errorScenario() {
    Node node = new Node(Token.FUNCTION);

    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typicalUseCase() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.BLOCK));

    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_edgeCase() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.LABEL));

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_errorScenario() {
    Node node = new Node(Token.EMPTY);

    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalUseCase() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.SCRIPT));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_edgeCase() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.SCRIPT));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_errorScenario() {
    Node node = new Node(Token.SCRIPT);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

}