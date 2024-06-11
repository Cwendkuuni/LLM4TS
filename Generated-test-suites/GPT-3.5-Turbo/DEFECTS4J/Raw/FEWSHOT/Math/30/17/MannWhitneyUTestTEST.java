package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Before;
import org.junit.Test;

public class MannWhitneyUTestTest {
    private MannWhitneyUTest test;

    @Before
    public void setUp() {
        test = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyU(x, y);
        assertNotNull(result);
        assertEquals(0.0, result, 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullX() throws NullArgumentException, NoDataException {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthX() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthY() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        test.mannWhitneyU(x, y);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double result = test.mannWhitneyUTest(x, y);
        assertNotNull(result);
        assertEquals(0.0, result, 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = null;
        double[] y = {4.0, 5.0, 6.0};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = null;
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {4.0, 5.0, 6.0};
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {};
        test.mannWhitneyUTest(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double result = test.calculateAsymptoticPValue(0.0, 3, 3);
        assertNotNull(result);
    }
}