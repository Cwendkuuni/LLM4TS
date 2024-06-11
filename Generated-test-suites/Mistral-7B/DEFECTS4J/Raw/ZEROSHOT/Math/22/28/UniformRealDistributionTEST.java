import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
 UniformRealDistribution uniform = new UniformRealDistribution();
 assertEquals(0, uniform.getSupportLowerBound(), 0.001);
 assertEquals(1, uniform.getSupportUpperBound(), 0.001);
 }

 @Test
 public void testConstructorWithLowerAndUpperBounds() {
 UniformRealDistribution uniform = new UniformRealDistribution(0.1, 0.3, 0.01);
 assertEquals(0.1, uniform.getSupportLowerBound(), 0.001);
 assertEquals(0.3, uniform.getSupportUpperBound(), 0.001);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithInvalidBounds() {
 UniformRealDistribution uniform = new UniformRealDistribution(1.0, 1.0, 0.01);
 }

 @Test
 public void testDensity() {
 double x = 0.2;
 assertEquals(1 / (1 - 0), distribution.density(x), 0.01);
 }

 @Test
 public void testCumulativeProbability() {
 double x = 0.3;
 assertEquals(0.3, distribution.cumulativeProbability(x), 0.01);
 }

 @Test
 public void testGetSolverAbsoluteAccuracy() {
 assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.001);
 }

 @Test
 public void testGetNumericalMean() {
 assertEquals(0.5, distribution.getNumericalMean(), 0.01);
 }

 @Test
 public void testGetNumericalVariance() {
 assertEquals(0.08333333333333333, distribution.getNumericalVariance(), 0.01);
 }

 @Test
 public void testGetSupportLowerBound() {
 assertEquals(0, distribution.getSupportLowerBound(), 0.001);
 }

 @Test
 public void testGetSupportUpperBound() {
 assertEquals(1, distribution.getSupportUpperBound(), 0.001);
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
 double sample = distribution.sample();
 assertTrue(sample >= 0 && sample <= 1);
 }
}