import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

 @Test
 public void testConstructors() {
 HypergeometricDistribution hd;

 // Test constructor with valid arguments
 hd = new HypergeometricDistribution(10, 5, 5);
 assertEquals("populationSize", 10, hd.getPopulationSize());
 assertEquals("numberOfSuccesses", 5, hd.getNumberOfSuccesses());
 assertEquals("sampleSize", 5, hd.getSampleSize());

 // Test constructor with invalid arguments - populationSize < 0
 try {
 hd = new HypergeometricDistribution(-5, 5, 5);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // expected
 }

 // Test constructor with invalid arguments - numberOfSuccesses < 0
 try {
 hd = new HypergeometricDistribution(10, -5, 5);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // expected
 }

 // Test constructor with invalid arguments - sampleSize < 0
 try {
 hd = new HypergeometricDistribution(10, 5, -5);
 fail("Expected NotPositiveException");
 } catch (NotPositiveException e) {
 // expected
 }

 // Test constructor with invalid arguments - numberOfSuccesses > populationSize
 try {
 hd = new HypergeometricDistribution(10, 6, 5);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // expected
 }

 // Test constructor with invalid arguments - sampleSize > populationSize
 try {
 hd = new HypergeometricDistribution(10, 5, 6);
 fail("Expected NumberIsTooLargeException");
 } catch (NumberIsTooLargeException e) {
 // expected
 }
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 // Test lower domain value
 assertEquals("cumulativeProbability(0)", 0.0, hd.cumulativeProbability(0), 0.001);

 // Test domain value
 assertEquals("cumulativeProbability(2)", 0.1388888888888889, hd.cumulativeProbability(2), 0.001);

 // Test upper domain value
 assertEquals("cumulativeProbability(5)", 0.609753125, hd.cumulativeProbability(5), 0.001);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 // Test lower domain value
 assertEquals("probability(0)", 0.0, hd.probability(0), 0.001);

 // Test domain value
 assertEquals("probability(2)", 0.08333333333333333, hd.probability(2), 0.001);

 // Test upper domain value
 assertEquals("probability(5)", 0.2734375, hd.probability(5), 0.001);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 // Test lower domain value
 assertEquals("upperCumulativeProbability(0)", 1.0, hd.upperCumulativeProbability(0), 0.001);

 // Test domain value
 assertEquals("upperCumulativeProbability(2)", 0.8611111111111111, hd.upperCumulativeProbability(2), 0.001);

 // Test upper domain value
 assertEquals("upperCumulativeProbability(5)", 0.4058823529411765, hd.upperCumulativeProbability(5), 0.001);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
 assertEquals("numericalMean", 3.75, hd.getNumericalMean(), 0.001);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
 double numericalVariance = hd.getNumericalVariance();
 assertEquals("numericalVariance is calculated", true, hd.isNumericalVarianceCalculated());
 assertEquals("numericalVariance", 1.317792388888889, numericalVariance, 0.001);
 }
}