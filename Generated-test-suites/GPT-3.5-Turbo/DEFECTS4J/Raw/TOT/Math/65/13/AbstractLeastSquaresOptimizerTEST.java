import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private double tolerance = 1e-6;

    @Before
    public void setUp() {
        optimizer = new CustomLeastSquaresOptimizer();
    }

    @Test
    public void testMaxIterations() {
        optimizer.setMaxIterations(100);
        assertEquals(100, optimizer.getMaxIterations());
    }

    @Test
    public void testMaxEvaluations() {
        optimizer.setMaxEvaluations(1000);
        assertEquals(1000, optimizer.getMaxEvaluations());
    }

    @Test
    public void testConvergenceChecker() {
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker(1e-9, 1e-9);
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeWithValidInput() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] target = {2.0, 4.0, 6.0};
        double[] weights = {1.0, 1.0, 1.0};

        DifferentiableMultivariateVectorialFunction function = new CustomFunction();
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        assertNotNull(result);
        double[] point = result.getPoint();
        double[] values = result.getValue();

        assertEquals(3, point.length);
        assertEquals(3, values.length);

        for (int i = 0; i < point.length; i++) {
            assertEquals(startPoint[i], point[i], tolerance);
        }

        for (int i = 0; i < values.length; i++) {
            assertEquals(target[i], values[i], tolerance);
        }
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeWithInvalidInput() {
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] target = {2.0, 4.0}; // Invalid target length
        double[] weights = {1.0, 1.0};

        DifferentiableMultivariateVectorialFunction function = new CustomFunction();
        optimizer.optimize(function, target, weights, startPoint);
    }

    private class CustomFunction implements DifferentiableMultivariateVectorialFunction {

        @Override
        public double[] value(double[] point) throws FunctionEvaluationException {
            double[] values = new double[point.length];
            for (int i = 0; i < point.length; i++) {
                values[i] = 2.0 * point[i];
            }
            return values;
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new CustomJacobian();
        }

        private class CustomJacobian implements MultivariateMatrixFunction {

            @Override
            public double[][] value(double[] point) throws FunctionEvaluationException {
                double[][] jacobian = new double[point.length][point.length];
                for (int i = 0; i < point.length; i++) {
                    for (int j = 0; j < point.length; j++) {
                        jacobian[i][j] = 2.0;
                    }
                }
                return jacobian;
            }
        }
    }

    private class CustomLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {

        @Override
        protected VectorialPointValuePair doOptimize()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            // Custom optimization algorithm
            return null;
        }
    }
}