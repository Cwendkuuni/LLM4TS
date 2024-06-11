import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Before
 public void setUp() {
 distribution = new NormalDistributionImpl(0, 1);
 }

 // Test constructor with valid arguments
 @Test
 public void testNormalDistributionImplConstructor() {
 NormalDistribution expected = new NormalDistribution(0, 1);
 assertEquals(expected.getMean(), distribution.getMean(), 0.001);
 assertEquals(expected.getStandardDeviation(), distribution.getStandardDeviation(), 0.001);
 }

 // Test setMean method
 @Test
 public void testSetMean() {
 distribution.setMean(2.5);
 assertEquals(2.5, distribution.getMean(), 0.001);
 }

 // Test setStandardDeviation method
 @Test
 public void testSetStandardDeviation() {
 distribution.setStandardDeviation(2.5);
 assertEquals(2.5, distribution.getStandardDeviation(), 0.001);
 }

 // Test getMean method
 @Test
 public void testGetMean() {
 assertEquals(0.0, distribution.getMean(), 0.001);
 }

 // Test getStandardDeviation method
 @Test
 public void testGetStandardDeviation() {
 assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
 }

 // Test cumulativeProbability method with valid x
 @Test
 public void testCumulativeProbabilityValidX() throws MathException {
 double x = 0.5;
 double expected = 0.5;
 assertEquals(expected, distribution.cumulativeProbability(x), 0.001);
 }

 // Test cumulativeProbability method with x close to mean
 @Test
 public void testCumulativeProbabilityXCloseToMean() throws MathException {
 double x = 0.2;
 double expected = 0.47134264246; // Approximate value using calculator
 assertEquals(expected, distribution.cumulativeProbability(x), 0.01);
 }

 // Test cumulativeProbability method with x far from mean
 @Test
 public void testCumulativeProbabilityXFarFromMean() throws MathException {
 double x = 3.5;
 double expected = 0.0000001; // Approximate value using calculator
 assertEquals(expected, distribution.cumulativeProbability(x), 0.0);
 }

 // Test inverseCumulativeProbability method with valid p
 @Test
 public void testInverseCumulativeProbabilityValidP() throws MathException {
 double p = 0.5;
 double expected = 0.0;
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.001);
 }

 // Test inverseCumulativeProbability method with p close to 0
 @Test
 public void testInverseCumulativeProbabilityPCloseToZero() throws MathException {
 double p = 0.01;
 double expected = -2.306612408; // Approximate value using calculator
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.1);
 }

 // Test inverseCumulativeProbability method with p close to 1
 @Test
 public void testInverseCumulativeProbabilityPCloseToOne() throws MathException {
 double p = 0.99;
 double expected = 1.644934066; // Approximate value using calculator
 assertEquals(expected, distribution.inverseCumulativeProbability(p), 0.1);
 }

 // Test inverseCumulativeProbability method with invalid p
 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidP() throws MathException {
 distribution.inverseCumulativeProbability(-0.1);
 }

 // Test inverseCumulativeProbability method with p = 1.1
 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityPGreaterThanOne() throws MathException {
 distribution.inverseCumulativeProbability(1.1);
 }
}