
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {
    
    private PeepholeOptimizationsPass pass;
    private DummyCompiler compiler;
    private DummyPeepholeOptimization optimization;
    
    @Before
    public void setUp() {
        compiler = new DummyCompiler();
        optimization = new DummyPeepholeOptimization();
        pass = new PeepholeOptimizationsPass(compiler, optimization);
    }
    
    @Test
    public void testProcess() {
        pass.process(null, null);
        assertTrue(compiler.addChangeHandlerCalled);
        assertTrue(compiler.removeChangeHandlerCalled);
        assertTrue(optimization.beginTraversalCalled);
        assertTrue(optimization.endTraversalCalled);
    }
    
    @Test
    public void testShouldVisit_withScriptNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(pass.shouldVisit(scriptNode));
        assertEquals(2, pass.traversalState.states.size());
        assertTrue(pass.traversalState.states.get(1).traverseChildScopes);
    }
    
    @Test
    public void testShouldVisit_withFunctionNode() {
        Node functionNode = new Node(Token.FUNCTION);
        pass.shouldVisit(functionNode);
        assertTrue(pass.traversalState.states.get(1).traverseChildScopes);
        assertEquals(2, pass.traversalState.states.size());
    }
    
    @Test
    public void testShouldRetraverse_withParentFunctionNodeAndScopeStateNotChanged() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(functionNode);
        Node scriptNode = new Node(Token.SCRIPT, parentFunctionNode);
        pass.shouldVisit(scriptNode);
        
        boolean shouldRetraverse = pass.shouldRetraverse(parentFunctionNode);
        
        assertFalse(shouldRetraverse);
    }
    
    @Test
    public void testShouldRetraverse_withParentFunctionNodeAndScopeStateChanged() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentFunctionNode = new Node(Token.FUNCTION);
        parentFunctionNode.addChildToFront(functionNode);
        Node scriptNode = new Node(Token.SCRIPT, parentFunctionNode);
        pass.shouldVisit(scriptNode);
        
        pass.traversalState.peek().changed = true;
        boolean shouldRetraverse = pass.shouldRetraverse(parentFunctionNode);
        
        assertTrue(shouldRetraverse);
        assertFalse(pass.traversalState.peek().traverseChildScopes);
    }
    
    @Test
    public void testShouldRetraverse_withScriptNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        pass.shouldVisit(scriptNode);
        
        boolean shouldRetraverse = pass.shouldRetraverse(scriptNode);
        
        assertFalse(shouldRetraverse);
    }
    
    @Test
    public void testShouldRetraverse_withFunctionNodeNotInScope() {
        Node functionNode = new Node(Token.FUNCTION);
        boolean shouldRetraverse = pass.shouldRetraverse(functionNode);
        
        assertFalse(shouldRetraverse);
    }
    
    private class DummyCompiler extends AbstractCompiler {
        boolean addChangeHandlerCalled = false;
        boolean removeChangeHandlerCalled = false;
        
        @Override
        public void addChangeHandler(CodeChangeHandler handler) {
            addChangeHandlerCalled = true;
        }
        
        @Override
        public void removeChangeHandler(CodeChangeHandler handler) {
            removeChangeHandlerCalled = true;
        }
    }
    
    private class DummyPeepholeOptimization extends AbstractPeepholeOptimization {
        boolean beginTraversalCalled = false;
        boolean endTraversalCalled = false;
        
        @Override
        protected Node optimizeSubtree(Node subtree) {
            return null;
        }
        
        @Override
        void beginTraversal(AbstractCompiler compiler) {
            beginTraversalCalled = true;
        }
        
        @Override
        void endTraversal(AbstractCompiler compiler) {
            endTraversalCalled = true;
        }
    }
}
