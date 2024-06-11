import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalCase() {
        // Test for typical case where node represents a FOR-IN loop
        Node node = createForInNode();
        boolean result = NodeUtil.isForIn(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_notForInLoop() {
        // Test for case where node does not represent a FOR-IN loop
        Node node = createNonForInNode();
        boolean result = NodeUtil.isForIn(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsForIn_edgeCase() {
        // Test for case where node represents a FOR-IN loop with only 2 children
        Node node = createForInNodeWithTwoChildren();
        boolean result = NodeUtil.isForIn(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_typicalCase() {
        // Test for typical case where node is used as a statement
        Node node = createStatementNode();
        boolean result = NodeUtil.isStatement(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_notUsedAsStatement() {
        // Test for case where node is not used as a statement
        Node node = createNonStatementNode();
        boolean result = NodeUtil.isStatement(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_typicalCase() {
        // Test for typical case where node is a function declaration
        Node node = createFunctionDeclarationNode();
        boolean result = NodeUtil.isFunctionDeclaration(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_notFunctionDeclaration() {
        // Test for case where node is not a function declaration
        Node node = createNonFunctionDeclarationNode();
        boolean result = NodeUtil.isFunctionDeclaration(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typicalCase() {
        // Test for typical case where node is a hoisted function declaration
        Node node = createHoistedFunctionDeclarationNode();
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
        // Test for case where node is not a hoisted function declaration
        Node node = createNonHoistedFunctionDeclarationNode();
        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
        Assert.assertFalse(result);
    }

    private Node createForInNode() {
        // Create a typical FOR-IN loop node
        Node forNode = new Node(Token.FOR);
        Node condition1 = new Node(Token.NAME);
        condition1.setString("i");
        Node condition2 = new Node(Token.NAME);
        condition2.setString("obj");
        Node body = new Node(Token.BLOCK);
        forNode.addChildToBack(condition1);
        forNode.addChildToBack(condition2);
        forNode.addChildToBack(body);
        return forNode;
    }

    private Node createNonForInNode() {
        // Create a node that is not a FOR-IN loop
        Node node = new Node(Token.LABEL);
        return node;
    }

    private Node createForInNodeWithTwoChildren() {
        // Create a FOR-IN loop node with only 2 children
        Node forNode = new Node(Token.FOR);
        Node condition1 = new Node(Token.NAME);
        condition1.setString("i");
        Node condition2 = new Node(Token.NAME);
        condition2.setString("obj");
        forNode.addChildToBack(condition1);
        forNode.addChildToBack(condition2);
        return forNode;
    }

    private Node createStatementNode() {
        // Create a node used as a statement
        Node node = new Node(Token.SCRIPT);
        return node;
    }

    private Node createNonStatementNode() {
        // Create a node not used as a statement
        Node node = new Node(Token.STRING);
        return node;
    }

    private Node createFunctionDeclarationNode() {
        // Create a function declaration node
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("myFunction");
        functionNode.addChildToBack(nameNode);
        return functionNode;
    }

    private Node createNonFunctionDeclarationNode() {
        // Create a node that is not a function declaration
        Node node = new Node(Token.NAME);
        return node;
    }

    private Node createHoistedFunctionDeclarationNode() {
        // Create a hoisted function declaration node
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("myFunction");
        functionNode.addChildToBack(nameNode);
        scriptNode.addChildToBack(functionNode);
        return functionNode;
    }

    private Node createNonHoistedFunctionDeclarationNode() {
        // Create a node that is not a hoisted function declaration
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("myFunction");
        functionNode.addChildToBack(nameNode);
        scriptNode.addChildToBack(functionNode);
        return scriptNode;
    }
}