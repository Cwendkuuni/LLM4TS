import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

 private UniformRealDistribution dist;
 private Well19937c rng;

 @Before
 public void setUp() {
 rng = new Well19937c();
 dist = new UniformRealDistribution(rng, 0.0, 1.0);
 }

 @Test
 public void testConstructorWithDefaultBounds() {
 UniformRealDistribution urd = new UniformRealDistribution();
 assertEquals(0.0, urd.getLowerBound(), 0.001);
 assertEquals(1.0, urd.getUpperBound(), 0.001);
 }

 @Test
 public void testConstructorWithBounds() {
 UniformRealDistribution urd = new UniformRealDistribution(0.5, 1.5);
 assertEquals(0.5, urd.getLowerBound(), 0.001);
 assertEquals(1.5, urd.getUpperBound(), 0.001);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorWithInvalidBounds() {
 UniformRealDistribution urd;
 urd = new UniformRealDistribution(2.0, 1.0);
 }

 @Test
 public void testDensity() {
 double x = 0.45;
 assertEquals(1.0 / (1.0 - 0.0), dist.density(x), 0.01);
 }

 @Test
 public void testCumulativeProbability() {
 double x = 0.45;
 assertEquals(0.45 / (1.0 - 0.0) + 0.0, dist.cumulativeProbability(x), 0.01);
 }

 @Test
 public void testGetSolverAbsoluteAccuracy() {
 assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, dist.getSolverAbsoluteAccuracy(), 0.001);
 }

 @Test
 public void testGetNumericalMean() {
 assertEquals(0.5, dist.getNumericalMean(), 0.01);
 }

 @Test
 public void testGetNumericalVariance() {
 double ul = dist.getUpperBound() - dist.getLowerBound();
 assertEquals(ul * ul / 12, dist.getNumericalVariance(), 0.01);
 }

 @Test
 public void testGetSupportLowerBound() {
 assertEquals(0.0, dist.getSupportLowerBound(), 0.001);
 }

 @Test
 public void testGetSupportUpperBound() {
 assertEquals(1.0, dist.getSupportUpperBound(), 0.001);
 }

 @Test
 public void testIsSupportLowerBoundInclusive() {
 assertTrue(dist.isSupportLowerBoundInclusive());
 }

 @Test
 public void testIsSupportUpperBoundInclusive() {
 assertTrue(dist.isSupportConnected());
 assertTrue(dist.isSupportUpperBoundInclusive());
 }

 @Test
 public void testSample() {
 double sample = dist.sample();
 assertTrue(sample >= dist.getLowerBound() && sample < dist.getUpperBound());
 }

 @Test
 public void testSampleMultipleTimes() {
 int count = 10;
 double[] samples = new double[count];
 for (int i = 0; i < count; ++i) {
 samples[i] = dist.sample();
 }
 for (int i = 0; i < count - 1; ++i) {
 assertTrue(samples[i] < samples[i + 1]);
 }
 }
}