package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(null, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException2() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[0], null);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException3() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[0], new double[0]);
    }

    @Test
    public void testMannWhitneyUBasicFunctionality() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4};
        double[] y = {3, 2, 1, 4, 5};
        double expected = 6.0;
        double actual = test.mannWhitneyU(x, y);
        assertEquals(expected, actual, 1e-6);
    }

    @Test
    public void testMannWhitneyUTestBasicFunctionality() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 3, 4};
        double[] y = {3, 2, 1, 4, 5};
        double expected = 0.015625;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestConvergenceException() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        double[] y = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceededException() throws Exception {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testMannWhitneyUTestTies() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4};
        double[] y = {2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4};
        double expected = 0.109375;
        double actual = test.mannWhitneyUTest(x, y);
        assertEquals(expected, actual, 1e-6);
    }

    @Test
    public void testMannWhitneyUTestNaN() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2.0, 3, 4};
        double[] y = {1, Double.NaN, 3, 4};
        try {
            test.mannWhitneyUTest(x, y);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testMannWhitneyUTestNaN2() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1, 2.0, 3};
        double[] y = {1, Double.NaN, Double.NaN};
        try {
            test.mannWhitneyUTest(x, y);
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    
}