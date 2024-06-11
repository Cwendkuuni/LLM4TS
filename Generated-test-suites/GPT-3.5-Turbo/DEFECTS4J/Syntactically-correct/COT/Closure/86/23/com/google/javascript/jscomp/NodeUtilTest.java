
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  private NodeUtil nodeUtil;
  
  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    
    boolean result = nodeUtil.isForIn(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsForIn_false() {
    Node node = new Node(Token.FOR);
    node.addChildToFront(new Node());
    node.addChildToFront(new Node());
    
    boolean result = nodeUtil.isForIn(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    
    boolean result = nodeUtil.isStatement(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsStatement_false() {
    Node node = new Node(Token.NAME);
    
    boolean result = nodeUtil.isStatement(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(new Node());
    
    boolean result = nodeUtil.isFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration_false() {
    Node node = new Node(Token.VAR);
    node.addChildToFront(new Node());
    
    boolean result = nodeUtil.isFunctionDeclaration(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.FUNCTION);
    grandParent.addChildToFront(parent);
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(grandParent);
    
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_false() {
    Node parent = new Node(Token.SCRIPT);
    Node grandParent = new Node(Token.SCRIPT);
    grandParent.addChildToFront(parent);
    Node node = new Node(Token.FUNCTION);
    node.addChildToFront(grandParent);
    
    boolean result = nodeUtil.isHoistedFunctionDeclaration(node);
    
    assertFalse(result);
  }
}

