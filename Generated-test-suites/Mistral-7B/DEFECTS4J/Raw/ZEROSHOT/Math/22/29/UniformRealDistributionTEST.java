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
 public void testConstructorWithInvalidArguments() {
 distribution = new UniformRealDistribution(random, 1, 0);
 }

 @Test
 public void testSample() {
 double sample = distribution.sample();
 assertEquals(sample, 0.5, 0.01);
 }

 @Test
 public void testDensity() {
 double x = 0.3;
 double density = distribution.density(x);
 assertEquals(density, 1.0 / (1.0 - 0.0), 0.01);
 }

 @Test
 public void testCumulativeProbability() {
 double x = 0.4;
 double cumulativeProbability = distribution.cumulativeProbability(x);
 assertEquals(cumulativeProbability, 0.4, 0.01);
 }

 @Test
 public void testGetNumericalMean() {
 double numericalMean = distribution.getNumericalMean();
 assertEquals(numericalMean, 0.5, 0.01);
 }

 @Test
 public void testGetNumericalVariance() {
 double numericalVariance = distribution.getNumericalVariance();
 assertEquals(numericalVariance, 0.0625, 0.0001);
 }

 @Test
 public void testGetSupportLowerBound() {
 double supportLowerBound = distribution.getSupportLowerBound();
 assertEquals(supportLowerBound, 0.0, 0.0);
 }

 @Test
 public void testGetSupportUpperBound() {
 double supportUpperBound = distribution.getSupportUpperBound();
 assertEquals(supportUpperBound, 1.0, 0.0);
 }

 @Test
 public void testIsSupportLowerBoundInclusive() {
 boolean isSupportLowerBoundInclusive = distribution.isSupportLowerBoundInclusive();
 assertTrue(isSupportLowerBoundInclusive);
 }

 @Test
 public void testIsSupportUpperBoundInclusive() {
 boolean isSupportUpperBoundInclusive = distribution.isSupportUpperBoundInclusive();
 assertFalse(isSupportUpperBoundInclusive);
 }

 @Test
 public void testIsSupportConnected() {
 boolean isSupportConnected = distribution.isSupportConnected();
 assertTrue(isSupportConnected);
 }
}