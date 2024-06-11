import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionImplTest {

 private FDistribution distribution;

 @Test
 public void testFDistributionImplConstruction() {
 distribution = new FDistributionImpl(2.0, 4.0);
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(4.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 distribution = new FDistributionImpl(2.0, 4.0);

 double x = 1.5;
 double cdf = distribution.cumulativeProbability(x);
 double expectedCdf = Beta.regularizedBeta((2.0 * x) / (4.0 + 2.0 * x), 1.0, 2.0);
 assertEquals(expectedCdf, cdf, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeX() throws Exception {
 distribution = new FDistributionImpl(2.0, 4.0);
 distribution.cumulativeProbability(-1.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 distribution = new FDistributionImpl(2.0, 4.0);

 double p = 0.95;
 double inverseCdf = distribution.inverseCumulativeProbability(p);
 double expectedInverseCdf = solutionQuadratic(p, 2.0, 4.0);
 assertEquals(expectedInverseCdf, inverseCdf, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityInvalidProbability() throws Exception {
 distribution = new FDistributionImpl(2.0, 4.0);
 distribution.inverseCumulativeProbability(-1.0);
 }

 @Test
 public void testInverseCumulativeProbabilityZeroProbability() throws Exception {
 distribution = new FDistributionImpl(2.0, 4.0);
 double inverseCdf = distribution.inverseCumulativeProbability(0.0);
 assertEquals(0.0, inverseCdf, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityOneProbability() throws Exception {
 distribution = new FDistributionImpl(2.0, 4.0);
 double inverseCdf = distribution.inverseCumulativeProbability(1.0);
 assertEquals(Double.POSITIVE_INFINITY, inverseCdf, 0.0);
 }

 private double solutionQuadratic(double p, double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom) {
 double a = 1.0 / (2.0 * denominatorDegreesOfFreedom);
 double b = -(numeratorDegreesOfFreedom / (2.0 * denominatorDegreesOfFreedom)) + p;
 double c = p * p / (2.0 * denominatorDegreesOfFreedom) - p / (2.0 * numeratorDegreesOfFreedom) + 1.0 / (2.0 * numeratorDegreesOfFreedom);

 double discriminant = b * b - 4.0 * a * c;
 if (discriminant < 0.0) {
 throw new RuntimeException("No solution found.");
 }

 double solution1 = (b + Math.sqrt(discriminant)) / (2.0 * a);
 double solution2 = (b - Math.sqrt(discriminant)) / (2.0 * a);

 return solution1 > solution2 ? solution1 : solution2;
 }

}