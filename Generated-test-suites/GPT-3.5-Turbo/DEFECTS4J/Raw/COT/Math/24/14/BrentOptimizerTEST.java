import org.apache.commons.math3.optimization.univariate.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1.0, 1.0, null);
    }

    @Test
    public void testDoOptimize() {
        // Test case 1: Basic functionality
        UnivariatePointValuePair expectedResult = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair actualResult = optimizer.doOptimize();
        assertTrue(Precision.equals(expectedResult.getPoint(), actualResult.getPoint()));
        assertTrue(Precision.equals(expectedResult.getValue(), actualResult.getValue()));

        // Test case 2: Optimization with negative values
        optimizer = new BrentOptimizer(1.0, 1.0, null);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        expectedResult = new UnivariatePointValuePair(1.0, -1.0);
        actualResult = optimizer.doOptimize();
        assertTrue(Precision.equals(expectedResult.getPoint(), actualResult.getPoint()));
        assertTrue(Precision.equals(expectedResult.getValue(), actualResult.getValue()));
    }

    @Test
    public void testBest() {
        // Test case 1: a and b are both null
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair expectedResult = null;
        UnivariatePointValuePair actualResult = optimizer.best(a, b, true);
        assertEquals(expectedResult, actualResult);

        // Test case 2: a is null
        a = null;
        b = new UnivariatePointValuePair(1.0, 1.0);
        expectedResult = b;
        actualResult = optimizer.best(a, b, true);
        assertEquals(expectedResult, actualResult);

        // Test case 3: b is null
        a = new UnivariatePointValuePair(1.0, 1.0);
        b = null;
        expectedResult = a;
        actualResult = optimizer.best(a, b, true);
        assertEquals(expectedResult, actualResult);

        // Test case 4: a and b are not null, isMinim = true
        a = new UnivariatePointValuePair(1.0, 1.0);
        b = new UnivariatePointValuePair(2.0, 2.0);
        expectedResult = a;
        actualResult = optimizer.best(a, b, true);
        assertEquals(expectedResult, actualResult);

        // Test case 5: a and b are not null, isMinim = false
        a = new UnivariatePointValuePair(1.0, 1.0);
        b = new UnivariatePointValuePair(2.0, 2.0);
        expectedResult = b;
        actualResult = optimizer.best(a, b, false);
        assertEquals(expectedResult, actualResult);
    }
}