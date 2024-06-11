import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Before
 public void setup() {
 distribution = new NormalDistributionImpl(0.0, 1.0);
 }

 @Test
 public void testConstructorWithMeanAndStandardDeviation() {
 NormalDistribution expected = new NormalDistribution(0.0, 1.0);
 assertEquals(expected.getMean(), distribution.getMean());
 assertEquals(expected.getStandardDeviation(), distribution.getStandardDeviation());
 }

 @Test
 public void testConstructorWithMeanAndStandardDeviationAndAccuracy() {
 NormalDistribution expected = new NormalDistribution(0.0, 1.0, 1e-9);
 assertEquals(expected.getMean(), distribution.getMean());
 assertEquals(expected.getStandardDeviation(), distribution.getStandardDeviation());
 assertEquals(expected.getSolverAbsoluteAccuracy(), distribution.getSolverAbsoluteAccuracy(), 0.0);
 }

 @Test
 public void testConstructorWithMeanAndZeroStandardDeviation() {
 try {
 new NormalDistributionImpl(0.0, 0.0);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test
 public void testDensity() {
 double point = 1.0;
 double expected = 0.241970392511287;
 assertEquals(expected, distribution.density(point), 0.0000001);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 double point = 0.5;
 double expected = 0.682689492137;
 assertEquals(expected, distribution.cumulativeProbability(point), 0.0000001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithOutOfRangeValue() throws MathException {
 distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 double probability = 0.1;
 double expected = -0.841621563168;
 assertEquals(expected, distribution.inverseCumulativeProbability(probability), 0.0000001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithNegativeProbability() throws MathException {
 distribution.inverseCumulativeProbability(-0.1);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithProbabilityOutOfRange() throws MathException {
 distribution.inverseCumulativeProbability(1.1);
 }

 @Test
 public void testSample() throws MathException {
 double expectedMean = 0.0;
 double expectedStandardDeviation = 1.0;
 double sample = distribution.sample();
 assertTrue(sample > expectedMean - 3 * expectedStandardDeviation);
 assertTrue(sample < expectedMean + 3 * expectedStandardDeviation);
 }

 @Test
 public void testGetSolverAbsoluteAccuracy() {
 double expected = 1e-9;
 assertEquals(expected, distribution.getSolverAbsoluteAccuracy(), 0.0);
 }
}