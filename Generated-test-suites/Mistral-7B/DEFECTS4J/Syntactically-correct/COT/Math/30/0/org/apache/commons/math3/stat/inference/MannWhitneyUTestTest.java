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
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(null, new double[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionB() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(new double[0], null);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.mannWhitneyU(new double[0], new double[0]);
        } catch (NullArgumentException e) {
            throw new AssertionError("should be NoDataException", e);
        }
    }

    @Test
    public void testBasicMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expected = 6.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testBasicMannWhitneyUTest() throws ConvergenceException, MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double expected = 0.015;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testNaNStrategy() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.MISSING, TiesStrategy.AVERAGE);
        double[] x = {1.0, 2.0, 3.0, Double.NaN};
        double[] y = {4.0, 5.0, 6.0, Double.NaN};
        double expected = 6.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testTiesStrategy() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.FIXED, TiesStrategy.MIN);
        double[] x = {1.0, 1.5, 2.0, 3.0, 3.0};
        double[] y = {2.0, 2.5, 3.5, 4.0, 4.5};
        double expected = 3.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testLargeSamples() throws ConvergenceException, MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[1000];
        double[] y = new double[1000];
        for (int i = 0; i < x.length; i++) {
            x[i] = FastMath.nextGaussian();
            y[i] = FastMath.nextGaussian();
        }
        double expected = test.mannWhitneyU(x, y) / (x.length * y.length);
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 0.05);
    }
}