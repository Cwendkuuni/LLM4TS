import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

    private PrepareAst prepareAst;

    @Before
    public void setUp() {
        prepareAst = new PrepareAst();
    }

    @Test
    public void testProcess_WithNullExternsAndRoot_ShouldNotThrowError() {
        Node externs = null;
        Node root = null;

        try {
            prepareAst.process(externs, root);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testProcess_WithNonEmptyExternsAndNullRoot_ShouldNotThrowError() {
        Node externs = new Node();
        Node root = null;

        try {
            prepareAst.process(externs, root);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testProcess_WithNullExternsAndNonEmptyRoot_ShouldNotThrowError() {
        Node externs = null;
        Node root = new Node();

        try {
            prepareAst.process(externs, root);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testProcess_WithNonEmptyExternsAndRoot_ShouldNotThrowError() {
        Node externs = new Node();
        Node root = new Node();

        try {
            prepareAst.process(externs, root);
        } catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testNormalizeNodeTypes_WithEmptyNode_ShouldNotChangeNode() {
        Node node = new Node();

        prepareAst.normalizeNodeTypes(node);

        assertEquals(new Node(), node);
    }

    @Test
    public void testNormalizeNodeTypes_WithSingleNode_ShouldNotChangeNode() {
        Node node = new Node(Token.FUNCTION);

        prepareAst.normalizeNodeTypes(node);

        assertEquals(new Node(Token.FUNCTION), node);
    }

    @Test
    public void testNormalizeNodeTypes_WithChildrenOfDifferentTypes_ShouldNotChangeNode() {
        Node node = new Node(Token.ROOT);

        node.addChildToFront(new Node(Token.STRING));
        node.addChildToFront(new Node(Token.NUMBER));
        node.addChildToFront(new Node(Token.TRUE));
        node.addChildToFront(new Node(Token.NULL));

        prepareAst.normalizeNodeTypes(node);

        Node expectedNode = new Node(Token.ROOT);
        expectedNode.addChildToFront(new Node(Token.STRING));
        expectedNode.addChildToFront(new Node(Token.NUMBER));
        expectedNode.addChildToFront(new Node(Token.TRUE));
        expectedNode.addChildToFront(new Node(Token.NULL));

        assertEquals(expectedNode, node);
    }

    @Test
    public void testNormalizeNodeTypes_WithEXPR_VOID_ShouldChangeToEXPR_RESULT() {
        Node node = new Node(Token.EXPR_VOID);

        prepareAst.normalizeNodeTypes(node);

        assertEquals(new Node(Token.EXPR_RESULT), node);
    }

    @Test
    public void testNormalizeBlocks_WithNonControlStructure_ShouldNotChangeNode() {
        Node node = new Node(Token.ASSIGN);

        prepareAst.normalizeBlocks(node);

        assertEquals(new Node(Token.ASSIGN), node);
    }

    @Test
    public void testNormalizeBlocks_WithControlStructureWithoutCodeBlock_ShouldNotChangeNode() {
        Node node = new Node(Token.IF);

        prepareAst.normalizeBlocks(node);

        assertEquals(new Node(Token.IF), node);
    }

    @Test
    public void testNormalizeBlocks_WithControlStructureWithCodeBlock_ShouldAddBlockNode() {
        Node node = new Node(Token.IF);

        node.addChildToFront(new Node(Token.BLOCK));

        prepareAst.normalizeBlocks(node);

        Node expectedNode = new Node(Token.IF);
        expectedNode.addChildToFront(new Node(Token.BLOCK));

        assertEquals(expectedNode, node);
    }

    @Test
    public void testPrepareAnnotations_WithObjectLitNode_ShouldNormalizeAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT);

        Node key1 = new Node(Token.LB);
        key1.setJSDocInfo(new JSDocInfo());
        key1.addChildToFront(new Node(Token.STRING));

        Node value1 = new Node(Token.STRING);
        value1.putString("b");

        key1.addChildToBack(value1);
        objlit.addChildToFront(key1);

        Node key2 = new Node(Token.RB);
        key2.setJSDocInfo(new JSDocInfo());
        key2.addChildToFront(new Node(Token.NAME));

        Node value2 = new Node(Token.NAME);
        value2.putString("d");

        key2.addChildToBack(value2);
        objlit.addChildToBack(key2);

        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);

        Node expectedObjlit = new Node(Token.OBJECTLIT);

        Node expectedKey1 = new Node(Token.LB);
        expectedKey1.addChildToFront(new Node(Token.STRING));

        expectedKey1.setJSDocInfo(null);
        value1.setJSDocInfo(key1.getJSDocInfo());

        expectedKey1.addChildToBack(value1);
        expectedObjlit.addChildToFront(expectedKey1);

        Node expectedKey2 = new Node(Token.RB);
        expectedKey2.addChildToFront(new Node(Token.NAME));

        expectedKey2.setJSDocInfo(null);
        value2.setJSDocInfo(key2.getJSDocInfo());

        expectedKey2.addChildToBack(value2);
        expectedObjlit.addChildToBack(expectedKey2);

        assertEquals(expectedObjlit, objlit);
    }

    @Test
    public void testPrepareAnnotations_WithCallNode_ShouldAnnotateCalls() {
        Node call = new Node(Token.CALL);

        Node first = new Node(Token.FIRST);
        first.putBooleanProp(Node.YIELD_CALL, true);
        call.addChildToFront(first);

        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        prepareAnnotations.annotateCalls(call);

        assertTrue(call.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testPrepareAnnotations_WithEvalNode_ShouldAnnotateDirectEval() {
        Node eval = new Node(Token.NAME);
        eval.putString("eval");

        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        prepareAnnotations.annotateCalls(eval);

        assertTrue(eval.getBooleanProp(Node.DIRECT_EVAL));
    }

    @Test
    public void testPrepareAnnotations_WithFunctionNode_ShouldAnnotateDispatchers() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN, new Node(), function);

        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setJavaDispatch(true);
        parent.setJSDocInfo(jsDocInfo);

        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        prepareAnnotations.annotateDispatchers(function, parent);

        assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));
    }
}