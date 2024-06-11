import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
   Node node = new Node(Token.FOR, new Node(Token.VAR), new Node(Token.EMPTY), new Node(Token.EMPTY));
   assertTrue(NodeUtil.isForIn(node));
 }

 @Test
 public void testIsStatement() {
   Node node = new Node(Token.SCRIPT);
   assertTrue(NodeUtil.isStatement(node));
 }

 @Test
 public void testIsFunctionDeclaration() {
   Node node = new Node(Token.FUNCTION);
   assertTrue(NodeUtil.isFunctionDeclaration(node));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
   Node scriptNode = new Node(Token.SCRIPT);
   Node functionNode = new Node(Token.FUNCTION);
   scriptNode.addChildToFront(functionNode);
   assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
 }

 @Test(expected = IllegalStateException.class)
 public void testIsStatementWithNullParent() {
   Node node = new Node(Token.FUNCTION);
   NodeUtil.isStatement(node);
 }

}