package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {
    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with empty arrays
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]));

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(new double[0], null));

        // Test with arrays of different sizes
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};
        assertEquals(10.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);

        // Test with arrays of the same size
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {2.0, 2.0, 2.0};
        assertEquals(3.0, mannWhitneyUTest.mannWhitneyU(x1, y1), 0.001);

        // Test with arrays containing NaN values
        double[] x2 = {1.0, 2.0, Double.NaN, 4.0};
        double[] y2 = {5.0, Double.NaN, 6.0, 7.0};
        MannWhitneyUTest mannWhitneyUTestWithNaN = new MannWhitneyUTest(NANStrategy.REMOVE, TiesStrategy.MIN);
        assertEquals(7.5, mannWhitneyUTestWithNaN.mannWhitneyU(x2, y2), 0.001);

        // Test with arrays containing the same value
        double[] x3 = {1.0, 1.0, 1.0};
        double[] y3 = {1.0, 1.0, 1.0};
        assertEquals(3.0, mannWhitneyUTest.mannWhitneyU(x3, y3), 0.001);

        // Test with arrays containing very large values
        double[] x4 = {1.0E308, 1.0E308};
        double[] y4 = {1.0E308, 1.0E308};
        assertEquals(6.0, mannWhitneyUTest.mannWhitneyU(x4, y4), 0.001);

        // Test with arrays containing very small values
        double[] x5 = {1.0E-308, 1.0E-308};
        double[] y5 = {1.0E-308, 1.0E-308};
        assertEquals(6.0, mannWhitneyUTest.mannWhitneyU(x5, y5), 0.001);

        // Test with arrays with very large difference in size
        double[] x6 = new double[Integer.MAX_VALUE];
        double[] y6 = new double[1];
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(x6, y6));
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with empty arrays
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]));

        // Test with null arrays
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(new double[0], null));

        // Test with arrays of different sizes
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 7.0};
        double expectedPValue = 0.05; // arbitrary value for demonstration purposes
        assertEquals(expectedPValue, mannWhitneyUTest.mannWhitneyUTest(x, y), 0.001);

        // Test with arrays of the same size
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {2.0, 2.0, 2.0};
        double actualPValue = mannWhitneyUTest.mannWhitneyUTest(x1, y1);
        assertEquals(0.5, actualPValue, 0.001);

        // Test with arrays containing NaN values
        double[] x2 = {1.0, 2.0, Double.NaN, 4.0};
        double[] y2 = {5.0, Double.NaN, 6.0, 7.0};
        MannWhitneyUTest mannWhitneyUTestWithNaN = new MannWhitneyUTest(NANStrategy.REMOVE, TiesStrategy.MIN);
        double actualPValueWithNaN = mannWhitneyUTestWithNaN.mannWhitneyUTest(x2, y2);
        assertEquals(0.4, actualPValueWithNaN, 0.01);

        // Test with arrays containing the same value
        double[] x3 = {1.0, 1.0, 1.0};
        double[] y3 = {1.0, 1.0, 1.0};
        double actualPValueWithSameValue = mannWhitneyUTest.mannWhitneyUTest(x3, y3);
        assertEquals(0.5, actualPValueWithSameValue, 0.001);

        // Test with arrays containing very large values
        double[] x4 = {1.0E308, 1.0E308};
        double[] y4 = {1.0E308, 1.0E308};
        double expectedPValueWithLargeValues = 0.25; // arbitrary value for demonstration purposes
        assertEquals(expectedPValueWithLargeValues, mannWhitneyUTest.mannWhitneyUTest(x4, y4), 0.001);

        // Test with arrays containing very small values
        double[] x5 = {1.0E-308, 1.0E-308};
        double[] y5 = {1.0E-308, 1.0E-308};
        double expectedPValueWithSmallValues = 0.25; // arbitrary value for demonstration purposes
        assertEquals(expectedPValueWithSmallValues, mannWhitneyUTest.mannWhitneyUTest(x5, y5), 0.001);

        // Test with arrays containing very large difference in size
        double[] x6 = new double[Integer.MAX_VALUE];
        double[] y6 = new double[1];
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x6, y6));
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        final MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        // Test with invalid Umin
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.calculateAsymptoticPValue(-1.0, 1, 1));

        // Test with invalid n1 or n2
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.calculateAsymptoticPValue(0.0, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.calculateAsymptoticPValue(0.0, 1, -1));

        // Test with valid inputs
        double Umin = 12.0;
        int n1 = 5;
        int n2 = 7;
        double expectedPValue = 0.033; // arbitrary value for demonstration purposes
        assertEquals(expectedPValue, mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2), 0.001);
    }
}