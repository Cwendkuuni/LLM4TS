package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * JUnit tests for {@link MannWhitneyUTest} class.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, new double[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArgumentException1() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    @Test
    public void testMannWhitneyU() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {5.0, 5.0, 6.0};
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        assertEquals(8.0, mannWhitneyU, 0.0);

        x = new double[0];
        y = new double[0];
        try {
            mannWhitneyUTest.mannWhitneyU(x, y);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test
    public void testMannWhitneyUTest() throws NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {5.0, 5.0, 6.0};
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.125, pValue, 0.01);

        x = new double[]{0.0, 0.0, 0.0};
        y = new double[]{0.0, 0.0, 0.0, 0.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 0.01);

        x = new double[]{1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(1.0, pValue, 0.01);

        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 5.0, 5.0, 5.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, pValue, 0.01);

        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 5.0, 5.0, 5.0, 5.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, pValue, 0.01);

        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 5.0, 5.0, 5.0, 5.0, 5.0};
        pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        assertEquals(0.0, pValue, 0.01);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(null, new double[0]);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException1() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhitneyUTest.mannWhitneyUTest(new double[0], null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestNoDataException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        try {
            mannWhititneyUTest.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // expected
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testCalculateAsymptoticPValueConvergenceException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
            y[i] = i * 2;
        }
        try {
            mannWhitneyUTest.calculateAsymptoticPValue(0.0, x.length, y.length);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testCalculateAsymptoticPValueMaxCountExceededException() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        try {
            mannWhitneyUTest.calculateAsymptoticPValue(0.0, x.length, y.length);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();

        double[] x = {1.0, 2.0, 3.0, 4.0};
        double[] y = {5.0, 5.0, 6.0};
        double pValue = mannWhitneyUTest.calculateAsymptoticPValue(8.0, x.length, y.length);
        assertEquals(0.125, pValue, Precision.DOUBLE_EPSILON);

        x = new double[0];
        y = new double[0];
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(0.0, x.length, y.length);
        assertEquals(1.0, pValue, Precision.DOUBLE_EPSILON);

        x = new double[]{1.0, 1.0, 1.0, 1.0};
        y = new double[]{1.0, 1.0, 1.0, 1.0};
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(1.0, x.length, y.length);
        assertEquals(1.0, pValue, Precision.DOUBLE_EPSILON);

        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 5.0, 5.0, 5.0};
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(0.0, x.length, y.length);
        assertEquals(0.0, pValue, Precision.DOUBLE_EPSILON);

        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 5.0, 5.0, 5.0, 5.0};
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(0.0, x.length, y.length);
        assertEquals(0.0, pValue, Precision.DOUBLE_EPSILON);

        x = new double[]{1.0, 2.0, 3.0, 4.0};
        y = new double[]{5.0, 5.0, 5.0, 5.0, 5.0, 5.0};
        pValue = mannWhitneyUTest.calculateAsymptoticPValue(0.0, x.length, y.length);
        assertEquals(0.0, pValue, Precision.DOUBLE_EPSILON);
    }
}