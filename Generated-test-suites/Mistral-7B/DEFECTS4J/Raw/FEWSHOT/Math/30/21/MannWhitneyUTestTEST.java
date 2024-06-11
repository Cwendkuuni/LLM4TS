package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionConstants;

import java.util.Random;

public class MannWhitneyUTestCase {

    @Test
    public void testMannWhitneyUTest() throws Exception {
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{2.0, 4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(mannWhitneyU, mannWhitneyUTestResult, Precision.SAFE_EQUALS_EPSILON);
    }

    @Test
    public void testMannWhitneyUTestWithCustomRanking() throws Exception {
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{2.0, 4.0, 5.0, 6.0};
        NaturalRanking customNaturalRanking = new NaturalRanking(
                NaNStrategy.LOWER_BOUND, TiesStrategy.MIN);
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(customNaturalRanking);
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(mannWhitneyU, mannWhititneyUTestResult, Precision.SAFE_EQUALS_EPSILON);
    }

    @Test
    public void testMannWhitneyUTestWithNullArguments() throws Exception {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(null, new double[1]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestWithEmptyArrays() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }
    }

    @Test
    public void testMannWhitneyUTestDifferentSizes() throws Exception {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{2.0, 4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(mannWhitneyU, mannWhitneyUTestResult, Precision.SAFE_EQUALS_EPSILON);
    }

    @Test
    public void testMannWhitneyUTestWithLargeArrays() throws Exception {
        Random random = new Random();
        int arraySize = 10000;
        double[] x = new double[arraySize];
        double[] y = new double[arraySize];
        for (int i = 0; i < arraySize; i++) {
            x[i] = random.nextDouble();
            y[i] = random.nextDouble();
        }
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double mannWhitneyUTestResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(mannWhitneyU, mannWhitneyUTestResult, Precision.SAFE_EQUALS_EPSILON);
    }
}