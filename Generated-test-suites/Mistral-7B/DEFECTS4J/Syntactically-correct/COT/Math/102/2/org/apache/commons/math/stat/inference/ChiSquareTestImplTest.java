package org.apache.commons.math.stat.inference;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.stat.inference.UnknownDistributionChiSquareTest;
import org.junit.Test;

public class ChiSquareTestImplTest {

    

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        assertNotNull(chiSquareTestImpl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplConstructorWithNullDistribution() {
        new ChiSquareTestImpl(null);
    }

    @Test
    public void testSetDistribution() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        chiSquareTestImpl.setDistribution(distribution);
        assertEquals(distribution, chiSquareTestImpl.getDistribution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareMethodWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed = new long[0];
        double[] expected = new double[0];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareMethodWithMismatchedSizes() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[3];
        long[] observed = new long[2];
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareMethodWithNegativeExpected() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.0, 1.0};
        long[] observed = new long[]{1, 2};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplChiSquareMethodWithNegativeObserved() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{-1.0, 1.0};
        long[] observed = new long[]{-1, 1};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethodWithValidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        assertEquals(result, 0.0, 0.001);
    }

    @Test
    public void testChiSquareTestMethodWithValidInput() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        double result = chiSquareTestImpl.chiSquareTest(expected, observed);
        assertEquals(result, 0.5, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidInput() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        chiSquareTestImpl.chiSquareTest(null, observed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidInput1() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        chiSquareTestImpl.chiSquareTest(expected, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidInput2() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        chiSquareTestImpl.chiSquareTest(expected, new long[0]);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestMethodWithInvalidDegreesOfFreedom() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        chiSquareTestImpl.chiSquareTest(expected, observed);
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(0.5);
        chiSquareTestImpl.setDistribution(distribution);
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestMethodWithValidInputAndAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, alpha);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestMethodWithValidInputAndAlphaAndSignificant() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 1.0};
        long[] observed = new long[]{2, 2};
        double alpha = 0.01;
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidInput() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidInput1() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[1];
        long[] observed2 = new long[0];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidInput2() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[0];
        long[] observed2 = new long[1];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInput() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 3};
        double result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(result, 2.0, 0.01);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInputAndAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 3};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInputAndAlphaAndSignificant() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 3};
        double alpha = 0.01;
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInputAndDifferentSampleSizes() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 3, 3};
        double result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(result, 4.0, 0.01);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInputAndDifferentSampleSizesAndAlpha() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 3, 3};
        double alpha = 0.05;
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonMethodWithValidInputAndDifferentSampleSizesAndSignificant() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{3, 3, 3};
        double alpha = 0.01;
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWith2WayTable() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[1][2];
        chiSquareTestImpl.chiSquare(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWith2WayTable() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[1][2];
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWith2WayTable() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[1][2];
        chiSquareTestImpl.chiSquareTestDataSetsComparison(counts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidDegreesOfFreedom() throws MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3};
        long[] observed2 = new long[]{2, 3, 3};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(-1.0);
        chiSquareTestImpl.setDistribution(distribution);
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    
}