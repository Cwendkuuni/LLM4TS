import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Test
 public void testNormalDistributionImplConstructionWithMeanAndStandardDeviation() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0.5, 1.0);
 assertEquals(0.5, normalDistribution.getMean(), 0.01);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.01);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNormalDistributionImplConstructionWithNegativeStandardDeviation() {
 new NormalDistributionImpl(0.5, -1.0);
 }

 @Test
 public void testNormalDistributionImplDensity() {
 distribution = new NormalDistributionImpl(0, 1);
 double density = distribution.density(0.5);
 assertEquals(0.1530347204522952, density, 0.0001);
 }

 @Test
 public void testNormalDistributionImplCumulativeProbability() throws MathException {
 distribution = new NormalDistributionImpl(0, 1);
 double cumulativeProbability = distribution.cumulativeProbability(0.5);
 assertEquals(0.682689462136619, cumulativeProbability, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplCumulativeProbabilityOutOfRange() throws MathException {
 distribution = new NormalDistributionImpl(0, 1);
 distribution.cumulativeProbability(2.5);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
 distribution = new NormalDistributionImpl(0, 1);
 double inverseCumulativeProbability = distribution.inverseCumulativeProbability(0.25);
 assertEquals(-0.806282435711452, inverseCumulativeProbability, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testNormalDistributionImplInverseCumulativeProbabilityOutOfRange() throws MathException {
 distribution = new NormalDistributionImpl(0, 1);
 distribution.inverseCumulativeProbability(1.2);
 }

 @Test
 public void testNormalDistributionImplSample() throws MathException {
 distribution = new NormalDistributionImpl(0, 1);
 double sample = distribution.sample();
 assertTrue(sample > -3 && sample < 3);
 }

}