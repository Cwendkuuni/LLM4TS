import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

 private NormalDistributionImpl normalDistribution;

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 assertEquals(0.5, normalDistribution.getMean(), 0.01);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.01);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNormalDistributionImplConstructorWithNegativeStandardDeviation() {
 normalDistribution = new NormalDistributionImpl(0.5, -1.0);
 }

 @Test(expected = MathException.class)
 public void testDensityWithNegativeValue() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 assertEquals(0.0, normalDistribution.density(-1.0), 0.01);
 }

 @Test
 public void testDensityWithValidValue() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 double value = 0.5;
 assertEquals(0.13595703588136813, normalDistribution.density(value), 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithNegativeValue() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 normalDistribution.cumulativeProbability(-1.0);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithValueMoreThanFourtyStandardDeviationsAwayFromMean() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 double value = 1000 * standardDeviation;
 normalDistribution.cumulativeProbability(value);
 }

 @Test
 public void testCumulativeProbabilityWithValidValue() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 double value = 0.75;
 assertEquals(0.7281222727808273, normalDistribution.cumulativeProbability(value), 0.0001);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testInverseCumulativeProbabilityWithInvalidProbability() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 normalDistribution.inverseCumulativeProbability(-1.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithProbabilityGreaterThanOne() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 normalDistribution.inverseCumulativeProbability(1.2);
 }

 @Test
 public void testInverseCumulativeProbabilityWithValidProbability() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 double probability = 0.3;
 assertEquals(0.2619565267567086, normalDistribution.inverseCumulativeProbability(probability), 0.0001);
 }

 @Test
 public void testSampleMethod() throws MathException {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 double sample = normalDistribution.sample();
 assertEquals(0.4660191725538326, sample, 0.001);
 }

 @Test(expected = MathException.class)
 public void testSampleMethodWithError() {
 normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 normalDistribution.sample();
 }
}