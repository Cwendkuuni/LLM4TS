import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
 distribution = new NormalDistributionImpl(0, 1);
 assertEquals("Mean should be 0", 0, distribution.getMean(), 0.001);
 assertEquals("Standard deviation should be 1", 1, distribution.getStandardDeviation(), 0.001);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNormalDistributionImplConstructorWithNegativeStandardDeviation() {
 distribution = new NormalDistributionImpl(0, -1);
 }

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviationAndAccuracy() {
 distribution = new NormalDistributionImpl(0, 1, 1e-10);
 }

 @Test
 public void testGetMean() {
 NormalDistributionImpl dist = new NormalDistributionImpl(1, 2);
 assertEquals("Mean should be 1", 1, dist.getMean(), 0.001);
 }

 @Test
 public void testGetStandardDeviation() {
 NormalDistributionImpl dist = new NormalDistributionImpl(1, 2);
 assertEquals("Standard deviation should be 2", 2, dist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testDensity() {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 double result = dist.density(0.5);
 assertEquals("Density should be 0.1510111022300153", 0.1510111022300153, result, 0.0001);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 double result = dist.cumulativeProbability(0.5);
 assertEquals("Cumulative probability should be 0.68268947212775", 0.68268947212775, result, 0.0001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithInvalidArgument() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 dist.cumulativeProbability(2);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 double result = dist.inverseCumulativeProbability(0.5);
 assertEquals("Inverse cumulative probability should be 0", 0, result, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithInvalidArgument() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 dist.inverseCumulativeProbability(1.5);
 }

 @Test
 public void testSample() throws MathException {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 double result = dist.sample();
 assertEquals("Sample should be close to mean", 0, result - 0, 0.1);
 }

 @Test
 public void testSolverAbsoluteAccuracy() {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 assertEquals("Solver absolute accuracy should be DEFAULT_INVERSE_ABSOLUTE_ACCURACY", 1e-9, dist.getSolverAbsoluteAccuracy(), 0.001);
 }

 @Test
 public void testGetDomainLowerBound() {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 double result = dist.getDomainLowerBound(0.1);
 assertLessThan("Domain lower bound should be less than 0.1", 0.1, result);
 }

 @Test
 public void testGetDomainUpperBound() {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 double result = dist.getDomainUpperBound(0.1);
 assertGreaterThan("Domain upper bound should be greater than 0.1", 0.1, result);
 }

 @Test
 public void testGetInitialDomain() {
 NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
 double result = dist.getInitialDomain(0.5);
 assertEquals("Initial domain should be close to mean", 0, result - 0.5, 0.1);
 }
}