import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistribution distribution;

 @Before
 public void setup() {
 distribution = new FDistributionImpl(2.0, 4.0);
 }

 @Test
 public void testConstructWithValidNumeratorAndDenominatorDegreesOfFreedom() {
 // test construction with valid degrees of freedom
 FDistribution expectedDistribution = new FDistribution(2.0, 4.0);
 assertEquals(expectedDistribution.getCumulativeProbability(0.0),
 distribution.cumulativeProbability(0.0), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidNumeratorDegreesOfFreedom() {
 // test construction with invalid degrees of freedom for numerator
 distribution = new FDistributionImpl(-2.0, 4.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidDenominatorDegreesOfFreedom() {
 // test construction with invalid degrees of freedom for denominator
 distribution = new FDistributionImpl(2.0, -4.0);
 }

 @Test
 public void testCumulativeProbabilityWithValidInput() throws MathException {
 // test cumulative probability with valid input
 double x = 1.0;
 double expectedCumulativeProbability = 0.82111690055961;
 double actualCumulativeProbability = distribution.cumulativeProbability(x);
 assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithNegativeInput() throws MathException {
 // test cumulative probability with negative input
 double x = -1.0;
 distribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbabilityWithValidInput() throws MathException {
 // test inverse cumulative probability with valid input
 double p = 0.9;
 double expectedValue = 1.4856789243256;
 double actualValue = distribution.inverseCumulativeProbability(p);
 assertEquals(expectedValue, actualValue, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithInvalidInput() throws MathException {
 // test inverse cumulative probability with invalid input (p < 0)
 distribution.inverseCumulativeProbability(0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithInvalidInput2() throws MathException {
 // test inverse cumulative probability with invalid input (p > 1)
 distribution.inverseCumulativeProbability(1.1);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedomWithValidInput() {
 // test setting numerator degrees of freedom with valid input
 double newNumeratorDegreesOfFreedom = 3.0;
 distribution.setNumeratorDegreesOfFreedom(newNumeratorDegreesOfFreedom);
 assertEquals(newNumeratorDegreesOfFreedom, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumeratorDegreesOfFreedomWithInvalidInput() {
 // test setting numerator degrees of freedom with invalid input
 distribution.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedomWithValidInput() {
 // test setting denominator degrees of freedom with valid input
 double newDenominatorDegreesOfFreedom = 5.0;
 distribution.setDenominatorDegreesOfFreedom(newDenominatorDegreesOfFreedom);
 assertEquals(newDenominatorDegreesOfFreedom, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDenominatorDegreesOfFreedomWithInvalidInput() {
 // test setting denominator degrees of freedom with invalid input
 distribution.setDenominatorDegreesOfFreedom(-1.0);
 }
}