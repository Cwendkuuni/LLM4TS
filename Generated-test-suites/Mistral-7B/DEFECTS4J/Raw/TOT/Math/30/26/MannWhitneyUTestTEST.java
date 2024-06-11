package org.apache.commons.math3.stat.inference.test;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest()}.
     */
    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        Assert.assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#MannWhitneyUTest(NaNStrategy, TiesStrategy)}.
     */
    @Test
    public void testMannWhitneyUTestConstructorWithStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(TiesStrategy.LOW, NaNStrategy.REMOVE);
        Assert.assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method for {@link MannWhitneyUTest#ensureDataConformance(double[], double[])}.
     */
    @Test
    public void testEnsureDataConformance() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    /**
     * Test method for {@link MannWhitneyUTest#concatenateSamples(double[], double[])}.
     */
    @Test
    public void testConcatenateSamples() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double[] z = mannWhitneyUTest.concatenateSamples(x, y);
        Assert.assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, z);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyU(double[], double[])}.
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double u = mannWhitneyUTest.mannWhitneyU(x, y);
        Assert.assertEquals(13.5, u, 0.001);
    }

    /**
     * Test method for {@link MannWhitneyUTest#mannWhitneyUTest(double[], double[])}.
     */
    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 6.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.111, pValue, 0.001);
    }

    /**
     * Test method for edge case where x and y have the same value in different positions.
     */
    @Test
    public void testMannWhitneyUTestWithIdenticalValues() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0, 3.0};
        double[] y = new double[]{4.0, 5.0, 3.0, 3.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        Assert.assertEquals(0.25, pValue, 0.001);
    }

    /**
     * Test method for error case where x and y are empty arrays.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyArrays() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[0];
        double[] y = new double[0];
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test method for error case where x is a null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentX() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = null;
        double[] y = new double[]{4.0, 5.0, 6.0};
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }

    /**
     * Test method for error case where y is a null argument.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentY() throws NoDataException, NullArgumentException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = null;
        mannWhitneyUTest.mannWhitneyUTest(x, y);
    }
}