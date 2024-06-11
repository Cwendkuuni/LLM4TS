package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.Ranking;
import org.junit.Test;

/**
 * MannWhitneyUTestTest class to test the MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        double expectedResult = 6.0;
        double result = test.mannWhitneyU(x, y);
        assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        double expectedResult = 0.05;
        try {
            double result = test.mannnWhitneyUTest(x, y);
            assertEquals(expectedResult, result, 0.001);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            // Expected exceptions
        }
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = {1.0, 2.0, 3.0};
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentExceptionY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataExceptionX() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {1.0, 2.0, 3.0};
        test.ensureDataConformance(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataExceptionY() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        test.ensureDataConformance(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};
        double[] expectedResult = {1.0, 2.0, 3.0, 2.0, 3.0, 4.0};
        double[] result = test.concatenateSamples(x, y);
        Ranking ranking = new NaturalRanking(NaNStrategy.FIXED, TiesStrategy.MIN);
        for (int i = 0; i < expectedResult.length; ++i) {
            assertEquals(expectedResult[i], result[i], 0.001);
        }
    }
}