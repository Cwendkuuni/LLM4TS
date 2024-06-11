import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;

public class FDistributionImplTest {

 @Test
 public void testFDistributionImpl() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test
 public void testCumulativeProbability() throws Exception {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 for (double i = 0.0; i <= 5.0; i += 0.1) {
 double cdf = fd.cumulativeProbability(i);
 double b = Beta.regularizedBeta((2.0 * i) / (3.0 + 2.0 * i), 1.0, 1.5);
 assertEquals(b, cdf, 0.0001);
 }
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityNegativeX() throws Exception {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.cumulativeProbability(-1.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws Exception {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 for (double p = 0.0; p <= 1.0; p += 0.1) {
 double x = fd.inverseCumulativeProbability(p);
 double b = Beta.incompleteBeta(0.5, 2.0, p);
 assertEquals(b, x, 0.0001);
 }
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityNegativeProbability() throws Exception {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.inverseCumulativeProbability(-0.1);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityNonMonotonicProbability() throws Exception {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.inverseCumulativeProbability(0.5);
 fd.inverseCumulativeProbability(0.4);
 }

 @Test
 public void testGetDomainLowerBound() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 for (double p = 0.0; p <= 1.0; p += 0.1) {
 assertEquals(0.0, fd.getDomainLowerBound(p), 0.0);
 }
 }

 @Test
 public void testGetDomainUpperBound() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 for (double p = 0.0; p <= 1.0; p += 0.1) {
 assertEquals(Double.POSITIVE_INFINITY, fd.getDomainUpperBound(p), 0.0);
 }
 }

 @Test
 public void testGetInitialDomain() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 for (double p = 0.0; p <= 1.0; p += 0.1) {
 double x = fd.getInitialDomain(p);
 assertEquals(3.0 / (3.0 - 2.0), x, 0.001);
 }
 }

 @Test
 public void testSetNumeratorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setNumeratorDegreesOfFreedom(4.0);
 assertEquals(4.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNumeratorDegreesOfFreedomNegative() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setNumeratorDegreesOfFreedom(-1.0);
 }

 @Test
 public void testSetDenominatorDegreesOfFreedom() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setDenominatorDegreesOfFreedom(5.0);
 assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetDenominatorDegreesOfFreedomNegative() {
 FDistributionImpl fd = new FDistributionImpl(2.0, 3.0);
 fd.setDenominatorDegreesOfFreedom(-1.0);
 }

}