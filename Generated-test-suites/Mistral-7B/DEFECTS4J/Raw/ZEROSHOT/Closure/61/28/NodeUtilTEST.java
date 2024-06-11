import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3}, new int[] {0, 0, 0});
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2}, new int[] {0, 0});
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = new Node(Token.IF, new int[] {1, 2}, new int[] {0, 0});
 Node parentOfStatement = new Node(Token.SCRIPT, new int[] {1}, new int[] {0});
 parentOfStatement.addChild(nodeStatement);
 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeNotStatement = new Node(Token.FOR, new int[] {1, 2, 3}, new int[] {0, 0, 0});
 Node parentOfNotStatement = new Node(Token.BLOCK, new int[] {1}, new int[] {0});
 parentOfNotStatement.addChild(nodeNotStatement);
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, new int[] {0, 0, 0, 0});
 nodeFunctionDeclaration.addChild(new Node("name", "myFunction"));
 Node parentOfFunctionDeclaration = new Node(Token.BLOCK, new int[] {1}, new int[] {0});
 parentOfFunctionDeclaration.addChild(nodeFunctionDeclaration);
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeNotFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, new int[] {0, 0, 0, 0});
 Node parentOfNotFunctionDeclaration = new Node(Token.EXPRESSION_STATEMENT, new int[] {1}, new int[] {0});
 parentOfNotFunctionDeclaration.addChild(nodeNotFunctionDeclaration);
 assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, new int[] {0, 0, 0, 0});
 nodeHoistedFunctionDeclaration.addChild(new Node("name", "myFunction"));
 Node parentOfHoistedFunctionDeclaration = new Node(Token.SCRIPT, new int[] {1}, new int[] {0});
 parentOfHoistedFunctionDeclaration.addChild(nodeHoistedFunctionDeclaration);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

 Node nodeNotHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2}, new int[] {0, 0, 0, 0});
 nodeNotHoistedFunctionDeclaration.addChild(new Node("name", "myFunction"));
 Node parentOfNotHoistedFunctionDeclaration = new Node(Token.BLOCK, new int[] {1}, new int[] {0});
 parentOfNotHoistedFunctionDeclaration.addChild(nodeNotHoistedFunctionDeclaration);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));

 Node nodeFunctionInFunction = new Node(Token.FUNCTION, new int[] {1, 2}, new int[] {0, 0, 0, 0});
 nodeFunctionInFunction.addChild(new Node("name", "myFunction"));
 Node parentOfFunctionInFunction = new Node(Token.FUNCTION, new int[] {1, 2}, new int[] {0, 0, 0, 0});
 parentOfFunctionInFunction.addChild(nodeFunctionInFunction);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
 }
}