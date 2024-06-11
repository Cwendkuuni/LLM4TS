package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RoundingMode;

import java.util.Random;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test case for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test.naturalRanking);
    }

    /**
     * Test case for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.LARGEST_TIE_NEUTRAL, TiesStrategy.DENSE_RANKS);
        assertNotNull(test.naturalRanking);
    }

    /**
     * Test case for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgument() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(null, new double[0]);
    }

    /**
     * Test case for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test case for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] expected = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};

        double[] result = test.concatenateSamples(x, y);
        assertArrayEquals(expected, result);
    }

    /**
     * Test case for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1.5, 2.5, 3.5};
        double[] y = {1.0, 2.0, 3.0};

        double result = test.mannWhitneyU(x, y);
        assertEquals(6.0, result, 0.0);
    }

    /**
     * Test case for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};

        double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0);
    }

    /**
     * Test case for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithCustomRanking() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.SMALLEST_TIE_ODD, TiesStrategy.LOWEST_TIE_VALUE);

        double[] x = {1.0, 2.0, 3.0, 3.0};
        double[] y = {4.0, 4.0, 5.0, 5.0};

        double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0);
    }

    /**
     * Test case for {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        NormalDistribution distribution = new NormalDistribution();

        double Umin = 3.0;
        int n1 = 5;
        int n2 = 7;

        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        double expected = 1.0 - distribution.cumulativeProbability(Math.abs(Math.sqrt(12.0 * (n1 * n2)) * FastMath.erf(FastMath.sqrt(12.0 * (n1 * n2)) / 2.0) / (2.0 * FastMath.SQRT_2_PI)));
        assertEquals(Precision.round(expected, 6), Precision.round(pValue, 6), 0.0);
    }

    /**
     * Test case for edge cases in {@link MannWhitneyUTest#calculateAsymptoticPValue(double, int, int)}.
     */
    @Test
    public void testCalculateAsymptoticPValueEdgeCases() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        NormalDistribution distribution = new NormalDistribution();

        // Test for smallest possible Mann-Whitney U value
        double Umin = 0.0;
        int n1 = 1;
        int n2 = 1;

        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.5, pValue, 0.0);

        // Test for largest possible Mann-Whitney U value
        double Umin = Math.pow(n1, 2) * (n1 + n2) / 2.0;
        pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.5, pValue, 0.0);
    }

    /**
     * Test case for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithCustomRankingAndCustomData() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest(NaturalRanking.SMALLEST_TIE_ODD, TiesStrategy.LOWEST_TIE_VALUE);

        double[] x = {1.0, 2.0, 3.0, 3.0};
        double[] y = {4.0, 4.0, 5.0, 5.0};

        double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0);
    }

    /**
     * Test case for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTestWithRandomData() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();

        Random random = new Random();
        double[] x = new double[10];
        double[] y = new double[10];

        for (int i = 0; i < x.length; i++) {
            x[i] = random.nextDouble();
            y[i] = random.nextDouble();
        }

        double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0);
    }
}