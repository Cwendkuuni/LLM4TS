import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test
 public void testCreateHypergeometricDistribution() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 assertEquals(5, hd.getPopulationSize());
 assertEquals(2, hd.getNumberOfSuccesses());
 assertEquals(3, hd.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testCreateNegativeNumberOfSuccesses() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 new HypergeometricDistribution(5, -1, 3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testCreateZeroPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 new HypergeometricDistribution(5, 2, 0);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testCreateNumberOfSuccessesGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 new HypergeometricDistribution(5, 6, 3);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testCreateSampleSizeGreaterThanPopulationSize() throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
 new HypergeometricDistribution(5, 2, 6);
 }

 @Test
 public void testGetDomain() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 assertEquals(3, domain[0]);
 assertEquals(3, domain[1]);
 }

 @Test
 public void testProbabilityZero() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 assertEquals(0.0, hd.probability(0), 1e-14);
 }

 @Test
 public void testProbabilityLowerBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 assertEquals(0.0344, hd.probability(domain[0]), 1e-14);
 }

 @Test
 public void testProbabilityUpperBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 assertEquals(0.1011, hd.probability(domain[1]), 1e-14);
 }

 @Test
 public void testProbabilityMiddle() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 int x = domain[0] + (domain[1] - domain[0]) / 2;
 assertEquals(0.0556, hd.probability(x), 1e-14);
 }

 @Test
 public void testCumulativeProbabilityZero() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 assertEquals(0.0, hd.cumulativeProbability(0), 1e-14);
 }

 @Test
 public void testCumulativeProbabilityLowerBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 assertEquals(0.0344, hd.cumulativeProbability(domain[0]), 1e-14);
 }

 @Test
 public void testCumulativeProbabilityMiddle() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 int x = domain[0] + (domain[1] - domain[0]) / 2;
 assertEquals(0.0899, hd.cumulativeProbability(x), 1e-14);
 }

 @Test
 public void testCumulativeProbabilityUpperBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 assertEquals(0.1353, hd.cumulativeProbability(domain[1]), 1e-14);
 }

 @Test
 public void testUpperCumulativeProbabilityZero() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 assertEquals(1.0, hd.upperCumulativeProbability(0), 1e-14);
 }

 @Test
 public void testUpperCumulativeProbabilityLowerBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 assertEquals(0.9657, hd.upperCumulativeProbability(domain[0]), 1e-14);
 }

 @Test
 public void testUpperCumulativeProbabilityMiddle() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 int x = domain[0] + (domain[1] - domain[0]) / 2;
 assertEquals(0.9201, hd.upperCumulativeProbability(x), 1e-14);
 }

 @Test
 public void testUpperCumulativeProbabilityUpperBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int[] domain = hd.getDomain();
 assertEquals(0.8648, hd.upperCumulativeProbability(domain[1]), 1e-14);
 }

 @Test
 public void testNumericalMean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 assertEquals(2.0, hd.getNumericalMean(), 1e-14);
 }

 @Test
 public void testNumericalVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 double numericalVariance = hd.getNumericalVariance();
 assertEquals(0.0337, numericalVariance, 1e-14);
 }

 @Test
 public void testIsSupportConnected() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 assertTrue(hd.isSupportConnected());
 }

 @Test
 public void testGetSupportLowerBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int lowerBound = hd.getSupportLowerBound();
 assertEquals(1, lowerBound);
 }

 @Test
 public void testGetSupportUpperBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(5, 2, 3);
 int upperBound = hd.getSupportUpperBound();
 assertEquals(3, upperBound);
 }
}