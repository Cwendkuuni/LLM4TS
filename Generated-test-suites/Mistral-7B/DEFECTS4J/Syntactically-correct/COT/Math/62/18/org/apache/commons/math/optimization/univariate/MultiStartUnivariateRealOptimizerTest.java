package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwisterRandomGenerator;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Runner;

import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Method under test.
     */
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    /**
     * Test instance of MultiStartUnivariateRealOptimizer.
     */
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;

    /**
     * Test function.
     */
    private UnivariateRealFunction testFunction;

    /**
     * Test goal type.
     */
    private GoalType goalType;

    /**
     * Test minimum and maximum bounds.
     */
    private double min, max;

    /**
     * Test number of starts.
     */
    private int starts;

    /**
     * Test random generator.
     */
    private RandomGenerator randomGenerator;

    /**
     * Setup method.
     *
     * @throws Exception if initialization fails.
     */
    @Before
    public void setUp() throws Exception {
        optimizer = new FibonacciOptimizer();
        testFunction = new PolynomialFunction(1, new double[]{-1.0, 1.0, -1.0, 1.0});
        goalType = GoalType.MINIMIZE;
        min = 0.0;
        max = 10.0;
        starts = 5;
        randomGenerator = new MersenneTwisterRandomGenerator();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);
    }

    /**
     * Test method {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} with
     * a convergence checker.
     *
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizeWithConvergenceChecker() throws Exception {
        ConvergenceChecker<PointValuePair> convergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean test(PointValuePair value, double tolerance) {
                return Precision.isClose(value.getValue(), 0.0, 1e-8);
            }
        };
        multiStartOptimizer.setConvergenceChecker(convergenceChecker);

        final PointValuePair result = multiStartOptimizer.optimize(testFunction, goalType, min, max);

        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test method {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)} with
     * no convergence checker.
     *
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizeWithoutConvergenceChecker() throws Exception {
        multiStartOptimizer.setConvergenceChecker(null);

        final PointValuePair result = multiStartOptimizer.optimize(testFunction, goalType, min, max);

        assertNotNull(result);
        Runner.runTest(new Runner.TwoValueRunner<Double, Double>() {
            @Override
            public Double test(Double arg1, Double arg2) {
                double x = result.getPoint();
                double fx = testFunction.value(x);
                return Precision.abs(fx);
            }

            @Override
            public Double expectedValue() {
                return Precision.abs(testFunction.minimum(min, max));
            }
        });
    }

    /**
     * Test method {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     *
     * @throws Exception if optimization fails.
     */
    @Test
    public void testGetOptima() throws Exception {
        final int numEvaluations = starts * optimizer.getMaxEvaluations();
        multiStartOptimizer.setMaxEvaluations(numEvaluations);
        multiStartOptimizer.optimize(testFunction, goalType, min, max);

        final PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(starts, optima.length);
        for (int i = 0; i < starts; ++i) {
            assertNotNull(optima[i]);
            Runner.runTest(new Runner.TwoValueRunner<Double, Double>() {
                @Override
                public Double test(Double arg1, Double arg2) {
                    double x = optima[i].getPoint();
                    double fx = testFunction.value(x);
                    return Precision.abs(fx);
                }

                @Override
                public Double expectedValue() {
                    return Precision.abs(testFunction.minimum(min, max));
                }
            });
        }
    }

    /**
     * Test edge cases and exception handling.
     *
     * @throws Exception if initialization or optimization fails.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        optimizer = null;
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException2() throws Exception {
        optimizer = new FibonacciOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, -1, randomGenerator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException3() throws Exception {
        optimizer = new FibonacciOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, null);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException();
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);
        multiStartOptimizer.optimize(testFunction, goalType, min, max);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new ConvergenceException();
            }
        };
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);
        multiStartOptimizer.optimize(testFunction, goalType, min, max);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testMathIllegalStateException() throws Exception {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, randomGenerator);
        multiStartOptimizer.optimize(testFunction, goalType, min, max);
        multiStartOptimizer.optimize(testFunction, goalType, min, max);
    }
}