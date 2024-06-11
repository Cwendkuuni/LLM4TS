package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplConstruction() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.getDistribution());
    }

    @Test
    public void testChiSquareTestImplWithDistributionConstruction() {
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(2);
        ChiSquareTestImpl test = new ChiSquareTestImpl(distribution);
        assertEquals(distribution, test.getDistribution());
    }

    @Test
    public void testChiSquareTestImplInvalidObservedAndExpectedLengths() {
        assertThrows(IllegalArgumentException.class, () -> new ChiSquareTestImpl(new long[1][], new long[2]));
        assertThrows(IllegalArgumentException.class, () -> new ChiSquareTestImpl(new long[2][], new long[1]));
        assertThrows(IllegalArgumentException.class, () -> new ChiSquareTestImpl(new long[0][], new long[0]));
    }

    @Test
    public void testChiSquareTestImplInvalidExpectedAndObservedValues() {
        long[][] counts = {{-1, 1}, {1, -1}};
        assertThrows(IllegalArgumentException.class, () -> new ChiSquareTestImpl(counts));
    }

    @Test
    public void testChiSquareTestImplNullDistribution() {
        assertThrows(NullPointerException.class, () -> new ChiSquareTestImpl(null));
    }

    @Test
    public void testChiSquareTestImplValidChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        double expectedChiSquare = 2.0;
        double calculatedChiSquare = test.chiSquare(counts);
        assertEquals(expectedChiSquare, calculatedChiSquare, 1e-10);
    }

    @Test
    public void testChiSquareTestImplPValue() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        double alpha = 0.05;
        double expectedPValue = 0.05;
        double calculatedPValue = test.chiSquareTest(counts, alpha);
        assertEquals(expectedPValue, calculatedPValue, 1e-10);
    }

    @Test
    public void testChiSquareTestImplRejectNullHypothesis() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        double alpha = 0.01;
        boolean expectedResult = true;
        boolean calculatedResult = test.chiSquareTest(counts, alpha);
        assertTrue(expectedResult == calculatedResult);
    }

    @Test
    public void testChiSquareTestImplDataSetsComparisonValidChiSquare() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        double expectedChiSquare = 2.0;
        double calculatedChiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(expectedChiSquare, calculatedChiSquare, 1e-10);
    }

    @Test
    public void testChiSquareTestImplDataSetsComparisonPValue() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        double alpha = 0.05;
        double expectedPValue = 0.05;
        double calculatedPValue = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertEquals(expectedPValue, calculatedPValue, 1e-10);
    }

    @Test
    public void testChiSquareTestImplDataSetsComparisonRejectNullHypothesis() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 1};
        double alpha = 0.01;
        boolean expectedResult = true;
        boolean calculatedResult = test.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(expectedResult == calculatedResult);
    }

    @Test
    public void testChiSquareTestImplInvalidAlpha() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = {{1, 2}, {2, 1}};
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(counts, -1));
        assertThrows(IllegalArgumentException.class, () -> test.chiSquareTest(counts, 1.1));
    }
}