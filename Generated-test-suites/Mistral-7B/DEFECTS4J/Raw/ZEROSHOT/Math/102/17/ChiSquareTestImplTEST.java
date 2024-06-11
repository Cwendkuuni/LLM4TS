import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplCreation() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        ChiSquareTestImpl testImplWithDistribution = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));

        assertEquals(1.0, testImpl.getDegreesOfFreedom(), 0.0);
        assertEquals(testImplWithDistribution, testImplWithDistribution.getDistribution());
        assertNull(testImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidObservedLength() {
        new ChiSquareTestImpl(new long[0][0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidExpectedLength() {
        new ChiSquareTestImpl(new double[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidObservedLengthAndExpectedLength() {
        new ChiSquareTestImpl(new double[1], new long[2]);
    }

    @Test
    public void testChiSquareTestImplValidParameters() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl();
        double[] observed = new double[2];
        long[] expected = new long[2];
        long[][] counts = new long[2][2];

        observed[0] = 5.0;
        observed[1] = 10.0;
        expected[0] = 2;
        expected[1] = 8;

        counts[0][0] = 3;
        counts[0][1] = 4;
        counts[1][0] = 7;
        counts[1][1] = 6;

        assertEquals(0.1616177026467444, testImpl.chiSquare(expected, observed), 0.00001);
        assertEquals(0.5165488321314931, testImpl.chiSquareTest(expected, observed), 0.00001);
        assertFalse(testImpl.chiSquareTest(expected, observed, 0.0));
        assertTrue(testImpl.chiSquareTest(expected, observed, 0.1));

        assertEquals(0.1616177026467444, testImpl.chiSquare(counts), 0.00001);
        assertEquals(0.5165488321314931, testImpl.chiSquareTest(counts), 0.00001);
        assertFalse(testImpl.chiSquareTest(counts, 0.0));
        assertTrue(testImpl.chiSquareTest(counts, 0.1));

        assertEquals(0.0, testImpl.chiSquareDataSetsComparison(new long[2], new long[2]));
        assertEquals(1.0, testImpl.chiSquareTestDataSetsComparison(new long[2], new long[2]), 0.0);
        assertFalse(testImpl.chiSquareTestDataSetsComparison(new long[2], new long[2], 0.0));
        assertTrue(testImpl.chiSquareTestDataSetsComparison(new long[2], new long[2], 0.1));

        assertEquals(1.0 / Math.sqrt(10.0), testImpl.chiSquareDataSetsComparison(new long[]{5, 10}, new long[]{7, 6}));
        assertEquals(1.0 - 0.05, testImpl.chiSquareTestDataSetsComparison(new long[]{5, 10}, new long[]{7, 6}), 0.0001);
        assertFalse(testImpl.chiSquareTestDataSetsComparison(new long[]{5, 10}, new long[]{7, 6}, 0.05));
        assertTrue(testImpl.chiSquareTestDataSetsComparison(new long[]{5, 10}, new long[]{7, 6}, 0.06));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplInvalidDegreesOfFreedom() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(-1.0));
    }

    @Test
    public void testChiSquareTestImplThrowMathException() {
        ChiSquareTestImpl testImpl = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        assertThrows(MathRuntimeException.class, () -> testImpl.chiSquareTestDataSetsComparison(new long[2], new long[2], -1.0));
    }
}