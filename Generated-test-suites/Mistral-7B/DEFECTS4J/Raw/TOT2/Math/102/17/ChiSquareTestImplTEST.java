import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistributionImpl;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeException;
import org.apache.commons.math3.stat.inference.ChiSquareTestImpl;
import org.junit.Assert;
import org.junit.Test;

public class ChiSquareTestImplTest {

 private ChiSquareTestImpl chiSquareTest;
 private long[] observed1 = {1, 2, 3, 4, 5};
 private long[] observed2 = {2, 3, 4, 5, 1};
 private long[] expected = {2, 3, 1, 3, 2};
 private long[][] counts = {{1, 2, 3, 4, 5}, {2, 3, 4, 5, 1}};

 @Test
 public void testChiSquare() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 Assert.assertEquals(1.0, chiSquareTest.chiSquare(expected, observed), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareNullArrays() {
 chiSquareTest = new ChiSquareTestImpl();
 chiSquareTest.chiSquare(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareInvalidLengths() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] invalidObs = {1, 2, 3};
 long[] invalidExp = {1, 2};
 chiSquareTest.chiSquare(invalidExp, invalidObs);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareNegativeExpected() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] negativeExp = {-1, 2, 1};
 long[] observed = {1, 2, 3};
 chiSquareTest.chiSquare(negativeExp, observed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareNegativeObserved() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] negativeObs = {1, -2, 3};
 long[] expected = {2, 3, 1, 3, 2};
 chiSquareTest.chiSquare(expected, negativeObs);
 }

 @Test
 public void testChiSquareTest() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 Assert.assertEquals(0.95, chiSquareTest.chiSquareTest(expected, observed), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestNullArrays() {
 chiSquareTest = new ChiSquareTestImpl();
 chiSquareTest.chiSquareTest(null, null);
 }

 @Test(expected = MathRuntimeException.class)
 public void testChiSquareTestInvalidDegreesOfFreedom() {
 chiSquareTest = new ChiSquareTestImpl();
 ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(2.5);
 chiSquareTest.setDistribution(dist);
 chiSquareTest.chiSquareTest(expected, observed);
 }

 @Test
 public void testChiSquareTestSmallSample() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] smallSample = {{1, 1, 2, 2}, {1, 2, 2, 1}};
 Assert.assertEquals(0.5, chiSquareTest.chiSquareTest(smallSample), 0.01);
 }

 @Test
 public void testChiSquareTestLargeSample() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] largeSample = {{10000, 9998, 10012, 9988, 10011},
 {9998, 10012, 9988, 10011, 10000}};
 Assert.assertEquals(0.05, chiSquareTest.chiSquareTest(largeSample, 0.05), 0.01);
 }

 @Test
 public void testChiSquareTestLargeSampleAlternativeHypothesis() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] largeSample = {{10000, 9998, 10012, 9988, 10011},
 {9998, 10012, 9988, 10011, 10000}};
 Assert.assertFalse(chiSquareTest.chiSquareTest(largeSample, 0.1));
 }

 @Test
 public void testChiSquareDataSetsComparison() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 Assert.assertEquals(3.0, chiSquareTest.chiSquareDataSetsComparison(observed1, observed2), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareDataSetsComparisonInvalidLengths() {
 chiSquareTest = new ChiSquareTestImpl();
 long[] invalidObs1 = {1, 2, 3};
 long[] invalidObs2 = {2, 3};
 chiSquareTest.chiSquareDataSetsComparison(invalidObs1, invalidObs2);
 }

 @Test
 public void testChiSquareTestDataSetsComparison() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 Assert.assertEquals(0.95, chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChiSquareTestDataSetsComparisonInvalidLengths() {
 chiSquareTest = new ChiSquareTestImpl();
 chiSquareTest.chiSquareTestDataSetsComparison(null, null);
 }

 @Test(expected = MathRuntimeException.class)
 public void testChiSquareTestDataSetsComparisonInvalidDegreesOfFreedom() {
 chiSquareTest = new ChiSquareTestImpl();
 ChiSquaredDistribution dist = new ChiSquaredDistributionImpl(2.5);
 chiSquareTest.setDistribution(dist);
 chiSquareTest.chiSquareTestDataSetsComparison(observed1, observed2);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonSmallSample() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] smallSample = {{1, 1, 2, 2}, {1, 2, 2, 1}};
 Assert.assertEquals(0.5, chiSquareTest.chiSquareTestDataSetsComparison(smallSample), 0.01);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonLargeSample() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] largeSample = {{10000, 9998, 10012, 9988, 10011},
 {9998, 10012, 9988, 10011, 10000}};
 Assert.assertEquals(0.05, chiSquareTest.chiSquareTestDataSetsComparison(largeSample, 0.05), 0.01);
 }

 @Test
 public void testChiSquareTestDataSetsComparisonLargeSampleAlternativeHypothesis() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] largeSample = {{10000, 9998, 10012, 9988, 10011},
 {9998, 10012, 9988, 10011, 10000}};
 Assert.assertFalse(chiSquareTest.chiSquareTestDataSetsComparison(largeSample, 0.1));
 }

 @Test
 public void testChiSquareTestWithCounts() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 Assert.assertEquals(3.0, chiSquareTest.chiSquare(counts), 0.01);
 }

 @Test
 public void testChiSquareTestWithCountsSmallSample() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] smallSample = {{1, 1, 2, 2}, {1, 2, 2, 1}};
 Assert.assertEquals(0.5, chiSquareTest.chiSquareTest(smallSample), 0.01);
 }

 @Test
 public void testChiSquareTestWithCountsLargeSample() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] largeSample = {{10000, 9998, 10012, 9988, 10011},
 {9998, 10012, 9988, 10011, 10000}};
 Assert.assertEquals(0.05, chiSquareTest.chiSquareTest(largeSample), 0.01);
 }

 @Test
 public void testChiSquareTestWithCountsLargeSampleAlternativeHypothesis() throws MathIllegalArgumentException, MathRuntimeException {
 chiSquareTest = new ChiSquareTestImpl();
 long[][] largeSample = {{10000, 9998, 10012, 9988, 10011},
 {9998, 10012, 9988, 10011, 10000}};
 Assert.assertFalse(chiSquareTest.chiSquareTest(largeSample, 0.1));
 }
}