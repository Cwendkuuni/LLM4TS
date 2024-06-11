import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistribution distribution;

 @Before
 public void setup() {
 distribution = new FDistributionImpl(2.0, 3.0);
 }

 /**
 * Test method for {@link FDistributionImpl#FDistributionImpl(double, double)}.
 */
 @Test
 public void testFDistributionImpl() {
 // Check if the constructor is working correctly
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test method for {@link FDistributionImpl#cumulativeProbability(double)}.
 */
 @Test
 public void testCumulativeProbability() throws MathException {
 // Test for valid inputs
 double x = 1.5;
 double expectedResult = 0.9008229169129041;
 double result = distribution.cumulativeProbability(x);
 assertEquals(expectedResult, result, 0.000001);

 // Test for edge cases (minimum and maximum)
 double minValue = Statics.MIN_DOUBLE;
 double maxValue = Statics.MAX_DOUBLE;
 expectedResult = Beta.regularizedBeta((2.0 * minValue) / (3.0 + 2.0 * minValue), 1.0, 1.5);
 result = distribution.cumulativeProbability(minValue);
 assertEquals(expectedResult, result, 0.000001);

 expectedResult = 1.0;
 result = distribution.cumulativeProbability(maxValue);
 assertEquals(expectedResult, result, 0.000001);

 // Test for negative input
 double negativeInput = -1.0;
 try {
 distribution.cumulativeProbability(negativeInput);
 fail("Expected MathException not thrown.");
 } catch (MathException e) {
 // Expected
 }
 }

 /**
 * Test method for {@link FDistributionImpl#inverseCumulativeProbability(double)}.
 */
 @Test
 public void testInverseCumulativeProbability() throws MathException {
 // Test for valid inputs
 double p = 0.8;
 double expectedResult = 1.513624383409385;
 double result = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedResult, result, 0.000001);

 // Test for edge cases (minimum and maximum)
 double minProbability = 0.0;
 double maxProbability = 1.0;
 double minValue = Statics.MIN_DOUBLE;
 double maxValue = Statics.MAX_DOUBLE;
 expectedResult = minValue;
 result = distribution.inverseCumulativeProbability(minProbability);
 assertEquals(expectedResult, result, 0.000001);

 expectedResult = maxValue;
 result = distribution.inverseCumulativeProbability(maxProbability);
 assertEquals(expectedResult, result, 0.000001);

 // Test for invalid input (p < 0 or p > 1)
 double invalidProbability = -1.0;
 try {
 distribution.inverseCumulativeProbability(invalidProbability);
 fail("Expected IllegalArgumentException not thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 invalidProbability = 1.1;
 try {
 distribution.inverseCumulativeProbability(invalidProbability);
 fail("Expected MathException not thrown.");
 } catch (MathException e) {
 // Expected
 }
 }

 /**
 * Test method for {@link FDistributionImpl#getNumeratorDegreesOfFreedom()}.
 */
 @Test
 public void testGetNumeratorDegreesOfFreedom() {
 double degreesOfFreedom = 4.0;
 distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
 assertEquals(degreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test method for {@link FDistributionImpl#getDenominatorDegreesOfFreedom()}.
 */
 @Test
 public void testGetDenominatorDegreesOfFreedom() {
 double degreesOfFreedom = 5.0;
 distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
 assertEquals(degreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test method for {@link FDistributionImpl#setNumeratorDegreesOfFreedom(double)}.
 */
 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 double degreesOfFreedom = 2.0;
 distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
 assertEquals(degreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 /**
 * Test method for {@link FDistributionImpl#setDenominatorDegreesOfFreedom(double)}.
 */
 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 double degreesOfFreedom = 3.0;
 distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
 assertEquals(degreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }
}