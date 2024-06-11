package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
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

public class MannWhitneyUTestTest {

    @Test
    public void testConstructor() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        assertEquals(NaturalRanking.FIXED, mwu.getNanStrategy());
        assertEquals(TiesStrategy.AVERAGE, mwu.getTiesStrategy());
    }

    @Test
    public void testConstructorWithCustomStrategies() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rank(double d) {
                if (Double.isNaN(d)) {
                    return Double.POSITIVE_INFINITY;
                }
                return 0.0;
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int compare(double x, double y) {
                if (x == y) {
                    return Integer.MAX_VALUE;
                }
                return 0;
            }
        };
        MannWhitneyUTest mwu = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mwu.getNanStrategy());
        assertEquals(tiesStrategy, mwu.getTiesStrategy());
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        mwu.ensureDataConformance(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        mwu.ensureDataConformance(new double[0], new double[0]);
    }

    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[] {1.0, 2.0, 3.0, 4.0};
        double[] y = new double[] {3.0, 2.0, 1.0, 4.0};
        double mannWhitneyU = mwu.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 0.0);
    }

    @Test
    public void testMannWhitneyUTest() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[] {1.0, 2.0, 3.0, 4.0};
        double[] y = new double[] {3.0, 2.0, 1.0, 4.0};
        double pValue = mwu.mannWhitneyUTest(x, y);
        assertEquals(0.0128, pValue, 0.001);
    }

    @Test
    public void testMannWhitneyUTestWithCustomStrategies() throws NoDataException,
            NullArgumentException, ConvergenceException,
            MaxCountExceededException {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rank(double d) {
                if (Double.isNaN(d)) {
                    return Double.POSITIVE_INFINITY;
                }
                return 0.0;
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int compare(double x, double y) {
                if (x == y) {
                    return Integer.MAX_VALUE;
                }
                return 0;
            }
        };
        MannWhitneyUTest mwu = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        double[] x = new double[] {1.0, 2.0, 3.0, 4.0};
        double[] y = new double[] {3.0, 2.0, 1.0, 4.0};
        double pValue = mwu.mannWhitneyUTest(x, y);
        assertEquals(0.0128, pValue, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testCalculateAsymptoticPValueConvergenceException()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[] {1.0, 2.0, 3.0, 4.0};
        double[] y = new double[] {3.0, 2.0, 1.0, 4.0};
        double Umin = 0.0;
        int n1 = x.length;
        int n2 = y.length;
        mwu.calculateAsymptoticPValue(Umin, n1, n2);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testCalculateAsymptoticPValueMaxCountExceededException()
            throws NoDataException, NullArgumentException,
            ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mwu = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        double Umin = 0.0;
        int n1 = x.length;
        int n2 = y.length;
        mwu.calculateAsymptoticPValue(Umin, n1, n2);
    }

}