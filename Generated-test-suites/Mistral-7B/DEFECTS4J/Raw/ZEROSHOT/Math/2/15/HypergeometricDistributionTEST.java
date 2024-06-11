import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testConstructorWithNegativeNumberOfSuccesses() {
 try {
 new HypergeometricDistribution(10, -1, 5);
 } catch (NotPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroPopulationSize() {
 try {
 new HypergeometricDistribution(10, 5, 0);
 } catch (NotStrictlyPositiveException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithNumberOfSuccessesGreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(10, 11, 5);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithSampleSizeGreaterThanPopulationSize() {
 try {
 new HypergeometricDistribution(10, 5, 11);
 } catch (NumberIsTooLargeException e) {
 // expected exception
 }
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(5, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.0, dist.cumulativeProbability(0), 1e-12);
 }

 @Test
 public void testCumulativeProbabilitySuccess() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.2734375, dist.cumulativeProbability(2), 1e-12);
 }

 @Test
 public void testCumulativeProbabilityFailure() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.7265625, dist.cumulativeProbability(3), 1e-12);
 }

 @Test
 public void testProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.0, dist.probability(0), 1e-12);
 }

 @Test
 public void testProbabilitySuccess() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.0453125, dist.probability(1), 1e-12);
 }

 @Test
 public void testProbabilityFailure() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.1388889, dist.probability(2), 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityZero() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(1.0, dist.upperCumulativeProbability(10), 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilitySuccess() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.0, dist.upperCumulativeProbability(1), 1e-12);
 }

 @Test
 public void testUpperCumulativeProbabilityFailure() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(0.9546875, dist.upperCumulativeProbability(4), 1e-12);
 }

 @Test
 public void testGetNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(3.5, dist.getNumericalMean(), 1e-12);
 }

 @Test
 public void testGetNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(1.1875, dist.getNumericalVariance(), 1e-12);
 }

 @Test
 public void testGetNumericalVarianceCalculated() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertTrue(dist.numericalVarianceIsCalculated);
 }

 @Test
 public void testGetNumericalVarianceNotCalculated() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertFalse(dist.numericalVarianceIsCalculated);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(3, dist.getSupportLowerBound());
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(5, dist.getSupportUpperBound());
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertTrue(dist.isSupportConnected());
 }
}