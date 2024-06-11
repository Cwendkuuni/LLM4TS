import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.gradient.VectorFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.vector.leastsq.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.leastsq.LevenbergMarquardtOptimizer.Algorithm;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestRuleChain;
import org.junit.rules.TestRuleChain.outerRule;
import org.junit.rules.TestRuleChain.innerRule;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    private static final int N = 3;
    private static final int D = 2;
    private static final double[][] X = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
    private static final double[] Y = new double[]{2.0, 3.5, 5.0};
    private static final double[] WEIGHTS = new double[]{1.0, 1.0, 1.0};

    private static final double[] START_POINT = new double[]{1.0, 1.0};
    private static final double EPSILON = 1e-8;

    private LevenbergMarquardtOptimizer optimizer;
    private MultivariateFunction function;
    private UnivariateFunction testFunction;
    private PointValuePair expectedOptimalPointValuePair;
    private TestRuleChain testRules;

    @Rule
    public TestRuleChain testRulesChain = new TestRuleChain();

    @Before
    public void setUp() {
        testFunction = new LinearRegressionTestFunction(X, Y);
        optimizer = new LevenbergMarquardtOptimizer(N, GoalType.MINIMIZE, new VectorFunctionGradient(testFunction));
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(1000);
        optimizer.setStartPoint(START_POINT);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(EPSILON));
        optimizer.setAlgorithm(Algorithm.LM_MARQUIS_BFGS);
        function = new MultivariateFunctionWrapper(testFunction);
        expectedOptimalPointValuePair = new PointValuePair(new double[]{1.5, 1.5}, 0.0);
        testRules = new TestRuleChain();
        testRules.around(new ExpectedException.ThrowingCallable<Object>() {
            @Override
            public Object call() throws Throwable {
                optimizer.optimize(function, null, null, null);
            }
        });
        testRulesChain.addRule(testRules);
    }

    @After
    public void tearDown() {
        optimizer = null;
    }

    @Test
    public void testOptimization() {
        PointValuePair optimalPointValuePair = optimizer.optimize(function, null, null, START_POINT);
        assertEquals("Unexpected optimal value", 0.0, optimalPointValuePair.getValue(), EPSILON);
        assertArrayEquals("Unexpected optimal point", expectedOptimalPointValuePair.getPoint(), optimalPointValuePair.getPoint(), EPSILON);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException {
        PointValuePair optimalPointValuePair = optimizer.optimize(function, null, null, null);
    }

    @Test(expected = OptimizationException.class)
    public void testConvergenceException() {
        optimizer.setMaxIterations(0);
        PointValuePair optimalPointValuePair = optimizer.optimize(function, null, null, START_POINT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        optimizer.setMaxIterations(-1);
        PointValuePair optimalPointValuePair = optimizer.optimize(function, null, null, START_POINT);
    }

    @Test
    public void testGetRMS() {
        double rms = AbstractLeastSquaresOptimizer.getRMS(X, Y, WEIGHTS);
        assertEquals("Unexpected RMS value", Math.sqrt(0.5), rms, EPSILON);
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = AbstractLeastSquaresOptimizer.getChiSquare(X, Y, WEIGHTS);
        assertEquals("Unexpected chi-square value", 2.3333333333333335, chiSquare, EPSILON);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        double[] errors = AbstractLeastSquaresOptimizer.guessParametersErrors(X, Y, WEIGHTS);
        Random random = new Random();
        for (int i = 0; i < errors.length; ++i) {
            assertTrue("Unexpected negative error", errors[i] > 0.0);
            double e = errors[i];
            double mean = Math.sqrt(0.5) * Math.sqrt(1.0 / (X.length - 1));
            assertTrue("Unexpected large error", Math.abs(e - mean) < 2 * mean);
            double n = random.nextGaussian() * 0.1;
            assertEquals("Unexpected error after adding random noise", mean + n, errors[i], EPSILON);
        }
    }

    @Test
    public void testCovariances() throws FunctionEvaluationException, OptimizationException {
        double[][] covariances = AbstractLeastSquaresOptimizer.getCovariances(X, Y, WEIGHTS);
        double[] errors = AbstractLeastSquaresOptimizer.guessParametersErrors(X, Y, WEIGHTS);
        for (int i = 0; i < covariances.length; ++i) {
            for (int j = 0; j < covariances[i].length; ++j) {
                assertTrue("Unexpected negative covariance", covariances[i][j] >= 0.0);
                double e = errors[i] * errors[j];
                double mean = Math.sqrt(0.5) * Math.sqrt(1.0 / (X.length - 1));
                assertEquals("Unexpected covariance value", (i == j) ? mean * mean : mean * Math.sqrt(1.0 / (X.length - 1)), covariances[i][j], EPSILON);
            }
        }
    }

    @Test
    public void testInitialize() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals("Unexpected default maximal number of iterations", DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals("Unexpected default convergence checker", new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(10);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(0.1));
        assertEquals("Unexpected maximal number of iterations after setting", 10, optimizer.getMaxIterations());
        assertEquals("Unexpected convergence checker after setting", new SimpleVectorialValueChecker(0.1), optimizer.getConvergenceChecker());
    }

    /**
     * Linear regression test function.
     */
    private static class LinearRegressionTestFunction implements MultivariateFunction {

        private final double[][] X;
        private final double[] Y;

        LinearRegressionTestFunction(double[][] X, double[] Y) {
            this.X = X;
            this.Y = Y;
        }

        @Override
        public double value(double[] point) {
            double sum = 0.0;
            for (int i = 0; i < X.length; ++i) {
                double innerProduct = 0.0;
                for (int j = 0; j < point.length; ++j) {
                    innerProduct += X[i][j] * point[j];
                }
                sum += (Y[i] - innerProduct) * (Y[i] - innerProduct);
            }
            return sum;
        }

        @Override
        public int getNumberOfVariables() {
            return D;
        }
    }

    /**
     * Wrapper for {@link LinearRegressionTestFunction} to be used as {@link MultivariateFunction}
     * in optimizer.
     */
    private static class MultivariateFunctionWrapper implements MultivariateFunction {

        private final UnivariateFunction function;

        MultivariateFunctionWrapper(UnivariateFunction function) {
            this.function = function;
        }

        @Override
        public double value(double[] point) {
            return function.value(new UnivariateFunctionWrapper(point).value());
        }

        @Override
        public int getNumberOfVariables() {
            return D;
        }

        private static class UnivariateFunctionWrapper implements UnivariateFunction {

            private final double[] point;

            UnivariateFunctionWrapper(double[] point) {
                this.point = point;
            }

            @Override
            public double value() {
                return point[0];
            }
        }
    }
}