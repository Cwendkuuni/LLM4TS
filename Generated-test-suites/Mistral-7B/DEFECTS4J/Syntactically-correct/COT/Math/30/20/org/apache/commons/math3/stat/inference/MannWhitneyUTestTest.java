package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

/**
 * Test class for MannWhitneyUTest
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest()
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test constructor MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy)
     */
    @Test
    public void testConstructorWithStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(NaNStrategy.LOWEST, TiesStrategy.LOWEST);
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method ensureDataConformance
     */
    @Test
    public void testEnsureDataConformance() throws Exception {
        double[] x = new double[0];
        double[] y = new double[0];

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }

        x = new double[]{1.0};
        y = new double[]{1.0};

        mannWhitneyUTest.ensureDataConformance(x, y);

        x = new double[]{1.0};
        y = null;

        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }

        x = null;
        y = new double[]{1.0};

        try {
            mannWhitneyUTest.ensureDataConformance(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    /**
     * Test method mannWhitneyU
     */
    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};

        double result = mannWhitneyUTest.mannWhitneyU(x, y);

        Ranking naturalRanking = new NaturalRanking();

        double[] ranksX = naturalRanking.rank(x);
        double[] ranksY = naturalRanking.rank(y);

        double sumRankX = 0;

        for (int i = 0; i < x.length; ++i) {
            sumRankX += ranksX[i];
        }

        double U1 = sumRankX - (x.length * (x.length + 1)) / 2;
        double U2 = x.length * y.length - U1;

        assertEquals(FastMath.max(U1, U2), result, 0.0);

        x = new double[]{1.0, 2.0, 2.0, 3.0};
        y = new double[]{4.0, 4.0, 4.0, 5.0};

        result = mannWhitneyUTest.mannWhitneyU(x, y);

        sumRankX = 0;

        for (int i = 0; i < x.length; ++i) {
            sumRankX += ranksX[i];
        }

        U1 = sumRankX - (x.length * (x.length + 1)) / 2;
        U2 = x.length * y.length - U1;

        assertEquals(FastMath.max(U1, U2), result, 0.0);

        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};

        result = mannWhitneyUTest.mannWhitneyU(x, y);

        sumRankX = 0;

        for (int i = 0; i < x.length; ++i) {
            sumRankX += ranksX[i];
        }

        U1 = sumRankX - (x.length * (x.length + 1)) / 2;
        U2 = x.length * y.length - U1;

        assertEquals(FastMath.max(U1, U2), result, 0.0);
    }

    /**
     * Test method mannWhitneyUTest
     */
    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};

        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

        NormalDistribution standardNormal = new NormalDistribution(0, 1);

        double z = (mannWhitneyUTest.mannWhitneyU(x, y) - mannWhitneyUTest.mannWhitneyU(new double[0], new double[0])) / Math.sqrt(mannWhitneyUTest.mannWhitneyU(x, x.length) * mannWhitneyUTest.mannWhitneyU(y, y.length) / 12.0);

        double expectedPValue = 2 * standardNormal.cumulativeProbability(z);

        assertEquals(expectedPValue, pValue, 0.0);

        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};

        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

        z = (mannWhitneyUTest.mannWhitneyU(x, y) - mannWhitneyUTest.mannWhitneyU(new double[0], new double[0])) / Math.sqrt(mannWhitneyUTest.mannWhitneyU(x, x.length) * mannWhitneyUTest.mannWhitneyU(y, y.length) / 12.0);

        expectedPValue = 2 * standardNormal.cumulativeProbability(z);

        assertEquals(expectedPValue, pValue, 0.0);

        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{2.0, 3.0, 4.0};

        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

        z = (mannWhitneyUTest.mannWhitneyU(x, y) - mannWhitneyUTest.mannWhitneyU(new double[0], new double[0])) / Math.sqrt(mannWhitneyUTest.mannWhitneyU(x, x.length) * mannWhitneyUTest.mannWhitneyU(y, y.length) / 12.0);

        expectedPValue = 2 * standardNormal.cumulativeProbability(z);

        assertEquals(expectedPValue, pValue, 0.0);

        x = new double[]{1.0, 2.0, 3.0};
        y = new double[]{1.0, 2.0, 2.0, 2.0, 2.0};

        try {
            mannWhitneyUTest.mannWhitneyUTest(x, y);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException e) {
            // expected
        } catch (MaxCountExceededException e) {
            // expected
        }
    }
}