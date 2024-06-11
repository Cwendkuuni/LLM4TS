import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.abstractapi.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.convergence.ConvergenceChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction testFunction;
    private double[] expectedSolution;
    private double[] startPoint;
    private double[] expectedTargetValues;
    private double[] expectedWeights;

    @Before
    public void setup() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // Empty implementation for testing purposes
                return new PointValuePair(point, FunctionUtils.value(function, point));
            }
        };

        testFunction = (DifferentiableMultivariateVectorialFunction) new MultivariateVectorFunction(x -> new double[]{-Math.pow(x[0], 2) - Math.pow(x[1], 2), Math.pow(x[0], 2) + Math.pow(x[1], 2)}){
            @Override
            public double value(double[] x) {
                return -value(x)[0];
            }

            @Override
            public double[] gradient(double[] x) {
                return value(x)[1] > 0 ? new double[]{-2 * x[0], -2 * x[1]} : new double[]{2 * x[0], 2 * x[1]};
            }
        };

        expectedSolution = new double[]{1.0, 1.0};
        expectedTargetValues = new double[]{-1.0, 1.0};
        expectedWeights = new double[]{1.0, 1.0};

        startPoint = new double[]{2.0, 2.0};
    }

    @Test
    public void testOptimize() throws Exception {
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(100);
        optimizer.setConvergenceChecker(new ConvergenceChecker() {
            @Override
            public boolean isConverged(double[] baseSolution, double[] currentSolution, double tolerance) {
                return FunctionUtils.distance(baseSolution, currentSolution) < 0.001;
            }
        });

        PointValuePair result = (PointValuePair) optimizer.optimize(testFunction, expectedTargetValues, expectedWeights, startPoint);

        Assert.assertArrayEquals(expectedSolution, result.getPoint(), 0.001);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceededException() throws Exception {
        optimizer.setMaxIterations(0);
        optimizer.optimize(testFunction, expectedTargetValues, expectedWeights, startPoint);
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceededException() throws Exception {
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(testFunction, expectedTargetValues, expectedWeights, startPoint);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        optimizer.optimize(testFunction, new double[]{1.0}, expectedWeights, startPoint);
    }
}