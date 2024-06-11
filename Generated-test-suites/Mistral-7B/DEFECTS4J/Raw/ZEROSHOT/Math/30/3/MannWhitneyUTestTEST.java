package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

/**
 * JUnit 4 test file for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test of mannWhitneyU method, of class MannWhitneyUTest.
     */
    @org.junit.Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        double mannWhitneyUResult = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = 6.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyUResult, 0.0);

        x = new double[]{1.5, 2.5};
        y = new double[]{2.5, 3.5};
        mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.LOW);
        mannWhitneyUResult = mannWhitneyUTest.mannWhitneyU(x, y);
        expectedMannWhitneyU = 3.0;
        assertEquals(expectedMannWhitneyU, mannWhitneyUResult, 0.0);
    }

    /**
     * Test of mannWhitneyUTest method, of class MannWhitneyUTest.
     */
    @org.junit.Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        double pValueResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue = 0.05;
        assertEquals(expectedPValue, pValueResult, 0.05);

        x = new double[]{1.5, 2.5};
        y = new double[]{2.5, 3.5};
        mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.LOW);
        pValueResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        expectedPValue = 0.1;
        assertEquals(expectedPValue, pValueResult, 0.05);

        x = new double[]{1.0, 1.5, 2.0, 2.5, 3.0};
        y = new double[]{1.5, 2.0, 2.5, 3.0, 3.5};
        mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.MEDIAN);
        pValueResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        expectedPValue = 0.2;
        assertEquals(expectedPValue, pValueResult, 0.05);
    }

    /**
     * Test of mannWhitneyUTest method with large datasets.
     */
    @org.junit.Test
    public void testMannWhitneyUTestWithLargeDatasets() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[10000];
        double[] y = new double[10000];

        Random random = new Random();
        for (int i = 0; i < x.length; i++) {
            x[i] = random.nextGaussian();
            y[i] = random.nextGaussian() + 1.0;
        }

        double pValueResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue = 0.0;
        assertEquals(Precision.isClose(expectedPValue, pValueResult, 0.05), true);
    }

    /**
     * Test of mannWhitneyU method with null arguments.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArguments() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(null, null);
    }

    /**
     * Test of mannWhitneyU method with empty arrays.
     */
    @org.junit.Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
    }

    /**
     * Test of mannWhitneyUTest method with null arguments.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArguments() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, null);
    }

    /**
     * Test of mannWhitneyUTest method with empty arrays.
     */
    @org.junit.Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyArrays() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
    }

    /**
     * Test of mannWhitneyUTest method with NaN values.
     */
    @org.junit.Test
    public void testMannWhitneyUTestWithNaNValues() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.DENSE_RANK);
        double[] x = {1.0, Double.NaN, 3.0, Double.NaN};
        double[] y = {2.0, Double.NaN, 3.0, Double.NaN};
        double pValueResult = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expectedPValue = 0.5;
        assertEquals(expectedPValue, pValueResult, 0.05);
    }
}