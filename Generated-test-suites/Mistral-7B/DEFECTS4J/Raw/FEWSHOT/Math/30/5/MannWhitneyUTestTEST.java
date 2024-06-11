package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MannWhitneyUTest {

    @Test
    public void testMannWhitneyU() {
        // Test with empty arrays
        double[] x = {};
        double[] y = {};
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(null, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(x, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with arrays of different lengths
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {1.5, 2.5, 3.5, 4.5};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double result = mannWhitneyUTest.mannWhitneyU(x1, y1);
        assertEquals(12.0, result, 0.001);

        // Test with arrays of same lengths and same elements
        double[] x2 = {1.0, 1.0, 1.0};
        double[] y2 = {1.0, 1.0, 1.0};
        MannWhitneyUTest mannWhitneyUTest2 = new MannWhitneyUTest();
        double result2 = mannWhitneyUTest2.mannWhitneyU(x2, y2);
        assertEquals(3.0, result2, 0.001);

        // Test with arrays of same lengths and different elements
        double[] x3 = {1.0, 1.0, 2.0};
        double[] y3 = {1.0, 2.0, 1.0};
        MannWhitneyUTest mannWhitneyUTest3 = new MannWhitneyUTest();
        double result3 = mannWhitneyUTest3.mannWhitneyU(x3, y3);
        assertEquals(3.5, result3, 0.001);

        // Test with arrays of same lengths and equal elements and different ranking strategies
        MannWhitneyUTest mannWhitneyUTest4 = new MannWhitneyUTest(TiesStrategy.MIN, NaNStrategy.REMOVE);
        double[] x4 = {Double.NaN, 1.0, 1.0};
        double[] y4 = {1.0, 1.0, Double.NaN};
        double result4 = mannWhitneyUTest4.mannWhitneyU(x4, y4);
        assertEquals(3.0, result4, 0.001);

        MannWhitneyUTest mannWhitneyUTest5 = new MannWhitneyUTest(TiesStrategy.MAX, NaNStrategy.KEEP);
        double[] x5 = {Double.NaN, 1.0, 1.0};
        double[] y5 = {1.0, 1.0, Double.NaN};
        double result5 = mannWhitneyUTest5.mannWhitneyU(x5, y5);
        assertEquals(3.5, result5, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() {
        // Test with empty arrays
        double[] x = {};
        double[] y = {};
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Test with null arrays
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(null, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(x, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test with arrays of different lengths
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {1.5, 2.5, 3.5, 4.5};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(x1, y1);
            fail("Expected NoSignificanceException");
        } catch (NoSignificanceException e) {
            // Expected
        }

        // Test with arrays of same lengths and same elements
        double[] x2 = {1.0, 1.0, 1.0};
        double[] y2 = {1.0, 1.0, 1.0};
        MannWhitneyUTest mannWhitneyUTest2 = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest2.mannWhitneyUTest(x2, y2);
        assertEquals(0.0, pValue, 0.001);

        // Test with arrays of same lengths and different elements
        double[] x3 = {1.0, 1.0, 2.0};
        double[] y3 = {1.0, 2.0, 1.0};
        MannWhitneyUTest mannWhitneyUTest3 = new MannWhitneyUTest();
        double pValue3 = mannWhitneyUTest3.mannWhitneyUTest(x3, y3);
        assertEquals(0.16666666666666663, pValue3, 0.001);

        // Test with arrays of same lengths and equal elements and different ranking strategies
        MannWhitneyUTest mannWhitneyUTest4 = new MannWhitneyUTest(TiesStrategy.MIN, NaNStrategy.REMOVE);
        double[] x4 = {Double.NaN, 1.0, 1.0};
        double[] y4 = {1.0, 1.0, Double.NaN};
        MannWhitneyUTest mannWhitneyUTest5 = new MannWhitneyUTest(TiesStrategy.MAX, NaNStrategy.KEEP);
        double[] x5 = {Double.NaN, 1.0, 1.0};
        double[] y5 = {1.0, 1.0, Double.NaN};
        double pValue4 = mannWhitneyUTest4.mannWhitneyUTest(x4, y4);
        double pValue5 = mannWhitneyUTest5.mannWhitneyUTest(x5, y5);
        assertEquals(0.16666666666666663, pValue4, 0.001);
        assertEquals(0.3333333333333333, pValue5, 0.001);
    }
}