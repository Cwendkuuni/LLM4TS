import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setup() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsExceptionForRelLessThanMinRelativeTolerance() {
        optimizer = new BrentOptimizer(1e-7, 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsExceptionForAbsNotStrictlyPositive() {
        optimizer = new BrentOptimizer(1e-6, 0);
    }

    @Test
    public void testDoOptimize() {
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testBestReturnsFirstValueIfNull() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testBestReturnsSecondValueIfNull() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = null;
        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBestReturnsFirstValueIfMinimAndFirstValueIsLessThanOrEqualToSecondValue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 2);
        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBestReturnsSecondValueIfMinimAndSecondValueIsLessThanFirstValue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        boolean isMinim = true;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testBestReturnsFirstValueIfNotMinimAndFirstValueIsGreaterThanOrEqualToSecondValue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        boolean isMinim = false;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(a, result);
    }

    @Test
    public void testBestReturnsSecondValueIfNotMinimAndSecondValueIsGreaterThanFirstValue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 2);
        boolean isMinim = false;
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(b, result);
    }

    @Test
    public void testGoldenSection() {
        double expected = 0.5 * (3.0 - Math.sqrt(5.0));

        assertEquals(expected, BrentOptimizer.GOLDEN_SECTION, 1e-6);
    }

    @Test
    public void testMinRelativeTolerance() {
        double expected = 2.0 * Math.ulp(1.0);

        assertEquals(expected, BrentOptimizer.MIN_RELATIVE_TOLERANCE, 1e-6);
    }

    @Test
    public void testRelativeThreshold() {
        double expected = 1e-6;

        assertEquals(expected, optimizer.getRelativeThreshold(), 1e-6);
    }

    @Test
    public void testAbsoluteThreshold() {
        double expected = 1e-6;

        assertEquals(expected, optimizer.getAbsoluteThreshold(), 1e-6);
    }

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        optimizer = new BrentOptimizer(1e-6, 1e-6, checker);

        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-6);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-6);
    }

    @Test
    public void testConstructor() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);

        assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-6);
        assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-6);
    }

    @Test
    public void testComputeObjectiveValue() {
        optimizer.setMin(0);
        optimizer.setMax(10);

        double result = optimizer.computeObjectiveValue(5);

        assertEquals(5, result, 1e-6);
    }

    @Test
    public void testComputeObjectiveValueWithMinimGoalType() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);

        double result = optimizer.computeObjectiveValue(5);

        assertEquals(-5, result, 1e-6);
    }

    @Test
    public void testComputeObjectiveValueWithMaximGoalType() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);

        double result = optimizer.computeObjectiveValue(5);

        assertEquals(5, result, 1e-6);
    }

}