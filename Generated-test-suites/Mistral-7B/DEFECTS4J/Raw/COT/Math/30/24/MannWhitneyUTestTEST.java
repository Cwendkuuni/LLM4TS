package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;
    private NaturalRanking naturalRanking;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
        naturalRanking = new NaturalRanking(NaturalRanking.DEFAULT_STRATEGY,
                NaturalRanking.DEFAULT_TIES_STRATEGY);
    }

    @Test
    public void testMannWhitneyU() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};

        double result = mannWhitneyUTest.mannWhitneyU(x, y);
        double expected = 6.0;
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testMannWhitneyUTest() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {2.0, 3.0, 4.0};

        double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 0.10545454545454547;
        assertEquals(expected, result, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionForX() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(null, new double[3]);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionForY() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[3], null);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataExceptionForX() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[3]);
    }

    @Test(expected = NoDataException.class)
    public void testNoDataExceptionForY() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[3], new double[0]);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[Integer.MAX_VALUE],
                new double[Integer.MAX_VALUE]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws ConvergenceException, MaxCountExceededException,
            NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.mannWhitneyUTest(new double[2147483647],
                new double[2147483647]);
    }

}