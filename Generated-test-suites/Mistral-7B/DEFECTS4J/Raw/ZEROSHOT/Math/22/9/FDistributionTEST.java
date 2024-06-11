import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import static org.junit.Assert.*;

public class FDistributionTest {

 @org.junit.BeforeClass
 public static void setUpBeforeClass() throws Exception {
 }

 @org.junit.AfterClass
 public static void tearDownAfterClass() throws Exception {
 }

 @org.junit.Before
 public void setUp() throws Exception {
 }

 @org.junit.After
 public void tearDown() throws Exception {
 }

 @org.junit.Test
 public void testFDistributionCreation() throws NotStrictlyPositiveException {
 FDistribution fd = new FDistribution(2.0, 3.0);
 assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
 assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testFDistributionNegativeNumeratorDegreesOfFreedom() throws Exception {
 new FDistribution(-2.0, 3.0);
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testFDistributionNegativeDenominatorDegreesOfFreedom() throws Exception {
 new FDistribution(2.0, -3.0);
 }

 @org.junit.Test
 public void testFDistributionCumulativeProbability() {
 FDistribution fd = new FDistribution(2.0, 3.0);
 double cp = fd.cumulativeProbability(2.5);
 double expected = Beta.regularizedBeta((2 * 2.5) / (3 + 2 * 2.5), 1.0, 1.5);
 assertEquals(expected, cp, 0.0001);
 }

 @org.junit.Test
 public void testFDistributionDensity() {
 FDistribution fd = new FDistribution(2.0, 3.0);
 double density = fd.density(2.5);
 double expected = FastMath.exp(2.0 * FastMath.log(2.5) - FastMath.log(2.5) + 1.5 * FastMath.log(1.5) -
 2.0 * FastMath.log(2.5 * 2.5 + 3.0) - 1.5 * FastMath.log(2.5 * 2.5 + 3.0) - Beta.logBeta(1.0, 1.5));
 assertEquals(expected, density, 0.0001);
 }

 @org.junit.Test
 public void testFDistributionVariance() throws Exception {
 FDistribution fd = new FDistribution(2.0, 3.0);
 double variance = fd.getNumericalVariance();
 double expected = (2.0 * 3.0 * 3.0 * 3.0) / (2.0 * (3.0 - 2.0) * (3.0 - 4.0));
 assertEquals(expected, variance, 0.0001);
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testFDistributionVarianceNegativeNumeratorDegreesOfFreedom() throws Exception {
 FDistribution fd = new FDistribution(-2.0, 3.0);
 fd.getNumericalVariance();
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testFDistributionVarianceNegativeDenominatorDegreesOfFreedom() throws Exception {
 FDistribution fd = new FDistribution(2.0, -3.0);
 fd.getNumericalVariance();
 }

 @org.junit.Test(expected = NotStrictlyPositiveException.class)
 public void testFDistributionNegativeDegreesOfFreedom() throws Exception {
 new FDistribution(-2.0, -3.0);
 }
}