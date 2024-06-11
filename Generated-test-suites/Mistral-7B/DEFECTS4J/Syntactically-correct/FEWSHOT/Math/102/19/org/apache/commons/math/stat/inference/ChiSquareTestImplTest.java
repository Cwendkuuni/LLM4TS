package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.distribution.DistributionFactory;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testConstructor() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test.distribution);
        assertEquals(1, test.distribution.getDegreesOfFreedom());
    }

    @Test
    public void testConstructorWithDistribution() {
        ChiSquareDistributionImpl dist = new ChiSquaredDistributionImpl(5.0);
        ChiSquareTestImpl test = new ChiSquareTestImpl(dist);
        assertNotNull(test.distribution);
        assertEquals(5, test.distribution.getDegreesOfFreedoms());
    }

    @Test
    public void testChiSquareDataSetsComparisonZeroLength() {
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        try {
            ChiSquareTestImpl test = new ChiSquareTestImpl();
            test.chiSquareDataSetsComparison(observed1, observed2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareDataSetsComparisonEqualLengths() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2, 3};
        double result = ChiSquareTestImpl.this.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, result, 1e-12);
    }

    @Test
    public void testChiSquareDataSetsComparisonUnequalLengths() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3};
        double result = ChiSquareTestImpl.this.chiSquareDataSetsComparison(observed1, observed2);
        double expected = 1.5;
        assertEquals(expected, result, 1e-12);
    }

    @Test
    public void testChiSquareDataSetsComparisonUnequalCounts() {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        double result = ChiSquareTestImpl.this.chiSquareDataSetsComparison(observed1, observed2);
        double expected = 3.0;
        assertEquals(expected, result, 1e-12);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonZeroLength() {
        long[] observed1 = new long[0];
        long[] observed2 = new long[0];
        try {
            ChiSquareTestImpl test = new ChiSquareTestImpl();
            test.chiSquareTestDataSetsComparison(observed1, observed2, 0.05);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestDataSetsComparisonEqualLengths() throws MathException, IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {1, 2, 3};
        double alpha = 0.05;
        double result = ChiSquareTestImpl.this.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        assertTrue(result > 0.95);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonUnequalLengths() throws MathException, IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {2, 3, 3};
        double alpha = 0.05;
        double result = ChiSquareTestImpl.this.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        double expected = 0.025;
        assertEquals(expected, result, 1e-12);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonUnequalCounts() throws MathException, IllegalArgumentException {
        long[] observed1 = {1, 2, 3};
        long[] observed2 = {3, 2, 1};
        double alpha = 0.05;
        double result = ChiSquareTestImpl.this.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
        double expected = 0.0;
        assertEquals(expected, result, 1e-12);
    }

    @Test
    public void testChiSquareTest() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        boolean result = ChiSquareTestImpl.this.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquare() throws IllegalArgumentException {
        long[][] counts = {{1, 2}, {2, 3}};
        double result = ChiSquareTestImpl.this.chiSquare(counts);
        double expected = 2.0;
        assertEquals(expected, result, 1e-12);
    }

    @Test
    public void testChiSquareTestWithDifferentDistribution() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(5.0));
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestWithSameDistribution() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result > 0.95);
    }

    @Test
    public void testChiSquareTestWithDifferentDegreesOfFreedom() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(5.0));
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertFalse(result);
    }

    @Test
    public void testChiSquareTestWithSameDegreesOfFreedom() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        boolean result = test.chiSquareTest(expected, observed, alpha);
        assertTrue(result > 0.95);
    }

    @Test
    public void testChiSquareTestWithNullData() throws MathException, IllegalArgumentException {
        double[] expected = null;
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithEmptyData() throws MathException, IllegalArgumentException {
        double[] expected = new double[0];
        long[] observed = new long[0];
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithNullExpectedData() throws MathException, IllegalArgumentException {
        double[] expected = null;
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithNullObservedData() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = null;
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithDifferentLengthExpectedAndObservedData() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithNegativeExpectedData() throws MathException, IllegalArgumentException {
        double[] expected = {-1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithNegativeObservedData() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {-2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithZeroExpectedData() throws MathException, IllegalArgumentException {
        double[] expected = {0.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithZeroObservedData() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {0, 3, 3};
        double alpha = 0.05;
        ChiSquareTestImpl test = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(3.0));
        try {
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testChiSquareTestWithNullDistribution() throws MathException, IllegalArgumentException {
        double[] expected = {1.0, 2.0, 3.0};
        long[] observed = {2, 3, 3};
        double alpha = 0.05;
        try {
            ChiSquareTestImpl test = new ChiSquareTestImpl();
            test.chiSquareTest(expected, observed, alpha);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}