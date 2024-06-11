import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Before
 public void setup() {
 distribution = new NormalDistributionImpl(0, 1);
 }

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
 NormalDistribution expected = new NormalDistribution(0, 1);
 NormalDistribution actual = distribution;
 assertEquals(expected.getMean(), actual.getMean(), 0.001);
 assertEquals(expected.getStandardDeviation(), actual.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplEmptyConstructor() {
 NormalDistribution expected = new NormalDistribution(0, 1);
 NormalDistribution actual = distribution;
 assertEquals(expected.getMean(), actual.getMean(), 0.001);
 assertEquals(expected.getStandardDeviation(), actual.getStandardDeviation(), 0.001);
 }

 @Test
 public void testGetMean() {
 double expected = 0.0;
 double actual = distribution.getMean();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testSetMean() {
 double newMean = 2.5;
 distribution.setMean(newMean);
 double actual = distribution.getMean();
 assertEquals(newMean, actual, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanWithNegativeValue() {
 distribution.setMean(-1.2);
 }

 @Test
 public void testGetStandardDeviation() {
 double expected = 1.0;
 double actual = distribution.getStandardDeviation();
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testSetStandardDeviation() {
 double newStandardDeviation = 2.5;
 distribution.setStandardDeviation(newStandardDeviation);
 double actual = distribution.getStandardDeviation();
 assertEquals(newStandardDeviation, actual, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationWithZeroOrNegativeValue() {
 distribution.setStandardDeviation(0.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 double x = 0.5;
 double expected = 0.682689462137;
 double actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testCumulativeProbabilityWithOutlierValue() throws MathException {
 double x = 100;
 double expected = 0.5;
 double actual = distribution.cumulativeProbability(x);
 assertEquals(expected, actual, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithValueFarFromMean() throws MathException {
 double x = 1000;
 distribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 double p = 0.95;
 double expected = 1.64485362987;
 double actual = distribution.inverseCumulativeProbability(p);
 assertEquals(expected, actual, 0.001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithEdgeValues() throws MathException {
 double p = 0.0;
 double expected = Double.NEGATIVE_INFINITY;
 double actual = distribution.inverseCumulativeProbability(p);
 assertEquals(expected, actual);

 double pMax = 1.0;
 double expectedMax = Double.POSITIVE_INFINITY;
 double actualMax = distribution.inverseCumulativeProbability(pMax);
 assertEquals(expectedMax, actualMax);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
 distribution.inverseCumulativeProbability(2.0);
 }
}