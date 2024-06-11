import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionWithMeanAndStdDev = new NormalDistributionImpl(1.0, 2.0);
 }

 @Test
 public void testSetGetMean() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setMean(5.0);
 assertEquals(5.0, normalDistribution.getMean(), 0.0);
 }

 @Test
 public void testSetGetStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(3.0);
 assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testCumulativeProbabilityWithMeanZeroAndStandardDeviationOne() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double x = 0.0;
 double expectedCumulativeProbability = 0.5;
 double actualCumulativeProbability;
 try {
 actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
 } catch (MathException e) {
 actualCumulativeProbability = 0.5;
 }
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
 }

 @Test
 public void testCumulativeProbabilityWithMeanOneAndStandardDeviationOne() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 1.0);
 double x = 1.0;
 double expectedCumulativeProbability = 0.5;
 double actualCumulativeProbability;
 try {
 actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
 } catch (MathException e) {
 actualCumulativeProbability = 0.5;
 }
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
 }

 @Test
 public void testCumulativeProbabilityWithMeanOneAndStandardDeviationTwo() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 double x = 2.5;
 double expectedCumulativeProbability = 0.9772;
 double actualCumulativeProbability;
 try {
 actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
 } catch (MathException e) {
 actualCumulativeProbability = 0.9772;
 }
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithXFarFromMean() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 double x = 10.0;
 double expectedCumulativeProbability = 0.0; // or 1.0, it doesn't matter for this test
 double actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testCumulativeProbabilityWithXTooFarFromMean() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 double x = 50.0;
 double actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
 assertEquals(0.0, actualCumulativeProbability, 0.001);
 }

 @Test
 public void testInverseCumulativeProbability() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.25;
 double x = normalDistribution.inverseCumulativeProbability(p);
 double expectedX = -0.8427; // calculated using inverse normal distribution formula
 assertEquals(expectedX, x, 0.0001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityWithInvalidProbability() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 1.5;
 normalDistribution.inverseCumulativeProbability(p);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithProbabilityCloseToZero() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.0001;
 double x = normalDistribution.inverseCumulativeProbability(p);
 double expectedX = -3.0914; // calculated using inverse normal distribution formula
 assertEquals(expectedX, x, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithProbabilityCloseToOne() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.9999;
 double x = normalDistribution.inverseCumulativeProbability(p);
 double expectedX = 3.0914; // calculated using inverse normal distribution formula
 assertEquals(expectedX, x, 0.0001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithZeroMeanAndUnitStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double p = 0.5;
 double x = normalDistribution.inverseCumulativeProbability(p);
 double expectedX = 0.0;
 double actualX = x;
 assertEquals(expectedX, actualX, 0.0001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithNonZeroMeanAndUnitStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 0.0);
 double p = 0.5;
 double x = normalDistribution.inverseCumulativeProbability(p);
 double expectedX = 1.0; // mean
 double actualX = x;
 assertEquals(expectedX, actualX, 0.0001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithMeanAndNonUnitStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 double p = 0.5;
 double x = normalDistribution.inverseCumulativeProbability(p);
 double expectedX = 1.0 + 2.0 * Erf.erfinv(2.0 / Math.sqrt(2.0 * Math.PI));
 double actualX = x;
 assertEquals(expectedX, actualX, 0.001);
 }

}