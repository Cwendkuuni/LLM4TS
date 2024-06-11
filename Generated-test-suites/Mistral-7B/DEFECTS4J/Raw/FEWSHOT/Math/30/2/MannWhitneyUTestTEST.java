package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.commons.math3.distribution.NormalDistribution;
import org.apache.commons.commons.math3.exception.ConvergenceException;
import org.apache.commons.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.commons.math3.exception.NoDataException;
import org.apache.commons.commons.math3.exception.NullArgumentException;
import org.apache.commons.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.commons.math3.stat.ranking.Ranking;
import org.apache.commons.commons.math3.util.FastMath;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {5.0, 6.0, 7.0, 8.0};
        assertEquals(12.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
        assertEquals(0.1332229344, mannWhitneyUTest.mannWhitneyUTest(x, y), 0.001);

        MannWhitneyUTest mannWhitneyUTest1 = new MannWhitneyUTest(NaturalRanking.LARGEST_TIES, NaturalRanking.LARGEST);
        assertEquals(12.0, mannWhitneyUTest1.mannWhitneyU(x, y), 0.001);
        assertEquals(0.1332229344, mannWhitneyUTest1.mannWhitneyUTest(x, y), 0.001);
    }

    @Test
    public void testMannWhitneyUTestNullArgumentException() {
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(null, null));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(new double[0], null));
        assertThrows(NullArgumentException.class, () -> new MannWhitneyUTest(null, new double[0]));
    }

    @Test
    public void testMannWhitneyUTestNoDataException() {
        assertThrows(NoDataException.class, () -> new MannWhitneyUTest(new double[]{}, new double[]{}));
        assertThrows(NoDataException.class, () -> new MannWhitneyUTest(new double[]{}, null));
        assertThrows(NoDataException.class, () -> new MannWhitneyUTest(null, new double[]{}));
    }

    @Test
    public void testMannWhitneyUTestNullSamples() {
        assertThrows(NullPointerException.class, () -> mannWhitneyUTest.mannWhitneyU(null, null));
        assertThrows(NullPointerException.class, () -> mannWhitneyUTest.mannWhitneyUTest(null, null));
    }

    @Test
    public void testMannWhitneyUTestEmptySamples() {
        double[] x = new double[0];
        double[] y = new double[0];
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyU(x, y));
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    @Test
    public void testMannWhitneyUTestNaN() {
        double[] x = {1.0, 2.0, 3.0, Double.NaN};
        double[] y = {5.0, 6.0, 7.0, 8.0};
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(x, y));
        assertThrows(IllegalArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));

        mannWhitneyUTest = new MannWhitneyUTest(NaturalRanking.SMALLEST_TIES, NaturalRanking.MIN);
        assertEquals(12.0, mannWhitneyUTest.mannWhitneyU(x, y), 0.001);
        assertEquals(0.1332229344, mannWhitneyUTest.mannWhitneyUTest(x, y), 0.001);
    }

    @Test
    public void testMannWhitneyUTestMaxCountExceededException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTestConvergenceException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 2.0, 2.0};
        double[] y = {1.0, 1.0, 1.0, 1.0};
        assertThrows(ConvergenceException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x, y));
    }

    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 0.0;
        int n1 = 10;
        int n2 = 20;
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(1.0, pValue, 0.001);

        Umin = (n1 * n2) / 2.0;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0, pValue, 0.001);

        NormalDistribution normalDistribution = new NormalDistribution(0, 1);
        pValue = normalDistribution.cumulativeProbability(-3.0);
        assertEquals(0.00135, pValue, 0.00001);

        pValue = normalDistribution.cumulativeProbability(3.0);
        assertEquals(0.99865, pValue, 0.00001);

        pValue = normalDistribution.cumulativeProbability(0.0);
        assertEquals(0.5, pValue, 0.00001);

        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin - 3.0, n1, n2);
        assertEquals(0.00135, pValue, 0.00001);

        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin + 3.0, n1, n2);
        assertEquals(0.99865, pValue, 0.00001);
    }
}