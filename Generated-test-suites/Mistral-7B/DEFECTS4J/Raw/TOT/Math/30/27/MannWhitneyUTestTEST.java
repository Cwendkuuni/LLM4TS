package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest.MannWhitneyUTestException;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @org.junit.Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test with empty arrays
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(null, null));
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(new double[0], new double[0]));

        // Test with arrays of equal length and same values
        double[] x = new double[] {1.0, 1.1, 1.2, 1.3};
        double[] y = new double[] {1.0, 1.1, 1.2, 1.3};
        double result = test.mannWhitneyU(x, y);
        assertEquals(6.0, result, 1e-14);

        // Test with arrays of equal length but different values
        x = new double[] {1.0, 1.1, 1.2, 1.3};
        y = new double[] {2.0, 2.1, 2.2, 2.3};
        result = test.mannWhitneyU(x, y);
        assertEquals(3.0, result, 1e-14);

        // Test with arrays of different lengths
        x = new double[] {1.0, 1.1, 1.2};
        y = new double[] {1.0, 1.1, 1.2, 1.3};
        result = test.mannWhitneyU(x, y);
        assertEquals(2.5, result, 1e-14);

        // Test with arrays containing NaNs
        x = new double[] {1.0, 1.1, Double.NaN, 1.3};
        y = new double[] {1.0, 1.1, 1.2, Double.NaN};
        test = new MannWhitneyUTest(NaturalRanking.STRATEGY_MINUS_INFINITY, TiesStrategy.LOWEST);
        result = test.mannWhitneyU(x, y);
        assertEquals(5.0, result, 1e-14);

        // Test with arrays containing ties
        x = new double[] {1.0, 1.1, 2.0, 1.1, 2.0};
        y = new double[] {1.1, 1.1, 2.0, 2.0, 2.0};
        test = new MannWhitneyUTest(NaturalRanking.STRATEGY_MINUS_INFINITY, TiesStrategy.AVERAGE);
        result = test.mannWhitneyU(x, y);
        assertEquals(4.5, result, 1e-14);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @org.junit.Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        // Test with empty arrays
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(null, null));
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[0], new double[0]));

        // Test with arrays of equal length and same values
        double[] x = new double[] {1.0, 1.1, 1.2, 1.3};
        double[] y = new double[] {1.0, 1.1, 1.2, 1.3};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-14);

        // Test with arrays of equal length but different values
        x = new double[] {1.0, 1.1, 1.2, 1.3};
        y = new double[] {2.0, 2.1, 2.2, 2.3};
        pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.0, pValue, 1e-14);

        // Test with arrays of different lengths
        x = new double[] {1.0, 1.1, 1.2};
        y = new double[] {1.0, 1.1, 1.2, 1.3};
        pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.5, pValue, 1e-14);

        // Test with arrays containing NaNs
        x = new double[] {1.0, 1.1, Double.NaN, 1.3};
        y = new double[] {1.0, 1.1, 1.2, Double.NaN};
        test = new MannWhitneyUTest(NaturalRanking.STRATEGY_MINUS_INFINITY, TiesStrategy.LOWEST);
        pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-14);

        // Test with arrays containing ties
        x = new double[] {1.0, 1.1, 2.0, 1.1, 2.0};
        y = new double[] {1.1, 1.1, 2.0, 2.0, 2.0};
        test = new MannWhitneyUTest(NaturalRanking.STRATEGY_MINUS_INFINITY, TiesStrategy.AVERAGE);
        pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.5, pValue, 1e-14);

        // Test with arrays causing convergence errors
        x = new double[] {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        y = new double[] {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertThrows(ConvergenceException.class, () -> test.mannWhitneyUTest(x, y));

        x = new double[] {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        y = new double[] {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertThrows(ConvergenceException.class, () -> test.mannWhitneyUTest(x, y));

        x = new double[] {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        y = new double[] {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        assertThrows(ConvergenceException.class, () -> test.mannWhitneyUTest(x, y));

        x = new double[] {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        y = new double[] {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
        assertThrows(ConvergenceException.class, () -> test.mannWhitneyUTest(x, y));

        // Test with arrays causing maximum iterations error
        x = new double[] {1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0};
        y = new double[] {1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 1.9, 1.9, 1.9, 1.9, 1.9, 1.9, 1.9, 1.9, 1.9, 1.9, 1.9};
        test = new MannWhitneyUTest(NaturalRanking.STRATEGY_MINUS_INFINITY, TiesStrategy.AVERAGE);
        assertThrows(MaxCountExceededException.class, () -> test.mannWhitneyUTest(x, y));
    }
}