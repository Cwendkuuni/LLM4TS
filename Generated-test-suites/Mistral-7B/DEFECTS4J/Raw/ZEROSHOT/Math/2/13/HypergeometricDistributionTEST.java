import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionConstructorNegativeNumberOfSuccesses() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(10, -2, 5);
 } catch (NotPositiveException e) {
 // expected
 }
 assertNull(dist);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionConstructorZeroPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(0, 2, 5);
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorNumberOfSuccessesGreaterThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(10, 11, 5);
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 assertNull(dist);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionConstructorSampleSizeGreaterThanPopulationSize() {
 HypergeometricDistribution dist = null;
 try {
 dist = new HypergeometricDistribution(10, 5, 11);
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 assertNull(dist);
 }

 @Test
 public void testHypergeometricDistributionConstructorValidParameters() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 assertNotNull(dist);
 assertEquals(10, dist.getPopulationSize());
 assertEquals(2, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getSampleSize());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCumulativeProbabilityNegativeIndex() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 dist.cumulativeProbability(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testProbabilityNegativeIndex() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 dist.probability(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testUpperCumulativeProbabilityNegativeIndex() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 dist.upperCumulativeProbability(-1);
 }

 @Test
 public void testCumulativeProbabilityValidIndex() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 double cdf = dist.cumulativeProbability(5);
 assertTrue(cdf > 0 && cdf < 1);
 }

 @Test
 public void testProbabilityValidIndex() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 double probability = dist.probability(2);
 assertTrue(probability > 0);
 }

 @Test
 public void testUpperCumulativeProbabilityValidIndex() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 double ucdf = dist.upperCumulativeProbability(5);
 assertTrue(ucdf < 1);
 }

 @Test
 public void testGetNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 assertEquals(2, dist.getNumberOfSuccesses());
 }

 @Test
 public void testGetPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 assertEquals(10, dist.getPopulationSize());
 }

 @Test
 public void testGetSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 assertEquals(5, dist.getSampleSize());
 }

 @Test(expected = IllegalStateException.class)
 public void testCalculateVarianceBeforeCalculation() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 double variance = dist.getNumericalVariance();
 }

 @Test
 public void testCalculateVarianceAfterCalculation() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 dist.getNumericalVariance();
 double variance = dist.getNumericalVariance();
 assertTrue(variance > 0);
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 int lowerBound = dist.getSupportLowerBound();
 assertEquals(3, lowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 int upperBound = dist.getSupportUpperBound();
 assertEquals(5, upperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 2, 5);
 assertTrue(dist.isSupportConnected());
 }
}