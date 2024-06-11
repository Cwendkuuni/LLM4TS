import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

   @Test
   public void testIsForIn() {
       Node node = new Node(Token.FOR);
       node.addChildToBack(new Node(Token.EMPTY));
       node.addChildToBack(new Node(Token.EMPTY));
       node.addChildToBack(new Node(Token.EMPTY));
       
       assertTrue(NodeUtil.isForIn(node));
   }

   @Test
   public void testIsNotForIn() {
       Node node = new Node(Token.IF);
       
       assertFalse(NodeUtil.isForIn(node));
   }

   @Test
   public void testIsStatement() {
       Node parent = new Node(Token.SCRIPT);
       Node node = new Node(Token.EMPTY);
       parent.addChildToBack(node);
       
       assertTrue(NodeUtil.isStatement(node));
   }

   @Test
   public void testIsNotStatement() {
       Node node = new Node(Token.VAR);
       
       assertFalse(NodeUtil.isStatement(node));
   }

   @Test
   public void testIsFunctionDeclaration() {
       Node parent = new Node(Token.SCRIPT);
       Node node = new Node(Token.FUNCTION);
       parent.addChildToBack(node);
       
       assertTrue(NodeUtil.isFunctionDeclaration(node));
   }

   @Test
   public void testIsNotFunctionDeclaration() {
       Node node = new Node(Token.EMPTY);
       
       assertFalse(NodeUtil.isFunctionDeclaration(node));
   }

   @Test
   public void testIsHoistedFunctionDeclaration() {
       Node grandParent = new Node(Token.SCRIPT);
       Node parent = new Node(Token.FUNCTION);
       grandParent.addChildToBack(parent);
       Node node = new Node(Token.FUNCTION);
       parent.addChildToBack(node);
       
       assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
   }

   @Test
   public void testIsNotHoistedFunctionDeclaration() {
       Node parent = new Node(Token.SCRIPT);
       Node node = new Node(Token.FUNCTION);
       parent.addChildToBack(node);
       
       assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
   }
}