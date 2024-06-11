import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {
    
    private AbstractLeastSquaresOptimizer optimizer;
    private double[] target;
    private double[] weights;
    private double[] startPoint;
    
    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return null;
            }
        };
        
        target = new double[]{1.0, 2.0, 3.0};
        weights = new double[]{0.5, 0.5, 0.5};
        startPoint = new double[]{0.0, 0.0, 0.0};
    }
    
    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(150);
        assertEquals(150, optimizer.getMaxIterations());
    }
    
    @Test
    public void testGetIterations() {
        assertEquals(0, optimizer.getIterations());
    }
    
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(1000);
        assertEquals(1000, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, optimizer.getJacobianEvaluations());
    }
    
    @Test
    public void testSetConvergenceChecker() {
        SimpleVectorialValueChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testGetRMS() {
        assertEquals(0.0, optimizer.getRMS(), 0.0001);
    }
    
    @Test
    public void testGetChiSquare() {
        assertEquals(0.0, optimizer.getChiSquare(), 0.0001);
    }
    
    @Test
    public void testGetCovariances() {
        try {
            double[][] covariances = optimizer.getCovariances();
            assertNull(covariances);
        } catch (FunctionEvaluationException e) {
            fail("An exception occurred: " + e.getMessage());
        } catch (OptimizationException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGuessParametersErrors() {
        try {
            double[] errors = optimizer.guessParametersErrors();
            assertNull(errors);
        } catch (FunctionEvaluationException e) {
            fail("An exception occurred: " + e.getMessage());
        } catch (OptimizationException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testOptimize() {
        try {
            VectorialPointValuePair result = optimizer.optimize(null, target, weights, startPoint);
            assertNull(result);
        } catch (FunctionEvaluationException e) {
            fail("An exception occurred: " + e.getMessage());
        } catch (OptimizationException e) {
            fail("An exception occurred: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}