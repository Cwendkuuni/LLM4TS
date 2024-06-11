import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;



public class BrentOptimizerTest {

    private static final double EPSILON = 1e-10;

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-8, 1e-8);
    }

    @Test
    public void testConstructor() {
        try {
            optimizer = new BrentOptimizer(-1, 1e-8);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
            assertNotNull(e);
        }

        try {
            optimizer = new BrentOptimizer(1e-8, -1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
            assertNotNull(e);
        }

        try {
            optimizer = new BrentOptimizer(1e-16, 1e-8);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
            assertNotNull(e);
        }
    }

    @Test
    public void testOptimize() {
        double min = -10;
        double max = 10;

        optimizer.setMin(min);
        optimizer.setMax(max);

        UnivariatePointValuePair result = optimizer.optimize(100,
                (x) -> x * x + 5 * x + 6,
                BrentOptimizer.GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(-2.5, result.getPoint(), EPSILON);
        assertEquals(-2.25, result.getValue(), EPSILON);

        result = optimizer.optimize(100,
                (x) -> x * x - 5 * x + 6,
                BrentOptimizer.GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(2.5, result.getPoint(), EPSILON);
        assertEquals(-2.25, result.getValue(), EPSILON);

        result = optimizer.optimize(100,
                (x) -> -x * x - 5 * x + 6,
                BrentOptimizer.GoalType.MAXIMIZE);
        assertNotNull(result);
        assertEquals(-2.5, result.getPoint(), EPSILON);
        assertEquals(2.25, result.getValue(), EPSILON);

        result = optimizer.optimize(100,
                (x) -> -x * x + 5 * x + 6,
                BrentOptimizer.GoalType.MAXIMIZE);
        assertNotNull(result);
        assertEquals(2.5, result.getPoint(), EPSILON);
        assertEquals(2.25, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeUnbounded() {
        UnivariatePointValuePair result = optimizer.optimize(100,
                (x) -> x * x + 5 * x + 6,
                BrentOptimizer.GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(-2.5, result.getPoint(), EPSILON);
        assertEquals(-2.25, result.getValue(), EPSILON);

        result = optimizer.optimize(100,
                (x) -> x * x - 5 * x + 6,
                BrentOptimizer.GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(2.5, result.getPoint(), EPSILON);
        assertEquals(-2.25, result.getValue(), EPSILON);

        result = optimizer.optimize(100,
                (x) -> -x * x - 5 * x + 6,
                BrentOptimizer.GoalType.MAXIMIZE);
        assertNotNull(result);
        assertEquals(-2.5, result.getPoint(), EPSILON);
        assertEquals(2.25, result.getValue(), EPSILON);

        result = optimizer.optimize(100,
                (x) -> -x * x + 5 * x + 6,
                BrentOptimizer.GoalType.MAXIMIZE);
        assertNotNull(result);
        assertEquals(2.5, result.getPoint(), EPSILON);
        assertEquals(2.25, result.getValue(), EPSILON);
    }

    @Test
    public void testOptimizeMaxEvaluationsReached() {
        double min = -10;
        double max = 10;

        optimizer.setMin(min);
        optimizer.setMax(max);

        UnivariatePointValuePair result = optimizer.optimize(5,
                (x) -> x * x + 5 * x + 6,
                BrentOptimizer.GoalType.MINIMIZE);
        assertNotNull(result);
        assertEquals(-2.5, result.getPoint(), EPSILON);
        assertEquals(-2.25, result.getValue(), EPSILON);
    }

    @Test(expected = NullPointerException.class)
    public void testOptimizeNullObjectiveFunction() {
        optimizer.optimize(100,
                null,
                BrentOptimizer.GoalType.MINIMIZE);
    }

    @Test(expected = NullPointerException.class)
    public void testOptimizeNullGoalType() {
        optimizer.optimize(100,
                (x) -> x * x + 5 * x + 6,
                null);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };

        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetInvalidConvergenceChecker() {
        try {
            optimizer.setConvergenceChecker(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            assertNotNull(e);
        }
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetRelativeThreshold() {
        assertEquals(1e-8, optimizer.getRelativeThreshold(), EPSILON);
    }

    @Test
    public void testGetAbsoluteThreshold() {
        assertEquals(1e-8, optimizer.getAbsoluteThreshold(), EPSILON);
    }

    @Test
    public void testGetGoalType() {
        assertEquals(BrentOptimizer.GoalType.MINIMIZE, optimizer.getGoalType());
    }

    @Test
    public void testGetGoalTypeMaximize() {
        optimizer = new BrentOptimizer(1e-8, 1e-8, null, BrentOptimizer.GoalType.MAXIMIZE);
        assertEquals(BrentOptimizer.GoalType.MAXIMIZE, optimizer.getGoalType());
    }

    @Test
    public void testGetStartValue() {
        assertEquals(0, optimizer.getStartValue(), EPSILON);
    }


}