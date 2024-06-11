package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() throws Exception {
        // Test with empty arrays
        double[] x = {};
        double[] y = {};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        try {
            mannWhitneyUTest.mannWhitneyU(null, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
        try {
            mannWhitneyUTest.mannWhitneyU(x, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with arrays of different sizes
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {4.0, 5.0, 6.0, 7.0};
        MannWhitneyUTest mannWhitneyUTest1 = new MannWhitneyUTest();
        double mannWhitneyU1 = mannWhitneyUTest1.mannWhitneyU(x1, y1);
        assertEquals(12.0, mannWhitneyU1, 0.0);

        // Test with arrays of the same size
        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {2.0, 1.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest2 = new MannWhitneyUTest();
        double mannWhitneyU2 = mannWhitneyUTest2.mannWhitneyU(x2, y2);
        assertEquals(6.0, mannWhitneyU2, 0.0);

        // Test with arrays containing NaN values
        double[] x3 = {1.0, Double.NaN, 3.0};
        double[] y3 = {Double.NaN, 2.0, 4.0};
        MannWhitneyUTest mannWhitneyUTest3 = new MannWhitneyUTest(
                MannWhitneyUTest.NaNStrategy.SKIP,
                MannWhitneyUTest.TiesStrategy.LOW);
        double mannWhitneyU3 = mannWhitneyUTest3.mannWhitneyU(x3, y3);
        assertEquals(6.0, mannWhitneyU3, 0.0);

        // Test with arrays containing ties
        double[] x4 = {1.0, 2.0, 2.0, 3.0};
        double[] y4 = {2.0, 2.0, 3.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest4 = new MannWhitneyUTest(
                MannWhitneyUTest.NaNStrategy.FIXED,
                MannWhitneyUTest.TiesStrategy.AVERAGE);
        double mannWhitneyU4 = mannWhitneyUTest4.mannWhitneyU(x4, y4);
        assertEquals(6.5, mannWhitneyU4, 0.0);

        // Test mannWhitneyUTest method
        double[] x5 = {1.0, 2.0, 3.0};
        double[] y5 = {4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest5 = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest5.mannWhitneyUTest(x5, y5);
        assertEquals(0.054, pValue, 0.001);
    }
}