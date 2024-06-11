import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistribution distribution;

 @Before
 public void setUp() {
 distribution = new NormalDistributionImpl(0, 1);
 }

 @Test
 public void testConstructorWithMeanAndStandardDeviation() {
 NormalDistribution expected = new NormalDistribution(0, 1);
 assertEquals(expected, distribution);
 }

 @Test
 public void testGetMean() {
 assertEquals(0, distribution.getMean(), 0.001);
 }

 @Test
 public void testGetStandardDeviation() {
 assertEquals(1, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testDensity() {
 // Test with mean and standard deviation
 double densityValue = distribution.density(0);
 assertEquals(0.398942280401, densityValue, 0.0001);

 // Test with negative value
 densityValue = distribution.density(-1);
 assertEquals(0.00013556933652, densityValue, 0.0001);

 // Test with positive value far from mean
 densityValue = distribution.density(5);
 assertEquals(7.45031089432E-11, densityValue, 0.0001);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 // Test with value close to mean
 double cumulativeProbabilityValue = distribution.cumulativeProbability(0);
 assertEquals(0.5, cumulativeProbabilityValue, 0.0001);

 // Test with value far from mean
 cumulativeProbabilityValue = distribution.cumulativeProbability(5);
 assertEquals(0.0001146355198, cumulativeProbabilityValue, 0.0001);

 // Test with value equal to mean
 cumulativeProbabilityValue = distribution.cumulativeProbability(0.0);
 assertEquals(0.5, cumulativeProbabilityValue, 0.0001);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException, NotStrictlyPositiveException {
 // Test with probability close to 0
 double inverseCumulativeProbabilityValue = distribution.inverseCumulativeProbability(0);
 assertEquals(-3.7126129283, inverseCumulativeProbabilityValue, 0.0001);

 // Test with probability close to 1
 inverseCumulativeProbabilityValue = distribution.inverseCumulativeProbability(1);
 assertEquals(0.3779649659, inverseCumulativeProbabilityValue, 0.0001);

 // Test with probability equal to 0
 try {
 distribution.inverseCumulativeProbability(0.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // Expected
 }

 // Test with probability equal to 1
 try {
 distribution.inverseCumulativeProbability(1.0);
 fail("Expected MathException");
 } catch (MathException e) {
 // Expected
 }
 }

 @Test
 public void testSample() throws MathException {
 double sampleValue;
 for (int i = 0; i < 10; i++) {
 sampleValue = distribution.sample();
 assertTrue(sampleValue > -3 && sampleValue < 3);
 }
 }
}