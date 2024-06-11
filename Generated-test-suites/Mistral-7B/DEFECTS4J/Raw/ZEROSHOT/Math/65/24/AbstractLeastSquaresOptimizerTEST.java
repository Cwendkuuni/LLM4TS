import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleLinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairChecker;
import org.junit.Assert;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {

            @Override
            protected PointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Perform optimization here
                return new PointValuePair(new double[]{1.0, 2.0}, new double[]{0.0, 0.0});
            }
        };

        // Test default constructor with default settings
        optimizer.setMaxIterations(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());

        // Test setting the maximum number of iterations
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());

        // Test setting the maximum number of evaluations
        optimizer.setMaxEvaluations(50);
        Assert.assertEquals(50, optimizer.getMaxEvaluations());

        // Test getting the iterations counter
        optimizer.optimize(function1, targetValues1, null, startPoint1);
        Assert.assertEquals(0, optimizer.getIterations());

        // Test getting the optimization evaluations counter
        optimizer.optimize(function1, targetValues1, null, startPoint1);
        Assert.assertEquals(0, optimizer.getEvaluations());

        // Test getting the jacobian evaluations counter
        optimizer.optimize(function1, targetValues1, null, startPoint1);
        Assert.assertEquals(0, optimizer.getJacobianEvaluations());

        // Test the updateJacobian method
        optimizer.updateJacobian();
        Assert.assertArrayEquals(new double[][]{
                {1.1, 1.2},
                {2.1, 2.2}}, optimizer.jacobian, 0.001);

        // Test the updateResidualsAndCost method
        optimizer.updateResidualsAndCost();
        Assert.assertEquals(0.1, optimizer.cost, 0.001);

        // Test the getRMS method
        optimizer.updateResidualsAndCost();
        Assert.assertEquals(0.14142135625, optimizer.getRMS(), 0.001);

        // Test the getChiSquare method
        optimizer.updateResidualsAndCost();
        Assert.assertEquals(1.4142135625, optimizer.getChiSquare(), 0.001);

        // Test the getCovariances method
        optimizer.optimize(function1, targetValues1, null, startPoint1);
        double[][] covariances = optimizer.getCovariances();
        Assert.assertArrayEquals(new double[][]{
                {1.1, 0.0},
                {0.0, 1.1}}, covariances, 0.001);

        // Test the guessParametersErrors method
        optimizer.optimize(function1, targetValues1, null, startPoint1);
        double[] errors = optimizer.guessParametersErrors();
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, errors, 0.001);
    }

    private static MultivariateFunction function1 = new MultivariateFunction() {
        @Override
        public double value(RealVector x) {
            double x1 = x.getEntry(0);
            double x2 = x.getEntry(1);
            return x1 * x1 + x2 * x2;
        }

        @Override
        public RealVector gradient(RealVector x) {
            double x1 = x.getEntry(0);
            double x2 = x.getEntry(1);
            RealVector result = new Array2DRowRealMatrix(2, 1).getColumnVector(0);
            result.setEntry(0, x1 * 2);
            result.setEntry(1, 2 * x2);
            return result;
        }
    };

    private static double[] targetValues1 = {1.0, 1.0};
    private static double[] startPoint1 = {2.0, 2.0};
}