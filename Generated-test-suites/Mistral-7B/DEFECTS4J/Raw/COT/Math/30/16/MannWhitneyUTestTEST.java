package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals("Default constructor called.", new NaturalRanking(
                NaNStrategy.FIXED, TiesStrategy.AVERAGE), mannWhitneyUTest.naturalRanking);
    }

    @Test
    public void testConstructorWithStrategies() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rankNaN(double nan, int index, double[] unsorted) {
                throw new UnsupportedOperationException();
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int rankTies(double value, int index, double[] unsorted) {
                throw new UnsupportedOperationException();
            }
        };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.naturalRanking.nanStrategy);
        assertEquals(tiesStrategy, mannWhitneyUTest.naturalRanking.tiesStrategy);
    }

    @Test
    public void testEnsureDataConformance() throws Exception {
        double[] x = null;
        double[] y = new double[0];

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.ensureDataConformance(x, y));

        x = new double[0];
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.ensureDataConformance(x, y));

        x = new double[] {1.0};
        y = new double[] {1.0};
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    @Test
    public void testConcatenateSamples() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[] z = mannWhitneyUTest.concatenateSamples(x, y);
        assertEquals(x.length + y.length, z.length);
        for (int i = 0; i < x.length; i++) {
            assertEquals(x[i], z[i]);
        }
        for (int i = x.length; i < z.length; i++) {
            assertEquals(y[i - x.length], z[i]);
        }
    }

    @Test
    public void testMannWhitneyU() throws Exception {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expectedMannWhitneyU = (3.0 * 6.0 + 4.0 * 6.0 + 3.0 * 5.0 + 1.0 * 5.0 + 1.0 * 4.0 + 2.0 * 4.0 + 2.0 * 5.0) / 2.0;
        assertEquals("mannWhitneyU calculation error.", expectedMannWhitneyU, mannWhitneyU, 1e-12);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        double Umin = 6.0;
        int n1 = 3;
        int n2 = 6;
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        NormalDistribution standardNormal = new NormalDistribution(0.0, 1.0);
        double z = (Umin - (n1 * n2) / 2.0) / FastMath.sqrt((n1 * n2 * (n1 + n2 + 1)) / 12.0);
        double expectedPValue = 2 * standardNormal.cumulativeProbability(Math.abs(z));
        assertEquals("calculateAsymptoticPValue calculation error.", expectedPValue, pValue, 1e-12);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        NormalDistribution standardNormal = new NormalDistribution(0.0, 1.0);
        double Umin = 6.0;
        int n1 = 3;
        int n2 = 6;
        double z = (Umin - (n1 * n2) / 2.0) / FastMath.sqrt((n1 * n2 * (n1 + n2 + 1)) / 12.0);
        double expectedPValue = 2 * standardNormal.cumulativeProbability(Math.abs(z));
        assertEquals("mannWhitneyUTest calculation error.", expectedPValue, pValue, 1e-12);
    }
}