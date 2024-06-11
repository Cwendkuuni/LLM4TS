import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

 private NormalDistribution dist;

 @Test
 public void testNormalDistributionImplConstruction() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 assertEquals(0, normalDist.getMean(), 0.001);
 assertEquals(1, normalDist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplMeanAndStandardDeviation() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(2, 3);
 assertEquals(2, normalDist.getMean(), 0.001);
 assertEquals(3, normalDist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplNegativeStandardDeviation() {
 try {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, -2);
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // expected
 }
 }

 @Test
 public void testNormalDistributionImplDensity() {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 double density = normalDist.density(0);
 assertEquals(0.39894228, density, 0.00001);
 }

 @Test
 public void testNormalDistributionImplCumulativeProbability() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 double cumulativeProbability = normalDist.cumulativeProbability(0.5);
 assertEquals(0.68268946, cumulativeProbability, 0.00001);
 }

 @Test
 public void testNormalDistributionImplCumulativeProbabilityNegativeValue() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 try {
 double cumulativeProbability = normalDist.cumulativeProbability(-1);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testNormalDistributionImplCumulativeProbabilityTooLargeValue() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 try {
 double cumulativeProbability = normalDist.cumulativeProbability(100);
 fail("Expected MathException");
 } catch (MathException e) {
 // expected
 }
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbability() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 double p = 0.25;
 double inverseCumulativeProbability = normalDist.inverseCumulativeProbability(p);
 assertEquals(-0.84263128, inverseCumulativeProbability, 0.00001);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbabilityZero() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 double inverseCumulativeProbability = normalDist.inverseCumulativeProbability(0);
 assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability);
 }

 @Test
 public void testNormalDistributionImplInverseCumulativeProbabilityOne() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 double inverseCumulativeProbability = normalDist.inverseCumulativeProbability(1);
 assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability);
 }

 @Test
 public void testNormalDistributionImplSample() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 double sample = normalDist.sample();
 assertEquals(0, sample, 0.01); // expected mean to be close to 0, not exactly 0
 }

 @Test
 public void testNormalDistributionImplSampleNegativeValue() throws MathException {
 NormalDistributionImpl normalDist = new NormalDistributionImpl(0, 1);
 double sample = normalDist.sample();
 assertFalse("Expected negative value to be negative", sample < 0);
 }
}