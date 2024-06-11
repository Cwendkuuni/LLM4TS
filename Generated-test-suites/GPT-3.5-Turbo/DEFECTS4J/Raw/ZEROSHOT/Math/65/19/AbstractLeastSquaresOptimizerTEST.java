import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
    }
    
    @Test
    public void testGetMaxIterations() {
        int maxIterations = optimizer.getMaxIterations();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, maxIterations);
    }
    
    @Test
    public void testSetMaxIterations() {
        int maxIterations = 10;
        optimizer.setMaxIterations(maxIterations);
        assertEquals(maxIterations, optimizer.getMaxIterations());
    }
    
    @Test
    public void testGetIterations() {
        int iterations = optimizer.getIterations();
        assertEquals(0, iterations);
    }
    
    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(Integer.MAX_VALUE, maxEvaluations);
    }
    
    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }
    
    @Test
    public void testGetJacobianEvaluations() {
        int jacobianEvaluations = optimizer.getJacobianEvaluations();
        assertEquals(0, jacobianEvaluations);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        VectorialConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleVectorialValueChecker);
    }
    
    @Test
    public void testSetConvergenceChecker() {
        VectorialConvergenceChecker checker = new ConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testIncrementIterationsCounter() {
        int maxIterations = 2;
        optimizer.setMaxIterations(maxIterations);
        
        try {
            optimizer.incrementIterationsCounter();
        } catch (OptimizationException e) {
            fail("Should not throw exception");
        }
        
        try {
            optimizer.incrementIterationsCounter();
            fail("Should throw exception");
        } catch (OptimizationException e) {
            assertEquals(2, e.getMaxIterations());
        }
    }
    
    // TODO: Add more tests for other methods

}