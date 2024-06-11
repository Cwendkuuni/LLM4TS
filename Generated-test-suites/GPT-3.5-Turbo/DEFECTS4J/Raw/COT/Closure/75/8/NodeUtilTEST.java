
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, null);
        node.addChildToFront(new Node(Token.EMPTY, null));
        node.addChildToFront(new Node(Token.EMPTY, null));
        node.addChildToFront(new Node(Token.EMPTY, null));

        Assert.assertTrue(NodeUtil.isForIn(node));
        
        Node invalidNode = new Node(Token.FOR, null);
        Assert.assertFalse(NodeUtil.isForIn(invalidNode));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT, null);
        Node parent = new Node(Token.FOR, null);
        parent.addChildToFront(scriptNode);
        Node node = new Node(Token.EMPTY, null);
        scriptNode.addChildToFront(node);

        Assert.assertTrue(NodeUtil.isStatement(node));
        
        Assert.assertFalse(NodeUtil.isStatement(parent));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, null);
        Node parent = new Node(Token.FOR, null);
        parent.addChildToFront(new Node(Token.SCRIPT, null));
        parent.getLastChild().addChildToFront(functionNode);

        Assert.assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
        
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(parent));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION, null);
        Node scriptNode = new Node(Token.SCRIPT, null);
        Node topLevelFunctionNode = new Node(Token.FUNCTION, null);
        scriptNode.addChildToFront(topLevelFunctionNode);
        topLevelFunctionNode.addChildToFront(functionNode);

        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
        
        functionNode = new Node(Token.FUNCTION, null);
        scriptNode = new Node(Token.SCRIPT, null);
        Node parent = new Node(Token.FOR, null);
        parent.addChildToFront(scriptNode);
        scriptNode.addChildToFront(functionNode);
        
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
} 
