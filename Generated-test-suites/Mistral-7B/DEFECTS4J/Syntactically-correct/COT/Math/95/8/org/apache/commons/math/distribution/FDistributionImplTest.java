import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImplConstructor() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbabilityZero() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 assertEquals(0.0, distribution.cumulativeProbability(0.0), 0.0);
 }

 @Test
 public void testCumulativeProbabilityPositive() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 double x = 1.5; // this value is within the valid range for F distribution
 double p = distribution.cumulativeProbability(x);
 assertTrue(p > 0.0 && p < 1.0);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegative() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 double x = -1.0;
 distribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 assertEquals(0.0, distribution.inverseCumulativeProbability(0.0), 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOne() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 assertEquals(Double.POSITIVE_INFINITY, distribution.inverseCumulativeProbability(1.0), 0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 double p = 0.5; // this value is out of the valid range for F distribution
 distribution.inverseCumulativeProbability(p);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 distribution.setNumeratorDegreesOfFreedom(6.0);
 assertEquals(6.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumeratorDegreesOfFreedomNegative() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 distribution.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 distribution.setDenominatorDegreesOfFreedom(6.0);
 assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDenominatorDegreesOfFreedomNegative() throws Exception {
 distribution = new FDistributionImpl(3.0, 5.0);
 distribution.setDenominatorDegreesOfFreedom(-1.0);
 }
}