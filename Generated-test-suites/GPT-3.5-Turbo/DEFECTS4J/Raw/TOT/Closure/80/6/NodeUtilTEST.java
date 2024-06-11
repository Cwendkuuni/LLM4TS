import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  // Test case for isForIn method
  @Test
  public void testIsForIn() {
    // Typical use cases
    assertTrue(NodeUtil.isForIn(createForInNode()));
    assertFalse(NodeUtil.isForIn(createNonForInNode()));
    
    // Edge case with empty for loop
    assertFalse(NodeUtil.isForIn(createEmptyForNode()));
    
    // Error scenario with null input
    assertFalse(NodeUtil.isForIn(null));
  }

  // Test case for isStatement method
  @Test
  public void testIsStatement() {
    // Typical use cases
    assertFalse(NodeUtil.isStatement(createNonStatementNode()));
    assertTrue(NodeUtil.isStatement(createStatementNode()));
    
    // Edge case with empty statement
    assertTrue(NodeUtil.isStatement(createEmptyStatementNode()));
    
    // Error scenario with null input
    assertFalse(NodeUtil.isStatement(null));
  }

  // Test case for isFunctionDeclaration method
  @Test
  public void testIsFunctionDeclaration() {
    // Typical use cases
    assertFalse(NodeUtil.isFunctionDeclaration(createNonFunctionDeclarationNode()));
    assertTrue(NodeUtil.isFunctionDeclaration(createFunctionDeclarationNode()));
    
    // Edge case with empty function declaration
    assertTrue(NodeUtil.isFunctionDeclaration(createEmptyFunctionDeclarationNode()));
    
    // Error scenario with null input
    assertFalse(NodeUtil.isFunctionDeclaration(null));
  }

  // Test case for isHoistedFunctionDeclaration method
  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Typical use cases
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNonHoistedFunctionDeclarationNode()));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(createHoistedFunctionDeclarationNode()));
    
    // Edge case with empty hoisted function declaration
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(createEmptyHoistedFunctionDeclarationNode()));
    
    // Error scenario with null input
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(null));
  }
  
  // Helper methods to create test nodes
  
  private Node createForInNode() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.TRUE));
    n.addChildToBack(new Node(Token.BLOCK));
    return n;
  }
  
  private Node createNonForInNode() {
    Node n = new Node(Token.LOOP);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.TRUE));
    n.addChildToBack(new Node(Token.BLOCK));
    return n;
  }
  
  private Node createEmptyForNode() {
    Node n = new Node(Token.FOR);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.TRUE));
    return n;
  }
  
  private Node createStatementNode() {
    Node n = new Node(Token.SCRIPT);
    n.addChildToFront(new Node(Token.NAME));
    return n;
  }
  
  private Node createNonStatementNode() {
    Node n = new Node(Token.EXPR_RESULT);
    n.addChildToFront(new Node(Token.NAME));
    return n;
  }
  
  private Node createEmptyStatementNode() {
    Node n = new Node(Token.SCRIPT);
    return n;
  }
  
  private Node createFunctionDeclarationNode() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    return n;
  }
  
  private Node createNonFunctionDeclarationNode() {
    Node n = new Node(Token.CALL);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    return n;
  }
  
  private Node createEmptyFunctionDeclarationNode() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    return n;
  }
  
  private Node createHoistedFunctionDeclarationNode() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    return n;
  }
  
  private Node createNonHoistedFunctionDeclarationNode() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    n.addChildToBack(new Node(Token.BLOCK));
    Node parent = new Node(Token.CALL);
    parent.addChildToBack(n);
    return n;
  }
  
  private Node createEmptyHoistedFunctionDeclarationNode() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME));
    return n;
  }

}