import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    /**
     * Test the BrentOptimizer constructor with valid parameters.
     */
    @Test
    public void testConstructor() {
        double rel = 0.1;
        double abs = 0.01;

        BrentOptimizer brentOptimizer = new BrentOptimizer(rel, abs, null);

        assertNotNull(brentOptimizer);
    }

    /**
     * Test the BrentOptimizer constructor with a relative threshold smaller than 2 * Math.ulp(1d).
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double rel = 0.00001;
        double abs = 0.01;

        BrentOptimizer brentOptimizer = new BrentOptimizer(rel, abs, null);
    }

    /**
     * Test the BrentOptimizer constructor with a negative absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double rel = 0.1;
        double abs = -0.01;

        BrentOptimizer brentOptimizer = new BrentOptimizer(rel, abs, null);
    }

    /**
     * Test the doOptimize method.
     */
    @Test
    public void testDoOptimize() {
        double rel = 0.1;
        double abs = 0.01;

        BrentOptimizer brentOptimizer = new BrentOptimizer(rel, abs, null);

        double expectedResult = 0.0;
        double result = brentOptimizer.doOptimize().getPoint();

        assertEquals(expectedResult, result, 0.0001);
    }

    /**
     * Test the best method when a is null and b is not null.
     */
    @Test
    public void testBestWithANullBNotNull() {
        double a = 0;
        double b = 1;
        double value = 0.5;

        UnivariatePointValuePair aPair = null;
        UnivariatePointValuePair bPair = new UnivariatePointValuePair(b, value);

        UnivariatePointValuePair result = brentOptimizer.best(aPair, bPair, true);

        assertEquals(bPair, result);
    }

    /**
     * Test the best method when a is not null and b is null.
     */
    @Test
    public void testBestWithANotNullBNull() {
        double a = 0;
        double b = 1;
        double value = 0.5;

        UnivariatePointValuePair aPair = new UnivariatePointValuePair(a, value);
        UnivariatePointValuePair bPair = null;

        UnivariatePointValuePair result = brentOptimizer.best(aPair, bPair, true);

        assertEquals(aPair, result);
    }

    /**
     * Test the best method when a and b are both not null and a has a lower value than b.
     */
    @Test
    public void testBestWithBothNotNullAndALowerThanB() {
        double a = 0;
        double b = 1;
        double aValue = 0.5;
        double bValue = 0.6;

        UnivariatePointValuePair aPair = new UnivariatePointValuePair(a, aValue);
        UnivariatePointValuePair bPair = new UnivariatePointValuePair(b, bValue);

        UnivariatePointValuePair result = brentOptimizer.best(aPair, bPair, true);

        assertEquals(aPair, result);
    }

    /**
     * Test the best method when a and b are both not null and a has a higher value than b.
     */
    @Test
    public void testBestWithBothNotNullAndAGreaterThanB() {
        double a = 0;
        double b = 1;
        double aValue = 0.6;
        double bValue = 0.5;

        UnivariatePointValuePair aPair = new UnivariatePointValuePair(a, aValue);
        UnivariatePointValuePair bPair = new UnivariatePointValuePair(b, bValue);

        UnivariatePointValuePair result = brentOptimizer.best(aPair, bPair, true);

        assertEquals(aPair, result);
    }

    /**
     * Test the best method when a and b are both not null and a and b have the same value.
     */
    @Test
    public void testBestWithBothNotNullAndASameAsB() {
        double a = 0;
        double b = 1;
        double aValue = 0.5;
        double bValue = 0.5;

        UnivariatePointValuePair aPair = new UnivariatePointValuePair(a, aValue);
        UnivariatePointValuePair bPair = new UnivariatePointValuePair(b, bValue);

        UnivariatePointValuePair result = brentOptimizer.best(aPair, bPair, true);

        assertEquals(aPair, result);
    }

    /**
     * Test the best method when a and b are both null.
     */
    @Test
    public void testBestWithBothNull() {
        UnivariatePointValuePair aPair = null;
        UnivariatePointValuePair bPair = null;

        UnivariatePointValuePair result = brentOptimizer.best(aPair, bPair, true);

        assertEquals(null, result);
    }

}