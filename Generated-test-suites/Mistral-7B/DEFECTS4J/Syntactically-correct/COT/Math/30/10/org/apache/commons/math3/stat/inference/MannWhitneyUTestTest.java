package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;

public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestConstructorDefault() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT_STRATEGY_FOR_NAN, mannWhitneyUTest.getNanStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.getTiesStrategy());
    }

    @Test
    public void testMannWhitneyUTestConstructorCustom() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rank(double d) {
                return Double.NaN;
            }
        };
        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int rank(double d1, double d2) {
                return d1.equals(d2) ? 0.5 : (d1 < d2 ? 1 : -1);
            }
        };
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.getNanStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.getTiesStrategy());
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullX() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[]{1.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullY() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[]{1.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyX() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[]{}, new double[]{1.0});
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyY() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[]{1.0}, new double[]{});
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{2.5, 3.5, 4.5};
        double actual = mannWhitneyUTest.mannWhitneyU(x, y);
        double expected = 7.0;
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testMannWhitneyUTest() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{2.5, 3.5, 4.5};
        double actual = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 0.0381;
        assertEquals(Precision.round(expected, 4), actual, 0.0);
    }

}