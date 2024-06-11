import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UniformRealDistributionTest {

 private UniformRealDistribution dist;
 private RandomGenerator rng;

 @Before
 public void setUp() {
 rng = new Well19937c();
 dist = new UniformRealDistribution(rng, 0, 1);
 }

 @Test
 public void testUniformRealDistributionConstruction() {
 UniformRealDistribution dist1 = new UniformRealDistribution();
 UniformRealDistribution dist2 = new UniformRealDistribution(0.5, 1.5);
 UniformRealDistribution dist3 = new UniformRealDistribution(0.1, 0.9, 0.001);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testUniformRealDistributionInvalidConstruction() {
 new UniformRealDistribution(0.5, 0.5);
 }

 @Test
 public void testDensity() {
 for (double x = this.dist.getSupportLowerBound(); x < this.dist.getSupportUpperBound(); x++) {
 double density = this.dist.density(x);
 assertEquals("Incorrect density: Expected: 0.00952381, Actual: " + density, 0.00952381, density, 0.000001);
 }
 }

 @Test
 public void testCumulativeProbability() {
 for (double x = this.dist.getSupportLowerBound(); x <= this.dist.getSupportUpperBound(); x++) {
 double cumProb = this.dist.cumulativeProbability(x);
 assertEquals("Incorrect cumulative probability for x = " + x + ", Expected: " + cumProb, cumProb, x / (this.dist.getSupportUpperBound() - this.dist.getSupportLowerBound()), 0.000001);
 }
 }

 @Test
 public void testInverseCumulativeProbability() {
 for (double p = 0; p <= 1; p += 0.01) {
 double x = this.dist.inverseCumulativeProbability(p);
 assertEquals("Incorrect inverse cumulative probability for p = " + p + ", Expected: " + x, x, p, 0.000001);
 }
 }

 @Test
 public void testSample() {
 for (int i = 0; i < 1000; i++) {
 double sample = this.dist.sample();
 assertEquals("Incorrect sample: Expected: " + this.dist.getSupportLowerBound() + " to " + this.dist.getSupportUpperBound(), sample, this.dist.getSupportLowerBound() + ((this.dist.getSupportUpperBound() - this.dist.getSupportLowerBound()) * Math.random()), 0.0001);
 }
 }

 @Test
 public void testGetNumericalMean() {
 double numericalMean = this.dist.getNumericalMean();
 assertEquals("Incorrect numerical mean: Expected: 0.5, Actual: " + numericalMean, 0.5, numericalMean, 0.0001);
 }

 @Test
 public void testGetNumericalVariance() {
 double numericalVariance = this.dist.getNumericalVariance();
 assertEquals("Incorrect numerical variance: Expected: 0.0025, Actual: " + numericalVariance, 0.0025, numericalVariance, 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
 double supportLowerBound = this.dist.getSupportLowerBound();
 assertEquals("Incorrect support lower bound: Expected: 0.0, Actual: " + supportLowerBound, 0.0, supportLowerBound, 0.0001);
 }

 @Test
 public void testGetSupportUpperBound() {
 double supportUpperBound = this.dist.getSupportUpperBound();
 assertEquals("Incorrect support upper bound: Expected: 1.0, Actual: " + supportUpperBound, 1.0, supportUpperBound, 0.0001);
 }

 @Test
 public void testIsSupportLowerBoundInclusive() {
 boolean isSupportLowerBoundInclusive = this.dist.isSupportLowerBoundInclusive();
 assertEquals("Incorrect support lower bound inclusivity: Expected: true, Actual: " + isSupportLowerBoundInclusive, true, isSupportLowerBoundInclusive);
 }

 @Test
 public void testIsSupportUpperBoundInclusive() {
 boolean isSupportUpperBoundInclusive = this.dist.isSupportUpperBoundInclusive();
 assertEquals("Incorrect support upper bound inclusivity: Expected: false, Actual: " + isSupportUpperBoundInclusive, false, isSupportUpperBoundInclusive);
 }

 @Test
 public void testIsSupportConnected() {
 boolean isSupportConnected = this.dist.isSupportConnected();
 assertEquals("Incorrect support connectedness: Expected: true, Actual: " + isSupportConnected, true, isSupportConnected);
 }
}