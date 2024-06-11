import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, null, null, new int[0]);
 Node nodeNotForIn = new Node(Token.IF, null, null, new int[0]);

 assertTrue(NodeUtil.isForIn(nodeForIn));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, null, null, new int[0]);
 Node nodeExpression = new Node(Token.FUNCTION, null, null, new int[0]);
 Node nodeScript = new Node(Token.SCRIPT, null, null, new int[0]);
 Node nodeLabel = new Node(Token.LABEL, null, null, new int[0]);

 assertTrue(NodeUtil.isStatement(nodeStatement));
 assertTrue(NodeUtil.isStatement(nodeScript));
 assertTrue(NodeUtil.isStatement(nodeLabel));

 assertFalse(NodeUtil.isStatement(nodeExpression));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "myFunction", null, new int[0]);
 Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, new int[0]);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, "myFunction", null, new int[0]);
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "myFunction", null, new int[0]);
 Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, "myFunction", null, new int[0]);
 Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, "myFunction", null, new int[0]);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
 }

 @Test(expected = IllegalStateException.class)
 public void testCheckStateParentIsNull() {
 Node node = new Node(Token.FUNCTION, null, null, new int[0]);
 Preconditions.checkState(NodeUtil.isStatement(node), "NodeUtil.isStatement: parent cannot be null.");
 }
}