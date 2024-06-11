package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

/**
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assert mannWhitneyUTest != null;
    }

    @Test
    public void testMannWhitneyUWithNullArguments() {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(new double[0], null);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(null, new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUWithZeroLengthArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];

        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUWithIdenticalArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 2.0, 2.0, 2.0};
        double[] y = new double[]{2.0, 2.0, 2.0, 2.0, 2.0};

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = x.length * y.length / 2.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithIdenticalArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 2.0, 2.0, 2.0};
        double[] y = new double[]{2.0, 2.0, 2.0, 2.0, 2.0};

        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedAsymptoticPValue = 1.0;
        assertEquals(expectedAsymptoticPValue, asymptoticPValue, 0.001);
    }

    @Test
    public void testMannWhitneyUWithDifferentLengthArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0, 7.0};

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 6.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyU, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithDifferentLengthArraysAndSmallSamples()
        throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0};
        double[] y = new double[]{3.0, 4.0, 5.0};

        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedAsymptoticPValue = 0.5;
        assertEquals(expectedAsymptoticPValue, asymptoticPValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithDifferentLengthArraysAndLargerSamples()
        throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = new double[]{6.0, 7.0, 8.0, 9.0, 10.0};

        double asymptoticPValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedAsymptoticPValue = 0.05;
        assertEquals(expectedAsymptoticPValue, asymptoticPValue, 0.001);
    }
}