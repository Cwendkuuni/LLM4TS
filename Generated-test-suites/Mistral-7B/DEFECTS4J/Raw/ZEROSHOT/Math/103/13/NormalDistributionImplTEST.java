import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();
 NormalDistributionImpl normalDistributionWithMeanAndStdDev = new NormalDistributionImpl(1.0, 2.0);
 }

 @Test
 public void testNormalDistributionImplGettersAndSetters() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl();

 assertEquals(0.0, normalDistribution.getMean(), 0.001);
 assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.001);

 normalDistribution.setMean(2.5);
 normalDistribution.setStandardDeviation(3.0);

 assertEquals(2.5, normalDistribution.getMean(), 0.001);
 assertEquals(3.0, normalDistribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplCumulativeProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.001);
 assertEquals(0.841345, normalDistribution.cumulativeProbability(0.5), 0.001);
 assertEquals(0.997344, normalDistribution.cumulativeProbability(1), 0.001);

 NormalDistributionImpl normalDistributionNegativeMean = new NormalDistributionImpl(0, -1);
 assertEquals(0.5, normalDistributionNegativeMean.cumulativeProbability(0), 0.001);
 assertEquals(0.15685, normalDistributionNegativeMean.cumulativeProbability(-0.5), 0.001);
 assertEquals(0, normalDistributionNegativeMean.cumulativeProbability(-1), 0.001);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 assertEquals(0.0, normalDistribution.inverseCumulativeProbability(0), 0.001);
 assertEquals(0.5, normalDistribution.inverseCumulativeProbability(0.5), 0.001);
 assertEquals(1.0, normalDistribution.inverseCumulativeProbability(1), 0.001);

 NormalDistributionImpl normalDistributionNegativeMean = new NormalDistributionImpl(0, -1);
 assertEquals(Double.NEGATIVE_INFINITY, normalDistributionNegativeMean.inverseCumulativeProbability(0), 0.0);
 assertEquals(-0.5, normalDistributionNegativeMean.inverseCumulativeProbability(0.5), 0.001);
 assertEquals(Double.NEGATIVE_INFINITY, normalDistributionNegativeMean.inverseCumulativeProbability(1), 0.0);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbabilityThrowsException() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 assertThrows(IllegalArgumentException.class, () -> normalDistribution.inverseCumulativeProbability(-1));
 assertThrows(IllegalArgumentException.class, () -> normalDistribution.inverseCumulativeProbability(2));
 }

 @Test
 public void testNormalDistributionImplExceptionOnCumulativeProbability() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 assertThrows(MathException.class, () -> normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY));
 assertThrows(MathException.class, () -> normalDistribution.cumulativeProbability(Double.NEGATIVE_INFINITY));
 }

 @Test
 public void testNormalDistributionImplMethodChaining() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 double result = normalDistribution.setMean(2.5).setStandardDeviation(2.5).cumulativeProbability(1.2345);
 assertEquals(0.459396, result, 0.001);
 }

 @Test
 public void testNormalDistributionImplMaxIterationsExceededException() throws Exception {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 assertThrows(MaxIterationsExceededException.class, () -> normalDistribution.cumulativeProbability(Double.MAX_VALUE));
 }

 @Test
 public void testNormalDistributionIMPLInverseCumulativeProbabilityMultipleRoots() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 double result = normalDistribution.inverseCumulativeProbability(0.3416);
 assertEquals(0.3416, result, 0.001);

 result = normalDistribution.inverseCumulativeProbability(0.3416);
 assertEquals(0.3416, result, 0.001);
 }

 @Test
 public void testNormalDistributionIMPLInverseCumulativeProbabilityFailToFindRoot() {
 NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

 assertThrows(MathException.class, () -> normalDistribution.inverseCumulativeProbability(1.23456789));
 }
}