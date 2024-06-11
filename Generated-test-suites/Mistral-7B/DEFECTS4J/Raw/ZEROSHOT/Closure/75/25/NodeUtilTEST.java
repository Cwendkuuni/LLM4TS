import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node nodeForIn = Node.newForInStatement(new int[] { 0, 1, 2 }, null);
 assertTrue(NodeUtil.isForIn(nodeForIn));

 Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
 assertFalse(NodeUtil.isForIn(nodeNotForIn));
 }

 @Test
 public void testIsStatement() {
 Node nodeStatement = Node.newExpressionStatement(Node.newNumberLiteral(1));
 assertTrue(NodeUtil.isStatement(nodeStatement));

 Node nodeNotStatement = Node.newName("x");
 assertFalse(NodeUtil.isStatement(nodeNotStatement));
 }

 @Test
 public void testIsFunctionDeclaration() {
 Node nodeFunctionDeclaration = Node.newFunction("func", new int[] { 1 }, null, null);
 assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

 Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction("func", new int[] { 1 }, null, null));
 assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node nodeHoistedFunctionDeclaration = Node.newFunction("func", new int[] { 1 }, null, null);
 Node scriptNode = Node.newScript(nodeHoistedFunctionDeclaration);
 Node nodeFunctionDeclarationInFunction = Node.newFunction("func2", new int[] { 1 }, null, null);
 Node nodeFunctionDeclarationInScript = Node.newScript(nodeFunctionDeclarationInFunction);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
 }
}