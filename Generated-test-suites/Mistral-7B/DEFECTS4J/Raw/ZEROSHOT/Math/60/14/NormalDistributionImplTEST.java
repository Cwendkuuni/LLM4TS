import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionConstructor() {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 assertEquals(0.5, dist.getMean(), 0.001);
 assertEquals(1.0, dist.getStandardDeviation(), 0.001);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNormalDistributionConstructorNegativeStandardDeviation() {
 new NormalDistributionImpl(0.5, -1.0);
 }

 @Test
 public void testDensity() {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 double result = dist.density(0.5);
 assertEquals(0.14869246437754523, result, 0.001);
 }

 @Test
 public void testCumulativeProbability() {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 double result = dist.cumulativeProbability(0.5);
 assertEquals(0.5, result, 0.001);
 }

 @Test
 public void testCumulativeProbabilityOutOfRange() {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 try {
 double result = dist.cumulativeProbability(-10.0);
 } catch (NumberIsTooLargeException e) {
 // Expected
 }
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 double p = 0.15;
 double result = dist.inverseCumulativeProbability(p);
 assertEquals(0.7343861944634197, result, 0.01);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws Exception {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 double result = dist.inverseCumulativeProbability(0.0);
 assertEquals(Double.NEGATIVE_INFINITY, result, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOne() throws Exception {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 double result = dist.inverseCumulativeProbability(1.0);
 assertEquals(Double.POSITIVE_INFINITY, result, 0.0);
 }

 @Test
 public void testSample() throws Exception {
 NormalDistribution dist = new NormalDistributionImpl(0.5, 1.0);
 double result = dist.sample();
 assertTrue(result > -3.0 && result < 3.0);
 }

}