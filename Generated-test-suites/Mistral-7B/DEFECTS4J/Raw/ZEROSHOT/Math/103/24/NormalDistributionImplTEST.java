import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
 distribution = new NormalDistributionImpl(1.5, 2.5);
 assertEquals(1.5, distribution.getMean(), 0.001);
 assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviationZero() {
 distribution = new NormalDistributionImpl();
 assertEquals(0.0, distribution.getMean(), 0.001);
 assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationNegative() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(-1.5);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutsideRangeWithMeanZeroStandardDeviationOne() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double probability = 4.0;
 double result = normalDistribution.cumulativeProbability(probability);
 assertEquals(0.0, result, 0.001);
 }

 @Test
 public void testCumulativeProbabilityInsideRangeWithMeanZeroStandardDeviationOne() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double probability = 0.5;
 double result = normalDistribution.cumulativeProbability(probability);
 assertEquals(0.5, result, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithMeanOneStandardDeviationOne() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 1.0);
 double probability = 0.5;
 double result = normalDistribution.cumulativeProbability(probability);
 assertEquals(0.5, result, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithMeanOneStandardDeviationTwo() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 double probability = 0.5;
 double result = normalDistribution.cumulativeProbability(probability);
 assertEquals(0.5, result, 0.001);
 }

 @Test
 public void testInverseCumulativeProbabilityZeroProbability() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double result = normalDistribution.inverseCumulativeProbability(0.0);
 assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOneProbability() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double result = normalDistribution.inverseCumulativeProbability(1.0);
 assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityWithMeanZeroStandardDeviationOne() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double probability = 0.6821; // standard normal distribution value
 double result = normalDistribution.inverseCumulativeProbability(probability);
 assertEquals(0.6745, result, 0.001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithMeanOneStandardDeviationOne() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 1.0);
 double probability = 0.6821;
 double result = normalDistribution.inverseCumulativeProbability(probability);
 assertEquals(1.0 + 0.6745 * 1.0, result, 0.001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithMeanOneStandardDeviationTwo() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 double probability = 0.6821;
 double result = normalDistribution.inverseCumulativeProbability(probability);
 assertEquals(1.0 + 0.6745 * 2.0, result, 0.001);
 }
}