package org.apache.commons.math3.stat.inference;

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

/**
 * JUnit test file for the MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertEquals(NaturalRanking.DEFAULT, test.getNaturalRanking());
    }

    /**
     * Test constructor with custom strategies.
     */
    @Test
    public void testConstructorWithCustomStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(
                NaNStrategy.SKIP, TiesStrategy.MIN);
        assertEquals(NaNStrategy.SKIP, test.getNaturalRanking().getNaNStrategy());
        assertEquals(TiesStrategy.MIN, test.getNaturalRanking().getTiesStrategy());
    }

    /**
     * Test method mannWhitneyU with equal samples.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEqualSamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[] { 1.0 };
        double[] y = new double[] { 1.0 };
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU with empty samples.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptySamples() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[] {};
        double[] y = new double[] {};
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU with null arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = new double[] {};
        test.mannWhitneyU(x, y);

        x = new double[] {};
        y = null;
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU.
     */
    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = new double[] { 1.0, 2.0, 3.0 };
        double[] y = new double[] { 4.0, 5.0, 6.0 };
        double mannWhitneyU = test.mannWhitneyU(x, y);
        double expected = 15.0;
        assertEquals(expected, mannWhitneyU, 0.001);
    }

    /**
     * Test method calculateAsymptoticPValue.
     */
    @Test
    public void testCalculateAsymptoticPValue() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 11.0;
        int n1 = 3;
        int n2 = 5;
        double pValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        double expected = 0.0173413181827;
        assertEquals(expected, pValue, 0.0001);
    }

    /**
     * Test method mannWhitneyUTest.
     */
    @Test
    public void testMannWhitneyUTest() {
        MannWhitneyUTest test = new MannWhitneyUTest();

        double[] x = new double[] { 1.0, 2.0, 3.0 };
        double[] y = new double[] { 4.0, 5.0, 6.0 };
        double pValue = test.mannWhitneyUTest(x, y);
        double expected = 0.0173413181827;
        assertEquals(expected, pValue, 0.0001);
    }

}