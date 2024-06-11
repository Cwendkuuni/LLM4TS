import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistribution dist;

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
 dist = new NormalDistributionImpl(0.5, 1.0);
 assertEquals(0.5, dist.getMean(), 0.01);
 assertEquals(1.0, dist.getStandardDeviation(), 0.01);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNormalDistributionImplConstructorWithNegativeStandardDeviation() {
 dist = new NormalDistributionImpl(0.5, -1.0);
 }

 @Test
 public void testNormalDistributionImplConstructorWithDefaultMeanAndStandardDeviation() {
 dist = new NormalDistributionImpl();
 assertEquals(0.0, dist.getMean(), 0.01);
 assertEquals(1.0, dist.getStandardDeviation(), 0.01);
 }

 @Test
 public void testDensity() {
 dist = new NormalDistributionImpl(0.5, 1.0);
 double density = dist.density(0.5);
 assertEquals(0.1460368195354981, density, 0.0001);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 dist = new NormalDistributionImpl(0.5, 1.0);
 double p = dist.cumulativeProbability(0.6);
 assertEquals(0.7126768554627492, p, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithExtremeValue() throws MathException {
 dist = new NormalDistributionImpl(0.5, 1.0);
 double p = dist.cumulativeProbability(1000.0);
 }

 @Test
 public void testInverseCumulativeProbabilityWithValidProbability() throws MathException {
 dist = new NormalDistributionImpl(0.5, 1.0);
 double p = 0.25;
 double inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
 assertEquals(-0.8244891566752217, inverseCumulativeProbability, 0.01);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
 dist = new NormalDistributionImpl(0.5, 1.0);
 double p = 2.5;
 assertNull(dist.inverseCumulativeProbability(p));
 }

 @Test
 public void testSample() throws MathException {
 dist = new NormalDistributionImpl(0.5, 1.0);
 double sample = dist.sample();
 assertEquals(0.5, sample, 0.01);
 }
}