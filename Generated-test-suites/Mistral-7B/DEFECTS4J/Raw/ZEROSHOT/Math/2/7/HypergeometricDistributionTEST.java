import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test
 public void testConstructors() {
 HypergeometricDistribution dist;

 // Test with valid arguments
 dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals(10, dist.getPopulationSize());
 assertEquals(5, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getSampleSize());

 // Test with invalid arguments - populationSize <= 0
 try {
 new HypergeometricDistribution(-1, 5, 5);
 assert false;
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 try {
 new HypergeometricDistribution(0, 5, 5);
 assert false;
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }

 // Test with invalid arguments - numberOfSuccesses < 0
 try {
 new HypergeometricDistribution(10, -1, 5);
 assert false;
 } catch (NotPositiveException e) {
 // Expected
 }

 // Test with invalid arguments - sampleSize <= 0
 try {
 new HypergeometricDistribution(10, 5, -1);
 assert false;
 } catch (NotPositiveException e) {
 // Expected
 }

 // Test with invalid arguments - numberOfSuccesses > populationSize
 try {
 new HypergeometricDistribution(10, 11, 5);
 assert false;
 } catch (NumberIsTooLargeException e) {
 // Expected
 }

 // Test with invalid arguments - sampleSize > populationSize
 try {
 new HypergeometricDistribution(10, 5, 11);
 assert false;
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 double prob;

 // Test lower bound
 prob = dist.cumulativeProbability(0);
 assertEquals(0.0, prob, 1e-14);

 // Test point value
 prob = dist.cumulativeProbability(2);
 assertEquals(0.0625, prob, 1e-14);

 // Test upper bound
 prob = dist.cumulativeProbability(5);
 assertEquals(1.0, prob, 1e-14);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 double prob;

 // Test lower bound
 prob = dist.probability(0);
 assertEquals(0.0, prob, 1e-14);

 // Test point value
 prob = dist.probability(2);
 assertEquals(0.0625, prob, 1e-14);

 // Test upper bound
 prob = dist.probability(5);
 assertEquals(0.0625, prob, 1e-14);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 double prob;

 // Test lower bound
 prob = dist.upperCumulativeProbability(0);
 assertEquals(1.0, prob, 1e-14);

 // Test point value
 prob = dist.upperCumulativeProbability(2);
 assertEquals(0.9375, prob, 1e-14);

 // Test upper bound
 prob = dist.upperCumulativeProbability(5);
 assertEquals(0.0, prob, 1e-14);
 }

 @Test
 public void testMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 double mean = dist.getNumericalMean();
 assertEquals(3.75, mean, 1e-14);
 }

 @Test
 public void testVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 double var = dist.getNumericalVariance();
 assertEquals(1.1875, var, 1e-14);
 }

 @Test
 public void testSupportLowerBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 int lowerBound = dist.getSupportLowerBound();
 assertEquals(0, lowerBound);
 }

 @Test
 public void testSupportUpperBound() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 int upperBound = dist.getSupportUpperBound();
 assertEquals(5, upperBound);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 boolean isConnected = dist.isSupportConnected();
 assertTrue(isConnected);
 }

 @Test
 public void testGenerateValues() {
 HypergeometricDistribution dist = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 int sampleSize = 10000;
 int[] values = new int[sampleSize];
 for (int i = 0; i < sampleSize; i++) {
 values[i] = dist.nextInt();
 }
 int[] domain = dist.getDomain();
 int lowerBound = domain[0];
 int upperBound = domain[1];
 int sum = 0;
 int count = 0;
 for (int value : values) {
 if (value >= lowerBound && value <= upperBound) {
 sum += value;
 count++;
 }
 }
 double mean = (double) sum / sampleSize;
 double variance = 0.0;
 for (int value : values) {
 if (value >= lowerBound && value <= upperBound) {
 double deviation = (double) value - mean;
 variance += deviation * deviation;
 }
 }
 variance /= sampleSize;
 assertEquals(3.75, mean, 1e-6);
 assertEquals(1.1875, variance, 1e-6);
 }
}