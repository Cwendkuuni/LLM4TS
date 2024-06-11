
import com.google.javascript.jscomp.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {

    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new Compiler();
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[2];
        optimizations[0] = new TestPeepholeOptimization1();
        optimizations[1] = new TestPeepholeOptimization2();

        pass = new PeepholeOptimizationsPass(compiler, optimizations);
    }

    @Test
    public void process_WithEmptyCode_ShouldNotChangeAnything() {
        Node externs = new Node(Token.EXTERN, new Node(Token.EMPTY));
        Node root = new Node(Token.ROOT, new Node(Token.EMPTY));

        pass.process(externs, root);

        // The peephole optimizations should not change anything
        assertEquals(externs.toStringTree(), pass.getCompiler().getRoot().toStringTree());
    }

    @Test
    public void process_WithSingleOptimization_ShouldApplyOptimization() {
        Node externs = new Node(Token.EXTERN, new Node(Token.EMPTY));
        Node root = new Node(Token.ROOT, new Node(Token.EMPTY));

        Node block = new Node(Token.BLOCK);
        block.addChildToFront(new Node(Token.FUNCTION, new Node(Token.NAME, new Node(Token.STRING))));
        root.addChildToBack(block);

        pass.process(externs, root);

        // The peephole optimizations should optimize the function node
        assertEquals("function(){}", pass.getCompiler().getRoot().getLastChild().getFirstChild().toStringTree());
    }

    // Test optimization 1

    private static class TestPeepholeOptimization1 extends AbstractPeepholeOptimization {
        @Override
        public Node optimizeSubtree(Node node) {
            if (node.isFunction()) {
                node.getFirstChild().removeChildren();
            }
            return node;
        }
    }

    // Test optimization 2

    private static class TestPeepholeOptimization2 extends AbstractPeepholeOptimization {
        @Override
        public Node optimizeSubtree(Node node) {
            if (node.isName()) {
                node.setString("");
            }
            return node;
        }
    }
}
