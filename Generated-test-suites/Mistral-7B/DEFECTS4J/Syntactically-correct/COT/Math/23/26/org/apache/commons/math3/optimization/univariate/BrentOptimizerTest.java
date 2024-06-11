package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends AbstractMathTestCase {
    /** Test function. */
    private static final UnivariateFunction FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x) + 1;
        }

        @Override
        public double derivative(double x) {
            return 3 * Math.pow(x, 2) - 2 * Math.E * x;
        }
    };

    /** Test optimizer. */
    private BrentOptimizer optimizer;

    /** Test point value pair. */
    private UnivariatePointValuePair result;

    /**
     * Set up test environment.
     * @throws Exception if initialization fails.
     */
    @Before
    public void setUp() throws Exception {
        double rel = 1e-6;
        double abs = 1e-6;
        optimizer = new BrentOptimizer(rel, abs, null);
    }

    /**
     * Test constructor with relative and absolute threshold.
     */
    @Test
    public void testBrentOptimizer() {
        assertEquals(BrentOptimizer.class, optimizer.getClass());
        assertEquals(rel, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 1e-9);
    }

    /**
     * Test optimization with initial guess provided by user.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithInitialGuess() throws Exception {
        double x = 2.0;
        optimizer.setStartValue(x);
        result = optimizer.optimize(FUNCTION, MaxEval.UNLIMITED);
        assertEquals(x, result.getPoint(), 1e-9);
        assertEquals(-Math.E * x + 1, result.getValue(), 1e-9);
    }

    /**
     * Test optimization without initial guess.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithoutInitialGuess() throws Exception {
        result = optimizer.optimize(FUNCTION, MaxEval.UNLIMITED);
        assertEquals(-1.7157258251129, result.getPoint(), 1e-9);
        assertEquals(2.2204460492503, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with relative threshold only.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithRelativeThresholdOnly() throws Exception {
        double abs = 0.0;
        optimizer = new BrentOptimizer(1e-6, abs);
        result = optimizer.optimize(FUNCTION, MaxEval.UNLIMITED);
        assertEquals(-1.7157258251129, result.getPoint(), 1e-9);
        assertEquals(2.2204460492503, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with absolute threshold only.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithAbsoluteThresholdOnly() throws Exception {
        double rel = 0.0;
        optimizer = new BrentOptimizer(rel, 1e-6);
        result = optimizer.optimize(FUNCTION, MaxEval.UNLIMITED);
        assertEquals(-1.7157258251129, result.getPoint(), 1e-9);
        assertEquals(2.2204460492503, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with no convergence checker.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithoutConvergenceChecker() throws Exception {
        optimizer.setConvergenceChecker(null);
        result = optimizer.optimize(FUNCTION, MaxEval.UNLIMITED);
        assertEquals(-1.7157258251129, result.getPoint(), 1e-9);
        assertEquals(2.2204460492503, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with custom convergence checker.
     * @throws Exception if optimization fails.
     */
    @Test
    public void testOptimizationWithConvergenceChecker() throws Exception {
        optimizer.setConvergenceChecker(new GoalTypeChecker(GoalType.MINIMIZE));
        result = optimizer.optimize(FUNCTION, MaxEval.UNLIMITED);
        assertEquals(-1.7157258251129, result.getPoint(), 1e-9);
        assertEquals(2.2204460492503, result.getValue(), 1e-9);
    }

    /**
     * Test exception handling when relative threshold is too small.
     * @throws Exception if optimization fails.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testExceptionHandlingRelativeThresholdTooSmall() throws Exception {
        optimizer = new BrentOptimizer(1e-10, 1e-6, null);
    }

    /**
     * Test exception handling when absolute threshold is non-positive.
     * @throws Exception if optimization fails.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testExceptionHandlingAbsoluteThresholdNonPositive() throws Exception {
        optimizer = new BrentOptimizer(1e-6, -1e-6, null);
    }
}

class GoalTypeChecker implements ConvergenceChecker<UnivariatePointValuePair> {

    private final GoalType goalType;

    /**
     * Constructor.
     * @param goalType Goal type.
     */
    public GoalTypeChecker(GoalType goalType) {
        this.goalType = goalType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean converged(int iteration,
                             UnivariatePointValuePair previous,
                             UnivariatePointValuePair current) {
        return previous == null ||
               goalType.equals(previous.getGoalType()) &&
               goalType.equals(current.getGoalType()) &&
               goalType.equals(goalType);
    }
}