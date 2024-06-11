import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setMaxIterations(200);
        assertEquals(200, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        assertEquals(1, optimizer.getIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setMaxEvaluations(1000);
        assertEquals(1000, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        assertEquals(1, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.setConvergenceChecker(new TestConvergenceChecker());
        assertTrue(optimizer.getConvergenceChecker() instanceof TestConvergenceChecker);
    }

    @Test
    public void testOptimize() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        assertTrue(optimizer.getCost() < Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        assertTrue(optimizer.getRMS() >= 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        assertTrue(optimizer.getChiSquare() >= 0.0);
    }

    @Test
    public void testGetCovariances() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        double[][] covariances = optimizer.getCovariances();
        assertNotNull(covariances);
        assertEquals(optimizer.getCols(), covariances.length);
        for (int i = 0; i < covariances.length; i++) {
            assertEquals(optimizer.getCols(), covariances[i].length);
        }
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        optimizer.optimize(new TestFunction(), new double[]{1.0}, new double[]{1.0}, new double[]{0.0});
        double[] errors = optimizer.guessParametersErrors();
        assertNotNull(errors);
        assertEquals(optimizer.getCols(), errors.length);
    }
    
    private class TestOptimizer extends AbstractLeastSquaresOptimizer {
        @Override
        protected VectorialPointValuePair doOptimize() {
            return new VectorialPointValuePair(new double[]{1.0}, 0.0);
        }
    }

    private class TestFunction implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] point) throws FunctionEvaluationException {
            return new double[]{1.0};
        }
        @Override
        public MultivariateMatrixFunction jacobian() {
            return new TestJacobian();
        }
    }

    private class TestJacobian implements MultivariateMatrixFunction {
        @Override
        public double[][] value(double[] point) throws FunctionEvaluationException {
            return new double[][]{{1.0}};
        }
    }
    
    private class TestConvergenceChecker implements VectorialConvergenceChecker {
        @Override
        public boolean converged(int iteration, VectorialPointValuePair previous, VectorialPointValuePair current) {
            return true;
        }
    }

}