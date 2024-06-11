package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.NormalDistribution;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        double[] x = {1.0, 2.5, 3.5};
        double[] y = {2.0, 3.0, 4.0, 5.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 6.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.001);

        x = new double[0];
        y = new double[0];
        try {
            mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }

        x = null;
        try {
            mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        y = null;
        try {
            mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        x = new double[]{1.0};
        y = new double[]{1.5};
        try {
            mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }

        x = new double[]{1.0, 1.5};
        y = new double[]{1.5, 2.0};
        mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU2 = 2.0;
        assertEquals(expectedMannWhitneyU2, mannWhitneyU, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = {1.0, 2.5, 3.5};
        double[] y = {2.0, 3.0, 4.0, 5.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue = 0.05; // Approximate value, change it as needed
        assertEquals(expectedPValue, pValue, 0.001);

        x = new double[0];
        y = new double[0];
        try {
            pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }

        x = null;
        try {
            pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        y = null;
        try {
            pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        x = new double[]{1.0};
        y = new double[]{1.5};
        try {
            pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }

        x = new double[]{1.0, 1.5};
        y = new double[]{1.5, 2.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue2 = 0.25; // Approximate value, change it as needed
        assertEquals(expectedPValue2, pValue, 0.001);

        MannWhitneyUTest mannWhitneyUTest2 = new MannWhitneyUTest(
                MannWhitneyUTest.TiesStrategy.DITHERING,
                MannWhitneyUTest.NaNStrategy.MARGINAL);
        pValue = mannWhitneyUTest2.mannWhitneyUTest(x, y);
        double expectedPValue3 = 0.0625; // Approximate value, change it as needed
        assertEquals(expectedPValue3, pValue, 0.001);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double Umin = 5.0;
        int n1 = 3;
        int n2 = 4;

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        double expectedPValue = 0.1282; // Approximate value, change it as needed
        assertEquals(expectedPValue, pValue, 0.001);
    }
}