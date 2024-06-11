import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplWithMeanAndStandardDeviation() {
 NormalDistribution normalDistribution = new NormalDistributionImpl(2.5, 1.2);
 Equals tester = new Equals();

 // Testing the getMean() method
 assertEquals("Mean should be 2.5", 2.5, normalDistribution.getMean(), 0.0);

 // Testing the getStandardDeviation() method
 assertEquals("Standard Deviation should be 1.2", 1.2, normalDistribution.getStandardDeviation(), 0.0);

 // Testing the cumulativeProbability() method
 assertEquals("CDF at x = 1.5 should be 0.4216", 0.4216, normalDistribution.cumulativeProbability(1.5), 0.001);

 // Testing the inverseCumulativeProbability() method
 assertEquals("Inverse CDF at p = 0.4216 should be 1.5", 1.5, normalDistribution.inverseCumulativeProbability(0.4216), 0.01);

 // Testing the equality
 assertTrue("NormalDistributionImpl with given parameters should be equal to itself", tester.equals(normalDistribution));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationWithNegativeValue() {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 normalDistribution.setStandardDeviation(-1.2);
 }

 @Test
 public void testNormalDistributionImplWithMeanZeroAndStandardDeviationOne() {
 NormalDistribution normalDistribution = new NormalDistributionImpl();
 Equals tester = new Equals();

 // Testing the getMean() method
 assertEquals("Mean should be 0.0", 0.0, normalDistribution.getMean(), 0.0);

 // Testing the getStandardDeviation() method
 assertEquals("Standard Deviation should be 1.0", 1.0, normalDistribution.getStandardDeviation(), 0.0);

 // Testing the cumulativeProbability() method
 assertEquals("CDF at x = 0.5 should be 0.6827", 0.6827, normalDistribution.cumulativeProbability(0.5), 0.001);

 // Testing the inverseCumulativeProbability() method
 assertEquals("Inverse CDF at p = 0.6827 should be 0.5", 0.5, normalDistribution.inverseCumulativeProbability(0.6827), 0.01);

 // Testing the equality
 assertTrue("NormalDistributionImpl should be equal to itself", tester.equals(normalDistribution));
 }
}