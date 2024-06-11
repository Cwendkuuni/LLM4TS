import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isForIn(n);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.WHILE);
    
    boolean result = NodeUtil.isForIn(n);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.NAME);
    
    boolean result = NodeUtil.isStatement(n);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.NAME);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    Assert.assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    Assert.assertTrue(result);
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandparent = new Node(Token.FUNCTION);
    grandparent.addChildToBack(parent);
    parent.addChildToBack(n);
    n.setParent(parent);
    parent.setParent(grandparent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    Assert.assertFalse(result);
  }
}