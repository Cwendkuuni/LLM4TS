package org.apache.commons.cli;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.apache.commons.math3.StatFunctions;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.MannWhitneyRankSum;
import org.apache.commons.math3.stat.descriptive.mean.Mean;
import org.apache.commons.math3.stat.descriptive.sample.Sum;

public class MannWhitneyUTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(13.0, mannWhitneyU, 0.0);
    }

    @Test
    public void testMannWhitneyU_null() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(null, new double[0]));
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(new double[0], null));
    }

    @Test
    public void testMannWhitneyU_noData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]));
    }

    @Test
    public void testMannWhitneyUTest() throws NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0546875, pValue, 0.0001);

        double[] x2 = {1.5, 2.5, 3.5};
        double[] y2 = {4.5, 5.5, 6.5};
        double pValue2 = mannWhitneyUTest.mannWhitneyUTest(x2, y2);
        assertEquals(0.0138889, pValue2, 0.0001);
    }

    @Test
    public void testMannWhitneyUTest_nan() throws NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, Double.NaN};
        double[] y = {4.0, 5.0, 6.0};
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));

        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {4.0, 5.0, Double.NaN};
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x2, y2));

        double[] x3 = {1.0, Double.NaN, 3.0};
        double[] y3 = {4.0, 5.0, 6.0};
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x3, y3));

        double[] x4 = {1.0, 2.0, 3.0};
        double[] y4 = {4.0, 5.0, Double.NaN};
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x4, y4));
    }

    @Test
    public void testMannWhitneyUTest_ties() throws NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 2.0, 3.0};
        double[] y = {4.0, 4.0, 5.0, 5.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.1, pValue, 0.01);

        double[] x2 = {1.0, 2.0, 2.0, 3.0, 3.0};
        double[] y2 = {4.0, 4.0, 4.0, 5.0, 5.0};
        double pValue2 = mannWhitneyUTest.mannWhitneyUTest(x2, y2);
        assertEquals(0.2, pValue2, 0.01);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws MaxCountExceededException, ConvergenceException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 0.0;
        int n1 = 5;
        int n2 = 5;
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.1, pValue, 0.01);

        Umin = 5.0;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.001, pValue, 0.0001);

        Umin = 10.0;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0, pValue, 0.0);
    }

    @Test
    public void testCalculateAsymptoticPValue_maxCountExceeded() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 10000.0;
        int n1 = 5;
        int n2 = 5;
        assertThrows(MaxCountExceededException.class, () -> mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2));
    }

    @Test
    public void testCalculateAsymptoticPValue_convergenceError() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = -1000.0;
        int n1 = 5;
        int n2 = 5;
        assertThrows(ConvergenceException.class, () -> mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2));
    }

    @Test
    public void testRanking() throws NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] ranks = mannWhitneyUTest.naturalRanking.rank(x);
        assertEquals(1.0, ranks[0], 0.0);
        assertEquals(2.0, ranks[1], 0.0);
        assertEquals(3.0, ranks[2], 0.0);

        double[] y = {4.0, 5.0, 6.0};
        double[] ranks2 = mannWhitneyUTest.naturalRanking.rank(y);
        assertEquals(4.0, ranks2[0], 0.0);
        assertEquals(5.0, ranks2[1], 0.0);
        assertEquals(6.0, ranks2[2], 0.0);

        double[] z = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        double[] ranks3 = mannWhitneyUTest.naturalRanking.rank(z);
        assertEquals(1.0, ranks3[0], 0.0);
        assertEquals(2.0, ranks3[1], 0.0);
        assertEquals(3.0, ranks3[2], 0.0);
        assertEquals(4.0, ranks3[3], 0.0);
        assertEquals(5.0, ranks3[4], 0.0);
        assertEquals(6.0, ranks3[5], 0.0);
    }

    @Test
    public void testSumRank() throws NoDataException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double sumRankX = mannWhitneyUTest.sumRank(x);
        assertEquals(6.0, sumRankX, 0.0);

        double[] y = {4.0, 5.0, 6.0};
        double sumRankY = mannWhitneyUTest.sumRank(y);
        assertEquals(12.0, sumRankY, 0.0);
    }

    @Test
    public void testSumRank_null() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.sumRank(null));
    }

    @Test
    public void testSumRank_noData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.sumRank(new double[0]));
    }

    private double sumRank(double[] data) {
        Ranking ranking = new MannWhitneyRankSum();
        double[] ranks = ranking.rank(data);
        Sum sum = new Sum();
        return sum.value(ranks);
    }

    private double mannWhitneyU(double[] x, double[] y) throws NoDataException {
        ensureDataConformance(x, y);

        double[] z = new double[x.length + y.length];
        System.arraycopy(x, 0, z, 0, x.length);
        System.arraycopy(y, 0, z, x.length, y.length);

        double[] ranks = naturalRanking.rank(z);

        double sumRankX = sumRank(x);
        return sumRankX - (x.length * (x.length + 1)) / 2;
    }
}