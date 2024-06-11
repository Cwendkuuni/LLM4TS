import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node forInNode = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        forInNode.addChildToBack(child1);
        forInNode.addChildToBack(child2);
        forInNode.addChildToBack(child3);

        assertTrue(NodeUtil.isForIn(forInNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node otherNode = new Node(Token.EMPTY);

        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertFalse(NodeUtil.isStatement(otherNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        Node otherNode = new Node(Token.EMPTY);
        parentScriptNode.addChildToBack(functionDeclarationNode);
        parentFunctionNode.addChildToBack(functionDeclarationNode);

        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
        assertFalse(NodeUtil.isFunctionDeclaration(otherNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        Node otherNode = new Node(Token.EMPTY);
        parentScriptNode.addChildToBack(functionDeclarationNode);
        parentFunctionNode.addChildToBack(functionDeclarationNode);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(otherNode));
    }
}