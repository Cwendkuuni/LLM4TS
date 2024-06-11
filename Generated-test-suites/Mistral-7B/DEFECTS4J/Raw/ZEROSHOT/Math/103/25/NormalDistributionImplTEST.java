import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImpl_meanZeroStandardDeviationOne() {
 NormalDistribution distribution = new NormalDistributionImpl();
 assertEquals(0.0, distribution.getMean(), 0.01);
 assertEquals(1.0, distribution.getStandardDeviation(), 0.01);
 }

 @Test
 public void testNormalDistributionImpl_meanNonZeroStandardDeviationOne() {
 NormalDistribution distribution = new NormalDistributionImpl(2.5, 1.0);
 assertEquals(2.5, distribution.getMean(), 0.01);
 assertEquals(1.0, distribution.getStandardDeviation(), 0.01);
 }

 @Test
 public void testNormalDistributionImpl_meanNonZeroStandardDeviationNonOne() {
 NormalDistribution distribution = new NormalDistributionImpl(2.5, 2.0);
 assertEquals(2.5, distribution.getMean(), 0.01);
 assertEquals(2.0, distribution.getStandardDeviation(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalDistributionImpl_negativeStandardDeviation() {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, -1.0);
 }

 @Test
 public void testNormalDistributionImpl_cumulativeProbability() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 double x = 0.0;
 double p = 0.5;
 assertEquals(p, distribution.cumulativeProbability(x), 0.01);

 x = 1.0;
 p = 0.95;
 assertEquals(p, distribution.cumulativeProbability(x), 0.01);

 x = -3.0;
 p = 0.01;
 assertEquals(p, distribution.cumulativeProbability(x), 0.01);

 x = 3.0;
 p = 0.99;
 assertEquals(p, distribution.cumulativeProbability(x), 0.01);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImpl_cumulativeProbabilityOutOfRange() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 double x = 10.0;
 distribution.cumulativeProbability(x);
 }

 @Test
 public void testNormalDistributionImpl_inverseCumulativeProbability_zero() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.0;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(Double.NEGATIVE_INFINITY, x, 0.0);
 }

 @Test
 public void testNormalDistributionImpl_inverseCumulativeProbability_one() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 1.0;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImpl_inverseCumulativeProbability_invalidProbability() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 double p = -0.5;
 distribution.inverseCumulativeProbability(p);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImpl_inverseCumulativeProbability_convergenceException() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.5000000001;
 distribution.inverseCumulativeProbability(p);
 }

 @Test
 public void testNormalDistributionImpl_inverseCumulativeProbability_validProbability() throws MathException {
 NormalDistribution distribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.682689492137; // z-score from a standard normal distribution
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(-0.2704513973078, x, 0.01);
 }
}