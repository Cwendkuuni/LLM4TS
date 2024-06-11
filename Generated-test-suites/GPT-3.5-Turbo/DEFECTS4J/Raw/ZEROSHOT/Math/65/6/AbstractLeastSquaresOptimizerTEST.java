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
            protected VectorialPointValuePair doOptimize() {
                return null;
            }
        };
        target = new double[]{1, 2, 3};
        weights = new double[]{1, 1, 1};
        startPoint = new double[]{0, 0, 0};
    }

    @Test
    public void testGetMaxIterations() {
        assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(200);
        assertEquals(200, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        assertEquals(0, optimizer.getIterations());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(500);
        assertEquals(500, optimizer.getMaxEvaluations());
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
    public void testGetConvergenceChecker() {
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetConvergenceChecker() {
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetRMS() throws FunctionEvaluationException, OptimizationException {
        assertEquals(Double.NaN, optimizer.getRMS(), 0.0);
    }

    @Test
    public void testGetChiSquare() throws FunctionEvaluationException, OptimizationException {
        assertEquals(Double.NaN, optimizer.getChiSquare(), 0.0);
    }

    @Test(expected = OptimizationException.class)
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        optimizer.getCovariances();
    }

    @Test(expected = OptimizationException.class)
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        optimizer.guessParametersErrors();
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize() throws FunctionEvaluationException, OptimizationException {
        optimizer.optimize(null, null, null, null);
    }
}