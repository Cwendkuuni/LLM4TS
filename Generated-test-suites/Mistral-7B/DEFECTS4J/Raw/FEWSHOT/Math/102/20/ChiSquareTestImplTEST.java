package org.apache.commons.math.stat.inference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ChiSquaredDistribution;
import org.apache.commons.math.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math.distribution.DistributionFactory;
import org.junit.Test;

public class ChiSquareTestImplTest {

    @Test
    public void testChiSquareTestImplCreation() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        assertNotNull(test);
        ChiSquareTestImpl testWithDistribution = new ChiSquareTestImpl(new ChiSquaredDistributionImpl(1.0));
        assertNotNull(testWithDistribution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestImplIncorrectArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] incorrectCounts = new long[1][1];
        test.chiSquareTest(incorrectCounts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareTestIllegalDegreesOfFreedom() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        ChiSquaredDistribution distribution = new ChiSquaredDistributionImpl(-1.0);
        test.setDistribution(distribution);
        test.chiSquareTest(new long[0][0]);
    }

    @Test
    public void testChiSquareSimple() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 1};
        long[] expected = new long[]{2, 2};
        double chiSquare = test.chiSquare(expected, observed);
        assertEquals(1.0, chiSquare, 1e-10);
    }

    @Test
    public void testChiSquareLarge() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[5][5];
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[i].length; j++) {
                counts[i][j] = 10;
            }
        }
        double chiSquare = test.chiSquare(counts);
        assertEquals(11.0, chiSquare, 1e-10);
    }

    @Test
    public void testChiSquareTestSmallDegreesFreedom() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed = new long[]{1, 1};
        long[] expected = new long[]{2, 2};
        double pValue = test.chiSquareTest(expected, observed);
        assertEquals(0.045, pValue, 1e-10);
    }

    @Test
    public void testChiSquareTestLargeDegreesFreedom() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[5][5];
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[i].length; j++) {
                counts[i][j] = 100;
            }
        }
        double pValue = test.chiSquareTest(counts);
        assertEquals(0.0, pValue, 1e-10);
    }

    @Test
    public void testChiSquareTestSignificant() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 2};
        long[] observed2 = new long[]{1, 2};
        double alpha = 0.05;
        assertTrue(test.chiSquareTest(observed1, observed2, alpha));
    }

    @Test
    public void testChiSquareTestNotSignificant() throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{10, 20};
        long[] observed2 = new long[]{11, 21};
        double alpha = 0.05;
        assertFalse(test.chiSquareTest(observed1, observed2, alpha));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChiSquareDataSetsComparisonInvalidArguments() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        test.chiSquareDataSetsComparison(new long[0], new long[0]);
    }

    @Test
    public void testChiSquareDataSetsComparisonSimple() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 1};
        long[] observed2 = new long[]{1, 1};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2);
        assertEquals(0.0, chiSquare, 1e-10);
    }

    @Test
    public void testChiSquareDataSetsComparisonLarge() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 1, 1};
        long[] observed2 = new long[]{1, 1, 1};
        long[] observed3 = new long[]{1, 2, 3};
        double chiSquare = test.chiSquareDataSetsComparison(observed1, observed2, observed3);
        assertEquals(2.0, chiSquare, 1e-10);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonSmallDegreesFreedom()
            throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 1};
        long[] observed2 = new long[]{1, 1};
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(1.0, pValue, 1e-10);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonLargeDegreesFreedom()
            throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] counts = new long[5][5];
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[i].length; j++) {
                counts[i][j] = 100;
            }
        }
        long[] observed1 = new long[counts.length];
        long[] observed2 = new long[counts.length];
        for (int i = 0; i < counts.length; i++) {
            observed1[i] = 100;
            observed2[i] = 101;
        }
        double pValue = test.chiSquareTestDataSetsComparison(observed1, observed2);
        assertEquals(0.0, pValue, 1e-10);
    }

    @Test
    public void testChiSquareTestDataSetsComparisonSignificant()
            throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{1, 1, 1, 1, 1};
        long[] observed2 = new long[]{1, 1, 2, 2, 2};
        double alpha = 0.05;
        assertTrue(test.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
    }

    @Test
    public void testChiSquareTestDataSetsComparisonNotSignificant()
            throws MathException {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[] observed1 = new long[]{10, 11, 12, 13, 14};
        long[] observed2 = new long[]{11, 12, 13, 14, 15};
        double alpha = 0.05;
        assertFalse(test.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
    }

    @Test
    public void testCheckArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] correctArray = new long[2][2];
        long[][] rectangularArray = new long[2][2];
        long[][] incorrectSizeArray = new long[3][2];
        long[][] incorrectTypeArray = new Object[2][2];
        long[][] nullArray = null;
        long[][] zeroArray = new long[0][0];
        long[][] negativeArray = new long[][]{-1,{-1, -2}};
        test.checkArray(correctArray);
        test.checkArray(rectangularArray);
        test.checkArray(incorrectSizeArray);
        test.checkArray(incorrectTypeArray);
        test.checkArray(nullArray);
        test.checkArray(zeroArray);
        test.checkArray(negativeArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckArrayInvalidArray() {
        ChiSquareTestImpl test = new ChiSquareTestImpl();
        long[][] array = new long[1][1];
        array[0][0] = -1;
        test.checkArray(array);
    }
}