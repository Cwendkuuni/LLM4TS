import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Typical Use Case
    Node node1 = new Node(Token.FOR);
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    node1.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(node1));
    
    // Edge Case: Empty node
    Node node2 = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(node2));
    
    // Error Scenario: Different token type
    Node node3 = new Node(Token.CONTINUE);
    assertFalse(NodeUtil.isForIn(node3));
  }
  
  @Test
  public void testIsStatement() {
    // Typical Use Case
    Node node1 = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(node1));
    
    // Edge Case: Statement as part of a block node
    Node node2 = new Node(Token.BLOCK);
    node2.addChildToBack(new Node(Token.SCRIPT));
    assertTrue(NodeUtil.isStatement(node2.getFirstChild()));
    
    // Error Scenario: Statement as part of a function node
    Node node3 = new Node(Token.FUNCTION);
    node3.addChildToBack(new Node(Token.SCRIPT));
    assertFalse(NodeUtil.isStatement(node3.getFirstChild()));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    // Typical Use Case
    Node node1 = new Node(Token.FUNCTION);
    Node scriptNode1 = new Node(Token.SCRIPT);
    scriptNode1.addChildToBack(node1);
    assertTrue(NodeUtil.isFunctionDeclaration(node1));
    
    // Edge Case: Function declaration inside a block node
    Node node2 = new Node(Token.FUNCTION);
    Node blockNode2 = new Node(Token.BLOCK);
    blockNode2.addChildToBack(new Node(Token.SCRIPT));
    blockNode2.getFirstChild().addChildToBack(node2);
    assertTrue(NodeUtil.isFunctionDeclaration(node2));
    
    // Error Scenario: Function declaration inside an expression node
    Node node3 = new Node(Token.FUNCTION);
    Node exprNode3 = new Node(Token.EXPR_RESULT);
    exprNode3.addChildToBack(node3);
    assertFalse(NodeUtil.isFunctionDeclaration(node3));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical Use Case
    Node node1 = new Node(Token.FUNCTION);
    Node scriptNode1 = new Node(Token.SCRIPT);
    scriptNode1.addChildToBack(node1);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
    
    // Edge Case: Hoisted function declaration inside a function node
    Node node2 = new Node(Token.FUNCTION);
    Node functionNode2 = new Node(Token.FUNCTION);
    functionNode2.addChildToBack(new Node(Token.SCRIPT));
    functionNode2.getFirstChild().addChildToBack(node2);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));
    
    // Error Scenario: Function declaration inside a non-root script node
    Node node3 = new Node(Token.FUNCTION);
    Node scriptNode3 = new Node(Token.SCRIPT);
    scriptNode3.addChildToBack(node3);
    Node scriptNode4 = new Node(Token.SCRIPT);
    scriptNode4.addChildToBack(scriptNode3);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
  }
}