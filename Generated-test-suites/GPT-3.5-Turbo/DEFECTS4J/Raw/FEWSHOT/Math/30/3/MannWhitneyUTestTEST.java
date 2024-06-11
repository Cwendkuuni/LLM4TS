package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.junit.Test;

public class MannWhitneyUTestTest {
    private final MannWhitneyUTest uTest = new MannWhitneyUTest();

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        assertEquals(6.0, uTest.mannWhitneyU(x, y), 0.0001);

        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 4.0, 5.0};
        assertEquals(6.0, uTest.mannWhitneyU(x2, y2), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullX() throws NullArgumentException, NoDataException {
        uTest.mannWhitneyU(null, new double[]{});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullY() throws NullArgumentException, NoDataException {
        uTest.mannWhitneyU(new double[]{}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthX() throws NullArgumentException, NoDataException {
        uTest.mannWhitneyU(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthY() throws NullArgumentException, NoDataException {
        uTest.mannWhitneyU(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        assertEquals(0.0174927113702624, uTest.mannWhitneyUTest(x, y), 0.0001);

        double[] x2 = {1.0, 2.0, 3.0};
        double[] y2 = {3.0, 4.0, 5.0};
        assertEquals(0.41474927287030555, uTest.mannWhitneyUTest(x2, y2), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        uTest.mannWhitneyUTest(null, new double[]{});
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        uTest.mannWhitneyUTest(new double[]{}, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        uTest.mannWhitneyUTest(new double[]{}, new double[]{1.0, 2.0, 3.0});
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        uTest.mannWhitneyUTest(new double[]{1.0, 2.0, 3.0}, new double[]{});
    }
}