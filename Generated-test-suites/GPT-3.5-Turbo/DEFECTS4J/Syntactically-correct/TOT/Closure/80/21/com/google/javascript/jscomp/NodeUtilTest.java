import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

   @Test
   public void testIsForIn() {
      // Typical use case
      Node node1 = new Node(Token.FOR);
      node1.addChildToBack(new Node());
      node1.addChildToBack(new Node());
      node1.addChildToBack(new Node());
      assertTrue(NodeUtil.isForIn(node1));

      // Edge case: Only two children
      Node node2 = new Node(Token.FOR);
      node2.addChildToBack(new Node());
      node2.addChildToBack(new Node());
      assertFalse(NodeUtil.isForIn(node2));

      // Edge case: More than three children
      Node node3 = new Node(Token.FOR);
      node3.addChildToBack(new Node());
      node3.addChildToBack(new Node());
      node3.addChildToBack(new Node());
      node3.addChildToBack(new Node());
      assertFalse(NodeUtil.isForIn(node3));

      // Error scenario: Node type not Token.FOR
      Node node4 = new Node(Token.VAR);
      node4.addChildToBack(new Node());
      node4.addChildToBack(new Node());
      node4.addChildToBack(new Node());
      assertFalse(NodeUtil.isForIn(node4));
   }

   @Test
   public void testIsStatement() {
      // Typical use case: Parent type is Token.SCRIPT
      Node parent1 = new Node(Token.SCRIPT);
      Node node1 = new Node();
      parent1.addChildToBack(node1);
      assertTrue(NodeUtil.isStatement(node1));

      // Typical use case: Parent type is Token.BLOCK
      Node parent2 = new Node(Token.BLOCK);
      Node node2 = new Node();
      parent2.addChildToBack(node2);
      assertTrue(NodeUtil.isStatement(node2));

      // Typical use case: Parent type is Token.LABEL
      Node parent3 = new Node(Token.LABEL);
      Node node3 = new Node();
      parent3.addChildToBack(node3);
      assertTrue(NodeUtil.isStatement(node3));

      // Error scenario: Parent type is Token.VAR
      Node parent4 = new Node(Token.VAR);
      Node node4 = new Node();
      parent4.addChildToBack(node4);
      assertFalse(NodeUtil.isStatement(node4));
   }

   @Test
   public void testIsFunctionDeclaration() {
      // Typical use case: Node type is Token.FUNCTION and isStatement() returns true
      Node node1 = new Node(Token.FUNCTION);
      NodeUtil stub1 = Mockito.mock(NodeUtil.class);
      Mockito.when(stub1.isStatement(node1)).thenReturn(true);
      assertTrue(stub1.isFunctionDeclaration(node1));

      // Error scenario: Node type is Token.VAR and isStatement() returns true
      Node node2 = new Node(Token.VAR);
      NodeUtil stub2 = Mockito.mock(NodeUtil.class);
      Mockito.when(stub2.isStatement(node2)).thenReturn(true);
      assertFalse(stub2.isFunctionDeclaration(node2));

      // Error scenario: Node type is Token.FUNCTION but isStatement() returns false
      Node node3 = new Node(Token.FUNCTION);
      NodeUtil stub3 = Mockito.mock(NodeUtil.class);
      Mockito.when(stub3.isStatement(node3)).thenReturn(false);
      assertFalse(stub3.isFunctionDeclaration(node3));
   }

   @Test
   public void testIsHoistedFunctionDeclaration() {
      // Typical use case: Function declaration in scope root
      Node parent1 = new Node(Token.SCRIPT);
      Node node1 = new Node(Token.FUNCTION);
      parent1.addChildToBack(node1);
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));

      // Typical use case: Function declaration in function
      Node parent2 = new Node(Token.FUNCTION);
      Node grandParent2 = new Node(Token.SCRIPT);
      grandParent2.addChildToBack(parent2);
      parent2.addChildToBack(new Node());
      Node node2 = new Node(Token.FUNCTION);
      parent2.addChildToBack(node2);
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(node2));

      // Error scenario: Function declaration not in scope root or function
      Node parent3 = new Node(Token.BLOCK);
      Node parent4 = new Node(Token.LABEL);
      Node node3 = new Node(Token.FUNCTION);
      parent3.addChildToBack(node3);
      parent4.addChildToBack(node3);
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));

      // Error scenario: Not a function declaration
      Node parent5 = new Node(Token.SCRIPT);
      Node node5 = new Node(Token.VAR);
      parent5.addChildToBack(node5);
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(node5));
   }
}