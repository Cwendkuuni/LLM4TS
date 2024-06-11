import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PoissonDistributionImplTest {

 private PoissonDistributionImpl poissonDistribution;

 @Test
 public void testPoissonDistributionImplConstructorWithPositiveMean() {
 poissonDistribution = new PoissonDistributionImpl(2.5);
 }

 @Test(expected = MathRuntimeException.class)
 public void testPoissonDistributionImplConstructorWithNegativeMean() {
 poissonDistribution = new PoissonDistributionImpl(-1.5);
 }

 @Test
 public void testGetMean() {
 assertEquals(2.5, poissonDistribution.getMean(), 0.0);
 }

 @Test
 public void testProbabilityZero() {
 assertEquals(FastMath.exp(-poissonDistribution.mean), poissonDistribution.probability(0), 0.0);
 }

 @Test
 public void testProbabilityValidValue() {
 assertEquals(0.437696573927345, poissonDistribution.probability(2), 0.0000001);
 }

 @Test
 public void testProbabilityMaxValue() {
 assertEquals(FastMath.ONE, poissonDistribution.probability(Integer.MAX_VALUE), 0.0);
 }

 @Test
 public void testProbabilityNegativeValue() {
 assertEquals(0.0, poissonDistribution.probability(-1), 0.0);
 }

 @Test
 public void testCumulativeProbabilityZero() throws MathException {
 assertEquals(FastMath.exp(-poissonDistribution.mean), poissonDistribution.cumulativeProbability(0), 0.0);
 }

 @Test
 public void testCumulativeProbabilityValidValue() throws MathException {
 assertEquals(0.852303061434484, poissonDistribution.cumulativeProbability(1), 0.0000001);
 }

 @Test
 public void testCumulativeProbabilityMaxValue() throws MathException {
 assertEquals(FastMath.ONE, poissonDistribution.cumulativeProbability(Integer.MAX_VALUE), 0.0);
 }

 @Test
 public void testNormalApproximateProbabilityZero() throws MathException {
 assertEquals(FastMath.exp(-poissonDistribution.mean), poissonDistribution.normalApproximateProbability(0), 0.0);
 }

 @Test
 public void testNormalApproximateProbabilityValidValue() throws MathException {
 assertEquals(0.436662849248122, poissonDistribution.normalApproximateProbability(2), 0.0000001);
 }

 @Test
 public void testSample() throws MathException {
 int sample = poissonDistribution.sample();
 assertEquals(2, sample);
 }

 @Test
 public void testSampleLargerThanMean() throws MathException {
 int sample = poissonDistribution.sample();
 assertEquals(3, sample);
 }

 @Test(expected = MathException.class)
 public void testSampleNegativeValue() throws MathException {
 int sample = poissonDistribution.sample();
 //This test should throw MathException as the sample value should not be negative
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws MathException {
 double p = 0.0;
 int x = poissonDistribution.inverseCumulativeProbability(p);
 assertEquals(0, x);
 }

 @Test
 public void testInverseCumulativeProbabilityValueBelowOne() throws MathException {
 double p = 0.2;
 int x = poissonDistribution.inverseCumulativeProbability(p);
 assertEquals(0, x);
 }

 @Test
 public void testInverseCumulativeProbabilityValueOne() throws MathException {
 double p = 1.0;
 int x = poissonDistribution.inverseCumulativeProbability(p);
 assertEquals(1, x);
 }

 @Test
 public void testInverseCumulativeProbabilityValueAboveOne() throws MathException {
 double p = 1.1;
 int x = poissonDistribution.inverseCumulativeProbability(p);
 assertEquals(2, x);
 }

 @Test
 public void testInverseCumulativeProbabilityMaxValue() throws MathException {
 double p = 1.0;
 int x = poissonDistribution.inverseCumulativeProbability(1.0 + 1.0e-12);
 assertEquals(Integer.MAX_VALUE, x);
 }
}