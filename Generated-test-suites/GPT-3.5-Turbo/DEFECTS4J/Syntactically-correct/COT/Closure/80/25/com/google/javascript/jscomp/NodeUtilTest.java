
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test for a valid FOR-IN loop node
        Node forInNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInNode));

        // Test for an invalid FOR-IN loop node
        Node forNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(forNode));
    }

    @Test
    public void testIsStatement() {
        // Test for a statement node
        Node parent = new Node(Token.SCRIPT);
        Node statementNode = new Node(Token.EMPTY);
        statementNode.setParent(parent);
        assertTrue(NodeUtil.isStatement(statementNode));

        // Test for a non-statement node
        Node nonStatementNode = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test for a function declaration node
        Node parent = new Node(Token.SCRIPT);
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        // Test for a non-function declaration node
        Node nonFunctionDeclarationNode = new Node(Token.EMPTY);
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test for a hoisted function declaration node
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.setParent(scriptNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        // Test for a non-hoisted function declaration node
        Node nonHoistedFunctionNode = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode));
    }
}
