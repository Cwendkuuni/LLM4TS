package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.distribution);
    }

    @Test
    public void testChiSquareTestImplWithDistributionConstructor() {
        ChiSquaredDistributionImpl dist = new ChiSquaredDistributionImpl(2.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertEquals(dist, test.distribution);
    }

    @Test
    public void testChiSquareWithEqualExpectedAndObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{10, 10};
        double[] expected = new double[]{10.0, 10.0};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(0.0, chiSquare, 1e-14);
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(1.0, pValue, 1e-14);
    }

    @Test
    public void testChiSquareWithUnequalExpectedAndObserved() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{10, 15};
        double[] expected = new double[]{10.0, 5.0};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(6.25, chiSquare, 1e-14);
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.0434, pValue, 1e-5);
    }

    @Test
    public void testChiSquareWithInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(new double[0], new long[0]));
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(new double[]{0.0}, new long[]{1, 1}));
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(new double[]{1.0, 1.0}, new long[]{1, 2}));
        assertThrows(IllegalArgumentException.class, () -> test.chiSquare(new double[]{1.0, 1.0}, new long[]{1, 1}));
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(new double[]{1.0, 1.0}, new long[]{1, 2}));
    }

    @Test
    public void testChiSquareTestWithDifferentArraysLength() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 2};
        double[] expected = new double[]{1.0, 2.0, 3.0};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareTestWithInvalidDegreesOfFreedom() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.5);
        test.setDistribution(distribution);
        long[] observed = new long[]{1, 2};
        double[] expected = new double[]{1.0, 2.0};
        assertThrows(MathException.class, () -> test.chiSquareTest(expected, observed));
    }

    @Test
    public void testChiSquareDataSetsComparison() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(3.15, chiSquare, 1e-14);
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.1013, pValue, 1e-5);
    }

    @Test
    public void testChiSquareDataSetsComparisonWithInvalidInput() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{1, 1};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareDataSetsComparison(observed1, observed2));
    }

    @Test
    public void testChiSquareDataSetsComparisonWithInvalidDegreesOfFreedom() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(1.5);
        test.setDistribution(distribution);
        long[] observed1 = new long[]{1, 2, 3, 4};
        long[] observed2 = new long[]{2, 3, 4, 5};
        assertThrows(MathException.class, () -> test.chiSquareTestDataSetsComparison(observed1, observed2));
    }
}