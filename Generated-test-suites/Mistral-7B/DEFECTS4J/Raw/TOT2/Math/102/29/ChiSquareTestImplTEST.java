package org.apache.commons.math.stat.inference;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChiSquareTestImplTest {

 private ChiSquareTestImpl chiSquareTest;
 private ChiSquaredDistribution distribution;

 @Test
 @DisplayName("TestchiSquareTestImpl constructor with default parameters")
 void testDefaultConstructor() {
 chiSquareTest = new ChiSquareTestImpl();
 assertNotNull(chiSquareTest.getDistribution());
 }

 @Test
 @DisplayName("TestchiSquareTestImpl constructor with custom ChiSquaredDistribution")
 void testCustomConstructor() {
 distribution = new ChiSquaredDistributionImpl(3.0);
 chiSquareTest = new ChiSquareTestImpl(distribution);
 assertSame(distribution, chiSquareTest.getDistribution());
 }

 @Test
 @DisplayName("TestchiSquare method with positive and non-negative inputs and sum equals")
 void testChiSquarePositiveAndNonNegative() {
 long[] observed = {1, 2, 3, 4, 5};
 double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
 double result = chiSquareTest.chiSquare(expected, observed);
 assertEquals(5.991464970288479, result, 0.001);
 }

 @Test
 @DisplayName("TestchiSquare method with positive and non-negative inputs and sum not equal")
 void testChiSquarePositiveAndNonNegativeWithUnequalSum() {
 long[] observed = {1, 2, 3, 4, 5};
 double[] expected = {3.0, 1.0, 1.0, 2.0, 2.0};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
 }

 @Test
 @DisplayName("TestchiSquare method with negative inputs")
 void testChiSquareNegativeInputs() {
 long[] observed = {-1, 2, 3, 4, 5};
 double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
 }

 @Test
 @DisplayName("TestchiSquare method with non-positive inputs")
 void testChiSquareNonPositiveInputs() {
 long[] observed = {1, -2, 3, 4, 5};
 double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquare(expected, observed));
 }

 @Test
 @DisplayName("TestchiSquareTest method with correct degrees of freedom")
 void testChiSquareTestCorrectDegreesOfFreedom() {
 long[] observed = {1, 2, 3, 4, 5};
 double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
 double pValue = chiSquareTest.chiSquareTest(expected, observed);
 assertEquals(0.02485414232148833, pValue, 0.001);
 }

 @Test
 @DisplayName("TestchiSquareTest method with incorrect degrees of freedom")
 void testChiSquareTestIncorrectDegreesOfFreedom() {
 long[] observed = {1, 2, 3, 4, 5};
 double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed, 10.0));
 }

 @Test
 @DisplayName("TestchiSquareTest method with incorrect p-value")
 void testChiSquareTestIncorrectPValue() {
 long[] observed = {1, 2, 3, 4, 5};
 double[] expected = {2.5, 2.5, 2.5, 2.5, 2.5};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed, 1.1));
 }

 @Test
 @DisplayName("TestchiSquareTest method with incorrect inputs")
 void testChiSquareTestIncorrectInputs() {
 long[] observed = {1, 2};
 double[] expected = {2.5, 2.5};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(expected, observed));
 }

 @Test
 @DisplayName("TestchiSquareDataSetsComparison method with correct inputs")
 void testChiSquareDataSetsComparisonCorrectInputs() {
 long[] observed1 = {1, 2, 3, 4, 5};
 long[] observed2 = {2, 2, 3, 4, 5};
 double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
 assertEquals(0.2238152794878783, result, 0.001);
 }

 @Test
 @DisplayName("TestchiSquareDataSetsComparison method with incorrect lengths")
 void testChiSquareDataSetsComparisonIncorrectLengths() {
 long[] observed1 = {1, 2, 3, 4, 5};
 long[] observed2 = {1, 2};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareDataSetsComparison(observed1, observed2));
 }

 @Test
 @DisplayName("TestchiSquareDataSetsComparison method with both arrays empty")
 void testChiSquareDataSetsComparisonBothArraysEmpty() {
 long[] observed1 = {};
 long[] observed2 = {};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareDataSetsComparison(observed1, observed2));
 }

 @Test
 @DisplayName("TestchiSquareDataSetsComparison method with both arrays null")
 void testChiSquareDataSetsComparisonBothArraysNull() {
 long[] observed1 = null;
 long[] observed2 = null;
 assertThrows(NullPointerException.class, () -> chiSquareTest.chiSquareDataSetsComparison(observed1, observed2));
 }

 @Test
 @DisplayName("TestchiSquareTestDataSetsComparison method with correct p-value")
 void testChiSquareTestDataSetsComparisonCorrectPValue() {
 long[] observed1 = {1, 2, 3, 4, 5};
 long[] observed2 = {2, 2, 3, 4, 5};
 double alpha = 0.05;
 boolean expectedResult = false;
 boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
 assertFalse(result);
 }

 @Test
 @DisplayName("TestchiSquareTestDataSetsComparison method with incorrect p-value")
 void testChiSquareTestDataSetsComparisonIncorrectPValue() {
 long[] observed1 = {1, 2, 3, 4, 5};
 long[] observed2 = {2, 2, 3, 4, 5};
 double alpha = 1.1;
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha));
 }

 @Test
 @DisplayName("TestchiSquareTestDataSetsComparison method with correct inputs and correct p-value")
 void testChiSquareTestDataSetsComparisonCorrectInputsAndCorrectPValue() {
 long[] observed1 = {1, 2, 3, 4, 5};
 long[] observed2 = {2, 2, 3, 4, 5};
 double alpha = 0.05;
 boolean expectedResult = true;
 boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
 assertTrue(result);
 }

 @Test
 @DisplayName("TestchiSquareTestDataSetsComparison method with correct inputs and incorrect p-value")
 void testChiSquareTestDataSetsComparisonCorrectInputsAndIncorrectPValue() {
 long[] observed1 = {1, 2, 3, 4, 5};
 long[] observed2 = {2, 2, 3, 4, 5};
 double alpha = 0.1;
 boolean expectedResult = false;
 boolean result = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, alpha);
 assertFalse(result);
 }

 @Test
 @DisplayName("TestcheckArray method with correct 2-way table")
 void testCheckArrayCorrect2WayTable() {
 long[][] counts = {{1, 2}, {2, 3}};
 chiSquareTest.chiSquareTest(counts);
 }

 @Test
 @DisplayName("TestcheckArray method with incorrect 2-way table - different number of rows")
 void testCheckArrayIncorrect2WayTableDifferentNumberOfRows() {
 long[][] counts = {{1, 2}, {2, 3}, {3, 3}};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(counts));
 }

 @Test
 @DisplayName("TestcheckArray method with incorrect 2-way table - different number of columns")
 void testCheckArrayIncorrect2WayTableDifferentNumberOfColumns() {
 long[][] counts = {{1, 2}, {2, 3}, {3, 3}, {3, 4}};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(counts));
 }

 @Test
 @DisplayName("TestcheckArray method with incorrect 2-way table - negative entries")
 void testCheckArrayIncorrect2WayTableNegativeEntries() {
 long[][] counts = {{1, -2}, {2, 3}, {3, 3}, {3, 4}};
 assertThrows(IllegalArgumentException.class, () -> chiSquareTest.chiSquareTest(counts));
 }
}