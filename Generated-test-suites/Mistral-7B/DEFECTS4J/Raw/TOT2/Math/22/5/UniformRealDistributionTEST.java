import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

 @Test
 public void testUniformRealDistributionDefaultConstructor() {
 UniformRealDistribution distribution = new UniformRealDistribution();
 assertEquals(0.0, distribution.getLowerBound(), 0.0);
 assertEquals(1.0, distribution.getUpperBound(), 0.0);
 }

 @Test
 public void testUniformRealDistributionCustomConstructor() {
 UniformRealDistribution distribution = new UniformRealDistribution(2.5, 5.5);
 assertEquals(2.5, distribution.getLowerBound(), 0.0);
 assertEquals(5.5, distribution.getUpperBound(), 0.0);
 }

 @Test
 public void testUniformRealDistributionCustomConstructorWithInvalidBounds() {
 try {
 UniformRealDistribution distribution = new UniformRealDistribution(5.5, 2.5);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 }

 @Test
 public void testUniformRealDistributionSample() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 Well19937c rng = new Well19937c();
 distribution.setSeed(rng);
 for (int i = 0; i < 100; i++) {
 double sample = distribution.sample();
 assertTrue(sample >= distribution.getLowerBound());
 assertTrue(sample < distribution.getUpperBound());
 }
 }

 @Test
 public void testUniformRealDistributionDensity() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 for (double x = 0.1; x <= 0.3; x += 0.01) {
 double density = distribution.density(x);
 double expectedDensity = 1 / (0.3 - 0.1);
 assertEquals(expectedDensity, density, 0.001);
 }
 }

 @Test
 public void testUniformRealDistributionCumulativeProbability() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 for (double x = 0.1; x <= 0.3; x += 0.01) {
 double cumProb = distribution.cumulativeProbability(x);
 double expectedCumProb = (x - 0.1) / (0.3 - 0.1);
 assertEquals(expectedCumProb, cumProb, 0.001);
 }
 }

 @Test
 public void testUniformRealDistributionGetNumericalMean() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 double mean = distribution.getNumericalMean();
 double expectedMean = 0.2;
 assertEquals(expectedMean, mean, 0.001);
 }

 @Test
 public void testUniformRealDistributionGetNumericalVariance() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 double variance = distribution.getNumericalVariance();
 double expectedVariance = (0.3 - 0.1) * (0.3 - 0.1) / 12;
 assertEquals(expectedVariance, variance, 0.001);
 }

 @Test
 public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 assertTrue(distribution.isSupportLowerBoundInclusive());
 }

 @Test
 public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 assertFalse(distribution.isSupportUpperBoundInclusive());
 }

 @Test
 public void testUniformRealDistributionIsSupportConnected() {
 UniformRealDistribution distribution = new UniformRealDistribution(0.1, 0.3);
 assertTrue(distribution.isSupportConnected());
 }
}