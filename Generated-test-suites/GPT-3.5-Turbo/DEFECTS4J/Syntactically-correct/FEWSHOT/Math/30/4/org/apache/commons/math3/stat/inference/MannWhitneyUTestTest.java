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
    public void testMannWhitneyU() {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(10.0, test.mannWhitneyU(x, y), 0.0001);

        x = new double[] {1, 2, 3, 4, 5};
        y = new double[] {1, 1, 1, 1, 1};
        assertEquals(0.0, test.mannWhitneyU(x, y), 0.0001);

        x = new double[] {1, 2, 3, 4, 5};
        y = new double[] {1, 2, 3, 4, 5};
        assertEquals(12.0, test.mannWhitneyU(x, y), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArgumentException() {
        test.mannWhitneyU(null, new double[]{});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUNoDataException() {
        test.mannWhitneyU(new double[]{}, new double[]{});
    }

    @Test
    public void testMannWhitneyUTest() throws Exception {
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {6, 7, 8, 9, 10};
        assertEquals(0.0, test.mannWhitneyUTest(x, y), 0.0001);

        x = new double[] {1, 2, 3, 4, 5};
        y = new double[] {1, 1, 1, 1, 1};
        assertEquals(0.0, test.mannWhitneyUTest(x, y), 0.0001);

        x = new double[] {1, 2, 3, 4, 5};
        y = new double[] {1, 2, 3, 4, 5};
        assertEquals(0.5, test.mannWhitneyUTest(x, y), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException() throws Exception {
        test.mannWhitneyUTest(null, new double[]{});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestNoDataException() throws Exception {
        test.mannWhitneyUTest(new double[]{}, new double[]{});
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestConvergenceException() throws Exception {
        double[] x = new double[100000];
        double[] y = new double[100000];
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceededException() throws Exception {
        double[] x = new double[100000];
        double[] y = new double[100000];
        test.mannWhitneyUTest(x, y);
    }
}