package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Test;

/**
 * Test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT, mannWhitneyUTest.naturalRanking);
    }

    @Test
    public void testConstructorWithCustomStrategies() {
        NaNStrategy nanStrategy = new NaNStrategy() {
            @Override
            public double rank(double d, double[] data, int index) {
                // Custom implementation for handling NaN's
                return -1;
            }
        };

        TiesStrategy tiesStrategy = new TiesStrategy() {
            @Override
            public int compare(double x, double y) {
                // Custom implementation for handling ties
                return 0;
            }
        };

        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(nanStrategy, tiesStrategy);
        assertEquals(nanStrategy, mannWhitneyUTest.naturalRanking.getNaNStrategy());
        assertEquals(tiesStrategy, mannWhitneyUTest.naturalRanking.getTiesStrategy());
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullArgument() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceWithNullFirstSample() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[] {1.0});
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptyFirstSample() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[] {1.0});
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceWithEmptySecondSample() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[] {1.0}, new double[0]);
    }

    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[] {1.0, 2.0, 3.0};
        double[] y = new double[] {3.0, 2.0, 1.0};

        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(4.0, result, 1e-12);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullFirstSample() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(null, new double[] {1.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullSecondSample() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(new double[] {1.0}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyFirstSample() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(new double[0], new double[] {1.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptySecondSample() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyU(new double[] {1.0}, new double[0]);
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[] {1.0, 2.0, 3.0};
        double[] y = new double[] {3.0, 2.0, 1.0};

        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.05, result, 1e-12);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestWithConvergenceException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[] {Double.NaN, Double.NaN};
        double[] y = new double[] {1.0, 2.0};

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestWithMaxCountExceededException() throws Exception {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];

        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
}