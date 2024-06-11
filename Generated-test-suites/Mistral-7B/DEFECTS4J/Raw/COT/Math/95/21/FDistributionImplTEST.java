import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImplConstruction() {
 distribution = new FDistributionImpl(5.0, 10.0);
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbabilityNormalCase() throws MathException {
 distribution = new FDistributionImpl(5.0, 10.0);
 double x = 1.5;
 double cdf = distribution.cumulativeProbability(x);
 Beta beta = new Beta(5.0 * x / (10.0 + 5.0 * x), 2.5 * 5.0, 2.5 * 10.0);
 double expectedCdf = beta.cumulativeProbability();
 assertEquals(expectedCdf, cdf, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeX() throws MathException {
 distribution = new FDistributionImpl(5.0, 10.0);
 double x = -1.0;
 distribution.cumulativeProbability(x);
 fail("Should throw MathException");
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityZeroDegreesOfFreedom() throws MathException {
 distribution = new FDistributionImpl(0.0, 10.0);
 distribution.cumulativeProbability(1.0);
 fail("Should throw MathException");
 }

 @Test
 public void testInverseCumulativeProbabilityZeroProbability() throws MathException {
 distribution = new FDistributionImpl(5.0, 10.0);
 double p = 0.0;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOneProbability() throws MathException {
 distribution = new FDistributionImpl(5.0, 10.0);
 double p = 1.0;
 double x = distribution.inverseCumulativeProbability(p);
 assertTrue(Double.isInfinite(x));
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
 distribution = new FDistributionImpl(5.0, 10.0);
 double p = 2.0;
 distribution.inverseCumulativeProbability(p);
 fail("Should throw MathException");
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 distribution = new FDistributionImpl(5.0, 10.0);
 distribution.setNumeratorDegreesOfFreedom(10.0);
 assertEquals(10.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumeratorDegreesOfFreedomNegative() {
 distribution = new FDistributionImpl(5.0, 10.0);
 distribution.setNumeratorDegreesOfFreedom(-1.0);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 distribution = new FDistributionImpl(5.0, 10.0);
 distribution.setDenominatorDegreesOfFreedom(15.0);
 assertEquals(15.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDenominatorDegreesOfFreedomNegative() {
 distribution = new FDistributionImpl(5.0, 10.0);
 distribution.setDenominatorDegreesOfFreedom(-1.0);
 fail("Should throw IllegalArgumentException");
 }
}