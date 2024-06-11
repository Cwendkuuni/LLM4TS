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
  public void testIsForIn_True() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    
    assertTrue(nodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsForIn_False() {
    Node node = new Node(Token.WHILE);
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    
    assertFalse(nodeUtil.isForIn(node));
  }
  
  @Test
  public void testIsStatement_True() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertTrue(nodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsStatement_False() {
    Node node = new Node(Token.ADD);
    
    assertFalse(nodeUtil.isStatement(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertTrue(nodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsFunctionDeclaration_False() {
    Node node = new Node(Token.ADD);
    
    assertFalse(nodeUtil.isFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    Node node = new Node(Token.FUNCTION);
    Node scriptParent = new Node(Token.SCRIPT);
    Node functionParent = new Node(Token.FUNCTION);
    scriptParent.addChildToFront(node);
    functionParent.addChildToFront(scriptParent);
    node.setParent(scriptParent);
    scriptParent.setParent(functionParent);
    
    assertTrue(nodeUtil.isHoistedFunctionDeclaration(node));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(node);
    node.setParent(parent);
    
    assertFalse(nodeUtil.isHoistedFunctionDeclaration(node));
  }
}