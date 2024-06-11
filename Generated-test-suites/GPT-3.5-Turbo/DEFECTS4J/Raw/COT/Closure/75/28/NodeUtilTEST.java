import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        NodeUtil n = new NodeUtil();
        Node nodeForIn = new Node(Token.FOR);
        nodeForIn.addChildToBack(new Node(Token.NAME));
        nodeForIn.addChildToBack(new Node(Token.NAME));
        nodeForIn.addChildToBack(new Node(Token.NAME));

        assertTrue(n.isForIn(nodeForIn));
    }

    @Test
    public void testIsStatement() {
        NodeUtil n = new NodeUtil();
        Node nodeStatement = new Node(Token.IF);

        assertTrue(n.isStatement(nodeStatement));
    }

    @Test
    public void testIsNotStatement() {
        NodeUtil n = new NodeUtil();
        Node nodeStatement = new Node(Token.ADD);

        assertFalse(n.isStatement(nodeStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        NodeUtil n = new NodeUtil();
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
        nodeFunctionDeclaration.setParent(new Node(Token.SCRIPT));

        assertTrue(n.isFunctionDeclaration(nodeFunctionDeclaration));
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        NodeUtil n = new NodeUtil();
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION);

        assertFalse(n.isFunctionDeclaration(nodeFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        NodeUtil n = new NodeUtil();
        Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentScriptNode.addChildToBack(nodeHoistedFunctionDeclaration);
        parentFunctionNode.addChildToBack(nodeHoistedFunctionDeclaration);

        assertTrue(n.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        NodeUtil n = new NodeUtil();
        Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node parentScriptNode = new Node(Token.SCRIPT);
        parentScriptNode.addChildToBack(nodeHoistedFunctionDeclaration);

        assertFalse(n.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    }
}