import org.apache.commons.math3.optim.AbstractVectorOptimizer;
import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.VectorialConvergenceChecker;
import org.apache.commons.math3.optim.VectorialPointValuePair;
import org.apache.commons.math3.optim.nonlinear.linear.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(5);
        Assert.assertEquals(5, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxIterations() {
        Assert.assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        Assert.assertEquals(0, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounter() throws OptimizationException {
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        double[][] jacobian = optimizer.jacobian(new double[] {1.0, 2.0});
        Assert.assertNotNull(jacobian);
        Assert.assertEquals(2, jacobian.length);
        Assert.assertEquals(2, jacobian[0].length);
        Assert.assertEquals(2, jacobian[1].length);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianThrowsFunctionEvaluationException() throws FunctionEvaluationException {
        optimizer.updateJacobian();
        throw new UnsupportedOperationException("updateJacobian should not be called directly");
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        optimizer.updateResidualsAndCost();
        Assert.assertEquals(0.0, optimizer.getCost(), 1e-15);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCostThrowsFunctionEvaluationException() throws FunctionEvaluationException {
        optimizer.updateResidualsAndCost();
        throw new UnsupportedOperationException("updateResidualsAndCost should not be called directly");
    }

    @Test
    public void testGetRMS() {
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {3.0, 4.0};
        double[] point = optimizer.optimize(new GaussNewtonFunction(), target, weights, startPoint).getPoint();
        Assert.assertArrayEquals(new double[] {point[0], point[1]}, new double[] {1.0, 1.0}, 0.001);
        Assert.assertEquals(1.0, optimizer.getRMS(), 1e-15);
    }

    @Test
    public void testGetChiSquare() {
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {3.0, 4.0};
        double[] point = optimizer.optimize(new GaussNewtonFunction(), target, weights, startPoint).getPoint();
        Assert.assertArrayEquals(new double[] {point[0], point[1]}, new double[] {1.0, 1.0}, 0.001);
        Assert.assertEquals(2.0, optimizer.getChiSquare(), 1e-15);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {3.0, 4.0};
        double[] point = optimizer.optimize(new GaussNewtonFunction(), target, weights, startPoint).getPoint();
        double[][] covariances = optimizer.getCovariances();
        Assert.assertArrayEquals(new double[][] {
                {0.00532516259153774, 0.0033845584258297},
                {0.0033845584258297, 0.00662358860572463}
        }, covariances, 1e-15);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        double[] target = new double[] {1.0, 2.0};
        double[] weights = new double[] {1.0, 1.0};
        double[] startPoint = new double[] {3.0, 4.0};
        double[] point = optimizer.optimize(new GaussNewtonFunction(), target, weights, startPoint).getPoint();
        double[] errors = optimizer.guessParametersErrors();
        Assert.assertEquals(Math.sqrt(2.0) / Math.sqrt(3.0), errors[0], 1e-15);
        Assert.assertEquals(Math.sqrt(2.0) / Math.sqrt(3.0), errors[1], 1e-15);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeThrowsMaxIterationsExceededException() throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxIterations(1);
        optimizer.optimize(new GaussNewtonFunction(), new double[] {1.0, 2.0}, new double[] {1.0, 1.0},
                new double[] {3.0, 4.0});
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimizeThrowsMaxEvaluationsExceededException() throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxEvaluations(1);
        optimizer.optimize(new GaussNewtonFunction(), new double[] {1.0, 2.0}, new double[] {1.0, 1.0},
                new double[] {3.0, 4.0});
    }

    @Test
    public void testSetConvergenceChecker() {
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        Assert.assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceChecker() {
        Assert.assertSame(optimizer.getClass().getSuperclass(), optimizer.getConvergenceChecker().getClass().getSuperclass());
    }

    private static class GaussNewtonFunction implements DifferentiableMultivariateVectorialFunction {

        @Override
        public double value(double[] x) {
            return Math.pow(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0), 0.5);
        }

        @Override
        public double[] value(double[] x, double[] grad, int index) {
            return new double[] {2.0 * (x[0] - 1.0) / Math.sqrt(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0)),
                    2.0 * (x[1] - 1.0) / Math.sqrt(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0))};
        }

        @Override
        public int getNumberOfVariables() {
            return 2;
        }

        @Override
        public int getNumberOfValues() {
            return 1;
        }

        @Override
        public VectorialPointValuePair optimize(double[] x, VectorialConvergenceChecker constraintHandler)
                throws FunctionEvaluationException, OptimizationException {
            return new VectorialPointValuePair(new double[] {1.0, 1.0}, 0.0);
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                @Override
                public double[] value(double[] x) {
                    return new double[] {2.0 * (x[0] - 1.0) / Math.sqrt(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0)),
                            2.0 * (x[1] - 1.0) / Math.sqrt(Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0))};
                }

                @Override
                public int getNumberOfVariables() {
                    return 2;
                }

                @Override
                public int getNumberOfValues() {
                    return 2;
                }
            };
        }
    }
}