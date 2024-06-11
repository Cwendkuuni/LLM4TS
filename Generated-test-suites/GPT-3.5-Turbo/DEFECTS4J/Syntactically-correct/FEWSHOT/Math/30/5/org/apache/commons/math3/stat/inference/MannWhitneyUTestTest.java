package org.apache.commons.math3.stat.inference;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.junit.Test;

public class MannWhitneyUTestTest {
    private final double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
    private final double[] y = {6.0, 7.0, 8.0, 9.0, 10.0};

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        new MannWhitneyUTest().mannWhitneyUTest(null, y);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        new MannWhitneyUTest().mannWhitneyUTest(x, null);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyX() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        new MannWhitneyUTest().mannWhitneyUTest(new double[]{}, y);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyY() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        new MannWhitneyUTest().mannWhitneyUTest(x, new double[]{});
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        assertEquals(15.0, new MannWhitneyUTest().mannWhitneyU(x, y), 0.0);
    }

    @Test
    public void testCalculateAsymptoticPValue() throws ConvergenceException, MaxCountExceededException {
        assertEquals(0.11905651381908373, new MannWhitneyUTest().calculateAsymptoticPValue(10, 5, 5), 0.0);
    }
}