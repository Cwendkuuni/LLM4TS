package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Before;
import org.junit.Test;

/**
 * MannWhitneyUTestTest class to test all methods in MannWhitneyUTest class.
 */
public class MannWhitneyUTestTest {

    private MannWhitneyUTest mannWhitneyUTest;
    private double[] x;
    private double[] y;
    private double[] xWithNaN;
    private double[] yWithNaN;

    @Before
    public void setUp() {
        mannWhitneyUTest = new MannWhitneyUTest();
        x = new double[]{1.0, 1.5, 2.0, 3.0};
        y = new double[]{2.5, 2.0, 3.5, 4.0};
        xWithNaN = new double[]{1.0, 1.5, Double.NaN, 3.0};
        yWithNaN = new double[]{2.5, 2.0, 3.5, Double.NaN};
    }

    /**
     * Test mannWhitneyU method with non-null and non-empty arrays.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyU_NonEmptyAndNonNullArray()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        double mannWhitneyU = mannWhitneyUTest.mannWhitneyU(x, y);
        double expected = 6.0;
        assertEquals(expected, mannWhitneyU, 0.0);
    }

    /**
     * Test mannWhitneyU method with null arrays.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyU_NullArrays()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyU(null, y);
    }

    /**
     * Test mannWhitneyU method with empty arrays.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyU_EmptyArrays()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyU(new double[0], y);
    }

    /**
     * Test mannWhitneyUTest method with non-null and non-empty arrays.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUTest_NonEmptyAndNonNullArray()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        double pValue = mannWhitneyUTest.mannWhitneyUTest(x, y);
        double expected = 0.2;
        assertEquals(expected, pValue, 0.001);
    }

    /**
     * Test mannWhitneyUTest method with null arrays.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTest_NullArrays()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(null, y);
    }

    /**
     * Test mannWhitneyUTest method with empty arrays.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTest_EmptyArrays()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest.mannWhitneyUTest(x, new double[0]);
    }

    /**
     * Test mannWhitneyUTest method with arrays having NaN values.
     *
     * @throws ConvergenceException
     * @throws MaxCountExceededException
     * @throws NoDataException
     * @throws NullArgumentException
     */
    @Test
    public void testMannWhitneyUTest_ArraysWithNaN()
        throws NoDataException, NullArgumentException,
        ConvergenceException, MaxCountExceededException {
        mannWhitneyUTest = new MannWhitneyUTest() {
            @Override
            NaturalRanking getRanking() {
                return new NaturalRanking(NaNStrategy.LOWEST,
                        TiesStrategy.LOWEST);
            }
        };

        double pValue = mannWhitneyUTest.mannWhitneyUTest(xWithNaN, yWithNaN);
        double expected = 0.5;
        assertEquals(expected, pValue, 0.001);
    }
}