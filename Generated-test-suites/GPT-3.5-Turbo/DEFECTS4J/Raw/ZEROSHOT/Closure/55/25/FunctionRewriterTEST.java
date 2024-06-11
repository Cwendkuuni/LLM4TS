import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionRewriterTest {

    private AbstractCompiler compiler;
    private FunctionRewriter functionRewriter;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        functionRewriter = new FunctionRewriter(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.ROOT, new Node(Token.BLOCK));

        functionRewriter.process(externs, root);

        // TODO: Add assertions
        assertTrue(true);
    }

    @Test
    public void testParseHelperCode() {
        FunctionRewriter.Reducer reducer = functionRewriter.new EmptyFunctionReducer();

        Node helperCode = functionRewriter.parseHelperCode(reducer);

        assertNotNull(helperCode);
        assertEquals(Token.BLOCK, helperCode.getType());
    }

    @Test
    public void testIsReduceableFunctionExpression() {
        Node functionExpression = new Node(Token.FUNCTION);

        assertTrue(FunctionRewriter.isReduceableFunctionExpression(functionExpression));
    }

    @Test
    public void testReduce() {
        FunctionRewriter.Reducer reducer = functionRewriter.new EmptyFunctionReducer();
        Node node = new Node(Token.FUNCTION);

        Node reducedNode = reducer.reduce(node);

        // TODO: Add assertions
        assertNotNull(reducedNode);
    }

    @Test
    public void testBuildCallNode() {
        String methodName = "method";
        Node argumentNode = new Node(Token.NAME);
        int lineno = 1;
        int charno = 2;

        Node callNode = functionRewriter.buildCallNode(methodName, argumentNode, lineno, charno);

        assertEquals(Token.CALL, callNode.getType());
        assertEquals(methodName, callNode.getFirstChild().getString());
        assertEquals(argumentNode, callNode.getLastChild());
    }
}