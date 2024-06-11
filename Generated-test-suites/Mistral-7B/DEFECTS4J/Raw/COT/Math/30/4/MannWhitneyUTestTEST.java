package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

/**
 * Test cases for {@link MannWhitneyUTest} class
 */
public class MannWhitneyUTestTest {

    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT_STRATEGY, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(TiesStrategy.AVERAGE, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    @Test
    public void testConstructorWithCustomStrategies() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rank(double d, double[] data) {
                // custom nan strategy implementation
                return 0;
            }
        };

        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int rankTies(int n, double[] dataset, int i) {
                // custom ties strategy implementation
                return 0;
            }
        };

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.getNaturalRanking().getNaNStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.getNaturalRanking().getTiesStrategy());
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullX() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullY() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[]{1.0, 2.0, 3.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyX() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceEmptyY() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{2.5, 3.5, 4.5, 5.0};

        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(12.0, result, 1e-12);

        x = new double[]{1.0, 2.0, 2.5, 2.6, 3.0, 3.5, 3.8, 4.0};
        y = new double[]{1.5, 2.0, 2.3, 2.5, 2.7, 3.2, 3.5, 3.8, 4.0};

        result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(21.0, result, 1e-12);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] y = new double[]{2.5, 3.5, 4.5, 5.0};

        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.12338180155639, result, 1e-5);

        x = new double[]{1.0, 2.0, 2.5, 2.6, 3.0, 3.5, 3.8, 4.0};
        y = new double[]{1.5, 2.0, 2.3, 2.5, 2.7, 3.2, 3.5, 3.8, 4.0};

        result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.38517647058824, result, 1e-5);
    }

}