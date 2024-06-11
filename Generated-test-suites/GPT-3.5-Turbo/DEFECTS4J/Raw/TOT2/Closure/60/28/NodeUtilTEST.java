import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

   @Test
   public void testIsForIn() {
      // Typical use case
      assertFalse(NodeUtil.isForIn(new Node(Token.FUNCTION, new Node(Token.NAME))));
   }

   @Test
   public void testIsStatement() {
      // Typical use case
      Node parent = new Node(Token.SCRIPT);
      Node testNode = new Node(Token.VAR);
      testNode.setParent(parent);
      assertTrue(NodeUtil.isStatement(testNode));

      // Edge case
      Node noParentNode = new Node(Token.VAR);
      assertFalse(NodeUtil.isStatement(noParentNode));
   }

   @Test
   public void testIsFunctionDeclaration() {
      // Typical use case
      Node testNode = new Node(Token.FUNCTION);
      assertTrue(NodeUtil.isFunctionDeclaration(testNode));

      // Edge case
      Node statementNode = new Node(Token.IF);
      assertFalse(NodeUtil.isFunctionDeclaration(statementNode));
   }

   @Test
   public void testIsHoistedFunctionDeclaration() {
      // Typical use case
      Node parent = new Node(Token.SCRIPT);
      Node grandparent = new Node(Token.FUNCTION);
      Node testNode = new Node(Token.FUNCTION);
      testNode.setParent(parent);
      parent.setParent(grandparent);
      assertTrue(NodeUtil.isHoistedFunctionDeclaration(testNode));

      // Edge case
      Node noGrandparentNode = new Node(Token.FUNCTION);
      noGrandparentNode.setParent(parent);
      assertFalse(NodeUtil.isHoistedFunctionDeclaration(noGrandparentNode));
   }

}