import org.junit.Test;

import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {

    @Test
    public void testProcess() {
        // Create a mock compiler and optimizations
        AbstractCompiler compiler = new AbstractCompiler();
        AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
            @Override
            protected Node optimizeSubtree(Node node) {
                // Return a modified version of the node
                return node;
            }
        };
        AbstractPeepholeOptimization[] optimizations = {optimization};

        // Create a PeepholeOptimizationsPass object and process the nodes
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);
        Node externs = new Node();
        Node root = new Node();
        pass.process(externs, root);

        // Verify that the process method has been called
        boolean processCalled = optimization.isProcessCalled();
        assertTrue(processCalled);
    }

    @Test
    public void testShouldRetraverse() {
        // Create a mock node with a parent
        Node parent = new Node();
        Node node = new Node();
        parent.addChildToBack(node);

        // Create a PeepholeOptimizationsPass object
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(new AbstractCompiler());

        // Verify that shouldRetraverse returns true when the parent is a function or script and the scope has changed
        pass.traversalState.peek().changed = true;
        boolean shouldRetraverse = pass.shouldRetraverse(node);
        assertTrue(shouldRetraverse);

        // Verify that shouldRetraverse returns false when the parent is not a function or script
        parent.setType(Token.NAME);
        boolean shouldNotRetraverse = pass.shouldRetraverse(node);
        assertFalse(shouldNotRetraverse);

        // Verify that shouldRetraverse returns false when the scope has not changed
        pass.traversalState.peek().changed = false;
        boolean shouldNotRetraverse2 = pass.shouldRetraverse(node);
        assertFalse(shouldNotRetraverse2);
    }

    @Test
    public void testShouldVisit() {
        // Create a mock node with a parent
        Node parent = new Node();
        Node node = new Node();
        parent.addChildToBack(node);

        // Create a PeepholeOptimizationsPass object
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(new AbstractCompiler());

        // Verify that shouldVisit returns true and pushes a new scope state onto the stack when the parent is a function or script
        boolean shouldVisit = pass.shouldVisit(node);
        assertTrue(shouldVisit);
        assertEquals(2, pass.traversalState.states.size());

        // Verify that shouldVisit returns false when the parent is not a function or script
        parent.setType(Token.NAME);
        boolean shouldNotVisit = pass.shouldVisit(node);
        assertFalse(shouldNotVisit);
        assertEquals(2, pass.traversalState.states.size());
    }

    @Test
    public void testExitNode() {
        // Create a mock node
        Node node = new Node();

        // Create a PeepholeOptimizationsPass object
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(new AbstractCompiler());

        // Push a new scope state onto the stack
        pass.traversalState.push();
        assertEquals(2, pass.traversalState.states.size());

        // Call exitNode to pop the scope state from the stack
        pass.exitNode(node);
        assertEquals(1, pass.traversalState.states.size());
    }

    @Test
    public void testVisit() {
        // Create a mock node
        Node node = new Node(Token.NAME);

        // Create a mock compiler and optimizations
        AbstractCompiler compiler = new AbstractCompiler();
        AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
            @Override
            protected Node optimizeSubtree(Node node) {
                // Return a modified version of the node
                return new Node(Token.FUNCTION);
            }
        };
        AbstractPeepholeOptimization[] optimizations = {optimization};

        // Create a PeepholeOptimizationsPass object
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

        // Call visit to optimize the node
        pass.visit(node);

        // Verify that the optimization was applied and the node was modified
        assertEquals(Token.FUNCTION, node.getType());
    }

    @Test
    public void testBeginTraversal() {
        // Create a mock compiler and optimizations
        AbstractCompiler compiler = new AbstractCompiler();
        AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
            @Override
            public void beginTraversal(AbstractCompiler compiler) {
                // Set a flag to indicate that beginTraversal was called
                setBeginTraversalCalled(true);
            }
        };
        AbstractPeepholeOptimization[] optimizations = {optimization};

        // Create a PeepholeOptimizationsPass object
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

        // Call beginTraversal to trigger the optimization's beginTraversal method
        pass.beginTraversal();

        // Verify that the optimization's beginTraversal method was called
        boolean beginTraversalCalled = optimization.isBeginTraversalCalled();
        assertTrue(beginTraversalCalled);
    }

    @Test
    public void testEndTraversal() {
        // Create a mock compiler and optimizations
        AbstractCompiler compiler = new AbstractCompiler();
        AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
            @Override
            public void endTraversal(AbstractCompiler compiler) {
                // Set a flag to indicate that endTraversal was called
                setEndTraversalCalled(true);
            }
        };
        AbstractPeepholeOptimization[] optimizations = {optimization};

        // Create a PeepholeOptimizationsPass object
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

        // Call endTraversal to trigger the optimization's endTraversal method
        pass.endTraversal();

        // Verify that the optimization's endTraversal method was called
        boolean endTraversalCalled = optimization.isEndTraversalCalled();
        assertTrue(endTraversalCalled);
    }

    // Mock classes

    private class AbstractCompiler {
        private ArrayList<CodeChangeHandler> changeHandlers = new ArrayList<>();

        void addChangeHandler(CodeChangeHandler handler) {
            changeHandlers.add(handler);
        }

        void removeChangeHandler(CodeChangeHandler handler) {
            changeHandlers.remove(handler);
        }
    }

    private class AbstractPeepholeOptimization {
        private boolean processCalled = false;
        private boolean beginTraversalCalled = false;
        private boolean endTraversalCalled = false;

        protected Node optimizeSubtree(Node node) {
            return node;
        }

        void beginTraversal(AbstractCompiler compiler) {
            beginTraversalCalled = true;
        }

        void endTraversal(AbstractCompiler compiler) {
            endTraversalCalled = true;
        }

        void setBeginTraversalCalled(boolean beginTraversalCalled) {
            this.beginTraversalCalled = beginTraversalCalled;
        }

        boolean isBeginTraversalCalled() {
            return beginTraversalCalled;
        }

        void setEndTraversalCalled(boolean endTraversalCalled) {
            this.endTraversalCalled = endTraversalCalled;
        }

        boolean isEndTraversalCalled() {
            return endTraversalCalled;
        }

        boolean isProcessCalled() {
            return processCalled;
        }

        void reportChange() {
            processCalled = true;
        }
    }

    private interface CodeChangeHandler {
        void reportChange();
    }

}