import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Before
 public void setUp() {
 distribution = new FDistributionImpl(2.0, 3.0);
 }

 @Test
 public void testConstructor() {
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNumeratorDegreesOfFreedom() {
 distribution = new FDistributionImpl(-1.0, 3.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDenominatorDegreesOfFreedom() {
 distribution = new FDistributionImpl(2.0, -1.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 // Test with valid input
 double x = 1.5;
 double p = distribution.cumulativeProbability(x);
 assertEquals(0.915467509123598, p, 0.0001);

 // Test with edge cases
 double minValue = Double.MIN_VALUE;
 double maxValue = Double.MAX_VALUE;
 double zero = 0.0;
 p = distribution.cumulativeProbability(minValue);
 assertTrue(p <= 0.0);
 p = distribution.cumulativeProbability(zero);
 assertEquals(0.0, p, 0.0);
 p = distribution.cumulativeProbability(maxValue);
 assertEquals(1.0, p, 0.0);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeX() throws MathException {
 distribution.cumulativeProbability(-1.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 // Test with valid input
 double p = 0.5;
 double x = distribution.inverseCumulativeProbability(p);
 assertEquals(1.275805367942227, x, 0.0001);

 // Test with edge cases
 p = 0.0;
 x = distribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 0.0);
 p = 1.0;
 x = distribution.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws MathException {
 distribution.inverseCumulativeProbability(-1.0);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 distribution.setNumeratorDegreesOfFreedom(5.0);
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 distribution.setDenominatorDegreesOfFreedom(6.0);
 assertEquals(6.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }
}