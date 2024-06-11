import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FDistributionImplTest {

 private FDistribution distribution;

 @Test
 public void testFDistributionImplConstructor() {
 distribution = new FDistributionImpl(2.0, 4.0);
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionImplNegativeNumeratorDOF() {
 distribution = new FDistributionImpl(-2.0, 4.0);
 // the exception should be thrown in the constructor
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionImplNegativeDenominatorDOF() {
 distribution = new FDistributionImpl(2.0, -4.0);
 // the exception should be thrown in the constructor
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 distribution = new FDistributionImpl(2.0, 4.0);
 double x = 1.5;
 double cdf = distribution.cumulativeProbability(x);
 assertEquals(0.913914816392478, cdf, 0.00001);
 }

 @Test
 public void testCumulativeProbabilityZero() throws MathException {
 distribution = new FDistributionImpl(2.0, 4.0);
 double cdf = distribution.cumulativeProbability(0.0);
 assertEquals(0.0, cdf, 0.0);
 }

 @Test
 public void testCumulativeProbabilityNegative() throws MathException {
 distribution = new FDistributionImpl(2.0, 4.0);
 double x = -1.0;
 assertFalse("The cumulative probability for negative x should be 0",
 distribution.cumulativeProbability(x) > 0.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 distribution = new FDistributionImpl(2.0, 4.0);
 double p = 0.9;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(1.521463415366991, x, 0.00001);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws MathException {
 distribution = new FDistributionImpl(2.0, 4.0);
 double p = 0.0;
 assertEquals(0.0, distribution.inverseCumulativeProbability(p), 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOne() throws MathException {
 distribution = new FDistributionImpl(2.0, 4.0);
 double p = 1.0;
 assertTrue("The inverse cumulative probability for p=1 should be positive infinity",
 Double.isInfinite(distribution.inverseCumulativeProbability(p)));
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfRange() throws MathException {
 distribution = new FDistributionImpl(2.0, 4.0);
 double p = -0.1;
 // the exception should be thrown in the inverseCumulativeProbability method
 }

}