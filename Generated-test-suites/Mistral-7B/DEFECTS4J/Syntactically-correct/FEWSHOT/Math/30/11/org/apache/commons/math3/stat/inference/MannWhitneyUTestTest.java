package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestEmptyArrays() {
        assertThrows(NoDataException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUTestNullArrays() {
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest().mannWhitneyUTest(new double[0], null));
    }

    @Test
    public void testMannWhitneyUTest() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double pValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);

        Ranking naturalRanking = mannWhitneyUTest.naturalRanking;

        // Verify mannWhitneyU calculation
        assertEquals(7.0, mannWhitneyU, 0.001);

        // Verify pValue calculation
        assertEquals(0.05, pValue, 0.001);

        // Verify ranks calculation
        double[] expectedRanksX = new double[]{1.5, 2.5, 3.5};
        double[] expectedRanksY = new double[]{4.5, 5.5};
        double[] ranksX = new double[x.length];
        double[] ranksY = new double[y.length];
        naturalRanking.rank(concatenateSamples(x, y), ranksX, ranksY);

        for (int i = 0; i < x.length; i++) {
            assertEquals(expectedRanksX[i], ranksX[i], 0.001);
        }
        for (int i = 0; i < y.length; i++) {
            assertEquals(expectedRanksY[i], ranksY[i], 0.001);
        }
    }

    @Test
    public void testMannWhitneyUTestWithCustomRanking() {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0};

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.DUPLICATES_SKIP, TiesStrategy.DUPLICATES_SKIP);
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double pValue = mannWhitneyUTest.mannnWhitneyUTest(x, y);

        Ranking naturalRanking = mannWhitneyUTest.naturalRanking;

        // Verify mannWhitneyU calculation
        assertEquals(7.0, mannWhitneyU, 0.001);

        // Verify pValue calculation
        assertEquals(0.05, pValue, 0.001);

        // Verify ranks calculation
        double[] expectedRanksX = new double[]{1.5, 2.5, 3.5};
        double[] expectedRanksY = new double[]{4.5, 5.5};
        double[] ranksX = new double[x.length];
        double[] ranksY = new double[y.length];
        naturalRanking.rank(concatenateSamples(x, y), ranksX, ranksY);

        for (int i = 0; i < x.length; i++) {
            assertEquals(expectedRanksX[i], ranksX[i], 0.001);
        }
        for (int i = 0; i < y.length; i++) {
            assertEquals(expectedRanksY[i], ranksY[i], 0.001);
        }
    }

    private double[] concatenateSamples(final double[] x, final double[] y) {
        final double[] z = new double[x.length + y.length];

        System.arraycopy(x, 0, z, 0, x.length);
        System.arraycopy(y, 0, z, x.length, y.length);

        return z;
    }
}