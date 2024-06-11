package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.junit.Test;

/**
 * Test cases for ChiSquareTestImpl class.
 */
public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        assertNotNull(chiSquareTestImpl);
    }

    @Test
    public void testChiSquareTestImplConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(distribution);
        assertNotNull(chiSquareTestImpl);
        assertEquals(distribution, chiSquareTestImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplUnsupportedConstructor() {
        new ChiSquareTestImpl(); // Use default constructor, but it's not supported
    }

    @Test
    public void testChiSquareTestImplGetDistribution() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        assertNull(chiSquareTestImpl.getDistribution());

        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        chiSquareTestImpl.setDistribution(distribution);
        assertSame(distribution, chiSquareTestImpl.getDistribution());
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWithValidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] expected = {1, 2, 3};
        long[] observed = {2, 2, 3};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(4.0, result, 0.001);
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWithInvalidArgumentsLength() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = new long[2];
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWithInvalidArgumentsLengthMismatch() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[1];
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWithNegativeExpectedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWithNegativeObservedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{-2, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquare(expected, observed));
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWithNullExpectedArray() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquare(null, observed));
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWithNullObservedArray() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquare(expected, null));
    }

    @Test
    public void testChiSquareTestImplChiSquareTestMethodWithValidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 2, 3};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareTestMethodWithInvalidArgumentsLength() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[1];
        long[] observed = new long[2];
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareTestMethodWithInvalidArgumentsLengthMismatch() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[2];
        long[] observed = new long[1];
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestImplChiSquareTestMethodWithNegativeExpectedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestImplChiSquareTestMethodWithNegativeObservedValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{-2, 2, 3};
        assertFalse(chiSquareTestImpl.chiSquareTest(expected, observed) > 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareTestMethodWithNullExpectedArray() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2, 3};
        chiSquareTestImpl.chiSquareTest(null, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareTestMethodWithNullObservedArray() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        chiSquareTestImpl.chiSquareTest(expected, null);
    }

    @Test
    public void testChiSquareTestImplChiSquareTestMethodWithInvalidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquareTest(expected, observed, -1.0));
    }

    @Test
    public void testChiSquareTestImplChiSquareTestMethodWithSignificanceLevelBiggerThan05() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 2, 3};
        assertFalse(chiSquareTestImpl.chiSquareTest(expected, observed, 0.6));
    }

    @Test
    public void testChiSquareTestImplChiSquareTestMethodWithValidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 2, 3};
        assertFalse(chiSquareTestImpl.chiSquareTest(expected, observed, 0.05));
    }

    @Test
    public void testChiSquareTestImplChiSquareTestDataSetComparisonMethodWithValidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareTestDataSetComparisonMethodWithInvalidArgumentsLengthMismatch() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[1];
        long[] observed2 = new long[2];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestImplChiSquareTestDataSetComparisonMethodWithNegativeValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, -3};
        long[] observed2 = {2, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareTestImplChiSquareTestDataSetComparisonMethodWithNullValues() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = null;
        long[] observed2 = {2, 2, 3};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestImplChiSquareTestDataSetComparisonMethodWithValidSignificanceLevel() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        assertFalse(chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05));
    }

    @Test
    public void testChiSquareTestImplChiSquareMethodWith2x2Table() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 3}};
        double pValue = chiSquareTestImpl.chiSquareTest(counts);
        assertTrue(pValue > 0.0 && pValue < 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareMethodWithInvalidArray() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        Object[][] counts = {{1, 2}, {new Object[] {2, 3}}} ;
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = MathRuntimeException.class)
    public void testChiSquareTestImplChiSquareDataSetsComparisonMethodWithInvalidDegreesOfFreedom() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 2, 3};
        chiSquareTestImpl.setDistribution(new ChiSquaredDistributionImpl(-1.0));
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }
}