import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {
    
    @Test
    public void testDefaultMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
    }
    
    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        int newMaxIterations = 200;
        optimizer.setMaxIterations(newMaxIterations);
        assertEquals(newMaxIterations, optimizer.getMaxIterations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        int newMaxEvaluations = 500;
        optimizer.setMaxEvaluations(newMaxEvaluations);
        assertEquals(newMaxEvaluations, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testRMS() {
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        double rms = optimizer.getRMS();
        assertTrue(rms >= 0);
    }
    
    @Test
    public void testChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        double chiSquare = optimizer.getChiSquare();
        assertTrue(chiSquare >= 0);
    }
    
    @Test
    public void testGuessParametersErrors() {
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        assertThrows(FunctionEvaluationException.class, () -> optimizer.guessParametersErrors());
    }
}

class TestLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
    // Implement necessary methods for testing
    // This class is used as a test implementation of AbstractLeastSquaresOptimizer
}