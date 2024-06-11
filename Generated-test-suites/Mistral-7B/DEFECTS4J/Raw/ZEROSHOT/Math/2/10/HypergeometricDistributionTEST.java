import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistribution() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 assertEquals("Population size", 10, dist.getPopulationSize());
 assertEquals("Number of successes", 5, dist.getNumberOfSuccesses());
 assertEquals("Sample size", 5, dist.getSampleSize());

 int[] domain = dist.getDomain();
 assertEquals("Lower bound of domain", 0, domain[0]);
 assertEquals("Upper bound of domain", 5, domain[1]);
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, -3, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(0, 3, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroNumberOfSuccesses() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 0, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroSampleSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionNumberOfSuccessesGreaterThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 6, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionSampleSizeGreaterThanPopulationSize() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 6);
 }

 @Test
 public void testHypergeometricDistributionProbabilities() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

 double probZero = dist.probability(0);
 double probFive = dist.probability(5);
 double probSix = dist.probability(6);

 assertEquals("Probability of 0", 0.0527, probZero, 0.0001);
 assertEquals("Probability of 5", 0.3604, probFive, 0.0001);
 assertEquals("Probability of 6", 0.0059, probSix, 0.0001);
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbabilities() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

 double cumProbZero = dist.cumulativeProbability(0);
 double cumProbFive = dist.cumulativeProbability(5);
 double cumProbSix = dist.cumulativeProbability(6);

 assertEquals("Cumulative probability of 0", 0.0, cumProbZero, 0.0001);
 assertEquals("Cumulative probability of 5", 0.3604, cumProbFive, 0.0001);
 assertEquals("Cumulative probability of 6", 0.3663, cumProbSix, 0.0001);
 }

 @Test
 public void testHypergeometricDistributionUpperCumulativeProbabilities() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);

 double upperCumProbZero = dist.upperCumulativeProbability(0);
 double upperCumProbFive = dist.upperCumulativeProbability(5);
 double upperCumProbSix = dist.upperCumulativeProbability(6);

 assertEquals("Upper cumulative probability of 0", 1.0, upperCumProbZero, 0.0001);
 assertEquals("Upper cumulative probability of 5", 0.3604, upperCumProbFive, 0.0001);
 assertEquals("Upper cumulative probability of 6", 0.3541, upperCumProbSix, 0.0001);
 }

 @Test
 public void testHypergeometricDistributionMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 double mean = dist.getNumericalMean();
 assertEquals("Mean", 3.5, mean, 0.001);
 }

 @Test
 public void testHypergeometricDistributionVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 5);
 double variance = dist.getNumericalVariance();
 assertEquals("Variance", 1.744856, variance, 0.0001);
 }

}