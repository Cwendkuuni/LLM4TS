import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FDistributionImplTest {

 private FDistributionImpl distribution;

 @Test
 public void testFDistributionImplConstructor() {
 distribution = new FDistributionImpl(2.0, 3.0);
 assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbabilityZero() throws Exception {
 distribution = new FDistributionImpl(1.0, 1.0);
 double result = distribution.cumulativeProbability(0.0);
 assertEquals(0.0, result, 0.0);
 }

 @Test
 public void testCumulativeProbabilityPositiveValue() throws Exception {
 distribution = new FDistributionImpl(1.0, 1.0);
 double result = distribution.cumulativeProbability(1.0);
 assertEquals(0.5, result, 0.01); // Approximate value
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeValue() throws Exception {
 distribution = new FDistributionImpl(1.0, 1.0);
 double result = distribution.cumulativeProbability(-1.0);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws Exception {
 distribution = new FDistributionImpl(1.0, 1.0);
 double result = distribution.inverseCumulativeProbability(0.0);
 assertEquals(0.0, result, 0.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityLessThanZero() throws Exception {
 distribution = new FDistributionImpl(1.0, 1.0);
 double result = distribution.inverseCumulativeProbability(-1.0);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityGreaterThanOne() throws Exception {
 distribution = new FDistributionImpl(1.0, 1.0);
 double result = distribution.inverseCumulativeProbability(2.0);
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 distribution = new FDistributionImpl(0.0, 1.0);
 distribution.setNumeratorDegreesOfFreedom(5.0);
 assertEquals(5.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumeratorDegreesOfFreedomNegative() {
 distribution = new FDistributionImpl(0.0, 1.0);
 distribution.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 distribution = new FDistributionImpl(1.0, 0.0);
 distribution.setDenominatorDegreesOfFreedom(5.0);
 assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDenominatorDegreesOfFreedomNegative() {
 distribution = new FDistributionImpl(1.0, 0.0);
 distribution.setDenominatorDegreesOfFreedom(-1.0);
 }
}