package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        Assert.assertNotNull(chiSquareTestImpl);
    }

    @Test
    public void testChiSquareTestImplConstructorWithDistribution() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.0);
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl(distribution);
        Assert.assertNotNull(chiSquareTestImpl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareConstructorWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[0];
        long[] observed = new long[]{1, 2};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareMethod() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3, 3};
        double result = chiSquareTestImpl.chiSquare(expected, observed);
        double expectedResult = 2.46195121837; // calculated using Mathematica
        Assert.assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2, 3};
        chiSquareTestImpl.chiSquare(expected, observed);
    }

    @Test
    public void testChiSquareTestMethod() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3, 3};
        double pValue = chiSquareTestImpl.chiSquareTest(expected, observed);
        double expectedPValue = 0.95673943482; // calculated using Mathematica
        Assert.assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0};
        long[] observed = new long[]{1, 2, 3};
        chiSquareTestImpl.chiSquareTest(expected, observed);
    }

    @Test
    public void testChiSquareTestWithSignificanceLevel() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        double[] expected = new double[]{1.0, 2.0, 3.0};
        long[] observed = new long[]{2, 3, 3};
        boolean result = chiSquareTestImpl.chiSquareTest(expected, observed, 0.05);
        Assert.assertFalse(result);
    }

    @Test(expected = MathException.class)
    public void testChiSquareTestWithInvalidDegreesOfFreedom() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[][] counts = new long[][]{{1, 2, 3}, {4, 5, 6}};
        chiSquareTestImpl.chiSquareTest(counts);
    }

    @Test
    public void testChiSquareDataSetComparisonMethod() throws IllegalArgumentException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 3};
        long[] observed2 = new long[]{3, 2, 3};
        double result = chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
        double expectedResult = 2.46195121837; // calculated using Mathematica
        Assert.assertEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetComparisonMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3};
        long[] observed2 = new long[]{3, 2, 3};
        chiSquareTestImpl.chiSquareDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareDataSetComparisonTestMethod() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 3};
        long[] observed2 = new long[]{3, 2, 3};
        double pValue = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
        double expectedPValue = 0.95673943482; // calculated using Mathematica
        Assert.assertEquals(expectedPValue, pValue, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestDataSetsComparisonMethodWithInvalidArguments() {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3};
        long[] observed2 = new long[]{3, 2, 3};
        chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonWithSignificanceLevel() throws IllegalArgumentException, MathException {
        ChiSquareTestImpl chiSquareTestImpl = new ChiSquareTestImpl();
        long[] observed1 = new long[]{2, 3, 3};
        long[] observed2 = new long[]{3, 2, 3};
        boolean result = chiSquareTestImpl.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
        Assert.assertFalse(result);
    }
}