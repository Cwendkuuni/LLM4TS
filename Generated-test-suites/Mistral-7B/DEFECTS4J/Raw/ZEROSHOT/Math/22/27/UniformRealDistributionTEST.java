import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

 private UniformRealDistribution distribution;
 private RandomGenerator random;

 @Before
 public void setUp() throws NumberIsTooLargeException {
 random = new Well19937c();
 distribution = new UniformRealDistribution(random, 0, 1);
 }

 @Test
 public void testDefaultConstructor() {
 UniformRealDistribution uniform = new UniformRealDistribution();
 assertEquals(0.0, uniform.getLowerBound(), 0.0);
 assertEquals(1.0, uniform.getUpperBound(), 0.0);
 }

 @Test
 public void testConstructorWithLowerAndUpperBounds() throws NumberIsTooLargeException {
 UniformRealDistribution uniform = new UniformRealDistribution(0.5, 1.5);
 assertEquals(0.5, uniform.getLowerBound(), 0.0);
 assertEquals(1.5, uniform.getUpperBound(), 0.0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithInvalidBounds() throws NumberIsTooLargeException {
 UniformRealDistribution uniform = new UniformRealDistribution(2.0, 1.0);
 fail("NumberIsTooLargeException expected but not thrown");
 }

 @Test
 public void testDensity() {
 for (double x = 0.0; x <= 1.0; x += 0.01) {
 double density = distribution.density(x);
 double expectedDensity = 1.0 / (1.0 - 0.0);
 assertEquals("Unexpected density: x = " + x + ", expected: " + expectedDensity + ", but was: " + density, expectedDensity, density, 0.001);
 }
 }

 @Test
 public void testCumulativeProbability() {
 for (double x = 0.0; x <= 1.0; x += 0.01) {
 double cumulativeProbability = distribution.cumulativeProbability(x);
 double expectedCumulativeProbability = x;
 assertEquals("Unexpected cumulative probability: x = " + x + ", expected: " + expectedCumulativeProbability + ", but was: " + cumulativeProbability, expectedCumulativeProbability, cumulativeProbability, 0.001);
 }
 }

 @Test
 public void testGetSolverAbsoluteAccuracy() {
 assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
 }

 @Test
 public void testGetNumericalMean() {
 double numericalMean = distribution.getNumericalMean();
 double expectedNumericalMean = 0.5;
 assertEquals("Unexpected numerical mean: expected = " + expectedNumericalMean + ", but was: " + numericalMean, expectedNumericalMean, numericalMean, 0.001);
 }

 @Test
 public void testGetNumericalVariance() {
 double numericalVariance = distribution.getNumericalVariance();
 double expectedNumericalVariance = 0.0625;
 assertEquals("Unexpected numerical variance: expected = " + expectedNumericalVariance + ", but was: " + numericalVariance, expectedNumericalVariance, numericalVariance, 0.001);
 }

 @Test
 public void testGetSupportLowerBound() {
 assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
 }

 @Test
 public void testGetSupportUpperBound() {
 assertEquals(1.0, distribution.getSupportUpperBound(), 0.0);
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
 for (int i = 0; i < 1000; i++) {
 double sample = distribution.sample();
 assertTrue("Unexpected value: " + sample, sample >= 0.0 && sample < 1.0);
 }
 }
}