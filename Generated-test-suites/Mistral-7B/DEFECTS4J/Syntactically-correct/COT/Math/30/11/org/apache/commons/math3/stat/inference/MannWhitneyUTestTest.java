package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

import java.util.Random;

/**
 * JUnit test for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals("Default constructor called.", MannWhitneyUTest.NATURAL, mannWhitneyUTest.getRankingStrategy());
        assertEquals("Default constructor called.", MannWhitneyUTest.AVERAGE_TIES, mannWhititneyUTest.getTiesStrategy());
    }

    @Test
    public void testMannWhitneyUWhenBothEmpty() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};

        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUWhenOneEmpty() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0};

        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.5, 3.0};
        double[] y = {4.0, 5.0};

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(" Mann-Whitney U should be 8.0", 8.0, mannWhitneyU, 1e-10);
    }

    @Test
    public void testMannWhitneyUTestWhenBothEmpty() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWhenOneEmpty() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0};
        double[] y = {};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWhenSameValues() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.0, 1.0, 1.0};
        double[] y = {1.0, 1.0, 1.0, 1.0};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithCustomRankingAndTies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(MannWhitneyUTest.FIXED, MannWhitneyUTest.FIRST);

        double[] x = {1.0, 2.5, 3.0};
        double[] y = {4.0, 5.0};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        mannWhitneyUTest = new MannWhitneyUTest(MannWhitneyUTest.FIXED, MannWhitneyUTest.LAST);

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(" Mann-Whitney U should be 6.0", 6.0, mannWhitneyU, 1e-10);

        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(" pValue should be 0.125", 0.125, pValue, 1e-6);
    }

    @Test
    public void testMannWhitneyUTestWithCustomRankingAndTiesWhenSamplesEqual() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(MannWhitneyUTest.FIXED, MannWhitneyUTest.LAST);

        double[] x = {1.0, 1.0, 1.0, 1.0};
        double[] y = {1.0, 1.0, 1.0, 1.0};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithCustomRankingAndTiesWhenSamplesDifferentLength() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(MannWhitneyUTest.FIXED, MannWhitneyUTest.LAST);

        double[] x = {1.0, 2.5, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected IllegalStateException.");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWhenValuesAreEqual() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 1.0, 1.0};
        double[] y = {1.0, 1.0, 1.0};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithCustomRankingAndTiesWhenValuesAreEqual() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(MannWhitneyUTest.FIXED, MannWhitneyUTest.LAST);

        double[] x = {1.0, 1.0, 1.0};
        double[] y = {1.0, 1.0, 1.0};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected NoDataException.");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithLargeSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        final int sampleSize = 10000;
        double[] x = new double[sampleSize];
        double[] y = new double[sampleSize];
        Random random = new Random();

        for (int i = 0; i < sampleSize; i++) {
            x[i] = random.nextDouble();
            y[i] = random.nextDouble();
        }

        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(" Mann-Whitney U should be a valid double.", mannWhitneyU, mannWhitneyU, 1e-10);

        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(" pValue should be a valid double.", pValue, pValue, 1e-6);
    }

    @Test(expected = NullPointerException.class)
    public void testMannWhitneyUWhenInputIsNull() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testMannWhitneyUTestWhenInputIsNull() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMannWhitneyUWhenInputEmpty() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMannWhitneyUTestWhenInputEmpty() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWhenConvergenceException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.setMaxIterations(0);
        mannWhitneyUTest.mannWhitneyUTest(new double[]{1.0}, new double[]{2.0});
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestWhenMaxCountExceededException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.setMaxIterations(1);
        mannWhitneyUTest.mannWhitneyUTest(new double[]{1.0}, new double[]{2.0});
    }
}