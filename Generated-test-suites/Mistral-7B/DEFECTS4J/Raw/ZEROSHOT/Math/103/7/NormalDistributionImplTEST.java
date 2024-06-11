import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 assertEquals(1.0, normalDistributionImpl.getMean(), 0.0);
 assertEquals(2.0, normalDistributionImpl.getStandardDeviation(), 0.0);
 }

 @Test
 public void testNormalDistributionImplZeroMeanOneStandardDeviation() {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 assertEquals(0.0, normalDistributionImpl.getMean(), 0.0);
 assertEquals(1.0, normalDistributionImpl.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplNegativeStandardDeviation() {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, -1.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 double p = 0.345;
 double result = normalDistributionImpl.cumulativeProbability(1.2);
 double expected = 0.35987541;
 assertEquals(expected, result, 0.000001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutsideRange() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 double x = 5.0;
 normalDistributionImpl.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 double p = 0.15;
 double result = normalDistributionImpl.inverseCumulativeProbability(p);
 double expected = -0.842701;
 assertEquals(expected, result, 0.01);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutsideRange() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 double p = 1.2;
 normalDistributionImpl.inverseCumulativeProbability(p);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationNegative() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 normalDistributionImpl.setStandardDeviation(-1.0);
 }

 @Test
 public void testSetMean() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl normalDistributionImpl = (NormalDistributionImpl) normalDistribution;
 double mean = 2.0;
 normalDistributionImpl.setMean(mean);
 assertEquals(mean, normalDistributionImpl.getMean(), 0.0);
 }
}