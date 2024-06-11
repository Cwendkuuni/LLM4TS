
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(node));
        
        Node nonForInNode = new Node(Token.IF, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(nonForInNode));
    }

    @Test
    public void testIsStatement() {
        Node parentNode = new Node(Token.SCRIPT);
        Node statementNode = new Node(Token.IF);
        statementNode.setParent(parentNode);
        
        assertTrue(NodeUtil.isStatement(statementNode));

        Node nonStatementNode = new Node(Token.FUNCTION);
        nonStatementNode.setParent(parentNode);
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.SCRIPT);
        functionNode.setParent(parentNode);

        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

        Node nonFunctionDeclarationNode = new Node(Token.VAR);
        nonFunctionDeclarationNode.setParent(parentNode);
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.SCRIPT);
        Node grandParentNode = new Node(Token.FUNCTION);
        functionNode.setParent(parentNode);
        parentNode.setParent(grandParentNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        Node nonHoistedFunctionNode = new Node(Token.FUNCTION);
        nonHoistedFunctionNode.setParent(parentNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode));
    }
}
