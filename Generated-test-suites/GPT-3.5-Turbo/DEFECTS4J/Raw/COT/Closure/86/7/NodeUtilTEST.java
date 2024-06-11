
// JUnit 4 test cases for NodeUtil class

import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

   @Test
   public void testIsForIn() {
       Node node = new Node(Token.FOR);
       Node child1 = new Node(Token.EMPTY);
       Node child2 = new Node(Token.EMPTY);
       Node child3 = new Node(Token.EMPTY);
       node.addChildToBack(child1);
       node.addChildToBack(child2);
       node.addChildToBack(child3);

       assertTrue(NodeUtil.isForIn(node));
   }

   @Test
   public void testIsStatement() {
       Node node = new Node(Token.EMPTY);
       Node parent = new Node(Token.SCRIPT);
       parent.addChildToBack(node);
       node.setParent(parent);

       assertTrue(NodeUtil.isStatement(node));
   }

   @Test
   public void testIsFunctionDeclaration() {
       Node node = new Node(Token.FUNCTION);
       Node parent = new Node(Token.SCRIPT);
       parent.addChildToBack(node);
       node.setParent(parent);

       assertTrue(NodeUtil.isFunctionDeclaration(node));
   }

   @Test
   public void testIsHoistedFunctionDeclaration() {
       Node node = new Node(Token.FUNCTION);
       Node parent1 = new Node(Token.SCRIPT);
       parent1.addChildToBack(node);
       node.setParent(parent1);
       Node parent2 = new Node(Token.FUNCTION);
       parent2.addChildToBack(parent1);
       parent1.setParent(parent2);

       assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
   }

   @Test
   public void testIsForIn_NotForIn() {
       Node node = new Node(Token.WHILE);
       Node child1 = new Node(Token.EMPTY);
       Node child2 = new Node(Token.EMPTY);
       node.addChildToBack(child1);
       node.addChildToBack(child2);

       assertFalse(NodeUtil.isForIn(node));
   }

   @Test
   public void testIsStatement_NotStatement() {
       Node node = new Node(Token.EMPTY);

       assertFalse(NodeUtil.isStatement(node));
   }

   @Test
   public void testIsFunctionDeclaration_NotFunctionDeclaration() {
       Node node = new Node(Token.EMPTY);

       assertFalse(NodeUtil.isFunctionDeclaration(node));
   }

   @Test
   public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
       Node node = new Node(Token.FUNCTION);

       assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
   }

}