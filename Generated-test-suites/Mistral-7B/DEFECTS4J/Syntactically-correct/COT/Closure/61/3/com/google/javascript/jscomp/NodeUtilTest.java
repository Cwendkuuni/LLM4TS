import static org.junit.Assert.*;
import staticcom.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 Node node = new Node(); // Create a dummy Node for testing
 node.setType(Token.FOR);
 node.setChildCount(3);

 assertTrue("Expected isForIn to return true for a FOR-IN loop", isForIn(node));

 Node invalidNode = new Node();
 invalidNode.setType(Token.IF);
 assertFalse("Expected isForIn to return false for a non-FOR-IN node", isForIn(invalidNode));
 }

 @Test
 public void testIsStatement() {
 Node statementNode = new Node(); // Create a dummy Node for testing
 statementNode.setParent(new Node()); // Set a parent for the statementNode
 statementNode.setType(Token.IF);

 Node nonStatementNode = new Node(); // Create a dummy Node for testing
 nonStatementNode.setParent(null); // Set a null parent for the nonStatementNode

 Node scriptNode = new Node(); // Create a dummy Node for testing
 scriptNode.setType(Token.SCRIPT);
 Node blockNode = new Node(); // Create a dummy Node for testing
 blockNode.setType(Token.BLOCK);
 Node labelNode = new Node(); // Create a dummy Node for testing
 labelNode.setType(Token.LABEL);

 assertTrue("Expected isStatement to return true for a statement node with a parent", isStatement(statementNode));
 assertFalse("Expected isStatement to return false for a node without a parent", isStatement(nonStatementNode));
 assertTrue("Expected isStatement to return true for a script node", isStatement(scriptNode));
 assertTrue("Expected isStatement to return true for a block node", isStatement(blockNode));
 assertTrue("Expected isStatement to return true for a label node", isStatement(labelNode));
}

 @Test(expected = AssertionError.class)
 public void testIsStatementWithNullParent() {
 Node node = new Node(); // Create a dummy Node for testing
 node.setParent(null);
 isStatement(node); // This should throw an AssertionError
}

 @Test
 public void testIsFunctionDeclaration() {
 Node functionDeclarationNode = new Node(); // Create a dummy Node for testing
 functionDeclarationNode.setType(Token.FUNCTION);
 Node statementFunctionDeclarationNode = new Node(); // Create a dummy Node for testing
 statementFunctionDeclarationNode.setType(Token.FUNCTION);
 statementFunctionDeclarationNode.setParent(new Node()); // Set a parent for the statementFunctionDeclarationNode

 Node functionExpressionNode = new Node(); // Create a dummy Node for testing
 functionExpressionNode.setType(Token.FUNCTION);
 Node expressionFunctionNode = new Node(); // Create a dummy Node for testing
 expressionFunctionNode.setType(Token.FUNCTION).setParent(new Node()); // Set a parent for the expressionFunctionNode

 assertTrue("Expected isFunctionDeclaration to return true for a function declaration with no parent", isFunctionDeclaration(functionDeclarationNode));
 assertFalse("Expected isFunctionDeclaration to return false for a function expression", isFunctionDeclaration(functionExpressionNode));
 assertTrue("Expected isFunctionDeclaration to return true for a function declaration with a parent", isFunctionDeclaration(statementFunctionDeclarationNode));
}

 @Test(expected = AssertionError.class)
 public void testIsFunctionDeclarationWithNullParent() {
 Node node = new Node(); // Create a dummy Node for testing
 node.setType(Token.FUNCTION);
 node.setParent(null);
 isFunctionDeclaration(node); // This should throw an AssertionError
}

 @Test
 public void testIsHoistedFunctionDeclaration() {
 Node hoistedFunctionDeclarationNode = new Node(); // Create a dummy Node for testing
 hoistedFunctionDeclarationNode.setType(Token.FUNCTION);
 hoistedFunctionDeclarationNode.setParent(new Node().setType(Token.SCRIPT));

 Node nonHoistedFunctionDeclarationNode = new Node(); // Create a dummy Node for testing
 nonHoistedFunctionDeclarationNode.setType(Token.FUNCTION);
 nonHoistedFunctionDeclarationNode.setParent(new Node().setType(Token.BLOCK));

 assertTrue("Expected isHoistedFunctionDeclaration to return true for a hoisted function declaration", isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
 assertFalse("Expected isHoistedFunctionDeclaration to return false for a non-hoisted function declaration", isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
}

}