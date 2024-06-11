package org.apache.commons.math3.stat.inference.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.junit.Test;

/**
 * Test class for MannWhitneyUTest.
 */
public class MannWhitneyUTestTest {

    @Test
    public void testMannWhitneyUTestConstructor() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        assertNotNull(test.naturalRanking);
    }

    @Test
    public void testMannWhitneyUTestConstructorWithStrategies() {
        MannWhitneyUTest test = new MannWhitneyUTest(NaNStrategy.MISSING, TiesStrategy.MIN);
        assertNotNull(test.naturalRanking);
    }

    @Test
    public void testEnsureDataConformance() throws NullArgumentException, NoDataException {
        double[] x = new double[0];
        double[] y = new double[0];
        MannWhitneyUTest test = new MannWhitneyUTest();
        try {
            test.ensureDataConformance(x, y);
            fail("Expected exception");
        } catch (NullArgumentException e) {
            // Expected
        } catch (NoDataException e) {
            // Expected
        }

        x = new double[]{1.0};
        y = new double[]{2.0};
        test.ensureDataConformance(x, y);
    }

    @Test
    public void testMannWhitneyU() throws NullArgumentException, NoDataException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
        double[] y = {4.0, 4.0, 4.0, 5.0, 5.0};
        double result = test.mannWhitneyU(x, y);
        assertEquals(12.0, result, 0.0);
    }

    @Test
    public void testMannWhitneyUTest() throws NullArgumentException, NoDataException, ConvergenceException, MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0, 3.0, 3.0};
        double[] y = {4.0, 4.0, 4.0, 5.0, 5.0};
        double pValue = test.mannWhitneyUTest(x, y);
        assertEquals(0.1, pValue, 0.01);
    }

    @Test(expected = NullArgumentException.class)
    public void testMannWhitneyUTestNullArgumentException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(null, new double[0]);
    }

    @Test(expected = NoDataException.class)
    public void testMannWhitneyUTestNoDataException() {
        MannWhitneyUTest test = new MannWhitneyUTest();
        test.mannWhitneyUTest(new double[0], new double[0]);
    }

    @Test(expected = ConvergenceException.class)
    public void testMannWhitneyUTestConvergenceException() throws MaxCountExceededException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[100000];
        double[] y = new double[100000];
        test.mannWhitneyUTest(x, y);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMannWhitneyUTestMaxCountExceededException() throws ConvergenceException {
        MannWhitneyUTest test = new MannWhitneyUTest();
        double[] x = new double[Integer.MAX_VALUE];
        double[] y = new double[Integer.MAX_VALUE];
        test.mannWhitneyUTest(x, y);
    }

}