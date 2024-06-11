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
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    node.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsNotForIn() {
    Node node = new Node(Token.FUNCTION);
    
    assertFalse(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    parent.addChildToBack(node);
    
    assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsNotStatement() {
    Node parent = new Node(Token.FUNCTION);
    Node node = new Node(Token.EMPTY);
    parent.addChildToBack(node);
    
    assertFalse(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));
    parent.addChildToBack(node);
    
    assertTrue(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.EMPTY);
    parent.addChildToBack(node);
    
    assertFalse(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node grandParent = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION);
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));
    parent.addChildToBack(node);
    grandParent.addChildToBack(parent);
    
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node node = new Node(Token.FUNCTION);
    node.addChildToBack(new Node(Token.EMPTY));
    parent.addChildToBack(node);
    
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
  }
  
}