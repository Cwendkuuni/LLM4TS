package org.apache.commons.math3.stat.inference;

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
import org.junit.Test;

/**
 * Test cases for MannWhitneyUTest class
 */
public class MannWhitneyUTestTest {

    /**
     * Test constructor MannWhitneyUTest()
     */
    @Test
    public void testConstructor() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test constructor MannWhitneyUTest(NaNStrategy nanStrategy, TiesStrategy tiesStrategy)
     */
    @Test
    public void testConstructorWithStrategies() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest(
                NANStrategy.MIN, TiesStrategy.LOW);
        assertNotNull(mannWhitneyUTest);
    }

    /**
     * Test method ensureDataConformance
     */
    @Test(expected = NullArgumentException.class)
    public void testEnsureDataConformanceNullArguments() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(null, null);
    }

    @Test(expected = NoDataException.class)
    public void testEnsureDataConformanceNoData() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        mannWhitneyUTest.ensureDataConformance(new double[0], new double[0]);
    }

    /**
     * Test method ensureDataConformance with valid arguments
     */
    @Test
    public void testEnsureDataConformance() {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        mannWhitneyUTest.ensureDataConformance(x, y);
    }

    /**
     * Test method mannWhitneyU
     */
    @Test
    public void testMannWhitneyU() throws NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {4.0, 5.0, 6.0, 7.0};
        double mannWhitneyU1 = mannWhitneyUTest.mannWhitneyU(x1, y1);
        assertEquals(15.0, mannWhitneyU1, 0.0);

        double[] x2 = {1.0, 1.5, 2.0, 2.5};
        double[] y2 = {3.0, 3.5, 4.0, 4.5};
        double mannWhitneyU2 = mannWhitneyUTest.mannWhitneyU(x2, y2);
        assertEquals(6.0, mannWhitneyU2, 0.0);
    }

    /**
     * Test method mannWhitneyUTest
     */
    @Test
    public void testMannWhitneyUTest() throws ConvergenceException,
            MaxCountExceededException, NoDataException, NullArgumentException {
        MannWhitneyUTest mannWhitneyUTest = new MannWhitneyUTest();
        double[] x1 = {1.0, 2.0, 3.0};
        double[] y1 = {4.0, 5.0, 6.0, 7.0};
        double pValue1 = mannWhitneyUTest.mannWhitneyUTest(x1, y1);
        assertEquals(0.0134584251629341, pValue1, 0.001);

        double[] x2 = {1.0, 1.5, 2.0, 2.5};
        double[] y2 = {3.0, 3.5, 4.0, 4.5};
        double pValue2 = mannWhitneyUTest.mannWhitneyUTest(x2, y2);
        assertEquals(0.0772639489204439, pValue2, 0.001);
    }
}