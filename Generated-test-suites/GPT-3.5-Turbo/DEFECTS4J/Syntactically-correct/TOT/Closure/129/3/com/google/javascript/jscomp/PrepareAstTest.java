import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PrepareAstTest {

    @Test
    public void testNormalizeNodeTypes() {
        PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
        // Test case 1: Empty AST node
        Node emptyNode = new Node(Token.EMPTY);
        prepareAst.normalizeNodeTypes(emptyNode);
        assertEquals(0, emptyNode.getChildCount());

        // Test case 2: AST node with a child
        Node parentNode = new Node(Token.ROOT);
        Node childNode = new Node(Token.ID, new Node(Token.NAME, "child"));
        parentNode.addChildToFront(childNode);
        prepareAst.normalizeNodeTypes(parentNode);
        assertEquals(1, parentNode.getChildCount());
        assertEquals(Token.BLOCK, childNode.getToken());
    }

    @Test
    public void testNormalizeBlocks() {
        PrepareAst prepareAst = new PrepareAst(new AbstractCompiler());
        // Test case 1: Control structure node that is not a label or a switch
        Node controlStructureNode = new Node(Token.IF);
        Node childNode = new Node(Token.ID, new Node(Token.NAME, "child"));
        controlStructureNode.addChildToFront(childNode);
        prepareAst.normalizeBlocks(controlStructureNode);
        assertEquals(Token.BLOCK, controlStructureNode.getToken());
        assertEquals(1, controlStructureNode.getChildCount());
        assertEquals(controlStructureNode, childNode.getParent());

        // Test case 2: Control structure node that is a label or a switch
        Node labelNode = new Node(Token.LABEL);
        Node switchNode = new Node(Token.SWITCH);
        prepareAst.normalizeBlocks(labelNode);
        prepareAst.normalizeBlocks(switchNode);
        assertEquals(Token.LABEL, labelNode.getToken());
        assertEquals(Token.SWITCH, switchNode.getToken());
    }

    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();

        // Test case 1: shouldTraverse() method
        NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(), null);
        Node objectLiteralNode = new Node(Token.OBJECTLIT);
        prepareAnnotations.shouldTraverse(traversal, objectLiteralNode, null);
        assertEquals(1, objectLiteralNode.getChildCount());

        // Test case 2: visit() method
        Node callNode = new Node(Token.CALL);
        prepareAnnotations.visit(traversal, callNode, null);
        assertEquals(true, callNode.getBooleanProp(Node.FREE_CALL));

        Node firstNode = new Node(Token.NAME, new Node(Token.STRING, "eval"));
        callNode.addChildToFront(firstNode);
        prepareAnnotations.visit(traversal, callNode, null);
        assertEquals(true, firstNode.getBooleanProp(Node.DIRECT_EVAL));

        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.CALL);
        parent.addChildToFront(functionNode);
        prepareAnnotations.visit(traversal, functionNode, parent);
        assertEquals(true, functionNode.getBooleanProp(Node.IS_DISPATCHER));
    }
}