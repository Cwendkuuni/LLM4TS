import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  private NodeUtil nodeUtil;
  
  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }
  
  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    assertTrue(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForInFalse() {
    Node node = new Node(Token.WHILE);
    node.addChildToBack(new Node());
    node.addChildToBack(new Node());
    assertFalse(NodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.VAR);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    assertTrue(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatementFalse() {
    Node node = new Node(Token.VAR);
    assertFalse(NodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    node.setParent(parent);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclarationFalse() {
    Node node = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.SCRIPT);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(node);
    grandParent.addChildToBack(parent);
    node.setParent(parent);
    parent.setParent(grandParent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(node);
    node.setParent(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}