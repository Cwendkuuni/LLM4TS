import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Before
 public void setup() {
 distribution = new NormalDistributionImpl(0.0, 1.0);
 }

 /**
 * Test constructor with valid arguments.
 */
 @Test
 public void testNormalDistributionImplConstructor() {
 NormalDistribution normalDistribution = new NormalDistribution();
 NormalDistributionImpl expectedNormalDistribution = new NormalDistributionImpl(normalDistribution.getMean(), normalDistribution.getStandardDeviation());
 assertEquals(expectedNormalDistribution, distribution);
 }

 /**
 * Test getMean method.
 */
 @Test
 public void testGetMean() {
 assertEquals(0.0, distribution.getMean(), 0.01);
 distribution.setMean(1.0);
 assertEquals(1.0, distribution.getMean(), 0.01);
 }

 /**
 * Test setMean method with invalid argument.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanWithInvalidArgument() {
 distribution.setMean(-1.0);
 }

 /**
 * Test getStandardDeviation method.
 */
 @Test
 public void testGetStandardDeviation() {
 assertEquals(1.0, distribution.getStandardDeviation(), 0.01);
 distribution.setStandardDeviation(2.0);
 assertEquals(2.0, distribution.getStandardDeviation(), 0.01);
 }

 /**
 * Test setStandardDeviation method with invalid argument.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationWithInvalidArgument() {
 distribution.setStandardDeviation(-1.0);
 }

 /**
 * Test cumulativeProbability method with valid argument.
 */
 @Test
 public void testCumulativeProbabilityWithValidArgument() {
 double expectedResult = 0.5 * (1.0 + Erf.erf((1.0 - distribution.getMean()) / (distribution.getStandardDeviation() * Math.sqrt(2.0))));
 double actualResult = distribution.cumulativeProbability(1.0);
 assertEquals(expectedResult, actualResult, 0.01);
 }

 /**
 * Test cumulativeProbability method with extreme arguments.
 */
 @Test
 public void testCumulativeProbabilityWithExtremeArguments() {
 // Test with a very small argument
 double expectedResult = 0.5;
 double actualResult = distribution.cumulativeProbability(-10 * distribution.getStandardDeviation());
 assertEquals(expectedResult, actualResult, 0.01);
 // Test with a very large argument
 expectedResult = 0.5;
 actualResult = distribution.cumulativeProbability(10 * distribution.getStandardDeviation());
 assertEquals(expectedResult, actualResult, 0.01);
 }

 /**
 * Test inverseCumulativeProbability method with valid arguments.
 */
 @Test
 public void testInverseCumulativeProbabilityWithValidArguments() {
 double p = 0.25;
 double expectedResult = distribution.inverseCumulativeProbability(p);
 double actualResult = Erf.erfInv(2 * p - 1) * distribution.getStandardDeviation() + distribution.getMean();
 assertEquals(expectedResult, actualResult, 0.01);
 }

 /**
 * Test inverseCumulativeProbability method with extreme arguments.
 */
 @Test
 public void testInverseCumulativeProbabilityWithExtremeArguments() {
 // Test with p=0
 double expectedResult = Double.NEGATIVE_INFINITY;
 double actualResult = distribution.inverseCumulativeProbability(0.0);
 assertEquals(expectedResult, actualResult);
 // Test with p=1
 expectedResult = Double.POSITIVE_INFINITY;
 actualResult = distribution.inverseCumulativeProbability(1.0);
 assertEquals(expectedResult, actualResult);
 }

 /**
 * Test inverseCumulativeProbability method with invalid argument.
 */
 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithInvalidArgument() {
 // Test with negative p
 distribution.inverseCumulativeProbability(-0.1);
 }

 // Add more test cases as needed for other methods and edge cases

}