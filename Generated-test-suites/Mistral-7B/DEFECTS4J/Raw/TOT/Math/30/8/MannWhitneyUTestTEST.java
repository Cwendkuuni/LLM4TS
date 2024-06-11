package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        // Sample 1: [1, 2, 3]
        // Sample 2: [4, 5]
        double[] x1 = {1, 2, 3};
        double[] y1 = {4, 5};
        double expectedU = 4.0;
        double actualU = new MannWhitneyUTest().mannWhitneyU(x1, y1);
        assertEquals(expectedU, actualU, 0.001);

        // Sample 1: [1, 1, 2, 3]
        // Sample 2: [4, 5]
        double[] x2 = {1, 1, 2, 3};
        double[] y2 = {4, 5};
        expectedU = 5.0;
        actualU = new MannWhitneyUTest().mannWhitneyU(x2, y2);
        assertEquals(expectedU, actualU, 0.001);

        // Sample 1: [1, 2, 3]
        // Sample 2: [1, 2, 3]
        double[] x3 = {1, 2, 3};
        double[] y3 = {1, 2, 3};
        expectedU = 6.0;
        actualU = new MannWhitneyUTest().mannWhitneyU(x3, y3);
        assertEquals(expectedU, actualU, 0.001);

        // Sample 1: [NaN]
        // Sample 2: [1, 2, 3]
        double[] x4 = {Double.NaN};
        double[] y4 = {1, 2, 3};
        try {
            new MannWhitneyUTest().mannWhitneyU(x4, y4);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Sample 1: [1, 2, 3]
        // Sample 2: [null]
        double[] x5 = {1, 2, 3};
        double[] y5 = null;
        try {
            new MannWhitneyUTest().mannWhitneyU(x5, y5);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Sample 1: []
        // Sample 2: [1, 2, 3]
        double[] x6 = {};
        double[] y6 = {1, 2, 3};
        try {
            new MannWhitneyUTest().mannWhitneyU(x6, y6);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Sample 1: [1, 2, 3]
        // Sample 2: []
        double[] x7 = {1, 2, 3};
        double[] y7 = {};
        try {
            new MannWhitneyUTest().mannWhitneyU(x7, y7);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Sample 1: [1, 2, 3]
        // Sample 2: [1, 2, 3]
        double[] x8 = {1, 2, 3};
        double[] y8 = {1, 2, 3};
        try {
            new MannWhitneyUTest().mannWhitneyUTest(x8, y8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTest() {
        // Sample 1: [1, 2, 3]
        // Sample 2: [4, 5]
        double[] x1 = {1, 2, 3};
        double[] y1 = {4, 5};
        double expectedPValue = 0.16666666666666663;
        double actualPValue = new MannWhitneyUTest().mannWhitneyUTest(x1, y1);
        assertEquals(expectedPValue, actualPValue, 0.001);

        // Sample 1: [1, 1, 2, 3]
        // Sample 2: [4, 5]
        double[] x2 = {1, 1, 2, 3};
        double[] y2 = {4, 5};
        expectedPValue = 0.09111111111111111;
        actualPValue = new MannWhitneyUTest().mannWhitneyUTest(x2, y2);
        assertEquals(expectedPValue, actualPValue, 0.001);

        // Sample 1: [1, 2, 3]
        // Sample 2: [1, 2, 3]
        double[] x3 = {1, 2, 3};
        double[] y3 = {1, 2, 3};
        expectedPValue = 0.5;
        actualPValue = new MannWhitneyUTest().mannWhitneyUTest(x3, y3);
        assertEquals(expectedPValue, actualPValue, 0.001);

        // Sample 1: [NaN]
        // Sample 2: [1, 2, 3]
        double[] x4 = {Double.NaN};
        double[] y4 = {1, 2, 3};
        try {
            new MannWhitneyUTest().mannWhitneyUTest(x4, y4);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Sample 1: [1, 2, 3]
        // Sample 2: [null]
        double[] x5 = {1, 2, 3};
        double[] y5 = null;
        try {
            new MannWhitneyUTest().mannWhitneyUTest(x5, y5);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Sample 1: []
        // Sample 2: [1, 2, 3]
        double[] x6 = {};
        double[] y6 = {1, 2, 3};
        try {
            new MannWhitneyUTest().mannWhitneyUTest(x6, y6);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Sample 1: [1, 2, 3]
        // Sample 2: []
        double[] x7 = {1, 2, 3};
        double[] y7 = {};
        try {
            new MannWhitneyUTest().mannWhitneyUTest(x7, y7);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        // Sample 1: [1, 2, 3]
        // Sample 2: [1, 2, 3]
        double[] x8 = {1, 2, 3};
        double[] y8 = {1, 2, 3};
        try {
            new MannWhitneyUTest().mannWhitneyUTest(x8, y8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}