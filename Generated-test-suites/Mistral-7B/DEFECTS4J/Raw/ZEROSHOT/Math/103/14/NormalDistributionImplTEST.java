import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Test
 public void testNormalDistributionImplWithDefaultConstructor() {
     distribution = new NormalDistributionImpl();
     NormalDistribution expectedDistribution = new NormalDistribution(0.0, 1.0);
     assertEquals(expectedDistribution, distribution);
 }

 @Test
 public void testNormalDistributionImplWithSpecifiedMeanAndStandardDeviation() {
     distribution = new NormalDistributionImpl(2.5, 1.5);
     NormalDistribution expectedDistribution = new NormalDistribution(2.5, 1.5);
     assertEquals(expectedDistribution, distribution);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNegativeStandardDeviation() {
     distribution = new NormalDistributionImpl(0.0, -1.0);
 }

 @Test
 public void testGetMean() {
     distribution = new NormalDistributionImpl(3.14, 1.0);
     assertEquals(3.14, distribution.getMean(), 0.001);
 }

 @Test
 public void testGetStandardDeviation() {
     distribution = new NormalDistributionImpl(0.0, 2.71828);
     assertEquals(2.71828, distribution.getStandardDeviation(), 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfRange() throws MathException {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     double outlier = Double.POSITIVE_INFINITY;
     distribution.cumulativeProbability(outlier);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
     distribution = new NormalDistributionImpl(0.0, 1.0);

     double expectedCumulativeProbability = 0.1545;
     double x = 0.5;
     double actualCumulativeProbability = distribution.cumulativeProbability(x);

     assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
     distribution = new NormalDistributionImpl(0.0, 1.0);
     double p = -0.1;
     distribution.inverseCumulativeProbability(p);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
     distribution = new NormalDistributionImpl(0.0, 1.0);

     double expectedInverseCumulativeProbability = 0.841345;
     double p = 0.84;
     double actualInverseCumulativeProbability = distribution.inverseCumulativeProbability(p);

     assertEquals(expectedInverseCumulativeProbability, actualInverseCumulativeProbability, 0.001);
 }
}