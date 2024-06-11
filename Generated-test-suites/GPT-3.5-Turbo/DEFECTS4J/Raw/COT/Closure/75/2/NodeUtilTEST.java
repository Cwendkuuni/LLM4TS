
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test when node represents a FOR-IN loop
        Node nodeForIn = new Node(Token.FOR);
        nodeForIn.addChildToBack(new Node(Token.NAME));
        nodeForIn.addChildToBack(new Node(Token.EMPTY));
        nodeForIn.addChildToBack(new Node(Token.EMPTY));
        Assert.assertTrue(NodeUtil.isForIn(nodeForIn));

        // Test when node does not represent a FOR-IN loop
        Node nodeNotForIn = new Node(Token.NAME);
        nodeNotForIn.addChildToBack(new Node(Token.EMPTY));
        Assert.assertFalse(NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        // Test when node is used as a statement
        Node nodeAsStatement = new Node(Token.SCRIPT);
        nodeAsStatement.addChildToBack(new Node(Token.BLOCK));
        nodeAsStatement.addChildToBack(new Node(Token.LABEL));
        Assert.assertTrue(NodeUtil.isStatement(nodeAsStatement));

        // Test when node is not used as a statement
        Node nodeNotStatement = new Node(Token.NAME);
        nodeNotStatement.addChildToBack(new Node(Token.EMPTY));
        Assert.assertFalse(NodeUtil.isStatement(nodeNotStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test when node is a function declaration
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
        nodeFunctionDeclaration.addChildToBack(new Node(Token.EMPTY));
        Assert.assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        // Test when node is not a function declaration
        Node nodeNotFunctionDeclaration = new Node(Token.FOR);
        nodeNotFunctionDeclaration.addChildToBack(new Node(Token.NAME));
        nodeNotFunctionDeclaration.addChildToBack(new Node(Token.EMPTY));
        nodeNotFunctionDeclaration.addChildToBack(new Node(Token.EMPTY));
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test when node is a hoisted function declaration
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(new Node(Token.EMPTY));
        functionNode.addChildToBack(new Node(Token.EMPTY));
        scriptNode.addChildToBack(functionNode);
        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));

        // Test when node is not a hoisted function declaration
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToBack(new Node(Token.EMPTY));
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    }
}
