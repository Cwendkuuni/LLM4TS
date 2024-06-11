import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceeded() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                throw new OptimizationException(new MaxIterationsExceededException(getMaxIterations()));
            }
        };

        optimizer.setMaxIterations(5);

        double[] target = {1, 2, 3, 4, 5};
        double[] weights = {1, 1, 1, 1, 1};
        double[] startPoint = {0, 0, 0, 0, 0};

        optimizer.optimize(new DifferentiableMultivariateVectorialFunction() {
            public double[] value(double[] point) throws FunctionEvaluationException {
                return null;
            }

            public MultivariateMatrixFunction jacobian(){
                return null;
            }
        }, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxEvaluationsExceeded() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                throw new OptimizationException(new MaxEvaluationsExceededException(getMaxEvaluations()));
            }
        };

        optimizer.setMaxEvaluations(10);

        double[] target = {1, 2, 3, 4, 5};
        double[] weights = {1, 1, 1, 1, 1};
        double[] startPoint = {0, 0, 0, 0, 0};

        optimizer.optimize(new DifferentiableMultivariateVectorialFunction() {
            public double[] value(double[] point) throws FunctionEvaluationException {
                return null;
            }

            public MultivariateMatrixFunction jacobian(){
                return null;
            }
        }, target, weights, startPoint);
    }

    @Test
    public void testOptimize() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            protected VectorialPointValuePair doOptimize() {
                return new VectorialPointValuePair(new double[]{1, 2, 3, 4, 5}, 0);
            }
        };

        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);

        double[] target = {1, 2, 3, 4, 5};
        double[] weights = {1, 1, 1, 1, 1};
        double[] startPoint = {0, 0, 0, 0, 0};

        VectorialPointValuePair result = optimizer.optimize(new DifferentiableMultivariateVectorialFunction() {
            public double[] value(double[] point) throws FunctionEvaluationException {
                return null;
            }

            public MultivariateMatrixFunction jacobian(){
                return null;
            }
        }, target, weights, startPoint);

        Assert.assertArrayEquals(new double[]{1, 2, 3, 4, 5}, result.getPoint(), 0.0001);
        Assert.assertEquals(0, result.getValue(), 0.0001);
    }
}