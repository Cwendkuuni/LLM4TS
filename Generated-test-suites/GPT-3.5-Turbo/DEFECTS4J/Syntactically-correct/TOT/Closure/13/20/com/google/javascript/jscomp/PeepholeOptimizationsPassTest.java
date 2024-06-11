
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {

    private PeepholeOptimizationsPass pass;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        pass = new PeepholeOptimizationsPass(compiler);
    }

    @Test
    public void testProcess_withEmptyAST() {
        Node externs = new Node(NodeTypes.EXTERN_KEYS);
        Node root = new Node(NodeTypes.ROOT);
        
        pass.process(externs, root);

        // Assert that no changes were made to the AST
        assertEquals(externs, root.getFirstChild());
        assertNull(root.getFirstChild().getNext());
    }

    @Test
    public void testProcess_withSingleFunction() {
        Node externs = new Node(NodeTypes.EXTERN_KEYS);
        Node root = new Node(NodeTypes.ROOT);

        // Create a simple function
        Node function = createFunctionNode();
        root.addChildToFront(function);

        pass.process(externs, root);

        // Assert that the optimization has been applied to the function
        assertTrue(hasAppliedOptimization(function));
    }

    private Node createFunctionNode() {
        Node function = new Node(NodeTypes.FUNCTION);
        function.putBooleanProp(Node.IS_ARROW_FUNCTION, false);

        Node name = new Node(NodeTypes.NAME, new Node(NodeTypes.STRING, "testFunction"));
        function.addChildToBack(name);

        Node body = new Node(NodeTypes.BLOCK);
        function.addChildToBack(body);

        return function;
    }

    private boolean hasAppliedOptimization(Node function) {
        Preconditions.checkNotNull(function);
        Node currentVersionOfNode = function;
        boolean somethingChanged = false;

        do {
            somethingChanged = false;
            for (AbstractPeepholeOptimization optimization : peepholeOptimizations) {
                Node newVersionOfNode = optimization.optimizeSubtree(currentVersionOfNode);

                if (newVersionOfNode != currentVersionOfNode) {
                    somethingChanged = true;
                    currentVersionOfNode = newVersionOfNode;
                }

                if (currentVersionOfNode == null) {
                    return true;
                }
            }
        } while (somethingChanged);

        return false;
    }

}
