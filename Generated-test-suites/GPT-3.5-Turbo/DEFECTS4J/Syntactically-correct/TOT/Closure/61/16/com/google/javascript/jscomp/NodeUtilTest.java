import org.junit.Assert;
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
  
  // Test case for isForIn method
  
  @Test
  public void testIsForIn() {
    // Typical use case
    Node n1 = new Node(Token.FOR);
    Node c1 = new Node(Token.NAME);
    Node c2 = new Node(Token.STRING);
    Node c3 = new Node(Token.NUMBER);
    
    n1.addChildToFront(c1);
    n1.addChildAfter(c2, c1);
    n1.addChildAfter(c3, c2);
    
    Assert.assertTrue(nodeUtil.isForIn(n1));
    
    // Edge case
    Node n2 = new Node(Token.FOR);
    Node c4 = new Node(Token.NAME);
    
    n2.addChildToFront(c4);
    
    Assert.assertFalse(nodeUtil.isForIn(n2));
    
    // Error scenario
    Node n3 = new Node(Token.IF);
    Node c5 = new Node(Token.STRING);
    Node c6 = new Node(Token.TRUE);
    
    n3.addChildToFront(c5);
    n3.addChildAfter(c6, c5);
    
    try {
      nodeUtil.isForIn(n3);
      Assert.fail("Expected an IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
  
  // Test case for isStatement method
  
  @Test
  public void testIsStatement() {
    // Typical use case
    Node n1 = new Node(Token.NAME);
    Node p1 = new Node(Token.SCRIPT);
    
    n1.setParent(p1);
    
    Assert.assertTrue(nodeUtil.isStatement(n1));
    
    // Edge case
    Node n2 = new Node(Token.ARRAYLIT);
    Node p2 = new Node(Token.NAME);
    
    n2.setParent(p2);
    
    Assert.assertFalse(nodeUtil.isStatement(n2));
    
    // Error scenario
    Node n3 = new Node(Token.OBJECTLIT);
    Node p3 = null;
    
    n3.setParent(p3);
    
    try {
      nodeUtil.isStatement(n3);
      Assert.fail("Expected an IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
  
  // Test case for isFunctionDeclaration method
  
  @Test
  public void testIsFunctionDeclaration() {
    // Typical use case
    Node n1 = new Node(Token.FUNCTION);
    Node p1 = new Node(Token.SCRIPT);
    
    n1.setParent(p1);
    
    Assert.assertTrue(nodeUtil.isFunctionDeclaration(n1));
    
    // Edge case
    Node n2 = new Node(Token.NAME);
    Node p2 = new Node(Token.BLOCK);
    
    n2.setParent(p2);
    
    Assert.assertFalse(nodeUtil.isFunctionDeclaration(n2));
    
    // Error scenario
    Node n3 = new Node(Token.DO);
    Node p3 = new Node(Token.SCRIPT);
    
    n3.setParent(p3);
    
    try {
      nodeUtil.isFunctionDeclaration(n3);
      Assert.fail("Expected an IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
  
  // Test case for isHoistedFunctionDeclaration method
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use case
    Node n1 = new Node(Token.FUNCTION);
    Node p1 = new Node(Token.BLOCK);
    Node gp1 = new Node(Token.SCRIPT);
    
    n1.setParent(p1);
    p1.setParent(gp1);
    
    Assert.assertTrue(nodeUtil.isHoistedFunctionDeclaration(n1));
    
    // Edge case
    Node n2 = new Node(Token.NAME);
    Node p2 = new Node(Token.NAME);
    Node gp2 = new Node(Token.SCRIPT);
    
    n2.setParent(p2);
    p2.setParent(gp2);
    
    Assert.assertFalse(nodeUtil.isHoistedFunctionDeclaration(n2));
    
    // Error scenario
    Node n3 = new Node(Token.OBJECTLIT);
    Node p3 = null;
    
    n3.setParent(p3);
    
    try {
      nodeUtil.isHoistedFunctionDeclaration(n3);
      Assert.fail("Expected an IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}