import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis();
    }

    @Test
    public void testShouldTraverse_FunctionConstructor() {
        assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, true), null));
    }

    @Test
    public void testShouldTraverse_FunctionThisAnnotation() {
        assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, true), null));
    }

    @Test
    public void testShouldTraverse_FunctionNoPlaceForThisAnnotation_VariableDeclaration() {
        assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.ASSIGN)));
    }

    @Test
    public void testShouldTraverse_FunctionNoPlaceForThisAnnotation_ObjectLiteral() {
        assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.STRING)));
    }

    @Test
    public void testShouldTraverse_FunctionName() {
        assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.NAME)));
    }

    @Test
    public void testShouldTraverse_FunctionBlock() {
        assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.BLOCK)));
    }

    @Test
    public void testShouldTraverse_FunctionScript() {
        assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.SCRIPT)));
    }

    @Test
    public void testShouldTraverse_FunctionAssign() {
        assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.ASSIGN)));
    }

    @Test
    public void testShouldTraverse_FunctionNestedAssign() {
        assertFalse(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.ASSIGN, createNode(Token.BLOCK))));
    }

    @Test
    public void testShouldTraverse_FunctionObjectLiteral() {
        assertTrue(checkGlobalThis.shouldTraverse(new NodeTraversal(), createFunctionNode(Token.FUNCTION, false, false), createNode(Token.BLOCK, createNode(Token.NAME))));
    }

    @Test
    public void testVisit_ThisOnLeftSideOfAssignment() {
        NodeTraversal.NodeVisitor visitor = new CheckGlobalThisVisitor();
        NodeTraversal traversal = new NodeTraversal(visitor);

        Node thisNode = createNode(Token.THIS);
        Node assignNode = createNode(Token.ASSIGN, thisNode, createNode(Token.NAME));
        traversal.visit(null, assignNode, null);
        assertEquals(1, visitor.getReportedErrors().size());
    }

    @Test
    public void testVisit_ThisWithPropertyAccess() {
        NodeTraversal.NodeVisitor visitor = new CheckGlobalThisVisitor();
        NodeTraversal traversal = new NodeTraversal(visitor);

        Node thisNode = createNode(Token.THIS);
        Node propertyAccessNode = createNode(Token.GETPROP, thisNode, createNode(Token.NAME, "property"));
        traversal.visit(null, propertyAccessNode, null);
        assertEquals(1, visitor.getReportedErrors().size());
    }

    @Test
    public void testVisit_ThisNotOnLeftSideOrPropertyAccess() {
        NodeTraversal.NodeVisitor visitor = new CheckGlobalThisVisitor();
        NodeTraversal traversal = new NodeTraversal(visitor);

        Node thisNode = createNode(Token.THIS);
        Node nameNode = createNode(Token.NAME);
        traversal.visit(null, thisNode, nameNode);
        assertEquals(0, visitor.getReportedErrors().size());
    }

    @Test
    public void testShouldReportThis_ThisOnLeftSideOfAssignment() {
        Node thisNode = createNode(Token.THIS);
        Node assignNode = createNode(Token.ASSIGN, thisNode, createNode(Token.NAME));
        assertTrue(checkGlobalThis.shouldReportThis(thisNode, assignNode));
    }

    @Test
    public void testShouldReportThis_ThisWithPropertyAccess() {
        Node thisNode = createNode(Token.THIS);
        Node propertyAccessNode = createNode(Token.GETPROP, thisNode, createNode(Token.NAME, "property"));
        assertTrue(checkGlobalThis.shouldReportThis(thisNode, propertyAccessNode));
    }

    @Test
    public void testShouldReportThis_ThisNotOnLeftSideOrPropertyAccess() {
        Node thisNode = createNode(Token.THIS);
        Node nameNode = createNode(Token.NAME);
        assertFalse(checkGlobalThis.shouldReportThis(thisNode, nameNode));
    }

    private Node createFunctionNode(int type, boolean isConstructor, boolean hasThisAnnotation) {
        Node functionNode = createNode(type);
        functionNode.putBooleanProp(Node.IS_CONSTRUCTOR, isConstructor);
        if (hasThisAnnotation) {
            functionNode.putBooleanProp(Node.HAS_THIS_ARGUMENT, true);
        }
        return functionNode;
    }

    private Node createFunctionNode(int type, boolean isConstructor, boolean hasThisAnnotation, String functionName) {
        Node functionNode = createFunctionNode(type, isConstructor, hasThisAnnotation);
        functionNode.putProp(Node.NAME, functionName);
        return functionNode;
    }

    private Node createNode(int type) {
        return new Node(type);
    }

    private Node createNode(int type, Node... children) {
        Node node = new Node(type);
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }
}