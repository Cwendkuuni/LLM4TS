package org.apache.commons.cli.test;

import static org.junit.Assert.assertTrue;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        // Test with empty arrays
        double[] x1 = {};
        double[] y1 = {};
        MannWhitneyUTest mannWhitneyUTest1 = new MannWhitneyUTest();
        try {
            mannWhitneyUTest1.mannWhitneyUTest(x1, y1);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        double[] x2 = null;
        double[] y2 = null;
        MannWhitneyUTest mannWhitneyUTest2 = new MannWhitneyUTest();
        try {
            mannWhitneyUTest2.mannWhitneyUTest(x2, y2);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with identical arrays
        double[] x3 = {1.0, 2.0, 3.0, 3.0, 3.0};
        double[] y3 = {3.0, 3.0, 3.0, 3.0, 3.0};
        MannWhitneyUTest mannWhitneyUTest3 = new MannWhitneyUTest();
        double mannWhitneyU3 = mannWhitneyUTest3.mannWhitneyU(x3, y3);
        double pValue3 = mannWhitneyUTest3.mannWhitneyUTest(x3, y3);
        assertTrue(mannWhitneyU3 > 0);
        assertTrue(pValue3 > 0.5);

        // Test with different arrays
        double[] x4 = {1.0, 2.0, 3.0};
        double[] y4 = {4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest4 = new MannWhitneyUTest();
        double mannWhitneyU4 = mannWhitneyUTest4.mannWhitneyU(x4, y4);
        double pValue4 = mannWhitneyUTest4.mannWhitneyUTest(x4, y4);
        assertTrue(mannWhitneyU4 > 0);
        assertTrue(pValue4 < 0.5);

        // Test with different strategies for NaN and ties
        NaNStrategy nanStrategy = new NaNStrategy.MedianAbsoluteDeviation();
        TiesStrategy tiesStrategy = TiesStrategy.LOW;
        MannWhitneyUTest mannWhitneyUTest5 = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        double[] x5 = {Double.NaN, 1.0, 2.0, Double.NaN, 3.0};
        double[] y5 = {1.0, 2.0, 3.0, 3.0, Double.NaN};
        double mannWhitneyU5 = mannWhitneyUTest5.mannWhitneyU(x5, y5);
        double pValue5 = mannWhitneyUTest5.mannWhitneyUTest(x5, y5);
        assertTrue(mannWhitneyU5 > 0);
        assertTrue(pValue5 > 0.5);
    }
}