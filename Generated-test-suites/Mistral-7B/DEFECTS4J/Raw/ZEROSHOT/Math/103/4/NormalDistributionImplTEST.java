import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplCreation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionWithMeanAndStdDev = new NormalDistributionImpl(1.0, 2.0);
 }

 @Test
 public void testGetMean() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 assertEquals(0.0, normalDistribution.getMean(), 0.0);

 NormalDistributionImpl normalDistributionWithMean = new NormalDistributionImpl(1.0);
 assertEquals(1.0, normalDistributionWithMean.getMean(), 0.0);
 }

 @Test
 public void testSetMean() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(5.0);
 assertEquals(5.0, normalDistribution.getMean(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMean() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(-1.0);
 }

 @Test
 public void testGetStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test
 public void testSetStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(2.0);
 assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetZeroOrNegativeStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(0.0);
 normalDistribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0.0, 1.0);

 double cumulativeProbability = normalDistributionImpl.cumulativeProbability(0.5);
 assertEquals(0.682689462137);

 cumulativeProbability = normalDistributionImpl.cumulativeProbability(2.0);
 assertEquals(0.977249868058);

 cumulativeProbability = normalDistributionImpl.cumulativeProbability(-2.0);
 assertEquals(0.022750132033);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfRange() throws MathException {
 NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0.0, 1.0);
 normalDistributionImpl.cumulativeProbability(20.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0.0, 1.0);

 double inverseCumulativeProbability = normalDistributionImpl.inverseCumulativeProbability(0.0);
 assertEquals(-Infinity.doubleValue(), inverseCumulativeProbability, 0.0);

 inverseCumulativeProbability = normalDistributionImpl.inverseCumulativeProbability(0.5);
 assertEquals(0.0, inverseCumulativeProbability, 0.0);

 inverseCumulativeProbability = normalDistributionImpl.inverseCumulativeProbability(1.0);
 assertEquals(Infinity.doubleValue(), inverseCumulativeProbability, 0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
 NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(0.0, 1.0);
 normalDistributionImpl.inverseCumulativeProbability(1.2);
 }
}