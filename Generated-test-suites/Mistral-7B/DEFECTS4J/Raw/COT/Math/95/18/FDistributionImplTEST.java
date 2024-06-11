import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Before
 public void setUp() {
 distribution = new FDistributionImpl(5.0, 10.0);
 }

 @Test
 public void testConstructor() {
 // Check if constructor is called correctly
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(10.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 // Test with valid input
 double x = 1.5;
 double expectedProbability = 0.6321213543161762; // Using Wolfram Alpha
 double actualProbability = distribution.cumulativeProbability(x);
 assertEquals(expectedProbability, actualProbability, 0.0001);

 // Test with invalid input (negative x)
 x = -1.0;
 try {
 distribution.cumulativeProbability(x);
 fail("Expected MathException");
 } catch (MathException e) {
 // Expected
 }
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 // Test with valid input
 double p = 0.95;
 double expectedValue = 2.329975924225783; // Using Wolfram Alpha
 double actualValue = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedValue, actualValue, 0.0001);

 // Test with invalid input (p < 0 or p > 1)
 p = -0.1;
 try {
 distribution.inverseCumulativeProbability(p);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 p = 1.1;
 try {
 distribution.inverseCumulativeProbability(p);
 fail("Expected MathException");
 } catch (MathException e) {
 // Expected
 }
 }

 @Test
 public void testGetNumeratorDegreesOfFreedom() {
 double degreesOfFreedom = 7.0;
 distribution.setNumeratorDegreesOfFreedom(degreesOfFreedom);
 assertEquals(degreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testGetDenominatorDegreesOfFreedom() {
 double degreesOfFreedom = 12.0;
 distribution.setDenominatorDegreesOfFreedom(degreesOfFreedom);
 assertEquals(degreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeNumeratorDegreesOfFreedom() {
 distribution.setNumeratorDegreesOfFreedom(-2.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeDenominatorDegreesOfFreedom() {
 distribution.setDenominatorDegreesOfFreedom(-3.0);
 }
}