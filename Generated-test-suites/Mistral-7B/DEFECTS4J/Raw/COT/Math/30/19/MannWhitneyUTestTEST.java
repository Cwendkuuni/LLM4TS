package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * JUnit test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;

    @Test
    public void testMannWhitneyUTest() {
        mannWhitneyUTest = new MannWhitneyUTest();
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException() {
        mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestNoDataException() {
        mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
    }

    @Test
    public void testMannWhitneyU() {
        mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.5, 3.0};
        double[] y = {2.0, 2.5, 4.0, 5.0};
        double actual = mannWhitneyUTest.mannWhitneyU(x, y);
        double expected = 6.0;
        assertEquals(expected, actual, Precision.SAFE_EQULALS_EPSILON);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest = new MannWhitneyUTest();
        double Umin = 5.0;
        int n1 = 3;
        int n2 = 4;
        double actual = mannWhitneyUTest.calculateAsymptoticPValue(Umin, n1, n2);
        double expected = 0.0381; // Approximate value based on Wikipedia
        assertEquals(expected, actual, Precision.SAFE_EQULALS_EPSILON);
    }

    @Test
    public void testMannWhitneyUTestWithCustomStrategies() {
        mannWhitneyUTest = new MannWhitneyUTest(NaturalRanking.MEDIAN, TiesStrategy.DENSE_RANK);
        double[] x = {1.0, 1.5, 1.5, 2.0, 2.5};
        double[] y = {1.5, 2.0, 2.5, 3.0, 3.5};
        double actual = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 0.128; // Approximate value based on Wikipedia
        assertEquals(expected, actual, Precision.SAFE_EQULALS_EPSILON);
    }
}