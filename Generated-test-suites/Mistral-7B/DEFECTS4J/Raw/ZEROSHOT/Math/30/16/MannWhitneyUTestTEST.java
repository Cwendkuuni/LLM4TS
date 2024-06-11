package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Test;

/**
 * Test class for MannWhitneyUTest
 */
public class MannWhitneyUTestTest {

    /**
     * Test of mannWhitneyU method, of class MannWhitneyUTest.
     */
    @Test
    public void testMannWhitneyU() throws Exception {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = new double[]{2.5, 2.5, 3.5, 6.0, 7.0};
        double expResult = 6.0;
        double result = instance.mannWhitneyU(x, y);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of mannWhitneyUTest method, of class MannWhitneyUTest.
     */
    @Test
    public void testMannWhitneyUTest() throws Exception {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = new double[]{2.5, 2.5, 3.5, 6.0, 7.0};
        double expResult = 0.0227582530825568;
        double result = instance.mannWhitneyUTest(x, y);
        assertEquals(expResult, result, 0.0001);

        instance = new MannWhitneyUTest(NaturalRanking.MEDIAN, TiesStrategy.DITHER);
        x = new double[]{1.0, 1.5, 2.0, 2.5, 3.0, 3.5};
        y = new double[]{2.0, 2.0, 2.5, 3.0, 3.5, 4.0};
        expResult = 0.05;
        result = instance.mannWhitneyUTest(x, y);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of mannWhitneyU method with null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArgument() {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = null;
        double[] y = new double[]{2.5, 2.5, 3.5, 6.0, 7.0};
        instance.mannWhitneyU(x, y);
    }

    /**
     * Test of mannWhitneyU method with empty array.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyArray() {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = {};
        double[] y = new double[]{2.5, 2.5, 3.5, 6.0, 7.0};
        instance.mannWhitneyU(x, y);
    }

    /**
     * Test of mannWhitneyUTest method with null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArgument() {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = null;
        instance.mannWhitneyUTest(x, y);
    }

    /**
     * Test of mannWhitneyUTest method with empty array.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyArray() {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = {};
        instance.mannWhitneyUTest(x, y);
    }

    /**
     * Test of mannWhitneyUTest method when x and y have the same elements.
     */
    @Test
    public void testMannWhitneyUTestWithSameData() throws Exception {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double[] y = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        double expResult = 1.0;
        double result = instance.mannWhitneyUTest(x, y);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of mannWhitneyUTest method with equal length and equal data.
     */
    @Test
    public void testMannWhitneyUTestWithEqualData() throws Exception {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        double[] y = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
        double expResult = 1.0;
        double result = instance.mannWhitneyUTest(x, y);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of calculateAsymptoticPValue method.
     */
    @Test
    public void testCalculateAsymptoticPValue() throws Exception {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double Umin = 6.0;
        int n1 = 5;
        int n2 = 6;
        double expResult = 0.0227582530825568;
        double result = instance.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(expResult, result, 0.0001);
    }

    /**
     * Test of calculateAsymptoticPValue method with null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testCalculateAsymptoticPValueWithNullArgument() {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double Umin = Double.NaN;
        int n1 = 5;
        int n2 = 6;
        instance.calculateAsymptoticPValue(Umin, n1, n2);
    }

    /**
     * Test of calculateAsymptoticPValue method with negative arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCalculateAsymptoticPValueWithNegativeArguments() {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double Umin = 6.0;
        int n1 = -1;
        int n2 = 6;
        instance.calculateAsymptoticPValue(Umin, n1, n2);
    }

    /**
     * Test of calculateAsymptoticPValue method with zero arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCalculateAsymptoticPValueWithZeroArguments() {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double Umin = 6.0;
        int n1 = 0;
        int n2 = 6;
        instance.calculateAsymptoticPValue(Umin, n1, n2);
    }

    /**
     * Test of calculateAsymptoticPValue method with null strategy.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCalculateAsymptoticPValueWithNullStrategy()
        throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double Umin = 6.0;
        int n1 = 5;
        int n2 = 6;
        instance.calculateAsymptoticPValue(Umin, n1, n2, null);
    }

    /**
     * Test of calculateAsymptoticPValue method with maximum count exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testCalculateAsymptoticPValueWithMaxCountExceeded()
        throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double Umin = 6.0;
        int n1 = 5;
        int n2 = 1000000;
        instance.calculateAsymptoticPValue(Umin, n1, n2);
    }

    /**
     * Test of calculateAsymptoticPValue method with convergence error.
     */
    @Test(expected = ConvergenceException.class)
    public void testCalculateAsymptoticPValueWithConvergenceError()
        throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest instance = new MannWhitneyUTest();
        double Umin = 6.0;
        int n1 = 5;
        int n2 = 5;
        NormalDistribution distribution = new NormalDistribution();
        instance.calculateAsymptoticPValue(Umin, n1, n2, new NormalDistribution() {
            @Override
            public double cumulativeProbability(double z) {
                throw new RuntimeException("Test method");
            }
        });
    }
}