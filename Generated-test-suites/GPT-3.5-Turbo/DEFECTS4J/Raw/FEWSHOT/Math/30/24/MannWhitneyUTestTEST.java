package org.apache.commons.math3.stat.inference;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MannWhitneyUTestTest {
    private MannWhitneyUTest test;

    @Before
    public void setup() {
        test = new MannWhitneyUTest();
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        double result = test.mannWhitneyU(x, y);
        assertEquals(10, result, 0.0000001);

        double[] a = {1, 2, 3, 4, 5, 6};
        double[] b = {7, 8, 9, 10, 11, 12};

        double result2 = test.mannWhitneyU(a, b);
        assertEquals(21, result2, 0.0000001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUWithNullArguments() throws NullArgumentException, NoDataException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;

        test.mannWhitneyU(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUWithEmptyArrays() throws NullArgumentException, NoDataException {
        double[] x = {};
        double[] y = {};

        test.mannWhitneyU(x, y);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        double result = test.calculateAsymptoticPValue(10, 5, 5);
        assertEquals(0.7458838, result, 0.000001);
        
        double result2 = test.calculateAsymptoticPValue(21, 6, 6);
        assertEquals(0.2795771, result2, 0.000001);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};

        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.6666666667, result, 0.0000001);

        double[] a = {1, 2, 3, 4, 5, 6};
        double[] b = {7, 8, 9, 10, 11, 12};

        double result2 = test.mannWhitneyUTest(a, b);
        assertEquals(0.857707, result2, 0.000001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestWithNullArguments() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = null;

        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestWithEmptyArrays() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {};
        double[] y = {};

        test.mannWhitneyUTest(x, y);
    }
}