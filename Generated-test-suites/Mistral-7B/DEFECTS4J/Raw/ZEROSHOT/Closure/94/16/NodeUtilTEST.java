import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.base.Preconditions.checkState;

import org.junit.Test;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = Node.newNode(Token.FOR, new int[] { 1, 2, 3 }, null);
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = Node.newNode(Token.IF, new int[] { 1, 2 }, null);
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = Node.newNode(Token.IF, new int[] { 1 }, null);
 Node parentNode = Node.newNode(Token.BLOCK, new int[] { 1 }, null);
 parentNode.addChild(nodeStatement);

 assertTrue(NodeUtil.isStatement(nodeStatement));
 checkState(parentNode != null);

 Node nodeNotStatement = Node.newNode(Token.IDENT, new int[] { 1 }, null);
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 1, 2 }, null);
 Node parentNode = Node.newNode(Token.BLOCK, new int[] { 1 }, null);
 parentNode.addChild(nodeFunctionDeclaration);
 nodeFunctionDeclaration.addChild(Node.newNode(Token.IDENT, new int[] { 1 }, "foo"));

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 checkState(parentNode != null);

 Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] { 1, 2 }, null);
 Node parentNodeFunctionExpression = Node.newNode(Token.ASSIGN, new int[] { 1, 2 }, null);
 parentNodeFunctionExpression.addChild(nodeFunctionExpression);
 nodeFunctionExpression.addChild(Node.newNode(Token.IDENT, new int[] { 1 }, "foo"));

 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 checkState(parentNodeFunctionExpression != null);
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeScript = Node.newNode(Token.SCRIPT, new int[] { 1 }, null);
 Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 1, 2 }, null);
 Node nodeFunctionDeclarationInBlock = Node.newNode(Token.BLOCK, new int[] { 1 }, null);
 nodeFunctionDeclarationInBlock.addChild(nodeFunctionDeclaration);
 nodeFunctionDeclaration.addChild(Node.newNode(Token.IDENT, new int[] { 1 }, "foo"));

 NodeUtil.addChild(nodeScript, nodeFunctionDeclarationInBlock);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionDeclarationOutsideScript = Node.newNode(Token.FUNCTION, new int[] { 1, 2 }, null);
 Node nodeFunctionDeclarationOutsideScriptInFunction = Node.newNode(Token.FUNCTION, new int[] { 1 }, null);
 Node nodeFunctionDeclarationOutsideScriptInFunctionBody = Node.newNode(Token.BLOCK, new int[] { 1 }, null);
 nodeFunctionDeclarationOutsideScriptInFunctionBody.addChild(nodeFunctionDeclarationOutsideScript);
 nodeFunctionDeclarationOutsideScript.addChild(Node.newNode(Token.IDENT, new int[] { 1 }, "foo"));
 Node nodeFunction = Node.newNode(Token.FUNCTION, new int[] { 1 }, null);
 nodeFunction.addChild(Node.newNode(Token.IDENT, new int[] { 1 }, "bar"));
 nodeFunctionDeclarationOutsideScriptInFunction.addChild(nodeFunction);
 Node nodeFunctionDeclarationOutsideScriptParent = Node.newNode(Token.BLOCK, new int[] { 1 }, null);
 nodeFunctionDeclarationOutsideScriptParent.addChild(nodeFunctionDeclarationOutsideScriptInFunction);

 NodeUtil.addChild(Node.newNode(Token.SCRIPT, new int[] { 1 }, null), nodeFunctionDeclarationOutsideScriptParent);

 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationOutsideScript));
 checkState(nodeFunctionDeclarationOutsideScriptParent != null);
 }
}