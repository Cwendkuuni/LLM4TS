import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {
    private PeepholeOptimizationsPass pass;
    private AbstractCompiler compiler;
  
    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        pass = new PeepholeOptimizationsPass(compiler);
    }
  
    @Test
    public void testGetCompiler() {
        assertEquals(compiler, pass.getCompiler());
    }
  
    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);
        CodeChangeHandler handler = mock(CodeChangeHandler.class);
    
        pass.process(externs, root);
    
        verify(compiler).addChangeHandler(handler);
        verify(compiler).removeChangeHandler(handler);
        verify(handler).reportChange();
    }
  
    @Test
    public void testShouldVisit() {
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
    
        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(true);
    
        assertTrue(pass.shouldVisit(node));
    
        pass.traversalState.peek().traverseChildScopes = false;
    
        assertFalse(pass.shouldVisit(node));
    }
  
    @Test
    public void testExitNode() {
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
    
        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(true);
    
        pass.traversalState.push();
        pass.exitNode(node);
    
        assertEquals(0, pass.traversalState.currentDepth);
    }

    // Add tests for other methods of the class

}