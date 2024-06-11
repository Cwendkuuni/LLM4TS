
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalCase() {
        Node n = new Node(Token.FOR);
        n.addChildToFront(new Node(Token.EMPTY).cloneTree());
        n.addChildToFront(new Node(Token.EMPTY).cloneTree());
        n.addChildToFront(new Node(Token.EMPTY).cloneTree());

        boolean result = NodeUtil.isForIn(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_emptyForLoop() {
        Node n = new Node(Token.FOR);

        boolean result = NodeUtil.isForIn(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsForIn_invalidForLoop() {
        Node n = new Node(Token.WHILE);
        n.addChildToFront(new Node(Token.EMPTY).cloneTree());
        n.addChildToFront(new Node(Token.EMPTY).cloneTree());

        boolean result = NodeUtil.isForIn(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_script() {
        Node n = new Node(Token.SCRIPT);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_block() {
        Node n = new Node(Token.BLOCK);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_label() {
        Node n = new Node(Token.LABEL);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_expression() {
        Node parent = new Node(Token.ADD);
        Node n = new Node(Token.EMPTY);
        parent.addChildToFront(n);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_typicalCase() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToFront(new Node(Token.NAME).cloneTree());

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_expression() {
        Node parent = new Node(Token.ADD);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToFront(n);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_invalidParent() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToFront(n);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typicalCase1() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        functionNode.addChildToFront(nameNode);
        scriptNode.addChildToFront(functionNode);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typicalCase2() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        functionNode.addChildToFront(nameNode);
        Node functionNodeParent = new Node(Token.FUNCTION);
        functionNodeParent.addChildToFront(functionNode);
        scriptNode.addChildToFront(functionNodeParent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        functionNode.addChildToFront(nameNode);
        Node parent = new Node(Token.ADD);
        parent.addChildToFront(functionNode);
        scriptNode.addChildToFront(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_invalidParent() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        functionNode.addChildToFront(nameNode);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(functionNode);
        scriptNode.addChildToFront(parent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionNode);

        Assert.assertFalse(result);
    }

}
