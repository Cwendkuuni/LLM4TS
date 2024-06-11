import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Erf;

import org.junit.Test;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @SuppressWarnings("static-access")
 @Before
 public void setup() {
 distribution = new NormalDistributionImpl(0.0, 1.0);
 }

 @Test
 public void testConstructorWithMeanAndStdDev() {
 NormalDistribution expected = new NormalDistribution(0.0, 1.0);
 assertEquals(expected, distribution);
 }

 @Test
 public void testConstructorWithMeanAndStdDevZero() {
 NormalDistribution expected = new NormalDistribution();
 assertEquals(expected, distribution);
 }

 @Test
 public void testGetMean() {
 assertEquals(0.0, distribution.getMean(), 0.001);
 }

 @Test
 public void testSetMean() {
 distribution.setMean(2.0);
 assertEquals(2.0, distribution.getMean(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanNegative() {
 distribution.setMean(-1.0);
 }

 @Test
 public void testGetStandardDeviation() {
 assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testSetStandardDeviation() {
 distribution.setStandardDeviation(2.0);
 assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationZeroOrNegative() {
 distribution.setStandardDeviation(0.0);
 // This line will throw an IllegalArgumentException
 distribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testCumulativeProbabilityPositiveValue() throws MathException {
 double p = 0.5;
 double expected = 0.5;
 assertEquals(expected, distribution.cumulativeProbability(0.0), 0.001);
 }

 @Test
 public void testCumulativeProbabilityNegativeValue() throws MathException {
 double p = -1.0;
 double expected = 0.0;
 assertEquals(expected, distribution.cumulativeProbability(Double.NEGATIVE_INFINITY), 0.001);
 }

 @Test
 public void testCumulativeProbabilityInfiniteValue() throws MathException {
 double p = 1.0;
 double expected = 0.5;
 assertEquals(expected, distribution.cumulativeProbability(Double.POSITIVE_INFINITY), 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityValueFarFromMean() throws MathException {
 double x = 10.0;
 assertThrows(MathException.class, () -> distribution.cumulativeProbability(x));
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws MathException {
 double p = 0.0;
 double expected = Double.NEGATIVE_INFINITY;
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.001);
 }

 @Test
 public void testInverseCumulativeProbabilityOne() throws MathException {
 double p = 1.0;
 double expected = Double.POSITIVE_INFINITY;
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
 double p = 1.1;
 assertThrows(MathException.class, () -> distribution.inverseCumulativeProbability(p));
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testInverseCumulativeProbabilityFailsToConverge() throws MathException {
 double p = 0.6;
 assertThrows(MaxIterationsExceededException.class, () -> distribution.inverseCumulativeProbability(p));
 }

}