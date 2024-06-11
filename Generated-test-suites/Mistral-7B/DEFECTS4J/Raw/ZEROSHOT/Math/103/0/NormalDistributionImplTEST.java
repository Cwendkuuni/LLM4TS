import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstructionWithMeanAndStandardDeviation() {
 NormalDistribution normalDistribution = new NormalDistributionImpl(1.0, 2.0);
 NormalDistribution expectedNormalDistribution = new NormalDistribution(1.0, 2.0);
 assertEquals(expectedNormalDistribution, normalDistribution);
 }

 @Test
 public void testNormalDistributionImplConstructionWithZeroMeanAndOneStandardDeviation() {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 NormalDistribution expectedNormalDistribution = new NormalDistribution();
 assertEquals(expectedNormalDistribution, normalDistribution);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNegativeStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testMeanGettersAndSetters() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double expectedMean = 0.0;
 assertEquals(expectedMean, normalDistribution.getMean());
 normalDistribution.setMean(2.0);
 assertEquals(2.0, normalDistribution.getMean());
 }

 @Test
 public void testStandardDeviationGettersAndSetters() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 double expectedStandardDeviation = 1.0;
 assertEquals(expectedStandardDeviation, normalDistribution.getStandardDeviation());
 normalDistribution.setStandardDeviation(3.0);
 assertEquals(3.0, normalDistribution.getStandardDeviation());
 }

 @Test
 public void testCumulativeProbability() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double x = 0.5;
 double expectedCumulativeProbability = 0.6826894621374134;
 double calculatedCumulativeProbability = normalDistribution.cumulativeProbability(x);
 assertEquals(expectedCumulativeProbability, calculatedCumulativeProbability, 0.0000001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityDiverges() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double x = 100.0;
 normalDistribution.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbability() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.5;
 double expectedResult = 0.0;
 double calculatedResult = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(expectedResult, calculatedResult, 0.0000001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityThrowsException() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 1.0;
 normalDistribution.inverseCumulativeProbability(p);
 }

 @Test
 public void testInverseCumulativeProbabilityWithSmallValues() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.0;
 double expectedResult = -3.14159265359;
 double calculatedResult = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(expectedResult, calculatedResult, 0.0000001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithLargeValues() throws MathException {
 NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 1.0;
 double expectedResult = 3.14159265359;
 double calculatedResult = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(expectedResult, calculatedResult, 0.0000001);
 }

 @Test
 public void testGetInitialDomain() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.5;
 double expectedValue = 0.0;
 double calculatedValue = normalDistribution.getInitialDomain(p);
 assertEquals(expectedValue, calculatedValue, 0.0000001);
 }

 @Test
 public void testGetDomainLowerBound() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.5;
 double expectedValue = -3.5;
 double calculatedValue = normalDistribution.getDomainLowerBound(p);
 assertEquals(expectedValue, calculatedValue, 0.0000001);
 }

 @Test
 public void testGetDomainUpperBound() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.5;
 double expectedValue = 3.5;
 double calculatedValue = normalDistribution.getDomainUpperBound(p);
 assertEquals(expectedValue, calculatedValue, 0.0000001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithLargeValues() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double x = 1000.0;
 normalDistribution.cumulativeProbability(x);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testCumulativeProbabilityWithMaxIterationsExceeded() throws MathException, MaxIterationsExceededException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double x = 1.01 * Math.E;
 normalDistribution.cumulativeProbability(x);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithNegativeX() throws MathException {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.0, 1.0);
 double x = -1.0;
 normalDistribution.cumulativeProbability(x);
 }
}