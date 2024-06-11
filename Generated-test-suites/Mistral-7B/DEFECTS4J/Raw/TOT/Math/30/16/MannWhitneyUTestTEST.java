package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

/**
 * Test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test {@code mannWhitneyU} method with normal distributions having the same mean.
     */
    @org.junit.Test
    public void testMannWhitneyUSameMean() {
        final double eps = 1e-12;
        final int sampleSize1 = 5;
        final int sampleSize2 = 5;
        final Random random = new Random(1);
        final MannWhitneyUTest test = new MannWhitneyUTest();

        final double[] x = new double[sampleSize1];
        final double[] y = new double[sampleSize2];

        for (int i = 0; i < sampleSize1; i++) {
            x[i] = new NormalDistribution(0.5, 1).sample(random);
        }
        for (int i = 0; i < sampleSize2; i++) {
            y[i] = new NormalDistribution(0.5, 1).sample(random);
        }

        final double result = test.mannWhitneyU(x, y);
        final double expected = (sampleSize1 * (sampleSize1 + sampleSize2 + 1)) / 2.0;

        assertEquals(expected, result, eps);
    }

    /**
     * Test {@code mannWhitneyU} method with normal distributions having different means.
     */
    @org.junit.Test
    public void testMannWhitneyUDifferentMeans() {
        final double eps = 1e-12;
        final int sampleSize1 = 5;
        final int sampleSize2 = 5;
        final Random random = new Random(1);
        final MannWhitneyUTest test = new MannWhitneyUTest();

        final double[] x = new double[sampleSize1];
        final double[] y = new double[sampleSize2];

        for (int i = 0; i < sampleSize1; i++) {
            x[i] = new NormalDistribution(1.5, 1).sample(random);
        }
        for (int i = 0; i < sampleSize2; i++) {
            y[i] = new NormalDistribution(0.5, 1).sample(random);
        }

        final double result = test.mannWhitneyU(x, y);
        final double expected = (sampleSize1 * sampleSize2) / 2.0;

        assertEquals(expected, result, eps);
    }

    /**
     * Test {@code mannWhitneyUTest} method with normal distributions having the
     * same mean.
     */
    @org.junit.Test
    public void testMannWhitneyUTestSameMean() {
        final double eps = 1e-12;
        final int sampleSize1 = 5;
        final int sampleSize2 = 5;
        final MannWhitneyUTest test = new MannWhitneyUTest();

        final double[] x = new double[sampleSize1];
        final double[] y = new double[sampleSize2];

        for (int i = 0; i < sampleSize1; i++) {
            x[i] = new NormalDistribution(0.5, 1).sample(new Random(1));
        }
        for (int i = 0; i < sampleSize2; i++) {
            y[i] = new NormalDistribution(0.5, 1).sample(new Random(1));
        }

        final double result = test.mannWhitneyUTest(x, y);

        final double pValue = 1 - Precision.doubleValue(0.975, 5, new NormalDistribution(0, 1));
        assertEquals(pValue, result, eps);
    }

    /**
     * Test {@code mannWhitneyUTest} method with normal distributions having
     * different means.
     */
    @org.junit.Test
    public void testMannWhitneyUTestDifferentMeans() {
        final double eps = 1e-12;
        final int sampleSize1 = 5;
        final int sampleSize2 = 5;
        final MannWhitneyUTest test = new MannWhitneyUTest();

        final double[] x = new double[sampleSize1];
        final double[] y = new double[sampleSize2];

        for (int i = 0; i < sampleSize1; i++) {
            x[i] = new NormalDistribution(1.5, 1).sample(new Random(1));
        }
        for (int i = 0; i < sampleSize2; i++) {
            y[i] = new NormalDistribution(0.5, 1).sample(new Random(1));
        }

        final double result = test.mannWhitneyUTest(x, y);

        final double pValue = Precision.doubleValue(0.95, 5, new NormalDistribution(0, 1));
        assertEquals(pValue, result, eps);
    }

    /**
     * Test {@code mannWhitneyU} method with null arguments.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArguments() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, null);
    }

    /**
     * Test {@code mannWhitneyUTest} method with null arguments.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArguments() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(null, null);
    }

    /**
     * Test {@code mannWhitneyU} method with empty arguments.
     */
    @org.junit.Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptyArguments() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[0], new double[0]);
    }

    /**
     * Test {@code mannWhitneyUTest} method with empty arguments.
     */
    @org.junit.Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyArguments() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[0], new double[0]);
    }

    /**
     * Test {@code calculateAsymptoticPValue} method with Umin and n1, n2 values
     * from the Mann-Whitney U test with different distributions.
     */
    @org.junit.Test
    public void testCalculateAsymptoticPValue() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double Umin = 15.0;
        final int n1 = 7;
        final int n2 = 11;

        final double result = test.calculateAsymptoticPValue(Umin, n1, n2);

        final double pValue = Precision.doubleValue(0.90, 5, new NormalDistribution(0, 1));
        assertEquals(pValue, result, 1e-12);
    }

    /**
     * Test {@code calculateAsymptoticPValue} method with Umin, n1, n2 values
     * from the Mann-Whitney U test with the same distribution.
     */
    @org.junit.Test
    public void testCalculateAsymptoticPValueSameDistribution() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double Umin = 12.0;
        final int n1 = 11;
        final int n2 = 11;

        final double result = test.calculateAsymptoticPValue(Umin, n1, n2);

        final double pValue = 1.0;
        assertEquals(pValue, result, 1e-12);
    }
}