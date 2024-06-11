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
  public void testIsForIn_typicalUseCase() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node(Token.VAR));
    node.addChildToFront(new Node(Token.IN));
    node.addChildToFront(new Node(Token.BLOCK));
    
    assertTrue(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_noForInLoop() {
    Node node = new Node(Token.IF);
    node.addChildToFront(new Node(Token.BLOCK));
    
    assertFalse(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsForIn_missingForToken() {
    Node node = new Node(Token.VAR);
    node.addChildToFront(new Node(Token.VAR));
    node.addChildToFront(new Node(Token.IN));
    node.addChildToFront(new Node(Token.BLOCK));
    
    assertFalse(nodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_typicalUseCase() {
    Node node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.VAR));
    node.addChildToFront(new Node(Token.LET));
    
    assertTrue(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatement_notAStatement() {
    Node node = new Node(Token.IF);
    node.addChildToFront(new Node(Token.BLOCK));
    
    assertFalse(nodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_typicalUseCase() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node(Token.BLOCK));
    
    assertTrue(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_notAFunctionDeclaration() {
    Node node = new Node(Token.VAR);
    node.addChildToFront(new Node(Token.BLOCK));
    
    assertFalse(nodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalUseCase() {
    // Mocking a function declaration in the scope root
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    scriptNode.addChildToFront(functionNode);
    
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    // Mocking a function declaration not in the scope root
    Node blockNode = new Node(Token.BLOCK);
    Node functionNode = new Node(Token.FUNCTION);
    blockNode.addChildToFront(functionNode);
    
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}