import org.junit.Test;
import static org.junit.Assert.*;

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
   public void testIsForInFalse() {
      Node n = new Node(Token.EMPTY);

      assertFalse(NodeUtil.isForIn(n));
   }

   @Test
   public void testIsStatement() {
      Node n = new Node(Token.EMPTY);
      Node parent = new Node(Token.SCRIPT);
      parent.addChildToBack(n);
      n.setParent(parent);

      assertTrue(NodeUtil.isStatement(n));
   }

   @Test
   public void testIsStatementFalse() {
      Node n = new Node(Token.EMPTY);
      Node parent = new Node(Token.EMPTY);
      parent.addChildToBack(n);
      n.setParent(parent);

      assertFalse(NodeUtil.isStatement(n));
   }

   @Test
   public void testIsFunctionDeclaration() {
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.TYPEOF);
      parent.addChildToBack(n);
      n.setParent(parent);

      assertTrue(NodeUtil.isFunctionDeclaration(n));
   }

   @Test
   public void testIsFunctionDeclarationFalse() {
      Node n = new Node(Token.EMPTY);
      Node parent = new Node(Token.TYPEOF);
      parent.addChildToBack(n);
      n.setParent(parent);

      assertFalse(NodeUtil.isFunctionDeclaration(n));
   }

   @Test
   public void testIsHoistedFunctionDeclaration() {
      Node n = new Node(Token.FUNCTION);
      Node parent1 = new Node(Token.SCRIPT);
      parent1.addChildToBack(n);
      n.setParent(parent1);
      Node grandParent = new Node(Token.SCRIPT);
      grandParent.addChildToBack(parent1);
      parent1.setParent(grandParent);

      assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
   }

   @Test
   public void testIsHoistedFunctionDeclarationFalse() {
      Node n = new Node(Token.FUNCTION);
      Node parent = new Node(Token.FUNCTION);
      parent.addChildToBack(n);
      n.setParent(parent);

      assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
   }
}