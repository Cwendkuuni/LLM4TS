import org.junit.*;
import static org.junit.Assert.*;

public class NodeUtilTest {

   @Test
   public void testIsForIn() {
      // Typical use case
      Node node1 = new Node(Token.FOR);
      node1.addChildToBack(new Node(Token.STRING));
      node1.addChildToBack(new Node(Token.STRING));
      node1.addChildToBack(new Node(Token.STRING));
      assertTrue(NodeUtil.isForIn(node1));

      // Edge case: Empty for-in loop
      Node node2 = new Node(Token.FOR);
      assertFalse(NodeUtil.isForIn(node2));

      // Error scenario: Node with type other than FOR
      Node node3 = new Node(Token.IF);
      assertFalse(NodeUtil.isForIn(node3));

      // Error scenario: Node with incorrect number of children
      Node node4 = new Node(Token.FOR);
      node4.addChildToBack(new Node(Token.STRING));
      assertFalse(NodeUtil.isForIn(node4));
   }

   @Test
   public void testIsStatement() {
      // Typical use case
      Node node1 = new Node(Token.SCRIPT);
      assertTrue(NodeUtil.isStatement(node1));

      // Typical use case
      Node node2 = new Node(Token.BLOCK);
      assertTrue(NodeUtil.isStatement(node2));

      // Typical use case
      Node node3 = new Node(Token.LABEL);
      assertTrue(NodeUtil.isStatement(node3));

      // Typical use case
      Node node4 = new Node(Token.STRING);
      Node parent = new Node(Token.SCRIPT);
      parent.addChildToBack(node4);
      node4.addChildToFront(parent);
      assertTrue(NodeUtil.isStatement(node4));

      // Edge case: Parent node is null
      Node node5 = new Node(Token.STRING);
      assertFalse(NodeUtil.isStatement(node5));

      // Error scenario: Node with type other than SCRIPT, BLOCK, or LABEL
      Node node6 = new Node(Token.WHILE);
      Node parent1 = new Node(Token.SCRIPT);
      parent1.addChildToBack(node6);
      node6.addChildToFront(parent1);
      assertFalse(NodeUtil.isStatement(node6));
   }

   @Test
   public void testIsFunctionDeclaration() {
      // Typical use case
      Node node1 = new Node(Token.FUNCTION);
      assertTrue(NodeUtil.isFunctionDeclaration(node1));

      // Edge case: Function is part of an expression
      Node node2 = new Node(Token.EXPR_RESULT);
      node2.addChildToBack(new Node(Token.FUNCTION));
      assertFalse(NodeUtil.isFunctionDeclaration(node2));

      // Error scenario: Node with type other than FUNCTION
      Node node3 = new Node(Token.STRING);
      assertFalse(NodeUtil.isFunctionDeclaration(node3));
   }

   @Test
   public void testIsHoistedFunctionDeclaration() {
      // Typical use case
      Node node1 = new Node(Token.FUNCTION);
      Node parent1 = new Node(Token.SCRIPT);
      parent1.addChildToBack(node1);
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

      // Typical use case
      Node node2 = new Node(Token.FUNCTION);
      Node parent2 = new Node(Token.FUNCTION);
      parent2.addChildToBack(node2);
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

      // Edge case: Function is not a function declaration
      Node node3 = new Node(Token.FUNCTION);
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));

      // Error scenario: Node with type other than FUNCTION
      Node node4 = new Node(Token.STRING);
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(node4));
   }
}