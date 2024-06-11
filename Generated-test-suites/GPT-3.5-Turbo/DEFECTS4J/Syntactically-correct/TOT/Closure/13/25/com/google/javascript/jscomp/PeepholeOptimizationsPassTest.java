import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {

    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        pass = new PeepholeOptimizationsPass();
    }

    @Test
    public void testProcess_WithEmptyAST_NoChange() {
        Node externs = null;
        Node root = null;
        pass.process(externs, root);
        assertEquals(false, pass.getCompiler().hasCodeChanged());
    }

    @Test
    public void testProcess_WithFunctionCall_OptimizationApplied() {
        Node externs = null;
        Node root = createASTWithFunctionCall();
        pass.process(externs, root);
        assertEquals(true, pass.getCompiler().hasCodeChanged());
    }

    @Test
    public void testProcess_WithFunctionCall_OptimizationNotApplied() {
        Node externs = null;
        Node root = createASTWithoutFunctionCall();
        pass.process(externs, root);
        assertEquals(false, pass.getCompiler().hasCodeChanged());
    }

    @Test
    public void testProcess_WithNestedFunctions_OptimizationApplied() {
        Node externs = null;
        Node root = createASTWithNestedFunctions();
        pass.process(externs, root);
        assertEquals(true, pass.getCompiler().hasCodeChanged());
    }

    @Test
    public void testProcess_WithNestedFunctions_OptimizationNotApplied() {
        Node externs = null;
        Node root = createASTWithoutNestedFunctions();
        pass.process(externs, root);
        assertEquals(false, pass.getCompiler().hasCodeChanged());
    }

    // Helper methods to create different ASTs for testing
    private Node createASTWithFunctionCall() {
        Node functionCall = new Node(Token.CALL, new Node(Token.NAME, new Node("myFunction")),
            new Node(Token.STRING, new Node("param1")), new Node(Token.STRING, new Node("param2")));
        Node script = new Node(Token.SCRIPT, functionCall);
        return script;
    }

    private Node createASTWithoutFunctionCall() {
        Node script = new Node(Token.SCRIPT);
        return script;
    }

    private Node createASTWithNestedFunctions() {
        Node nestedFunction1 = new Node(Token.FUNCTION, new Node("nestedFunction1"));
        Node nestedFunction2 = new Node(Token.FUNCTION, new Node("nestedFunction2"));
        Node script = new Node(Token.SCRIPT, nestedFunction1, nestedFunction2);
        return script;
    }

    private Node createASTWithoutNestedFunctions() {
        Node script = new Node(Token.SCRIPT);
        return script;
    }
}