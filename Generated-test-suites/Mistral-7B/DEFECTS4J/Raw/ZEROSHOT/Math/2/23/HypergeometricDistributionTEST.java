import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HypergeometricDistributionTest {

 @Test
 public void testConstructors() {
 HypergeometricDistribution hd1 = new HypergeometricDistribution(10, 5, 5);
 HypergeometricDistribution hd2 = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);
 HypergeometricDistribution hd3 = new HypergeometricDistribution(new Well19937c(), 10, 5, 6);

 assertEquals(10, hd1.getPopulationSize());
 assertEquals(5, hd1.getNumberOfSuccesses());
 assertEquals(5, hd1.getSampleSize());
 assertEquals(10, hd2.getPopulationSize());
 assertEquals(5, hd2.getNumberOfSuccesses());
 assertEquals(5, hd2.getSampleSize());
 assertEquals(10, hd3.getPopulationSize());
 assertEquals(5, hd3.getNumberOfSuccesses());
 assertEquals(6, hd3.getSampleSize());
 }

 @Test(expected = NotPositiveException.class)
 public void testNegativeNumberOfSuccesses() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, -5, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(0, 5, 5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroPopulationSize2() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 0, 5, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testNumberOfSuccessesLargerThanPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 11, 5);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSampleSizeLargerThanPopulationSize() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 11);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testSampleSizeLargerThanPopulationSize2() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 11);
 }

 @Test
 public void testCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(0.0, hd.cumulativeProbability(0), 1e-10);
 assertEquals(0.1428571428571429, hd.cumulativeProbability(1), 1e-10);
 assertEquals(0.4761904814813753, hd.cumulativeProbability(2), 1e-10);
 assertEquals(0.7499567086646475, hd.cumulativeProbability(3), 1e-10);
 assertEquals(0.958958958958959, hd.cumulativeProbability(4), 1e-10);
 assertEquals(1.0, hd.cumulativeProbability(5), 1e-10);
 }

 @Test
 public void testCumulativeProbabilityOutOfRange() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(0.0, hd.cumulativeProbability(-1), 1e-10);
 assertEquals(1.0, hd.cumulativeProbability(6), 1e-10);
 }

 @Test
 public void testProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(0.045, hd.probability(0), 1e-10);
 assertEquals(0.137, hd.probability(1), 1e-10);
 assertEquals(0.161, hd.probability(2), 1e-10);
 assertEquals(0.161, hd.probability(3), 1e-10);
 assertEquals(0.210, hd.probability(4), 1e-10);
 assertEquals(0.210, hd.probability(5), 1e-10);
 }

 @Test
 public void testProbabilityOutOfRange() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(0.0, hd.probability(-1), 1e-10);
 assertEquals(0.0, hd.probability(6), 1e-10);
 }

 @Test
 public void testUpperCumulativeProbability() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(0.958958958958959, hd.upperCumulativeProbability(0), 1e-10);
 assertEquals(0.862912968709677, hd.upperCumulativeProbability(1), 1e-10);
 assertEquals(0.72380543481179, hd.upperCumulativeProbability(2), 1e-10);
 assertEquals(0.5817948717841972, hd.upperCumulativeProbability(3), 1e-10);
 assertEquals(0.448205188855955, hd.upperCumulativeProbability(4), 1e-10);
 assertEquals(0.3333333333333333, hd.upperCumulativeProbability(5), 1e-10);
 }

 @Test
 public void testUpperCumulativeProbabilityOutOfRange() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(1.0, hd.upperCumulativeProbability(-1), 1e-10);
 assertEquals(0.0, hd.upperCumulativeProbability(6), 1e-10);
 }

 @Test
 public void testMean() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(3.75, hd.getNumericalMean(), 1e-10);
 }

 @Test
 public void testVariance() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(2.8488572552752583, hd.getNumericalVariance(), 1e-10);
 }

 @Test
 public void testNumericalVarianceCalculation() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(2.8488572552752583, hd.getNumericalVariance(), 1e-10);
 }

 @Test
 public void testNumericalVarianceCalculation2() {
 HypergeometricDistribution hd = new HypergeometricDistribution(new Well19937c(), 10, 5, 5);

 assertEquals(2.8488572552752583, hd.getNumericalVariance(), 1e-10);
 }

 @Test
 public void testSupportLowerBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(0, hd.getSupportLowerBound());
 }

 @Test
 public void testSupportUpperBound() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(5, hd.getSupportUpperBound());
 }

 @Test
 public void testSupportConnected() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);

 assertEquals(true, hd.isSupportConnected());
 }

 @Test
 public void testStats() {
 HypergeometricDistribution hd = new HypergeometricDistribution(10, 5, 5);
 double[] data = new double[10000];
 for (int i = 0; i < data.length; i++) {
 data[i] = hd.sample();
 }

 assertEquals(3.747363353562759, StatUtils.mean(data), 1e-5);
 assertEquals(2.852483173848646, StatUtils.variance(data), 1e-5);
 }
}