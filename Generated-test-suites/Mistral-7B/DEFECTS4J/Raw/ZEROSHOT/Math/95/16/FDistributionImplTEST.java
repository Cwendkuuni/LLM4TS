import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

 private FDistribution distribution;

 @Test
 public void testFDistributionConstruction() {
 // Test construction with valid parameters
 distribution = new FDistributionImpl(3.0, 5.0);
 assertEquals("Numerator degrees of freedom should be set correctly", 3.0,
 distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals("Denominator degrees of freedom should be set correctly", 5.0,
 distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionInvalidNumeratorDegreesOfFreedom() {
 // Test construction with invalid numerator degrees of freedom
 distribution = new FDistributionImpl(-3.0, 5.0);
 fail("IllegalArgumentException should be thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFDistributionInvalidDenominatorDegreesOfFreedom() {
 // Test construction with invalid denominator degrees of freedom
 distribution = new FDistributionImpl(3.0, -5.0);
 fail("IllegalArgumentException should be thrown.");
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 // Test cumulative probability for various input values
 distribution = new FDistributionImpl(3.0, 5.0);
 double[] xValues = {-1.0, 0.0, 1.0, 2.0, 3.0, 5.0, 10.0};
 double[] expectedCumulativeProbabilities = {0.0, 0.0436, 0.1536, 0.3615,
 0.5415, 0.9591, 1.0};
 for (int i = 0; i < xValues.length; i++) {
 double actualCumulativeProbability = distribution.cumulativeProbability(xValues[i]);
 assertEquals("Cumulative probability for x = " + xValues[i] + " is incorrect.",
 expectedCumulativeProbabilities[i], actualCumulativeProbability, 0.001);
 }
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 // Test inverse cumulative probability for various input probabilities
 distribution = new FDistributionImpl(3.0, 5.0);
 double[] pValues = {0.0, 0.25, 0.5, 0.75, 0.95};
 double[] expectedXValues = {0.0, 0.8567, 1.5223, 2.3768, 5.6315};
 for (int i = 0; i < pValues.length; i++) {
 double actualXValue = distribution.inverseCumulativeProbability(pValues[i]);
 assertEquals("Inverse cumulative probability for p = " + pValues[i] +
 " is incorrect.", expectedXValues[i], actualXValue, 0.001);
 }
 }
}