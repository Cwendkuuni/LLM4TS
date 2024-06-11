package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import org.apache.commons.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.commons.math3.stat.inference.MannWhitneyUTest.NanStrategy;
import org.apache.commons.commons.math3.stat.inference.MannWhitneyUTest.TiesStrategy;

public class MannWhitneyUTestTest {

    @Test
    public void testCreateInstance() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        assertEquals(NaturalRanking.FIXED, mwu.getNaturalRanking().getNaNStrategy());
        assertEquals(TiesStrategy.AVERAGE, mwu.getNaturalRanking().getTiesStrategy());
    }

    @Test
    public void testCreateInstanceWithCustomStrategies() {
        NaNStrategy nanStrategy = NaNStrategy.LOWER;
        TiesStrategy tiesStrategy = TiesStrategy.MIN;
        MannWhitneyUTest mwu = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mwu.getNaturalRanking().getNaNStrategy());
        assertEquals(tiesStrategy, mwu.getNaturalRanking().getTiesStrategy());
    }

    @Test
    public void testEnsureDataConformanceNullArgumentException() {
        assertThrows(NullArgumentException.class, () -> {
            new MannWhitneyUTest().mannWhitneyU(null, new double[0]);
        });

        assertThrows(NullArgumentException.class, () -> {
            new MannWhitneyUTest().mannWhitneyU(new double[0], null);
        });
    }

    @Test
    public void testEnsureDataConformanceNoDataException() {
        assertThrows(NoDataException.class, () -> {
            new MannWhitneyUTest().mannWhitneyU(new double[0], new double[0]);
        });
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testMannWhitneyUWithEmptyArrays() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        mwu.mannWhitneyU(new double[0], new double[0]);
    }

    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double u = mwu.mannWhitneyU(x, y);
        assertEquals(20.0, u, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithEmptyArrays() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double pValue = mwu.mannWhitneyUTest(new double[0], new double[0]);
        assertEquals(1.0, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 6.0};
        double pValue = mwu.mannWhitneyUTest(x, y);
        assertEquals(0.025, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithCustomStrategies() throws Exception {
        NaNStrategy nanStrategy = NaNStrategy.LOWER;
        TiesStrategy tiesStrategy = TiesStrategy.MIN;
        MannWhitneyUTest mwu = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        double[] x = {1.0, 2.0, 3.0, 3.0};
        double[] y = {4.0, 5.0, 6.0, 6.0};
        double pValue = mwu.mannWhitneyUTest(x, y);
        assertEquals(0.025, pValue, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 3.0};
        double[] y = {1.0, 2.0, 3.0, 3.0};
        mwu.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        mwu.mannWhitneyUTest(x, y);
    }
}