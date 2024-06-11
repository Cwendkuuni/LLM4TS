import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImpl() {
 // Test constructor with valid inputs
 distribution = new FDistributionImpl(2.0, 4.0);
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

 // Test constructor with invalid input (negative numerator degrees of freedom)
 try {
 distribution = new FDistributionImpl(-1.0, 4.0);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test constructor with invalid input (negative denominator degrees of freedom)
 try {
 distribution = new FDistributionImpl(2.0, -1.0);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 // Test valid inputs
 distribution = new FDistributionImpl(2.0, 4.0);
 double x = 1.5;
 double cdf = distribution.cumulativeProbability(x);
 assertEquals(0.924545336282534, cdf, 1e-7);

 // Test extreme values (0 and infinity)
 x = 0.0;
 cdf = distribution.cumulativeProbability(x);
 assertEquals(0.0, cdf, 1e-7);

 x = Double.POSITIVE_INFINITY;
 cdf = distribution.cumulativeProbability(x);
 assertEquals(1.0, cdf, 1e-7);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 // Test valid inputs
 distribution = new FDistributionImpl(2.0, 4.0);
 double p = 0.95;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(1.613924596321325, x, 1e-7);

 // Test extreme values (0 and 1)
 p = 0.0;
 x = distribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 1e-7);

 p = 1.0;
 x = distribution.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x, 1e-7);
 }

 @Test
 public void testSetGetNumeratorDegreesOfFreedom() {
 // Test valid input
 distribution = new FDistributionImpl(2.0, 4.0);
 double newNumeratorDegreesOfFreedom = 5.0;
 distribution.setNumeratorDegreesOfFreedom(newNumeratorDegreesOfFreedom);
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);

 // Test negative input
 try {
 distribution.setNumeratorDegreesOfFreedom(-1.0);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSetGetDenominatorDegreesOfFreedom() {
 // Test valid input
 distribution = new FDistributionImpl(2.0, 4.0);
 double newDenominatorDegreesOfFreedom = 6.0;
 distribution.setDenominatorDegreesOfFreedom(newDenominatorDegreesOfFrequency);
 assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

 // Test negative input
 try {
 distribution.setDenominatorDegreesOfFreedom(-1.0);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}