import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test when node is a FOR-IN loop
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        forInNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(forInNode));

        // Test when node is not a FOR-IN loop
        Node notForInNode = new Node(Token.WHILE);
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        // Test when node is a statement
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);

        scriptNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(scriptNode));

        blockNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(blockNode));

        labelNode.addChildToBack(new Node(Token.EMPTY));
        assertTrue(NodeUtil.isStatement(labelNode));

        // Test when node is not a statement
        Node notStatementNode = new Node(Token.IF);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a function declaration
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        // Test when node is not a function declaration
        Node notFunctionDeclarationNode = new Node(Token.IMPORT);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted function declaration
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));

        Node functionNodeInScript = new Node(Token.SCRIPT);
        functionNodeInScript.addChildToBack(hoistedFunctionDeclarationNode);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        Node functionNodeInFunction = new Node(Token.FUNCTION);
        functionNodeInFunction.addChildToBack(hoistedFunctionDeclarationNode);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToBack(functionNodeInFunction);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        // Test when node is not a hoisted function declaration
        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        notHoistedFunctionDeclarationNode.addChildToBack(new Node(Token.NAME));

        Node notFunctionNodeInScript = new Node(Token.SCRIPT);
        notFunctionNodeInScript.addChildToBack(notHoistedFunctionDeclarationNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));

        Node notFunctionNodeInFunction = new Node(Token.FUNCTION);
        notFunctionNodeInFunction.addChildToBack(notHoistedFunctionDeclarationNode);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}