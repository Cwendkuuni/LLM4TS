import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[0]);
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.PRINT, new int[]{}, new int[1]);
 Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
 parentScript.addChildToEnd(nodeStatement);
 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeNotStatement = new Node(Token.PRINT, new int[]{}, new int[1]);
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE}, new int[0]);
 nodeFunctionDeclaration.setChild(0, new Node(Token.ID, new int[]{}, new int[1]));
 nodeFunctionDeclaration.setChild(1, new Node(Token.SEMICOLON, new int[]{}, new int[0]));
 nodeFunctionDeclaration.setChild(2, new Node(Token.LEFT_BRACE, new int[]{}, new int[0]));
 nodeFunctionDeclaration.setChild(3, new Node(Token.RETURN, new int[]{}, new int[0]));

 Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE}, new int[0]);
 nodeFunctionExpression.setChild(0, new Node(Token.ID, new int[]{}, new int[1]));
 nodeFunctionExpression.setChild(1, new Node(Token.SEMICOLON, new int[]{}, new int[0]));
 nodeFunctionExpression.setChild(2, new Node(Token.RETURN, new int[]{}, new int[0]));

 Node parentScriptFunctionDeclaration = new Node(Token.SCRIPT, new int[]{}, new int[0]);
 parentScriptFunctionDeclaration.addChildToEnd(nodeFunctionDeclaration);

 Node parentScriptFunctionExpression = new Node(Token.SCRIPT, new int[]{}, new int[0]);
 parentScriptFunctionExpression.addChildToEnd(nodeFunctionExpression);

 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE}, new int[0]);
 nodeHoistedFunctionDeclaration.setChild(0, new Node(Token.ID, new int[]{}, new int[1]));
 Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
 parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);

 Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE}, new int[0]);
 nodeFunctionDeclarationInFunction.setChild(0, new Node(Token.ID, new int[]{}, new int[1]));
 Node parentFunction = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE}, new int[0]);
 parentFunction.setChild(0, nodeFunctionDeclarationInFunction);
 parentFunction.setChild(1, nodeHoistedFunctionDeclaration);

 Node parentScriptFunction = new Node(Token.SCRIPT, new int[]{}, new int[0]);
 parentScriptFunction.addChildToEnd(parentFunction);

 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 }
}