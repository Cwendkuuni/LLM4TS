import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplCreation() {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 assertEquals("Mean should be set correctly", 0.0, distribution.getMean(), 1e-6);
 assertEquals("Standard deviation should be set correctly", 1.0, distribution.getStandardDeviation(), 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplNegativeStandardDeviation() {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, -1.0);
 }

 @Test
 public void testNormalDistributionImplMean() {
 NormalDistribution distribution = new NormalDistributionImpl(2.5, 1.0);
 assertEquals("Mean should be set correctly", 2.5, distribution.getMean(), 1e-6);
 }

 @Test
 public void testNormalDistributionImplStandardDeviation() {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 2.5);
 assertEquals("Standard deviation should be set correctly", 2.5, distribution.getStandardDeviation(), 1e-6);
 }

 @Test
 public void testNormalDistributionImplCumulativeProbability() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

 // Test values close to mean
 assertEquals("Cumulative probability at mean", 0.5, distribution.cumulativeProbability(0.0), 1e-6);
 // Test values below mean
 assertEquals("Cumulative probability below mean", 0.2413793112542214, distribution.cumulativeProbability(-2.0), 1e-6);
 // Test values above mean
 assertEquals("Cumulative probability above mean", 0.8413793112542214, distribution.cumulativeProbability(2.0), 1e-6);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplCumulativeProbabilityExtremeValues() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 distribution.cumulativeProbability(Double.POSITIVE_INFINITY);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);

 // Test values close to mean
 assertEquals("Inverse cumulative probability at mean", 0.0, distribution.inverseCumulativeProbability(0.5), 1e-6);
 // Test values below mean
 assertEquals("Inverse cumulative probability below mean (low)", -1.2821275663628256, distribution.inverseCumulativeProbability(0.2), 1e-6);
 // Test values above mean
 assertEquals("Inverse cumulative probability above mean (high)", 1.2821275663628256, distribution.inverseCumulativeProbability(0.8), 1e-6);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplInverseCumulativeProbabilityZero() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 distribution.inverseCumulativeProbability(0.0);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplInverseCumulativeProbabilityOne() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 distribution.inverseCumulativeProbability(1.0);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbabilityExtremeValues() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 assertTrue("Inverse cumulative probability of extreme values should return NaN", Double.isNaN(distribution.inverseCumulativeProbability(1.0)));
 }
}