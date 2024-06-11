package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

/**
 * Tests {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Tests that an instance of {@link MannWhitneyUTest} can be created
     * correctly.
     */
    @Test
    public void testInstanceCreation() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    /**
     * Tests that an instance of {@link MannWhitneyUTest} can be created
     * with custom NaN strategy and ties strategy.
     */
    @Test
    public void testInstanceCreationWithCustomStrategies() {
        final NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public int handleNaN(double d, int rank) {
                return rank;
            }
        };
        final TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int handleTie(int previousRank, int currentRank) {
                return previousRank;
            }
        };
        final MannWhitneyUTest test = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertNotNull(test);
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}
     * correctly computes the Mann-Whitney U statistic.
     */
    @Test
    public void testMannWhitneyU() {
        final double[] x = { 3.0, 2.0, 3.5, 2.5, 3.5 };
        final double[] y = { 2.5, 2.0, 3.0, 3.5, 2.5 };
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double mannWhitneyU = test.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 1e-6);
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly computes the p-value.
     */
    @Test
    public void testMannWhitneyUTest() {
        final double[] x = { 3.0, 2.0, 3.5, 2.5, 3.5 };
        final double[] y = { 2.5, 2.0, 3.0, 3.5, 2.5 };
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.05);
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly returns the expected p-value for equal samples.
     */
    @Test
    public void testMannWhitneyUTestEqualSamples() {
        final double[] x = { 3.0, 3.0, 3.0, 3.0, 3.0 };
        final double[] y = { 3.0, 3.0, 3.0, 3.0, 3.0 };
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-6);
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly returns the expected p-value for identical samples.
     */
    @Test
    public void testMannWhitneyUTestIdenticalSamples() {
        final double[] x = { 3.0, 3.0 };
        final double[] y = { 3.0, 3.0 };
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 1e-6);
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly returns the expected p-value for samples with no common elements.
     */
    @Test
    public void testMannWhitneyUTestNoCommonElements() {
        final double[] x = { 3.0, 2.0, 4.0 };
        final double[] y = { 1.0, 2.5, 3.5 };
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.05);
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly throws an {@link NullArgumentException} for {@code null}
     * samples.
     */
    @Test
    public void testMannWhitneyUTestNullArgumentException() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyUTest(new double[0], null));
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly throws a {@link NoDataException} for empty samples.
     */
    @Test
    public void testMannWhitneyUTestNoDataException() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> test.mannWhitneyUTest(new double[0], new double[0]));
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly throws a {@link NullArgumentException} for {@code null} input
     * arrays.
     */
    @Test
    public void testMannWhitneyUTestNullInputArrays() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(new double[0], null));
        assertThrows(NullArgumentException.class, () -> test.mannWhitneyU(null, null));
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly throws a {@link NoDataException} for zero-length input arrays.
     */
    @Test
    public void testMannWhitneyUTestNoDataInputArrays() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> test.mannWhitneyU(new double[0], new double[0]));
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly throws a {@link ConvergenceException} for invalid p-value.
     */
    @Test
    public void testMannWhitneyUTestConvergenceException() {
        final NormalDistribution normalDistribution = new NormalDistribution();
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = { 1.0, 2.0, 3.0 };
        final double[] y = { 1.5, 2.5, 3.5 };
        final double pValue = test.mannWhitneyUTest(x, y);
        assertFalse(normalDistribution.probabilityDensityFunction(pValue) > 0);
        assertThrows(ConvergenceException.class, () -> test.mannWhitneyUTest(x, y));
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly throws a {@link MaxCountExceededException} for invalid p-value.
     */
    @Test
    public void testMannWhitneyUTestMaxCountExceededException() {
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = new double[Integer.MAX_VALUE];
        final double[] y = new double[Integer.MAX_VALUE];
        assertThrows(MaxCountExceededException.class, () -> test.mannWhitneyUTest(x, y));
    }

    /**
     * Tests that {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}
     * correctly generates random samples and returns a p-value.
     */
    @Test
    public void testMannWhitneyUTestRandomSamples() {
        final int sampleSize = 10;
        final MannWhitneyUTest test = new MannWhitneyUTest();
        final double[] x = new double[sampleSize];
        final double[] y = new double[sampleSize];
        final Random random = new Random();
        for (int i = 0; i < sampleSize; ++i) {
            x[i] = random.nextGaussian();
            y[i] = random.nextGaussian();
        }
        final double pValue = test.mannWhitneyUTest(x, y);
        assertTrue(pValue > 0.0 && pValue < 1.0);
    }

}