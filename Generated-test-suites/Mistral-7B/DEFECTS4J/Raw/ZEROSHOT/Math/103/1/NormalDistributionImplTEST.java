import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

 public NormalDistributionImplTest() {
 }

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 assertEquals(1.0, normalDistribution.getMean(), 0.0);
 assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test
 public void testNormalDistributionImplZeroMeanOneSigma() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 assertEquals(0.0, normalDistribution.getMean(), 0.0);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test
 public void testCumulativeProbabilityNormalDistribution() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.5, 1.0);
 double cumulativeProbability = testInstance.cumulativeProbability(0.5);
 assertEquals(0.5, cumulativeProbability, 0.0001);
 }

 @Test
 public void testCumulativeProbabilityOutOfRange() throws MathException {
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.0, 1.0);
 assertThrows(MathException.class, () -> testInstance.cumulativeProbability(100.0));
 }

 @Test
 public void testInverseCumulativeProbabilityNormalDistribution() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.5, 1.0);
 double inverseCumulativeProbability = testInstance.inverseCumulativeProbability(0.5);
 assertEquals(0.0, inverseCumulativeProbability, 0.0001);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws MathException {
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.0, 1.0);
 assertEquals(Double.NEGATIVE_INFINITY, testInstance.inverseCumulativeProbability(0.0), 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOne() throws MathException {
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.0, 1.0);
 assertEquals(Double.POSITIVE_INFINITY, testInstance.inverseCumulativeProbability(1.0), 0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.0, 1.0);
 testInstance.inverseCumulativeProbability(2.0);
 }

 @Test
 public void testMeanAndStandardDeviationAccessors() {
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.0, 1.0);
 testInstance.setMean(3.0);
 testInstance.setStandardDeviation(2.0);
 assertEquals(3.0, testInstance.getMean(), 0.0);
 assertEquals(2.0, testInstance.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationNegativeValue() {
 NormalDistributionImpl testInstance = new NormalDistributionImpl(0.0, 1.0);
 testInstance.setStandardDeviation(-1.0);
 }
}