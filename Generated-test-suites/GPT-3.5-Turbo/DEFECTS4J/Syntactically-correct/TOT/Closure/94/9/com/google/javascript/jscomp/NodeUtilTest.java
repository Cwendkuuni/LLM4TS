import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

   @Test
   public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
      Node n = createForInNode();
      boolean result = NodeUtil.isForIn(n);
      assertTrue(result);
   }
   
   @Test
   public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
      Node n = createRegularNode();
      boolean result = NodeUtil.isForIn(n);
      assertFalse(result);
   }
   
   @Test
   public void testIsStatement_WhenNodeIsPartOfScript_ReturnsTrue() {
      Node n = createStatementInScript();
      boolean result = NodeUtil.isStatement(n);
      assertTrue(result);
   }
   
   @Test
   public void testIsStatement_WhenNodeIsPartOfBlock_ReturnsTrue() {
      Node n = createStatementInBlock();
      boolean result = NodeUtil.isStatement(n);
      assertTrue(result);
   }
   
   @Test
   public void testIsStatement_WhenNodeIsPartOfLabel_ReturnsTrue() {
      Node n = createStatementInLabel();
      boolean result = NodeUtil.isStatement(n);
      assertTrue(result);
   }
   
   @Test
   public void testIsStatement_WhenNodeIsNotPartOfScriptOrBlockOrLabel_ReturnsFalse() {
      Node n = createNonStatementNode();
      boolean result = NodeUtil.isStatement(n);
      assertFalse(result);
   }
   
   @Test
   public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclarationAndStatement_ReturnsTrue() {
      Node n = createFunctionDeclarationNode();
      boolean result = NodeUtil.isFunctionDeclaration(n);
      assertTrue(result);
   }
   
   @Test
   public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclarationOrNotStatement_ReturnsFalse() {
      Node n = createNonFunctionDeclarationNode();
      boolean result = NodeUtil.isFunctionDeclaration(n);
      assertFalse(result);
   }
   
   @Test
   public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
      Node n = createHoistedFunctionDeclarationNode();
      boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
      assertTrue(result);
   }
   
   @Test
   public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
      Node n = createNonHoistedFunctionDeclarationNode();
      boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
      assertFalse(result);
   }
   
   private Node createForInNode() {
      Node n = new Node(1, Token.FOR);
      Node child1 = new Node(2, Token.EXPR_RESULT);
      Node child2 = new Node(3, Token.VAR);
      Node child3 = new Node(4, Token.STRING);
      n.addChildToBack(child1);
      n.addChildToBack(child2);
      n.addChildToBack(child3);
      return n;
   }
   
   private Node createRegularNode() {
      Node n = new Node(1, Token.REGULAR);
      return n;
   }
   
   private Node createStatementInScript() {
      Node n = new Node(1, Token.SCRIPT);
      Node child = new Node(2, Token.EXPR_RESULT);
      n.addChildToBack(child);
      return child;
   }
   
   private Node createStatementInBlock() {
      Node n = new Node(1, Token.BLOCK);
      Node child = new Node(2, Token.EXPR_RESULT);
      n.addChildToBack(child);
      return child;
   }
   
   private Node createStatementInLabel() {
      Node n = new Node(1, Token.LABEL);
      Node child = new Node(2, Token.EXPR_RESULT);
      n.addChildToBack(child);
      return child;
   }
   
   private Node createNonStatementNode() {
      Node n = new Node(1, Token.REGULAR);
      return n;
   }
   
   private Node createFunctionDeclarationNode() {
      Node n = new Node(1, Token.FUNCTION);
      Node parent = new Node(2, Token.SCRIPT);
      n.setParent(parent);
      return n;
   }
   
   private Node createNonFunctionDeclarationNode() {
      Node n = new Node(1, Token.REGULAR);
      return n;
   }
   
   private Node createHoistedFunctionDeclarationNode() {
      Node n = new Node(1, Token.FUNCTION);
      Node parent1 = new Node(2, Token.SCRIPT);
      Node parent2 = new Node(3, Token.FUNCTION);
      n.setParent(parent1);
      parent1.setParent(parent2);
      return n;
   }
   
   private Node createNonHoistedFunctionDeclarationNode() {
      Node n = new Node(1, Token.FUNCTION);
      Node parent = new Node(2, Token.BLOCK);
      n.setParent(parent);
      return n;
   }
}