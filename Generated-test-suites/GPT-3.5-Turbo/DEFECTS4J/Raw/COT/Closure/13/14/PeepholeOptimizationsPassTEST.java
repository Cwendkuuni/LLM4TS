
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PeepholeOptimizationsPassTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

        // Set up expectations for the compiler and optimizations

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
        
        // Call the process method
        pass.process(mock(Node.class), mock(Node.class));
        
        // Verify that the compiler methods were called
        verify(compiler).addChangeHandler(any(PeepholeOptimizationsPass.PeepholeChangeHandler.class));
        verify(compiler).removeChangeHandler(any(PeepholeOptimizationsPass.PeepholeChangeHandler.class));

        // Verify that the optimization methods were called
        verify(optimization1, atLeastOnce()).optimizeSubtree(any(Node.class));
        verify(optimization2, atLeastOnce()).optimizeSubtree(any(Node.class));
        verify(optimization1).beginTraversal(compiler);
        verify(optimization2).beginTraversal(compiler);
        verify(optimization1).endTraversal(compiler);
        verify(optimization2).endTraversal(compiler);
    }
}
