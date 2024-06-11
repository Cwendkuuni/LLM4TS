import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {
    
    private AbstractLeastSquaresOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                // dummy implementation for testing
                return null;
            }
        };
    }
    
    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testSetConvergenceChecker() {
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        assertSame(checker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetIterations() {
        optimizer.incrementIterationsCounter();
        assertEquals(1, optimizer.getIterations());
    }
    
    @Test
    public void testGetEvaluations() {
        optimizer.updateResidualsAndCost();
        assertEquals(1, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetJacobianEvaluations() {
        optimizer.updateJacobian();
        assertEquals(1, optimizer.getJacobianEvaluations());
    }
    
    @Test
    public void testGetRMS() {
        assertEquals(0.0, optimizer.getRMS(), 0.0001);
    }
    
    @Test
    public void testGetChiSquare() {
        assertEquals(0.0, optimizer.getChiSquare(), 0.0001);
    }
    
    @Test(expected = OptimizationException.class)
    public void testGetCovariancesThrowsException() throws FunctionEvaluationException, OptimizationException {
        optimizer.getCovariances();
    }
    
    @Test(expected = OptimizationException.class)
    public void testGuessParametersErrorsThrowsException() throws FunctionEvaluationException, OptimizationException {
        optimizer.guessParametersErrors();
    }
    
    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException{
        DifferentiableMultivariateVectorialFunction f = new DifferentiableMultivariateVectorialFunction() {
            public double[] value(double[] point) throws FunctionEvaluationException {
                // dummy implementation for testing
                return new double[1];
            }
            
            public MultivariateMatrixFunction jacobian() {
                // dummy implementation for testing
                return null;
            }
        };
        
        double[] target = new double[1];
        double[] weights = new double[1];
        double[] startPoint = new double[1];
        
        VectorialPointValuePair result = optimizer.optimize(f, target, weights, startPoint);
        
        assertNull(result);
    }
}