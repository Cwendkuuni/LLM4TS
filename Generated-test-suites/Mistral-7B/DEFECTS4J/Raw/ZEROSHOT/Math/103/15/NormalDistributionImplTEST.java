import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Test
 public void testNormalDistributionImplWithValidParameters() {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     assertTrue(distribution instanceof NormalDistributionImpl);
     assertEquals(0.5, distribution.getMean(), 0.01);
     assertEquals(1.0, distribution.getStandardDeviation(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImplWithNegativeStandardDeviation() {
     distribution = new NormalDistributionImpl(0.5, -1.0);
     fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testCumulativeProbability() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double x = 0.5;
     double cdf = distribution.cumulativeProbability(x);
     assertEquals(0.682689462136617, cdf, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutsideRange() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double x = 5.0;
     distribution.cumulativeProbability(x);
     fail("MathException should be thrown");
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double p = 0.1587; // 1-phi(1.25)
     double inverseCdf = distribution.inverseCumulativeProbability(p);
     assertEquals(-0.805388923909719, inverseCdf, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double p = -0.1;
     distribution.inverseCumulativeProbability(p);
     fail("MathException should be thrown");
 }

 @Test
 public void testInverseCumulativeProbabilityZeroAndOne() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double p = 0.0;
     assertEquals(Double.NEGATIVE_INFINITY, distribution.inverseCumulativeProbability(p), 0.0);

     p = 1.0;
     assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(p), 0.0);
 }

 @Test
 public void testProbabilityDensityFunction() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double x = 0.5;
     double pdf = distribution.probabilityDensityFunction(x);
     assertEquals(0.3192857142857143, pdf, 0.001);
 }

 @Test(expected = MathException.class)
 public void testProbabilityDensityFunctionOutsideRange() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double x = 5.0;
     distribution.probabilityDensityFunction(x);
     fail("MathException should be thrown");
 }

 @Test
 public void testGetMean() {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     assertEquals(0.5, distribution.getMean(), 0.01);
 }

 @Test
 public void testSetMean() {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     distribution.setMean(2.5);
     assertEquals(2.5, distribution.getMean(), 0.01);
 }

 @Test
 public void testGetStandardDeviation() {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     assertEquals(1.0, distribution.getStandardDeviation(), 0.01);
 }

 @Test
 public void testSetStandardDeviation() {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     distribution.setStandardDeviation(2.0);
     assertEquals(2.0, distribution.getStandardDeviation(), 0.01);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testCumulativeProbabilityWithLargeX() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double x = 50.0;
     distribution.cumulativeProbability(x);
     fail("MaxIterationsExceededException should be thrown");
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithLargeX() throws MathException {
     distribution = new NormalDistributionImpl(0.5, 1.0);
     double x = 50.0;
     distribution.inverseCumulativeProbability(x);
     fail("MathException should be thrown");
 }

}