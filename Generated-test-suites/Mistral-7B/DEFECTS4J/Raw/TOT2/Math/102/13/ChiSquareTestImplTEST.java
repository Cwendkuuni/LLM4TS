package org.apache.commons.math.stat.inference;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.distribution.ChiSquaredDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChiSquareTestImplTest {

 private ChiSquareTestImpl chiSquareTest;

 @Test
 public void testConstructorWithDefaultDistribution() {
 chiSquareTest = new ChiSquareTestImpl();
 assertNotNull(chiSquareTest.getDistribution());
 }

 @Test
 public void testConstructorWithCustomDistribution() {
 ChiSquaredDistributionImpl customDistribution = new ChiSquaredDistributionImpl(5.0);
 chiSquareTest = new ChiSquareTestImpl(customDistribution);
 assertSame(customDistribution, chiSquareTest.getDistribution());
 }

 @Test
 public void testChiSquareMethodWithValidInputs() {
 double[] expected = new double[]{1.0, 2.0, 3.0};
 long[] observed = new long[]{100, 200, 300};
 chiSquareTest = new ChiSquareTestImpl();
 double result = chiSquareTest.chiSquare(expected, observed);
 assertEquals(7.811339140568921, result, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareMethodWithInvalidInputsNullExpectedArray() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquare(null, new long[]{1, 1, 1});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareMethodWithInvalidInputsNullObservedArray() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquare(new double[]{1.0, 2.0, 3.0}, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareMethodWithInvalidInputsInvalidExpectedArrayLength() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquare(new double[]{1.0}, new long[]{1, 2, 3});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareMethodWithInvalidInputsInvalidObservedArrayLength() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquare(new double[]{1.0, 2.0, 3.0}, new long[]{1, 1, 1, 1});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareMethodWithInvalidInputsInvalidInputsLengthMismatch() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquare(new double[]{1.0, 2.0}, new long[]{1, 2, 3});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testChiSquareMethodWithInvalidInputsNegativeExpected() {
 chiSquareTest = new ChiSquareTestImpl();
 double[] negativeExpected = new double[]{-1.0, 1.0};
 long[] observed = new long[]{1, 1};
 try {
 chiSquareTest.chiSquare(negativeExpected, observed);
 fail("Expected MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testChiSquareMethodWithInvalidInputsNegativeObserved() {
 chiSquareTest = new ChiSquareTestImpl();
 double[] expected = new double[]{1.0, 1.0};
 long[] negativeObserved = new long[]{-1, 1};
 try {
 chiSquareTest.chiSquare(expected, negativeObserved);
 fail("Expected MathIllegalArgumentException");
 } catch (MathIllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testChiSquareTestMethodWithValidInputs() {
 double[] expected = new double[]{1.0, 2.0, 3.0};
 long[] observed = new long[]{100, 200, 300};
 double significanceLevel = 0.05;
 chiSquareTest = new ChiSquareTestImpl();
 double pValue = chiSquareTest.chiSquareTest(expected, observed);
 assertTrue(pValue < significanceLevel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestMethodWithInvalidInputsNullExpectedArray() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquareTest(null, new long[]{1, 1, 1});
 fail("ExpectedIllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestMethodWithInvalidInputsNullObservedArray() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquareTest(new double[]{1.0, 2.0, 3.0}, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestMethodWithInvalidInputsInvalidExpectedArrayLength() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquareTest(new double[]{1.0}, new long[]{1, 2, 3});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestMethodWithInvalidInputsInvalidObservedArrayLength() {
 chiSquareTest = new ChiSquareTestImpl();
 try {
 chiSquareTest.chiSquareTest(new double[]{1.0, 2.0, 3.0}, new long[]{1, 1, 1, 1});
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testChiSquareTestMethodWithValidInputsAndSignificanceLevel() {
 double[] expected = new double[]{1.0, 2.0, 3.0};
 long[] observed = new long[]{100, 200, 300};
 double significanceLevel = 0.05;
 chiSquareTest = new ChiSquareTestImpl();
 boolean rejection = chiSquareTest.chiSquareTest(expected, observed, significanceLevel);
 assertTrue(rejection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestMethodWithInvalidInputsAndSignificanceLevelZero() {
 chiSquareTest = new ChiSquareTestImpl();
 double[] expected = new double[]{1.0, 2.0, 3.0};
 long[] observed = new long[]{100, 200, 300};
 try {
 chiSquareTest.chiSquareTest(expected, observed, 0.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestMethodWithInvalidInputsAndSignificanceLevelGreaterThan05() {
 chiSquareTest = new ChiSquareTestImpl();
 double[] expected = new double[]{1.0, 2.0, 3.0};
 long[] observed = new long[]{100, 200, 300};
 try {
 chiSquareTest.chiSquareTest(expected, observed, 0.6);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testChiSquareComparisonMethodsWithValidInputs() {
 long[] observed1 = new long[]{1, 2, 3};
 long[] observed2 = new long[]{2, 2, 3};
 chiSquareTest = new ChiSquareTestImpl();
 double result = chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
 assertEquals(2.0, result, 0.001);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonMethodWithValidInputs() {
 long[] observed1 = new long[]{1, 2, 3};
 long[] observed2 = new long[]{2, 2, 3};
 double significanceLevel = 0.05;
 chiSquareTest = new ChiSquareTestImpl();
 double pValue = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
 assertTrue(pValue < significanceLevel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareDataSetsComparisonMethodWithInvalidInputsNullObserved1() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] observed2 = new long[]{2, 2, 3};
 try {
 chiSquareTest.chiSquareDataSetsComparison(null, observed2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareDataSetsComparisonMethodWithInvalidInputsNullObserved2() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] observed1 = new long[]{1, 2, 3};
 try {
 chiSquareTest.chiSquareDataSetsComparison(observed1, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareDataSetsComparisonMethodWithInvalidInputsInvalidLength() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] observed1 = new long[]{1, 2};
 long[] observed2 = new long[]{3, 2, 1};
 try {
 chiSquareTest.chiSquareDataSetsComparison(observed1, observed2);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testChiSquareTestDataSetsComparisonMethodWithValidInputsAndSignificanceLevel() {
 long[] observed1 = new long[]{1, 2, 3};
 long[] observed2 = new long[]{2, 2, 3};
 double significanceLevel = 0.05;
 chiSquareTest = new ChiSquareTestImpl();
 boolean rejection = chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, significanceLevel);
 assertTrue(rejection);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestDataSetsComparisonMethodWithInvalidInputsAndSignificanceLevelZero() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] observed1 = new long[]{1, 2, 3};
 long[] observed2 = new long[]{2, 2, 3};
 try {
 chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestDataSetsComparisonMethodWithInvalidInputsAndSignificanceLevelGreaterThan05() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] observed1 = new long[]{1, 2, 3};
 long[] observed2 = new long[]{2, 2, 3};
 try {
 chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2, 0.6);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }
}