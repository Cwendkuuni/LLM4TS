import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Typical use case
        Assert.assertTrue(NodeUtil.isForIn(createForInNode()));
        // Edge case: Node is not a FOR
        Assert.assertFalse(NodeUtil.isForIn(createNonForNode()));
        // Edge case: Node does not have exactly 3 children
        Assert.assertFalse(NodeUtil.isForIn(createForWithWrongChildCountNode()));
    }
  
    @Test
    public void testIsStatement() {
        // Typical use case
        Assert.assertTrue(NodeUtil.isStatement(createStatementNode()));
        // Typical use case: Node is a child of a SCRIPT
        Assert.assertTrue(NodeUtil.isStatement(createChildOfScriptNode()));
        // Edge case: Node is a child of a FUNCTION
        Assert.assertFalse(NodeUtil.isStatement(createChildOfFunctionNode()));
        // Edge case: Node is a child of a LABEL
        Assert.assertTrue(NodeUtil.isStatement(createChildOfLabelNode()));
        // Edge case: Node is not a child of any of the expected parent types
        Assert.assertFalse(NodeUtil.isStatement(createNonChildNode()));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Typical use cases
        Assert.assertTrue(NodeUtil.isFunctionDeclaration(createFunctionDeclarationNode()));
        Assert.assertTrue(NodeUtil.isFunctionDeclaration(createMethodDeclarationNode()));
        // Edge case: Node is not a FUNCTION
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(createNonFunctionNode()));
        // Edge case: Node is not a statement
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(createNonStatementNode()));
        // Edge case: Node does not have a parent
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(createNoParentNode()));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Typical use case: Function declaration in the scope root
        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(createHoistedFunctionDeclarationNode()));
        // Typical use case: Function declaration in a function in the scope root
        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(createNestedHoistedFunctionDeclarationNode()));
        // Edge case: Node is not a hoisted function declaration
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNonHoistedFunctionDeclarationNode()));
        // Edge case: Node is not a function declaration
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(createNonFunctionDeclarationNode()));
        // Edge case: Node's parent is not SCRIPT or FUNCTION
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(createInvalidParentNode()));
    }

    // Helper methods to create test nodes

    private Node createForInNode() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToFront(new Node(Token.EMPTY));
        forInNode.addChildToFront(new Node(Token.EMPTY));
        return forInNode;
    }

    private Node createNonForNode() {
        Node nonForNode = new Node(Token.EXPR_RESULT);
        nonForNode.addChildToFront(new Node(Token.EMPTY));
        nonForNode.addChildToFront(new Node(Token.EMPTY));
        nonForNode.addChildToFront(new Node(Token.EMPTY));
        return nonForNode;
    }

    private Node createForWithWrongChildCountNode() {
        Node forNode = new Node(Token.FOR);
        forNode.addChildToFront(new Node(Token.EMPTY));
        forNode.addChildToFront(new Node(Token.EMPTY));
        return forNode;
    }

    private Node createStatementNode() {
        return new Node(Token.EXPR_RESULT);
    }

    private Node createChildOfScriptNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(new Node(Token.EXPR_RESULT));
        return scriptNode.getFirstChild();
    }

    private Node createChildOfFunctionNode() {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(new Node(Token.EXPR_RESULT));
        return functionNode.getFirstChild();
    }

    private Node createChildOfLabelNode() {
        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToFront(new Node(Token.EXPR_RESULT));
        return labelNode.getFirstChild();
    }

    private Node createNonChildNode() {
        return new Node(Token.LT);
    }

    private Node createFunctionDeclarationNode() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(functionNode);
        return functionNode;
    }

    private Node createMethodDeclarationNode() {
        Node methodNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.OBJECTLIT);
        parent.addChildToBack(new Node(Token.STRING));
        parent.getFirstChild().addChildToFront(methodNode);
        return methodNode;
    }

    private Node createNonFunctionNode() {
        return new Node(Token.LT);
    }

    private Node createNonStatementNode() {
        Node nonStatementNode = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(nonStatementNode);
        return nonStatementNode;
    }

    private Node createNoParentNode() {
        return new Node(Token.FUNCTION);
    }

    private Node createHoistedFunctionDeclarationNode() {
        Node hoistedFunctionDeclNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(hoistedFunctionDeclNode);
        return hoistedFunctionDeclNode;
    }

    private Node createNestedHoistedFunctionDeclarationNode() {
        Node hoistedFunctionDeclNode = new Node(Token.FUNCTION);
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToFront(functionNode);
        functionNode.addChildToFront(hoistedFunctionDeclNode);
        return hoistedFunctionDeclNode;
    }

    private Node createNonHoistedFunctionDeclarationNode() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(functionNode);
        return functionNode;
    }

    private Node createNonFunctionDeclarationNode() {
        Node varNode = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(varNode);
        return varNode;
    }

    private Node createInvalidParentNode() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LABEL);
        parent.addChildToFront(functionNode);
        return functionNode;
    }
}