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
import org.junit.Test;

/**
 * Test cases for MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor with default strategies.
     */
    @Test
    public void testDefaultConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test);
    }

    /**
     * Test constructor with custom strategies.
     */
    @Test
    public void testCustomConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.MISSING,
                TiesStrategy.LOWER);
        assertNotNull(test);
    }

    /**
     * Test method mannWhitneyU with same arrays.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUSameArrays()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU with null arrays.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUNullArrays()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = null;
        double[] y = null;
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU with zero-length arrays.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUZeroLengthArrays()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyU(x, y);
    }

    /**
     * Test method mannWhitneyU with arrays having identical elements.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUIdenticalElements()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[]{1.0, 1.0, 1.0};
        double[] y = new double[]{1.0, 1.0, 1.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
        assertEquals(3.0, result, 0.0);
    }

    /**
     * Test method mannWhitneyU with arrays having same sum.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUSameSum()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 2.0, 1.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
        assertEquals(3.0, result, 0.0);
    }

    /**
     * Test method mannWhitneyU with arrays having different sums.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUDifferentSums()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 4.0, 5.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyU(x, y);
        assertEquals(6.0, result, 0.0);
    }

    /**
     * Test method mannWhitneyUTest with same arrays.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestSameArrays()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test method mannWhitneyUTest with null arrays.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArrays()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = null;
        double[] y = null;
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test method mannWhitneyUTest with zero-length arrays.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestZeroLengthArrays()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test method mannWhitneyUTest with arrays having identical elements.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestIdenticalElements()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[]{1.0, 1.0, 1.0};
        double[] y = new double[]{1.0, 1.0, 1.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(x, y);
    }

    /**
     * Test method mannWhitneyUTest with arrays having same sum.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUTestSameSum()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 2.0, 1.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(1.0, result, 0.05);
    }

    /**
     * Test method mannWhitneyUTest with arrays having different sums.
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUTestDifferentSums()
        throws ConvergenceException, MaxCountExceededException,
        NoDataException, NullArgumentException {
        double[] x = new double[]{1.0, 2.0, 3.0};
        double[] y = new double[]{3.0, 4.0, 5.0};
        MannWhitneyUTest test = new MannWhitneyUTest();
        double result = test.mannWhitneyUTest(x, y);
        assertEquals(0.01, result, 0.05);
    }

}