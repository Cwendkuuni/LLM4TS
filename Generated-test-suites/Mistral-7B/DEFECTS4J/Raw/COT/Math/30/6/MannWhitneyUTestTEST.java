package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.junit.Test;

import java.util.Random;

/**
 * Test case for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 2.0, 1.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(3.0, mannWhitneyU, 1e-10);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest()} constructor.
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(NaNStrategy, TiesStrategy)} constructor.
     */
    @Test
    public void testMannWhitneyUTestWithStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NaNStrategy.EXCLUDE, TiesStrategy.MIN);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNull() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 2.0, 1.0};
        double[] z = mannWhitneyUTest.concatenateSamples(x, y);
        assertEquals(6, z.length);
    }

    /**
     * Test method for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}
     */
    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 5.0;
        int n1 = 3;
        int n2 = 5;
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.013898304361249, pValue, 1e-6);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithRandomData() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        Random random = new Random();
        double[] x = new double[10];
        double[] y = new double[10];
        for (int i = 0; i < x.length; i++) {
            x[i] = random.nextGaussian();
            y[i] = random.nextGaussian();
        }
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        System.out.println("pValue = " + pValue);
    }
}