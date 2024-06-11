import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

   @Test
   public void testIsForIn() {
       Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
       assertTrue(NodeUtil.isForIn(node));
   }

   @Test
   public void testIsNotForIn() {
       Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));
       assertFalse(NodeUtil.isForIn(node));
   }

   @Test
   public void testIsStatement() {
       Node parent = new Node(Token.SCRIPT, new Node(Token.FUNCTION));
       Node node = new Node(Token.FUNCTION, parent);
       assertTrue(NodeUtil.isStatement(node));
   }

   @Test
   public void testIsNotStatement() {
       Node parent = new Node(Token.EMPTY);
       Node node = new Node(Token.FUNCTION, parent);
       assertFalse(NodeUtil.isStatement(node));
   }

   @Test
   public void testIsFunctionDeclaration() {
       Node node = new Node(Token.FUNCTION);
       assertTrue(NodeUtil.isFunctionDeclaration(node));
   }

   @Test
   public void testIsNotFunctionDeclaration() {
       Node node = new Node(Token.EMPTY);
       assertFalse(NodeUtil.isFunctionDeclaration(node));
   }

   @Test
   public void testIsHoistedFunctionDeclaration() {
       Node parent = new Node(Token.SCRIPT);
       Node node = new Node(Token.FUNCTION, parent);
       assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
   }

   @Test
   public void testIsNotHoistedFunctionDeclaration() {
       Node parent = new Node(Token.FUNCTION);
       Node node = new Node(Token.FUNCTION, parent);
       assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
   }
}