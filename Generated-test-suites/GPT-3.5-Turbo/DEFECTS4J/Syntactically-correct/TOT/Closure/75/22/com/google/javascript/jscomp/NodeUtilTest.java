import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    /*----------------------------------------
    isForIn method test cases
    ----------------------------------------*/

    // Typical use case
    @Test
    public void testIsForIn_TypicalCase() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(node);

        Assert.assertTrue(result);
    }

    // Edge case: Number of child nodes is less than 3
    @Test
    public void testIsForIn_LessThanThreeChildNodes() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(node);

        Assert.assertFalse(result);
    }

    // Edge case: Node type is not FOR
    @Test
    public void testIsForIn_NonForNodeType() {
        Node node = new Node(Token.NAME);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToFront(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(node);

        Assert.assertFalse(result);
    }

    /*----------------------------------------
    isStatement method test cases
    ----------------------------------------*/

    // Typical use case
    @Test
    public void testIsStatement_TypicalCase() {
        Node node = new Node(Token.TRUE);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(node);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertTrue(result);
    }

    // Edge case: Parent node is not SCRIPT, BLOCK, or LABEL
    @Test
    public void testIsStatement_NonScriptBlockLabelParent() {
        Node node = new Node(Token.TRUE);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(node);

        boolean result = NodeUtil.isStatement(node);

        Assert.assertFalse(result);
    }

    /*----------------------------------------
    isFunctionDeclaration method test cases
    ----------------------------------------*/

    // Typical use case
    @Test
    public void testIsFunctionDeclaration_TypicalCase() {
        Node node = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertTrue(result);
    }

    // Edge case: Parent node is not a statement
    @Test
    public void testIsFunctionDeclaration_NonStatementParent() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToFront(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        Assert.assertFalse(result);
    }

    /*----------------------------------------
    isHoistedFunctionDeclaration method test cases
    ----------------------------------------*/

    // Typical use case
    @Test
    public void testIsHoistedFunctionDeclaration_TypicalCase() {
        Node node = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);

        scriptNode.addChildToFront(functionNode);
        functionNode.addChildToFront(node);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        Assert.assertTrue(result);
    }

    // Edge case: Parent node is not SCRIPT or FUNCTION
    @Test
    public void testIsHoistedFunctionDeclaration_NonScriptFunctionParent() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(node);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        Assert.assertFalse(result);
    }

}