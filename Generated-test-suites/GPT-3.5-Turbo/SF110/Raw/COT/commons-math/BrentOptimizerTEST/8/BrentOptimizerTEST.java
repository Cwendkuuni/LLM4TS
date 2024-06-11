import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructor() {
        double rel = 0.1;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }

    @Test
    public void testConstructorWithException() {
        double rel = -0.1;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        try {
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            assertEquals(rel, e.getArgument(), 0.0);
            assertEquals(BrentOptimizer.MIN_RELATIVE_TOLERANCE, e.getMin(), 0.0);
            assertTrue(e.getIncludeMin());
        }
    }

    // Basic test for doOptimize() method
    @Test
    public void testDoOptimize() {
        double rel = 0.1;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        // Set input values
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Test doOptimize() method
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    // Test for best() method with a < b
    @Test
    public void testBestWithALessThanB() {
        double aPoint = 1.0;
        double aValue = 10.0;
        double bPoint = 2.0;
        double bValue = 5.0;
        boolean isMinim = true;

        UnivariatePointValuePair a = new UnivariatePointValuePair(aPoint, aValue);
        UnivariatePointValuePair b = new UnivariatePointValuePair(bPoint, bValue);

        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.0, null);

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(aPoint, result.getPoint(), 0.0);
        assertEquals(aValue, result.getValue(), 0.0);
    }

    // Test for best() method with b < a
    @Test
    public void testBestWithBLessThanA() {
        double bPoint = 1.0;
        double bValue = 5.0;
        double aPoint = 2.0;
        double aValue = 10.0;
        boolean isMinim = true;

        UnivariatePointValuePair a = new UnivariatePointValuePair(aPoint, aValue);
        UnivariatePointValuePair b = new UnivariatePointValuePair(bPoint, bValue);

        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.0, null);

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(bPoint, result.getPoint(), 0.0);
        assertEquals(bValue, result.getValue(), 0.0);
    }

    // Test for best() method with a = b
    @Test
    public void testBestWithAEqualsB() {
        double bPoint = 1.0;
        double bValue = 5.0;
        double aPoint = 1.0;
        double aValue = 10.0;
        boolean isMinim = true;

        UnivariatePointValuePair a = new UnivariatePointValuePair(aPoint, aValue);
        UnivariatePointValuePair b = new UnivariatePointValuePair(bPoint, bValue);

        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.0, null);

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        assertEquals(aPoint, result.getPoint(), 0.0);
        assertEquals(aValue, result.getValue(), 0.0);
    }

}