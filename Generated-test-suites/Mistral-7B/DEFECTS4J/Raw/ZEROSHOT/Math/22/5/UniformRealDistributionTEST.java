import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

 private UniformRealDistribution distribution;
 private RandomGenerator random;

 @Before
 public void setUp() {
 random = new Well19937c();
 distribution = new UniformRealDistribution(random, 0, 1);
 }

 @Test
 public void testDefaultConstructor() {
 UniformRealDistribution urd = new UniformRealDistribution();
 assertEquals(0, urd.getSupportLowerBound(), 0.001);
 assertEquals(1, urd.getSupportUpperBound(), 0.001);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testInvalidConstructor() throws NumberIsTooLargeException {
 new UniformRealDistribution(random, 1, 0);
 }

 @Test
 public void testSample() {
 for (int i = 0; i < 100; i++) {
 double value = distribution.sample();
 assertTrue(value >= 0);
 assertTrue(value < 1);
 }
 }

 @Test
 public void testDensity() {
 for (double x = 0.1; x < 1; x += 0.1) {
 double density = distribution.density(x);
 assertEquals(0.009999999999999998, density, 0.00000001);
 }
 }

 @Test
 public void testCumulativeProbability() {
 for (double x = 0; x <= 1; x += 0.1) {
 double cumProb = distribution.cumulativeProbability(x);
 if (x <= 0.5) {
 assertEquals(x / 1, cumProb, 0.001);
 } else {
 assertEquals(1 - (1 - x) / 1, cumProb, 0.001);
 }
 }
 }

 @Test
 public void testNumericalMean() {
 double mean = distribution.getNumericalMean();
 assertEquals(0.5, mean, 0.001);
 }

 @Test
 public void testNumericalVariance() {
 double variance = distribution.getNumericalVariance();
 assertEquals(0.0004545454545454545, variance, 0.0000001);
 }

 @Test
 public void testSupportBounds() {
 assertEquals(0, distribution.getSupportLowerBound(), 0.001);
 assertEquals(1, distribution.getSupportUpperBound(), 0.001);
 }

 @Test
 public void testSupportInclusive() {
 assertTrue(distribution.isSupportLowerBoundInclusive());
 assertFalse(distribution.isSupportUpperBoundInclusive());
 }

 @Test
 public void testSupportConnected() {
 assertTrue(distribution.isSupportConnected());
 }
}