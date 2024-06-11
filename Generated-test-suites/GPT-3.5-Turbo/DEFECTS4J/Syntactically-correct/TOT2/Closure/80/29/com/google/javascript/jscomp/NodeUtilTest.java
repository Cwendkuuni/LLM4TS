import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node script = new Node(Token.SCRIPT);
    Node n = new Node(Token.EMPTY);
    script.addChildToBack(n);
    
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsNotStatement() {
    Node parent = new Node(Token.FUNCTION);
    Node n = new Node(Token.EMPTY);
    parent.addChildToBack(n);
    
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node variableNode = new Node(Token.VAR);
    
    assertFalse(NodeUtil.isFunctionDeclaration(variableNode));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(functionNode);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(functionNode);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}