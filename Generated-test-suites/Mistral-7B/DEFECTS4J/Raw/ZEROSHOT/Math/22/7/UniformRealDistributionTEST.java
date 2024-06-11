import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

 private UniformRealDistribution distribution;
 private Well19937c random;

 @Before
 public void setUp() {
 random = new Well19937c();
 distribution = new UniformRealDistribution(random, 0, 1);
 }

 @Test
 public void testDefaultConstructor() {
 distribution = new UniformRealDistribution();
 assertEquals(0, distribution.getLowerBound(), 0.0);
 assertEquals(1, distribution.getUpperBound(), 0.0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithInvalidBounds() {
 distribution = new UniformRealDistribution(random, 1, 0);
 }

 @Test
 public void testGetDensity() {
 // Test with valid values
 double value = 0.5;
 assertEquals(2, distribution.density(value), 0.0001);

 // Test with lower bound
 value = distribution.getLowerBound();
 assertEquals(1, distribution.density(value), 0.0001);

 // Test with upper bound
 value = distribution.getUpperBound();
 assertEquals(0, distribution.density(value), 0.0001);
 }

 @Test
 public void testGetCumulativeProbability() {
 // Test with valid values
 double value = 0.5;
 assertEquals(0.5, distribution.cumulativeProbability(value), 0.0001);

 // Test with lower bound
 value = distribution.getLowerBound();
 assertEquals(0.0, distribution.cumulativeProbability(value), 0.0001);

 // Test with upper bound
 value = distribution.getUpperBound();
 assertEquals(1.0, distribution.cumulativeProbability(value), 0.0001);
 }

 @Test
 public void testGetSolverAbsoluteAccuracy() {
 assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,
 distribution.getSolverAbsoluteAccuracy(), 0.0001);
 }

 @Test
 public void testGetNumericalMean() {
 assertEquals(0.5, distribution.getNumericalMean(), 0.0001);
 }

 @Test
 public void testGetNumericalVariance() {
 assertEquals(0.0004166666666666667, distribution.getNumericalVariance(),
 0.000001);
 }

 @Test
 public void testGetSupportLowerBound() {
 assertEquals(0, distribution.getSupportLowerBound(), 0.0);
 }

 @Test
 public void testGetSupportUpperBound() {
 assertEquals(1, distribution.getSupportUpperBound(), 0.0);
 }

 @Test
 public void testIsSupportLowerBoundInclusive() {
 assertTrue(distribution.isSupportLowerBoundInclusive());
 }

 @Test
 public void testIsSupportUpperBoundInclusive() {
 assertFalse(distribution.isSupportUpperBoundInclusive());
 }

 @Test
 public void testIsSupportConnected() {
 assertTrue(distribution.isSupportConnected());
 }

 @Test
 public void testSample() {
 // Test with default random
 double sample = distribution.sample();
 assertEquals(0.5, sample, 0.01);

 // Test with given random
 distribution = new UniformRealDistribution(random, 1, 2);
 sample = distribution.sample();
 assertTrue(sample >= 1.0);
 assertTrue(sample <= 2.0);
 }
}