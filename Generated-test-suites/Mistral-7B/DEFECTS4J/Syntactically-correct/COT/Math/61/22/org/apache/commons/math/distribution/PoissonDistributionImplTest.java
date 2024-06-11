import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import org.junit.Assert.*;

public class PoissonDistributionImplTest {

 private PoissonDistributionImpl poissonDistribution;

 @Test
 public void testConstructorWithPositiveMean() {
 poissonDistribution = new PoissonDistributionImpl(2.5);
 Assert.assertEquals(2.5, poissonDistribution.getMean(), 0.001);
 }

 @Test(expected = MathRuntimeException.class)
 public void testConstructorWithNegativeMean() {
 poissonDistribution = new PoissonDistributionImpl(-1.5);
 fail("Should not be able to create a PoissonDistributionImpl with negative mean");
 }

 @Test
 public void testProbabilityMethodWithValidInput() {
 int x = 3;
 double probability = poissonDistribution.probability(x);
 Assert.assertEquals(0.02870981, probability, 0.001);
 }

 @Test
 public void testProbabilityMethodWithZeroInput() {
 double probability = poissonDistribution.probability(0);
 Assert.assertEquals(0.03678794, probability, 0.001);
 }

 @Test
 public void testProbabilityMethodWithNegativeInput() {
 double probability = poissonDistribution.probability(-1);
 Assert.assertEquals(0, probability, 0.001);
 }

 @Test
 public void testProbabilityMethodWithMaxIntegerInput() {
 int x = Integer.MAX_VALUE;
 double probability = poissonDistribution.probability(x);
 Assert.assertEquals(0, probability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityMethodWithValidInput() throws MathException {
 int x = 3;
 double cumulativeProbability = poissonDistribution.cumulativeProbability(x);
 Assert.assertEquals(0.059127633, cumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityMethodWithZeroInput() throws MathException {
 double cumulativeProbability = poissonDistribution.cumulativeProbability(0);
 Assert.assertEquals(0.03678794, cumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityMethodWithNegativeInput() throws MathException {
 double cumulativeProbability = poissonDistribution.cumulativeProbability(-1);
 Assert.assertEquals(0, cumulativeProbability, 0.001);
 }

 @Test
 public void testCumulativeProbabilityMethodWithMaxIntegerInput() throws MathException {
 int x = Integer.MAX_VALUE;
 double cumulativeProbability = poissonDistribution.cumulativeProbability(x);
 Assert.assertEquals(1, cumulativeProbability, 0.001);
 }

 @Test
 public void testNormalApproximateProbabilityMethodWithValidInput() throws MathException {
 int x = 3;
 double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
 Assert.assertEquals(0.02870981, normalApproximateProbability, 0.001);
 }

 @Test
 public void testNormalApproximateProbabilityMethodWithNegativeInput() throws MathException {
 int x = -1;
 double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
 Assert.assertEquals(0, normalApproximateProbability, 0.001);
 }

 @Test
 public void testNormalApproximateProbabilityMethodWithMaxIntegerInput() throws MathException {
 int x = Integer.MAX_VALUE;
 double normalApproximateProbability = poissonDistribution.normalApproximateProbability(x);
 Assert.assertEquals(1, normalApproximateProbability, 0.001);
 }

 @Test
 public void testSampleMethodWithSmallMean() throws MathException {
 int sample = poissonDistribution.sample();
 Assert.assertTrue(sample >= 0 && sample <= 10 * poissonDistribution.getMean());
 }

 @Test
 public void testSampleMethodWithLargeMean() throws MathException {
 int sample = poissonDistribution.sample();
 Assert.assertTrue(sample >= 0 && sample <= Integer.MAX_VALUE);
 }

 @Test(expected = MathException.class)
 public void testSampleMethodWithNegativeMean() throws MathException {
 poissonDistribution = new PoissonDistributionImpl(-1.5);
 poissonDistribution.sample();
 fail("Should not be able to call sample method with negative mean.");
 }
}