
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    // Test case 1: FOR node with 3 children
    Node testNode1 = new Node(Token.FOR);
    testNode1.addChildToBack(new Node());
    testNode1.addChildToBack(new Node());
    testNode1.addChildToBack(new Node());
    
    boolean result = NodeUtil.isForIn(testNode1);
    assertTrue(result);

    // Test case 2: FOR node with less than 3 children
    Node testNode2 = new Node(Token.FOR);
    testNode2.addChildToBack(new Node());
    testNode2.addChildToBack(new Node());

    boolean result = NodeUtil.isForIn(testNode2);
    assertFalse(result);

    // Test case 3: Non-FOR node
    Node testNode3 = new Node(Token.WHILE);

    boolean result = NodeUtil.isForIn(testNode3);
    assertFalse(result);
  }

  @Test
  public void testIsStatement() {
    // Test case 1: SCRIPT parent node
    Node testNode1 = new Node();
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(testNode1);
    
    boolean result = NodeUtil.isStatement(testNode1);
    assertTrue(result);

    // Test case 2: BLOCK parent node
    Node testNode2 = new Node();
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(testNode2);

    boolean result = NodeUtil.isStatement(testNode2);
    assertTrue(result);

    // Test case 3: LABEL parent node
    Node testNode3 = new Node();
    Node labelNode = new Node(Token.LABEL);
    labelNode.addChildToBack(testNode3);

    boolean result = NodeUtil.isStatement(testNode3);
    assertTrue(result);

    // Test case 4: Non-statement parent node
    Node testNode4 = new Node();
    Node exprNode = new Node(Token.EXPR_RESULT);
    exprNode.addChildToBack(testNode4);

    boolean result = NodeUtil.isStatement(testNode4);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Test case 1: FUNCTION node with statement parent
    Node testNode1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(testNode1);

    boolean result = NodeUtil.isFunctionDeclaration(testNode1);
    assertTrue(result);

    // Test case 2: FUNCTION node with non-statement parent
    Node testNode2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.EXPR_RESULT);
    parent2.addChildToBack(testNode2);

    boolean result = NodeUtil.isFunctionDeclaration(testNode2);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Test case 1: Valid hoisted function declaration
    Node testNode1 = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    parent1.addChildToBack(testNode1);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(testNode1);
    assertTrue(result);

    // Test case 2: Non-hoisted function declaration
    Node testNode2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.BLOCK);
    parent2.addChildToBack(testNode2);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(testNode2);
    assertFalse(result);

    // Test case 3: Non-function node
    Node testNode3 = new Node(Token.VAR);

    boolean result = NodeUtil.isHoistedFunctionDeclaration(testNode3);
    assertFalse(result);
  }
}
