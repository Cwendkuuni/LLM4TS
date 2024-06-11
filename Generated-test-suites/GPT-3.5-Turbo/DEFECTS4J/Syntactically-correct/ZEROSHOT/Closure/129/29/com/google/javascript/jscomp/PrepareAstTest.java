import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

    private PrepareAst prepareAst;

    @Before
    public void setUp() {
        prepareAst = new PrepareAst();
    }

    @Test
    public void testNormalizeNodeTypes() {
        // Test case 1
        Node node1 = new Node(Token.EXPR_VOID);
        Node result1 = prepareAst.normalizeNodeTypes(node1);
        assertEquals(Token.EXPR_RESULT, result1.getType());

        // Test case 2
        Node node2 = new Node(Token.EXPR_VOID);
        node2.addChildToBack(new Node(Token.STRING));
        Node result2 = prepareAst.normalizeNodeTypes(node2);
        assertEquals(Token.EXPR_RESULT, result2.getType());
        assertEquals(1, result2.getChildCount());
        assertEquals(Token.STRING, result2.getFirstChild().getType());
    }

    @Test
    public void testNormalizeBlocks() {
        // Test case 1
        Node node1 = new Node(Token.IF);
        node1.addChildToBack(new Node(Token.STRING));
        Node result1 = prepareAst.normalizeBlocks(node1);
        assertEquals(Token.BLOCK, result1.getChildAtIndex(1).getType());
        assertEquals(Token.STRING, result1.getChildAtIndex(1).getFirstChild().getType());

        // Test case 2
        Node node2 = new Node(Token.WHILE);
        node2.addChildToBack(new Node(Token.STRING));
        Node block = new Node(Token.BLOCK);
        node2.addChildToBack(block);
        block.addChildToBack(new Node(Token.STRING));
        Node result2 = prepareAst.normalizeBlocks(node2);
        assertEquals(Token.BLOCK, result2.getChildAtIndex(1).getType());
        assertEquals(Token.STRING, result2.getChildAtIndex(1).getFirstChild().getType());
        assertEquals(Token.BLOCK, result2.getChildAtIndex(2).getType());
        assertEquals(Token.STRING, result2.getChildAtIndex(2).getFirstChild().getType());
    }

    @Test
    public void testPrepareAnnotations() {
        // Test case 1
        Node node1 = new Node(Token.OBJECTLIT);
        Node key1 = new Node(Token.STRING);
        key1.setJSDocInfo(new JSDocInfoBuilder().build());
        Node value1 = new Node(Token.NUMBER);
        node1.addChildToBack(key1);
        key1.addChildToBack(value1);
        prepareAst.prepareAnnotations(node1);
        assertNull(key1.getJSDocInfo());
        assertNotNull(value1.getJSDocInfo());

        // Test case 2
        Node node2 = new Node(Token.OBJECTLIT);
        Node key2 = new Node(Token.STRING);
        Node value2 = new Node(Token.FUNCTION);
        node2.addChildToBack(key2);
        key2.addChildToBack(value2);
        prepareAst.prepareAnnotations(node2);
        assertNull(key2.getJSDocInfo());
        assertNotNull(value2.getJSDocInfo());
    }

    @Test
    public void testAnnotateCalls() {
        // Test case 1
        Node node1 = new Node(Token.CALL);
        Node first1 = new Node(Token.NAME, "test");
        node1.addChildToBack(first1);
        prepareAst.annotateCalls(node1);
        assertFalse(node1.getBooleanProp(Node.FREE_CALL));

        // Test case 2
        Node node2 = new Node(Token.CALL);
        Node first2 = new Node(Token.GETPROP);
        first2.addChildToBack(new Node(Token.NAME, "test"));
        first2.addChildToBack(new Node(Token.NAME, "method"));
        node2.addChildToBack(first2);
        prepareAst.annotateCalls(node2);
        assertTrue(node2.getBooleanProp(Node.FREE_CALL));

        // Test case 3
        Node node3 = new Node(Token.CALL);
        Node first3 = new Node(Token.NAME, "eval");
        node3.addChildToBack(first3);
        prepareAst.annotateCalls(node3);
        assertTrue(first3.getBooleanProp(Node.DIRECT_EVAL));
    }

    @Test
    public void testAnnotateDispatchers() {
        // Test case 1
        Node node1 = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.ASSIGN);
        parent1.addChildToBack(new Node(Token.NAME, "test"));
        parent1.addChildToBack(node1);
        parent1.setJSDocInfo(new JSDocInfoBuilder().build());
        prepareAst.annotateDispatchers(node1, parent1);
        assertTrue(node1.getBooleanProp(Node.IS_DISPATCHER));

        // Test case 2
        Node node2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.ASSIGN);
        parent2.addChildToBack(new Node(Token.NAME, "test"));
        parent2.addChildToBack(node2);
        parent2.setJSDocInfo(null);
        prepareAst.annotateDispatchers(node2, parent2);
        assertFalse(node2.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testNormalizeObjectLiteralAnnotations() {
        // Test case 1
        Node node1 = new Node(Token.OBJECTLIT);
        Node key1 = new Node(Token.STRING);
        Node value1 = new Node(Token.FUNCTION);
        node1.addChildToBack(key1);
        key1.addChildToBack(value1);
        prepareAst.normalizeObjectLiteralAnnotations(node1);
        assertNull(key1.getJSDocInfo());
        assertNotNull(value1.getJSDocInfo());

        // Test case 2
        Node node2 = new Node(Token.OBJECTLIT);
        Node key2 = new Node(Token.STRING);
        key2.setJSDocInfo(new JSDocInfoBuilder().build());
        Node value2 = new Node(Token.NUMBER);
        node2.addChildToBack(key2);
        key2.addChildToBack(value2);
        prepareAst.normalizeObjectLiteralAnnotations(node2);
        assertNull(key2.getJSDocInfo());
        assertNotNull(value2.getJSDocInfo());
    }
}