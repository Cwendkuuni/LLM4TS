import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistributionTest;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HypergeometricDistributionTest {

 @Test
 public void testHypergeometricDistributionConstruction() {
 HypergeometricDistribution hd;

 try {
 hd = new HypergeometricDistribution(5, 3, 4);
 } catch (NotPositiveException | NotStrictlyPositiveException | NumberIsTooLargeException e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage());
 }

 assertEquals(3, hd.getNumberOfSuccesses());
 assertEquals(5, hd.getPopulationSize());
 assertEquals(4, hd.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
 HypergeometricDistribution hd;
 hd = new HypergeometricDistribution(10, -2, 3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroPopulationSize() {
 HypergeometricDistribution hd;
 hd = new HypergeometricDistribution(0, 3, 3);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testHypergeometricDistributionZeroNumberOfSuccesses() {
 HypergeometricDistribution hd;
 hd = new HypergeometricDistribution(10, 0, 3);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution hd;
 hd = new HypergeometricDistribution(10, 12, 3);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution hd;
 hd = new HypergeometricDistribution(10, 3, 12);
 }

 @Test
 public void testHypergeometricDistributionProbability() {
 HypergeometricDistribution hd = null;
 double prob = 0;
 int x = 0;

 try {
 hd = new HypergeometricDistribution(10, 3, 4);
 } catch (Exception e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage());
 }

 prob = hd.probability(x);
 assertEquals(hd.cumulativeProbability(x), prob, 1e-14);

 prob = hd.probability(hd.getPopulationSize() - 1);
 assertEquals(1.0 - hd.cumulativeProbability(hd.getPopulationSize() - 1), prob, 1e-14);

 prob = hd.probability(hd.getPopulationSize());
 assertEquals(1.0, prob, 1e-14);
 }

 @Test
 public void testHypergeometricDistributionCumulativeProbability() {
 HypergeometricDistribution hd = null;
 double x = 0.0;
 double cdf = 0.0;

 try {
 hd = new HypergeometricDistribution(10, 3, 4);
 } catch (Exception e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage());
 }

 cdf = hd.cumulativeProbability(0);
 assertEquals(hd.probability(0), cdf, 1e-14);

 x = hd.getSampleSize() - 1;
 cdf = hd.cumulativeProbability(hd.getSampleSize() - 1);
 assertEquals(1.0 - hd.probability(hd.getSampleSize() - 1), cdf, 1e-14);

 x = hd.getPopulationSize();
 cdf = hd.cumulativeProbability(hd.getPopulationSize());
 assertEquals(hd.probability(hd.getPopulationSize()), cdf, 1e-14);
 }

 @Test
 public void testHypergeometricDistributionUpperTailCumulativeProbability() {
 HypergeometricDistribution hd = null;
 double x = 0.0;
 double utc = 0.0;

 try {
 hd = new HypergeometricDistribution(10, 3, 4);
 } catch (Exception e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage());
 }

 utc = hd.upperCumulativeProbability(0);
 assertEquals(1.0 - hd.probability(0), utc, 1e-14);

 utc = hd.upperCumulativeProbability(hd.getPopulationSize());
 assertEquals(hd.probability(hd.getPopulationSize()), utc, 1e-14);

 utc = hd.upperCumulativeProbability(hd.getSampleSize());
 assertEquals(hd.probability(hd.getSampleSize()), utc, 1e-14);
 }

 @Test
 public void testHypergeometricDistributionLowerTailCumulativeProbability() {
 HypergeometricDistribution hd = null;
 double x = 0.0;
 double ltc = 0.0;

 try {
 hd = new HypergeometricDistribution(10, 3, 4);
 } catch (Exception e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage());
 }

 ltc = hd.lowerCumulativeProbability(0);
 assertEquals(hd.probability(0), 1.0 - ltc, 1e-14);

 ltc = hd.lowerCumulativeProbability(hd.getSampleSize());
 assertEquals(hd.probability(hd.getSampleSize()), ltc, 1e-14);

 ltc = hd.lowerCumulativeProbability(hd.getPopulationSize());
 assertEquals(0.0, ltc, 1e-14);
 }

 @Test
 public void testHypergeometricDistributionNumericalVariance() {
 HypergeometricDistribution hd = null;
 double nv = 0.0;

 try {
 hd = new HypergeometricDistribution(10, 3, 4);
 } catch (Exception e) {
 throw new RuntimeException("Unexpected exception: " + e.getMessage());
 }

 nv = hd.getNumericalVariance();
 assertTrue(Double.isFinite(nv));
 }
}