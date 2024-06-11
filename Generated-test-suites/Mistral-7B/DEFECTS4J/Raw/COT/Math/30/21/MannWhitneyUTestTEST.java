package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * JUnit test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest().
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertEquals("default NaNStrategy and tiesStrategy",
                NaturalRanking.DEFAULT_TIE_STRATEGY,
                test.getNaturalRanking().getTiesStrategy());
        assertEquals("default NaNStrategy",
                NaturalRanking.DEFAULT_NAN_STRATEGY,
                test.getNaturalRanking().getNaNStrategy());

        MannWhitneyUTest test2 = new MannWhitneyUTest(
                NaturalRanking.MIN_TIE_STRATEGY,
                NaturalRanking.MIN_NAN_STRATEGY);
        assertEquals("custom NaNStrategy and tiesStrategy",
                NaturalRanking.MIN_TIE_STRATEGY,
                test2.getNaturalRanking().getTiesStrategy());
        assertEquals("custom NaNStrategy",
                NaturalRanking.MIN_NAN_STRATEGY,
                test2.getNaturalRanking().getNaNStrategy());
    }

    /**
     * Test method mannWhitneyU().
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        double[] x = {1.0, 2.0, 2.5};
        double[] y = {2.0, 2.5, 3.0};
        MannWhitneyUTest test = new MannWhitneyUTest();

        double result = test.mannWhitneyU(x, y);
        assertEquals("Mann-Whitney U statistic", 3.0, result);
    }

    /**
     * Test method mannWhitneyUTest().
     */
    @Test
    public void testMannWhitneyUTest()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {

        double[] x = {1.0, 2.0, 2.5};
        double[] y = {2.0, 2.5, 3.0};
        MannWhitneyUTest test = new MannWhitneyUTest();

        double pValue = test.mannnWhitneyUTest(x, y);
        assertEquals("p-value", 0.125, pValue, 0.01);

        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0, 2.0};
        pValue = test.mannnWhitneyUTest(x, y);
        assertEquals("p-value with ties", 0.0, pValue, 0.01);

        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0, 2.0};
        test = new MannWhitneyUTest(NaturalRanking.MIN_NAN_STRATEGY,
                NaturalRanking.MIN_TIE_STRATEGY);
        pValue = test.mannnWhitneyUTest(x, y);
        assertEquals("p-value with custom strategies", 0.0, pValue, 0.01);

        x = new double[0];
        y = new double[0];
        try {
            test.mannnWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        x = new double[]{1.0};
        y = new double[0];
        try {
            test.mannnWhitneyUTest(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected
        }

        x = new double[]{1.0};
        y = new double[]{1.0};
        try {
            test.mannnWhitneyUTest(x, y);
            fail("Expected ConvergenceException or MaxCountExceededException");
        } catch (ConvergenceException | MaxCountExceededException e) {
            // Expected
        }

        x = new double[]{Double.NaN, 1.0};
        y = new double[]{1.0, Double.NaN};
        try {
            test.mannnWhitneyUTest(x, y);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0, 2.0};
        test = new MannWhitneyUTest(NaturalRanking.MAX_NAN_STRATEGY,
                NaturalRanking.MAX_TIE_STRATEGY);
        pValue = test.mannnWhitneyUTest(x, y);
        assertEquals("p-value with custom strategies", 1.0, pValue, 0.01);
    }
}