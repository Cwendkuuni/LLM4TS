import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FunctionRewriterTest {

    @Test
    public void testProcess() {
        // Test if process method works correctly
        // Create a mock Compiler object
        AbstractCompiler compiler = new AbstractCompiler() {
            @Override
            public void reportCodeChange() {
                // Do nothing for testing purposes
            }
        };

        FunctionRewriter rewriter = new FunctionRewriter(compiler);

        // Create a mock Node object for root and externs
        Node root = new Node(Token.ROOT);
        Node externs = new Node(Token.ROOT);

        // Test if process method can handle null root and externs nodes
        rewriter.process(null, null);

        // Test if process method can handle empty root and externs nodes
        rewriter.process(externs, root);
    }

    @Test
    public void testReductionGatherer() {
        // Test ReductionGatherer class to verify correct reductions are applied
        // Create a mock Compiler object
        AbstractCompiler compiler = new AbstractCompiler();

        FunctionRewriter rewriter = new FunctionRewriter(compiler);

        List<Reducer> reducers = ImmutableList.of(new ReturnConstantReducer());

        Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();

        Node root = new Node(Token.ROOT);

        // Test if ReductionGatherer can correctly gather and apply reductions
        ReductionGatherer gatherer = new ReductionGatherer(reducers, reductionMap);
        gatherer.shouldTraverse(new NodeTraversal(compiler, root), new Node(Token.FUNCTION), root);
        gatherer.shouldTraverse(new NodeTraversal(compiler, root), new Node(Token.FUNCTION), root);
        gatherer.visit(new NodeTraversal(compiler, root), new Node(Token.FUNCTION), root);

        assertEquals(2, reductionMap.size());
    }

    @Test
    public void testReduction() {
        // Test Reduction class to verify proper functionality
        AbstractCompiler compiler = new AbstractCompiler();

        Node parent = new Node(Token.STRING);
        Node oldChild = new Node(Token.VAR, new Node(Token.NAME, "oldChild"));
        Node newChild = new Node(Token.VAR, new Node(Token.NAME, "newChild"));

        Reduction reduction = new Reduction(parent, oldChild, newChild);

        // Test applying reduction and checking if parent node has been replaced
        reduction.apply();
        assertEquals(newChild, parent.getFirstChild());

        // Test estimation of savings after applying reduction
        assertEquals(0, reduction.estimateSavings());
    }

    @Test
    public void testEmptyFunctionReducer() {
        // Test EmptyFunctionReducer class to verify correct reduction of empty functions
        EmptyFunctionReducer reducer = new EmptyFunctionReducer();

        // Test reduction of an empty function
        Node functionNode = new Node(Token.FUNCTION);
        Node reducedNode = reducer.reduce(functionNode);

        assertTrue(reducedNode.isCall());
        assertEquals("JSCompiler_emptyFn", reducedNode.getFirstChild().getString());
        assertEquals(1, reducedNode.getChildCount());
    }

    @Test
    public void testIdentityReducer() {
        // Test IdentityReducer class to verify correct reduction of identity functions
        IdentityReducer reducer = new IdentityReducer();

        // Test reduction of an identity function
        Node functionNode = new Node(Token.FUNCTION);
        Node name = new Node(Token.NAME, "value");
        Node returnStmt = new Node(Token.RETURN, name);
        Node body = new Node(Token.BLOCK, returnStmt);
        functionNode.addChildrenToBack(new Node(Token.NAME, "identity"), body);

        Node reducedNode = reducer.reduce(functionNode);

        assertTrue(reducedNode.isCall());
        assertEquals("JSCompiler_identityFn", reducedNode.getFirstChild().getString());
        assertEquals(2, reducedNode.getChildCount());
    }

    @Test
    public void testReturnConstantReducer() {
        // Test ReturnConstantReducer class to verify correct reduction of constant return functions
        ReturnConstantReducer reducer = new ReturnConstantReducer();

        // Test reduction of a constant return function
        Node functionNode = new Node(Token.FUNCTION);
        Node returnStmt = new Node(Token.RETURN, new Node(Token.NUMBER, 10));
        Node body = new Node(Token.BLOCK, returnStmt);
        functionNode.addChildrenToBack(new Node(Token.NAME, "returnConstant"), body);

        Node reducedNode = reducer.reduce(functionNode);

        assertTrue(reducedNode.isCall());
        assertEquals("JSCompiler_returnArg", reducedNode.getFirstChild().getString());
        assertEquals(2, reducedNode.getChildCount());
    }

    @Test
    public void testGetterReducer() {
        // Test GetterReducer class to verify correct reduction of getter functions
        GetterReducer reducer = new GetterReducer();

        // Test reduction of a getter function
        Node functionNode = new Node(Token.FUNCTION);
        Node name = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "b_"));
        Node returnStmt = new Node(Token.RETURN, name);
        Node body = new Node(Token.BLOCK, returnStmt);
        functionNode.addChildrenToBack(new Node(Token.NAME, "getter"), body);

        Node reducedNode = reducer.reduce(functionNode);

        assertTrue(reducedNode.isCall());
        assertEquals("JSCompiler_get", reducedNode.getFirstChild().getString());
        assertEquals(2, reducedNode.getChildCount());
    }

    @Test
    public void testSetterReducer() {
        // Test SetterReducer class to verify correct reduction of setter functions
        SetterReducer reducer = new SetterReducer();

        // Test reduction of a setter function
        Node functionNode = new Node(Token.FUNCTION);
        Node argName = new Node(Token.NAME, "value");
        Node setProp = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "b_"));
        Node assign = new Node(Token.ASSIGN, setProp, argName);
        Node body = new Node(Token.BLOCK, new Node(Token.EXPR_RESULT, assign));
        functionNode.addChildrenToBack(new Node(Token.NAME, "setter"), new Node(Token.PARAM_LIST), body);

        Node reducedNode = reducer.reduce(functionNode);

        assertTrue(reducedNode.isCall());
        assertEquals("JSCompiler_set", reducedNode.getFirstChild().getString());
        assertEquals(2, reducedNode.getChildCount());
    }
}