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
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest().
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
    }

    /**
     * Test constructor MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy).
     */
    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.MEDIAN, TiesStrategy.DENSE_RANK);
    }

    /**
     * Test method mannWhitneyU(double[], double[]) for null arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU(double[], double[]) for empty data.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUEmptyData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU(double[], double[]) with same arrays.
     */
    @Test
    public void testMannWhitneyUSameArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {1.0, 1.5, 2.0, 2.5};
        double mannWhitneyU = test.mannWhitneyU(x, y);
        assertEquals(6.0, mannWhitneyU, 0.0);
    }

    /**
     * Test method mannWhitneyU(double[], double[]) with different arrays.
     */
    @Test
    public void testMannWhitneyUDifferentArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {2.5, 3.0, 4.0, 5.0};
        double mannWhitneyU = test.mannWhitneyU(x, y);
        assertEquals(4.5, mannWhitneyU, 0.0);
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) for null arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArguments() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = null;
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) for empty data.
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestEmptyData() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) with same arrays.
     */
    @Test
    public void testMannWhitneyUTestSameArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {1.0, 1.5, 2.0, 2.5};
        double mannWhitneyUTest = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, mannWhitneyUTest, 0.0);
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) with different arrays.
     */
    @Test
    public void testMannWhitneyUTestDifferentArrays() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 1.5, 2.0, 2.5};
        double[] y = {2.5, 3.0, 4.0, 5.0};
        double mannWhitneyUTest = test.mannWhitneyUTest(x, y);
        assertEquals(0.05, mannWhitneyUTest, 0.01);
    }

    /**
     * Test method calculateAsymptoticPValue(double, int, int) for small inputs.
     */
    @Test
    public void testCalculateAsymptoticPValueSmallInputs() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 0.0;
        int n1 = 2;
        int n2 = 3;
        double asymptoticPValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.0, asymptoticPValue, 0.0);
    }

    /**
     * Test method calculateAsymptoticPValue(double, int, int) for large inputs.
     */
    @Test
    public void testCalculateAsymptoticPValueLargeInputs() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double Umin = 15.0;
        int n1 = 10;
        int n2 = 15;
        double asymptoticPValue = test.calculateAsymptoticPValue(Umin, n1, n2);
        assertEquals(0.01, asymptoticPValue, 0.001);
    }
}