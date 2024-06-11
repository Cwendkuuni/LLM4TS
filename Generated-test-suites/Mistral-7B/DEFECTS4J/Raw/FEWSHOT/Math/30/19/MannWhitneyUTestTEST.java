package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x1 = {1.0D, 2.0D, 3.0D, 4.0D};
        double[] y1 = {3.0D, 4.0D, 5.0D, 6.0D};
        double u = mannWhitneyUTest.mannWhitneyU(x1, y1);
        assertEquals(6.0D, u, 0.001D);

        double[] x2 = {1.5D, 2.0D, 3.0D, 4.5D};
        double[] y2 = {2.5D, 3.5D, 6.0D};
        u = mannWhitneyUTest.mannWhitneyU(x2, y2);
        assertEquals(4.5D, u, 0.001D);

        double[] x3 = {};
        double[] y3 = {1.0D, 2.0D, 3.0D};
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyU(x3, y3));

        double[] x4 = {1.0D, 2.0D, 3.0D};
        double[] y4 = {};
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyU(x4, y4));

        double[] x5 = null;
        double[] y5 = {1.0D, 2.0D, 3.0D};
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(x5, y5));

        double[] x6 = {1.0D, 2.0D, 3.0D};
        double[] y6 = null;
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyU(x6, y6));

        double[] x7 = {1.0D, 1.0D, 1.0D, 1.0D, 1.0D};
        double[] y7 = {1.0D, 1.0D, 1.0D, 1.0D, 1.0D};
        double u7 = mannWhitneyUTest.mannWhitneyU(x7, y7);
        assertEquals(5.0D, u7, 0.001D);

        MannWhitneyUTest mannWhitneyUTestTies = new MannWhitneyUTest(TiesStrategy.LOWEST, TiesStrategy.LOWEST);
        double[] x8 = {1.0D, 1.0D, 2.0D, 3.0D, 3.0D};
        double[] y8 = {1.0D, 2.0D, 2.0D, 3.0D, 4.0D};
        double u8 = mannWhitneyUTestTies.mannWhitneyU(x8, y8);
        assertEquals(3.0D, u8, 0.001D);
    }

    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x1 = {1.0D, 2.0D, 3.0D, 4.0D};
        double[] y1 = {3.0D, 4.0D, 5.0D, 6.0D};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x1, y1);
        assertTrue(pValue > 0.05);

        double[] x2 = {1.5D, 2.0D, 3.0D, 4.5D};
        double[] y2 = {2.5D, 3.5D, 6.0D};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x2, y2);
        assertTrue(pValue < 0.05);

        double[] x3 = {};
        double[] y3 = {1.0D, 2.0D, 3.0D};
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x3, y3));

        double[] x4 = {1.0D, 2.0D, 3.0D};
        double[] y4 = {};
        assertThrows(NoDataException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x4, y4));

        double[] x5 = null;
        double[] y5 = {1.0D, 2.0D, 3.0D};
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x5, y5));

        double[] x6 = {1.0D, 2.0D, 3.0D};
        double[] y6 = null;
        assertThrows(NullArgumentException.class, () -> mannWhitneyUTest.mannWhitneyUTest(x6, y6));

        double[] x7 = {1.0D, 1.0D, 1.0D, 1.0D, 1.0D};
        double[] y7 = {1.0D, 1.0D, 1.0D, 1.0D, 1.0D};
        double pValue7 = mannWhitneyUTest.mannWhitneyUTest(x7, y7);
        assertEquals(1.0, pValue7, 0.001);

        MannWhitneyUTest mannWhitneyUTestTies = new MannWhitneyUTest(TiesStrategy.LOWEST, TiesStrategy.LOWEST);
        double[] x8 = {1.0D, 1.0D, 2.0D, 3.0D, 3.0D};
        double[] y8 = {1.0D, 2.0D, 2.0D, 3.0D, 4.0D};
        double pValue8 = mannWhitneyUTestTies.mannWhitneyUTest(x8, y8);
        assertEquals(1.0 / 3.0, pValue8, 0.001);

        MannWhitneyUTest mannWhitneyUTestCustom = new MannWhitneyUTest(TiesStrategy.MEDIAN, TiesStrategy.MEDIAN);
        double[] x9 = {1.0D, 1.0D, 2.0D, 3.0D, 3.0D};
        double[] y9 = {1.0D, 2.0D, 2.0D, 3.0D, 4.0D};
        pValue = mannWhitneyUTestCustom.mannWhitneyUTest(x9, y9);
        assertEquals(2.0 / 3.0, pValue, 0.001);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double Umin = 3.5;
        int n1 = 5;
        int n2 = 5;
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.03763723378623924, pValue, 0.001);

        Umin = 12.0;
        n1 = 5;
        n2 = 15;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.009325316456828826, pValue, 0.001);

        Umin = 25.0;
        n1 = 15;
        n2 = 5;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0003814616185338097, pValue, 0.001);

        Umin = 30.0;
        n1 = 5;
        n2 = 10;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.001349631421372117, pValue, 0.001);

        Umin = 35.0;
        n1 = 10;
        n2 = 5;
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.00011843771748321715, pValue, 0.001);
    }
}