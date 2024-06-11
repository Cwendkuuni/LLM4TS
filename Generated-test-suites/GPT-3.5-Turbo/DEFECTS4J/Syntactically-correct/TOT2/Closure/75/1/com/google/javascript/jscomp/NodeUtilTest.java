import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInNode));

        Node nonForInNode = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(nonForInNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));

        Node nonStatementNode = new Node(Token.VAR);
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclNode = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclNode));

        Node nonFunctionDeclNode = new Node(Token.VAR);
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT, hoistedFunctionNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));

        Node parentFunctionNode = new Node(Token.FUNCTION, new Node(Token.FUNCTION));
        Node grandparentFunctionNode = new Node(Token.FUNCTION, parentFunctionNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(parentFunctionNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(grandparentFunctionNode));
    }
}