package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 2.5, 3.5};

        double mannWhitneyUStatistic = mannWhitneyUTest.mannWhitneyU(x, y);
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

        Ranking naturalRanking = new NaturalRanking(Ranking.NaNStrategy.FIXED, Ranking.TiesStrategy.AVERAGE);
        double[] ranksX = naturalRanking.rank(x);
        double[] ranksY = naturalRanking.rank(y);

        double sumRankX = 0;
        for (int i = 0; i < x.length; i++) {
            sumRankX += ranksX[i];
        }

        double U1 = sumRankX - (x.length * (x.length + 1)) / 2;
        double U2 = x.length * y.length - U1;

        assertEquals(mannWhitneyUStatistic, Math.max(U1, U2), 0.001);
        assertEquals(pValue, 0.125, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithCustomRanking() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                Ranking.NaNStrategy.REMOVE, Ranking.TiesStrategy.MIN);

        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.5, 2.5, 3.5};

        double mannWhitneyUStatistic = mannWhitneyUTest.mannWhitneyU(x, y);
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);

        Ranking customRanking = new NaturalRanking(Ranking.NaNStrategy.REMOVE, Ranking.TiesStrategy.MIN);
        double[] ranksX = customRanking.rank(x);
        double[] ranksY = customRanking.rank(y);

        double sumRankX = 0;
        for (int i = 0; i < x.length; i++) {
            sumRankX += ranksX[i];
        }

        double U1 = sumRankX - (x.length * (x.length + 1)) / 2;
        double U2 = x.length * y.length - U1;

        assertEquals(mannWhitneyUStatistic, Math.max(U1, U2), 0.001);
        assertEquals(pValue, 0.125, 0.001);
    }
}