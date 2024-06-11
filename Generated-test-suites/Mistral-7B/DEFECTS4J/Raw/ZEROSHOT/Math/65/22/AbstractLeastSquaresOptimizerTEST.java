import org.apache.commons.math3.optim.AbstractMultivariateOptimizerTest;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.RealVector;
import org.apache.commons.math3.optim.nonlinear.functions.LinearRegressionFunction;
import org.apache.commons.math3.optim.nonlinear.functions.LinearRegressionFunctionGradient;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AbstractLeastSquaresOptimizerTest extends AbstractMultivariateOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() throws Exception {
        // Given
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new LinearRegressionFunction(3);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                optimizeInternal(function, target, weights, startPoint);
                return new PointValuePair(point, objective);
            }
        };

        // When
        PointValuePair result = optimizer.optimize(function, target, weights, startPoint);

        // Then
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbstractLeastSquaresOptimizerDimensionsMismatch() throws Exception {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new LinearRegressionFunction(3);
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();

        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testAbstractLeastSquaresOptimizerFunctionEvaluationException() throws Exception {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {1.0, 1.0, 1.0};
        DifferentiableMultivariateVectorialFunction function = new LinearRegressionFunction(3) {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x, LocalizedFormats.SINGULAR_MATRIX, 3, 3);
            }
        };
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                optimizeInternal(function, target, weights, startPoint);
                return new PointValuePair(point, objective);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testAbstractLeastSquaresOptimizerConvergenceException() throws Exception {
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        DifferentiableMultivariateVectorialFunction function = new LinearRegressionFunction(3) {
            @Override
            public double[] value(double[] x) throws FunctionEvaluationException {
                return new double[]{x[0], x[1], x[2]};
            }

            @Override
            public RealVector jacobian(double[] x) throws FunctionEvaluationException {
                return new org.apache.commons.math3.linear.ArrayRealVector(new double[]{1.0, 1.0, 1.0});
            }
        };
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                optimizeInternal(function, target, weights, startPoint);
                return new PointValuePair(point, objective);
            }
        };

        optimizer.setMaxIterations(0);
        optimizer.optimize(function, target, weights, startPoint);
    }

    private void optimizeInternal(DifferentiableMultivariateVectorialFunction function, double[] target,
                                 double[] weights, double[] startPoint) throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Initialize optimizer
                this.setMaxIterations(100);
                this.setMaxEvaluations(Integer.MAX_VALUE);
                this.setConvergenceChecker(new org.apache.commons.math3.optim.nonlinear.SimpleVectorValueChecker(1.0E-6));
                updateResidualsAndCost();

                while (iterations < maxIterations && cost > 1E-6) {
                    incrementIterationsCounter();
                    updateJacobian();
                    point = minimizeFunctionWithJacobian(function, jF, point, objective, target, weights);
                    updateResidualsAndCost();
                }

                return new PointValuePair(point, objective);
            }
        };

        optimizer.optimize(function, target, weights, startPoint);
    }
}