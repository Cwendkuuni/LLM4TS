import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.exception.*;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new ConcreteLeastSquaresOptimizer();
        startPoint = new double[]{1.0, 2.0, 3.0};
    }

    @Test
    public void testSetMaxIterations() {
        int maxIterations = 100;
        optimizer.setMaxIterations(maxIterations);
        assertEquals(maxIterations, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxIterations() {
        int maxIterations = 100;
        optimizer.setMaxIterations(maxIterations);
        assertEquals(maxIterations, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        assertEquals(0, optimizer.getIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 100;
        optimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
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
        VectorialConvergenceChecker checker = new ConcreteConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        VectorialConvergenceChecker checker = new ConcreteConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testIncrementIterationsCounter() {
        int maxIterations = 100;
        optimizer.setMaxIterations(maxIterations);
        for (int i = 0; i < maxIterations; i++) {
            optimizer.incrementIterationsCounter();
            assertEquals(i + 1, optimizer.getIterations());
        }
        try {
            optimizer.incrementIterationsCounter();
            fail("Expected OptimizationException");
        } catch (OptimizationException e) {
            assertEquals(LocalizedFormats.MAX_ITERATIONS_EXCEEDED, e.getSpecifier());
        }
    }

    @Test
    public void testDoOptimize() {
        VectorialPointValuePair result = optimizer.optimize(new ConcreteFunction(), new double[]{1.0}, new double[]{1.0},
                startPoint);
        assertNotNull(result);
        assertEquals(3, result.getPoint().length);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    private class ConcreteLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {

        @Override
        protected VectorialPointValuePair doOptimize()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            double[] point = new double[]{0.0, 0.0, 0.0};
            return new VectorialPointValuePair(point, 0.0);
        }
    }

    private class ConcreteFunction implements DifferentiableMultivariateVectorialFunction {

        @Override
        public double[] value(double[] point) throws FunctionEvaluationException {
            return new double[]{0.0};
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                @Override
                public double[][] value(double[] point) throws FunctionEvaluationException {
                    return new double[][]{{0.0, 0.0, 0.0}};
                }
            };
        }
    }

    private class ConcreteConvergenceChecker implements VectorialConvergenceChecker {

        @Override
        public boolean converged(int iteration, VectorialPointValuePair previous, VectorialPointValuePair current) {
            return false;
        }
    }
}