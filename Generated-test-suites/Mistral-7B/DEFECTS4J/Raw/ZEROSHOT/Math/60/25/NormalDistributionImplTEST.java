import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NormalDistributionImplTest {

 @Test
 public void testNormalDistributionCreation() {
 NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
 NormalDistribution distMean1SD = new NormalDistributionImpl(1.0, 1.0);
 NormalDistribution distMean2SD = new NormalDistributionImpl(2.0, 1.0);
 NormalDistribution distMean0SD = new NormalDistributionImpl();

 assertEquals("Mean should be 0.0", 0.0, distMean0SD.getMean(), 1e-6);
 assertEquals("Mean should be 1.0", 1.0, distMean1SD.getMean(), 1e-6);
 assertEquals("Mean should be 2.0", 2.0, distMean2SD.getMean(), 1e-6);
 }

 @Test
 public void testNegativeStandardDeviation() {
 assertThrows(NotStrictlyPositiveException.class, () -> new NormalDistributionImpl(0.0, -1.0));
 }

 @Test
 public void testDensity() {
 NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
 double x = 1.0;
 double density = dist.density(x);

 assertEquals("Density should be 0.3989422804010993", 0.3989422804010993, density, 1e-6);
 }

 @Test
 public void testDistributionFunction() throws Exception {
 NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
 double x = -3.0;
 double p = dist.cumulativeProbability(x);

 assertEquals("Probability should be 0.0", 0.0, p, 1e-6);

 x = 0.0;
 p = dist.cumulativeProbability(x);

 assertEquals("Probability should be 0.5", 0.5, p, 1e-6);

 x = 3.0;
 p = dist.cumulativeProbability(x);

 assertEquals("Probability should be 0.5", 0.5, p, 1e-6);
 }

 @Test
 public void testInverseDistributionFunction() throws Exception {
 NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
 double p = 0.1;
 double inverseCdf = dist.inverseCumulativeProbability(p);

 assertEquals("InverseCDF should be -1.2816391042857165", -1.2816391042857165, inverseCdf, 1e-6);

 p = 0.5;
 inverseCdf = dist.inverseCumulativeProbability(p);

 assertEquals("InverseCDF should be 0.0", 0.0, inverseCdf, 1e-6);

 p = 0.9;
 inverseCdf = dist.inverseCumulativeProbability(p);

 assertEquals("InverseCDF should be 1.2816391042857165", 1.2816391042857165, inverseCdf, 1e-6);
 }

 @Test
 public void testSample() throws Exception {
 NormalDistribution dist = new NormalDistributionImpl(0.0, 1.0);
 Random random = new Random();
 double sample;

 for (int i = 0; i < 10; i++) {
 sample = dist.sample();
 assertEquals("Sample should be in range [-3,3]", -3.0, sample, 3.0);
 }
 }
}