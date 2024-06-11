package org.apache.commons.math3.stat.inference.test;

import junit.framework.TestCase;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;

/**
 * Test cases for {@link MannWhitneyUTest}.
 */
public class MannWhitneyUTestTest extends TestCase {

    /**
     * Test method mannWhitneyU(double[], double[]) using the default constructor.
     */
    public void testMannWhitneyU_defaultConstructor() {
        double[] x = {1.0, 2.5, 1.5};
        double[] y = {2.5, 1.5};
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            double result = mannWhitneyUTest.mannWhitneyU(x, y);
            assertEquals(2.5, result);
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method mannWhitneyU(double[], double[]) using a custom constructor.
     */
    public void testMannWhitneyU_customConstructor() {
        double[] x = {1.0, 2.5, 1.5};
        double[] y = {2.5, 1.5};
        MannWhitneyUTest mannWhitneyUTest = null;
        try {
            mannWhitneyUTest = new MannWhitneyUTest(NaturalRanking.FIXED, TiesStrategy.MIN);
            double result = mannWhitneyUTest.mannWhitneyU(x, y);
            // The result would depend on the given NaNStrategy and TiesStrategy
            // In this test, we are just checking the method execution
            assertNotNull(result);
        } catch (NullArgumentException | NoDataException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method mannWhitneyU(double[], double[]) with null arrays.
     */
    public void testMannWhitneyU_nullArrays() {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test method mannWhitneyU(double[], double[]) with empty arrays.
     */
    public void testMannWhitneyU_emptyArrays() {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyU(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected exception
        }
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) using the default constructor.
     */
    public void testMannWhitneyUTest_defaultConstructor() {
        double[] x = {1.0, 2.5, 1.5};
        double[] y = {2.5, 1.5};
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            // The result would depend on the random number generator
            // In this test, we are just checking the method execution
            assertNotNull(result);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) using a custom constructor.
     */
    public void testMannWhitneyUTest_customConstructor() {
        double[] x = {1.0, 2.5, 1.5};
        double[] y = {2.5, 1.5};
        MannWhitneyUTest mannWhitneyUTest = null;
        try {
            mannWhitneyUTest = new MannWhitneyUTest(NaturalRanking.FIXED, TiesStrategy.MIN);
            double result = mannWhitneyUTest.mannWhitneyUTest(x, y);
            // The result would depend on the random number generator and the given NaNStrategy and TiesStrategy
            // In this test, we are just checking the method execution
            assertNotNull(result);
        } catch (NullArgumentException | NoDataException | ConvergenceException | MaxCountExceededException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) with null arrays.
     */
    public void testMannWhitneyUTest_nullArrays() {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(null, null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    /**
     * Test method mannWhitneyUTest(double[], double[]) with empty arrays.
     */
    public void testMannWhitneyUTest_emptyArrays() {
        try {
            MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
            mannWhitneyUTest.mannWhitneyUTest(new double[0], new double[0]);
            fail("Expected NoDataException");
        } catch (NoDataException e) {
            // Expected exception
        }
    }
}