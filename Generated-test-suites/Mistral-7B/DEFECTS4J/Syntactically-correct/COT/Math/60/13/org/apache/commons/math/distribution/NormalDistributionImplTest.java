import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistribution dist;

 @Before
 public void setup() {
 mean = 0.0;
 standardDeviation = 1.0;
 dist = new NormalDistributionImpl(mean, standardDeviation);
 }

 @Test
 public void testConstructorWithMeanAndStdDev() {
 NormalDistribution expectedDist = new NormalDistribution(mean, standardDeviation);
 assertEquals("Check mean", expectedDist.getMean(), dist.getMean(), 0.001);
 assertEquals("Check stdDev", expectedDist.getStandardDeviation(), dist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testConstructorWithMeanStdDevAndAccuracy() {
 NormalDistribution expectedDist = new NormalDistribution(mean, standardDeviation);
 assertEquals("Check mean", expectedDist.getMean(), dist.getMean(), 0.001);
 assertEquals("Check stdDev", expectedDist.getStandardDeviation(), dist.getStandardDeviation(), 0.001);
 assertSame("Check solverAbsAccuracy", expectedDist.getSolverAbsoluteAccuracy(), dist.getSolverAbsoluteAccuracy());
 }

 @Test
 public void testConstructorWithDefaults() {
 NormalDistribution distDefault = new NormalDistributionImpl();
 assertEquals("Check mean", 0.0, distDefault.getMean(), 0.001);
 assertEquals("Check stdDev", 1.0, distDefault.getStandardDeviation(), 0.001);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeStandardDeviation() {
 NormalDistributionImpl distNegativeSD = new NormalDistributionImpl(mean, -standardDeviation);
 fail("Should throw NotStrictlyPositiveException with negative standardDeviation");
 }

 @Test
 public void testDensity() {
 NormalDistribution expectedDist = new NormalDistribution(mean, standardDeviation);
 double x = 1.0;
 double expectedDensity = expectedDist.density(x);
 double tolerance = 1e-6;
 double actualDensity = dist.density(x);
 assertEquals("Check density for positive x", expectedDensity, actualDensity, tolerance);
 x = -1.0;
 expectedDensity = expectedDist.density(x);
 actualDensity = dist.density(x);
 assertEquals("Check density for negative x", expectedDensity, actualDensity, tolerance);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 NormalDistribution expectedDist = new NormalDistribution(mean, standardDeviation);
 double x = 1.5 * standardDeviation;
 double expectedProbability = expectedDist.cumulativeProbability(x);
 double tolerance = 1e-6;
 double actualProbability = dist.cumulativeProbability(x);
 assertEquals("Check cumulativeProbability for positive x", expectedProbability, actualProbability, tolerance);
 x = mean;
 expectedProbability = 0.5;
 actualProbability = dist.cumulativeProbability(x);
 assertEquals("Check cumulativeProbability for mean", expectedProbability, actualProbability, tolerance);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfRange() throws MathException {
 double x = 50 * standardDeviation;
 dist.cumulativeProbability(x);
 fail("Should throw MathException for x out of range");
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 NormalDistribution expectedDist = new NormalDistribution(mean, standardDeviation);
 double p = 0.1;
 double expectedValue = expectedDist.inverseCumulativeProbability(p);
 double tolerance = 1e-6;
 double actualValue = dist.inverseCumulativeProbability(p);
 assertEquals("Check inverseCumulativeProbability for positive p", expectedValue, actualValue, tolerance);
 p = 0.5;
 expectedValue = mean;
 actualValue = dist.inverseCumulativeProbability(p);
 assertEquals("Check inverseCumulativeProbability for mean p", expectedValue, actualValue, tolerance);
 p = 0.9;
 expectedValue = mean + 3 * standardDeviation;
 actualValue = dist.inverseCumulativeProbability(p);
 assertEquals("Check inverseCumulativeProbability for high p", expectedValue, actualValue, tolerance);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityNegativeProbability() throws MathException {
 dist.inverseCumulativeProbability(-0.1);
 fail("Should throw MathException for negative probability");
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
 dist.inverseCumulativeProbability(1.2);
 fail("Should throw MathException for probability out of range");
 }

 @Test
 public void testSample() throws MathException {
 NormalDistribution expectedDist = new NormalDistribution(mean, standardDeviation);
 double x = dist.sample();
 double expectedX = expectedDist.sample();
 double tolerance = 0.01;
 assertEquals("Check sample with given distribution", expectedX, x, tolerance);
 }

}