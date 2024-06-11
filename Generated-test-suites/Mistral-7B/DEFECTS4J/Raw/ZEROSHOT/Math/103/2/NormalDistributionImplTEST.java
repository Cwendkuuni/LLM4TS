import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplCreation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
 assertEquals(2.5, normalDistribution.getMean(), 0.001);
 assertEquals(1.5, normalDistribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplZeroMeanOneStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 assertEquals(0.0, normalDistribution.getMean(), 0.001);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
 double result;

 // Test for x = mean
 result = normalDistribution.cumulativeProbability(0);
 assertEquals(0.5, result, 0.001);

 // Test for x < mean
 result = normalDistribution.cumulativeProbability(-2.5);
 assertEquals(0.0, result, 0.001);

 // Test for x = mean + standard deviation
 result = normalDistribution.cumulativeProbability(1.5);
 assertEquals(0.841344746, result, 0.001);

 // Test for x > mean + standard deviation
 result = normalDistribution.cumulativeProbability(3.5);
 assertEquals(1.0, result, 0.001);

 // Test for x = -20 * standard deviation
 result = normalDistribution.cumulativeProbability(-30);
 assertEquals(0.0, result, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationNegativeValue() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
 normalDistribution.setStandardDeviation(-1);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
 double p, x;

 // Test for p = 0
 p = 0;
 x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(Double.NEGATIVE_INFINITY, x, 0.001);

 // Test for p = 0.25
 p = 0.25;
 x = normalDistribution.inverseCumulativeProbability(p);
 assertTrue(x < 0);

 // Test for p = 0.5
 p = 0.5;
 x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(0.0, x, 0.001);

 // Test for p = 0.75
 p = 0.75;
 x = normalDistribution.inverseCumulativeProbability(p);
 assertTrue(x > 0);

 // Test for p = 1
 p = 1;
 x = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x, 0.001);
 }
}