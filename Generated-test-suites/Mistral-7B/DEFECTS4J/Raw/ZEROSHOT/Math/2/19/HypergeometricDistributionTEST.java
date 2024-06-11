import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HypergeometricDistributionTest {

 @Test
 public void testConstruction() {
 HypergeometricDistribution dist;

 // Test valid construction
 dist = new HypergeometricDistribution(5, 3, 3);
 assertEquals(3, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getPopulationSize());
 assertEquals(3, dist.getSampleSize());

 // Test invalid construction with negative number of successes
 assertThrows(NotPositiveException.class, () -> {
 dist = new HypergeometricDistribution(5, -1, 3);
 });

 // Test invalid construction with non-positive population size
 assertThrows(NotStrictlyPositiveException.class, () -> {
 dist = new HypergeometricDistribution(5, 3, 0);
 });

 // Test invalid construction with non-positive sample size
 assertThrows(NotStrictlyPositiveException.class, () -> {
 dist = new HypergeometricDistribution(5, 3, -1);
 });

 // Test invalid construction with number of successes greater than population size
 assertThrows(NumberIsTooLargeException.class, () -> {
 dist = new HypergeometricDistribution(5, 6, 3);
 });

 // Test invalid construction with sample size greater than population size
 assertThrows(NumberIsTooLargeException.class, () -> {
 dist = new HypergeometricDistribution(5, 3, 6);
 });

 // Test valid construction with RandomGenerator
 dist = new HypergeometricDistribution(new Well19937c(), 5, 3, 3);
 assertEquals(3, dist.getNumberOfSuccesses());
 assertEquals(5, dist.getPopulationSize());
 assertEquals(3, dist.getSampleSize());
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 3);
 int[] values = { 0, 1, 2, 3, 4, 5 };
 double[] probabilities = { 0.0, 0.04761904761904762, 0.13529411764705886, 0.2741619872233571, 0.3527448796936483, 0.41428571428571426 };
 for (int i = 0; i < values.length; i++) {
 assertEquals(probabilities[i], dist.cumulativeProbability(values[i]), 0.00001);
 }
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 3);
 int[] values = { 0, 1, 2, 3, 4, 5 };
 double[] probabilities = { 0.015625, 0.04761904761904762, 0.0974871795286721, 0.13529411764705886, 0.13529411764705886, 0.13529411764705886 };
 for (int i = 0; i < values.length; i++) {
 assertEquals(probabilities[i], dist.probability(values[i]), 0.00001);
 }
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 3);
 int[] values = { 0, 1, 2, 3, 4, 5 };
 double[] probabilities = { 0.9843809523529412, 0.9323809523529413, 0.8712809523529413, 0.8001809523529412, 0.7290809523529413, 0.6584809523529413 };
 for (int i = 0; i < values.length; i++) {
 assertEquals(probabilities[i], dist.upperCumulativeProbability(values[i]), 0.00001);
 }
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 3);
 assertEquals(3.0, dist.getNumericalMean(), 0.00001);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution dist = new HypergeometricDistribution(5, 3, 3);
 assertEquals(0.6666666666666666, dist.getNumericalVariance(), 0.00001);
 }
}