package org.apache.commons.math.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

 NormalDistributionImpl dist;
 NormalDistribution expected;

 @Before
 public void setUp() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 expected = new NormalDistribution();
 }

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStdDeviation() {
 NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
 assertEquals(1.0, dist.getMean(), 0.001);
 assertEquals(2.0, dist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplDefaultConstructor() {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 assertEquals(0.0, dist.getMean(), 0.001);
 assertEquals(1.0, dist.getStandardDeviation(), 0.001);
 }

 @Test
 public void testGetMean() {
 NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
 dist.setMean(3.5);
 assertEquals(3.5, dist.getMean(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanWithNegativeValue() {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 dist.setMean(-1.5);
 }

 @Test
 public void testGetStandardDeviation() {
 NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
 dist.setStandardDeviation(3.5);
 assertEquals(3.5, dist.getStandardDeviation(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationWithNegativeValue() {
 NormalDistributionImpl dist = new NormalDistributionImpl();
 dist.setStandardDeviation(-1.5);
 }

 @Test
 public void testCumulativeProbabilityNormalDistribution() throws MathException {
 double x = 0.5;
 double p = dist.cumulativeProbability(x);
 assertEquals(0.5, p, 0.001);
 }

 @Test
 public void testCumulativeProbabilityOutsideMeanPlus3StdDev() throws MathException {
 double x = mean + 3 * stdDev;
 double p = dist.cumulativeProbability(x);
 assertEquals(0, p, 0.001);
 }

 @Test
 public void testCumulativeProbabilityOutsideMeanMinus3StdDev() throws MathException {
 double x = mean - 3 * stdDev;
 double p = dist.cumulativeProbability(x);
 assertEquals(1, p, 0.001);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityWithXMoreThan20StdDevFromMean() throws MathException {
 double x = mean + 20 * stdDev;
 dist.cumulativeProbability(x);
 }

 @Test
 public void testInverseCumulativeProbabilityLessThanZero() throws MathException {
 double p = 0.0;
 double x = dist.inverseCumulativeProbability(p);
 assertEquals(Double.NEGATIVE_INFINITY, x, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityGreaterThanOne() throws MathException {
 double p = 1.0;
 double x = dist.inverseCumulativeProbability(p);
 assertEquals(Double.POSITIVE_INFINITY, x, 0.0);
 }

 @Test
 public void testInverseCumulativeProbabilityZero() throws MathException {
 double p = 0.5;
 double x = dist.inverseCumulativeProbability(p);
 assertEquals(mean, x, 0.001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithXCloseToMean() throws MathException {
 double p = 0.95;
 double x = dist.inverseCumulativeProbability(p);
 assertEquals(mean + standardDeviation * Erf.erfinv(2 * p - 1), x, 0.001);
 }

 @Test
 public void testInverseCumulativeProbabilityWithXFarFromMean() throws MathException {
 double p = 0.025;
 double x = dist.inverseCumulativeProbability(p);
 assertEquals(mean + standardDeviation * Erf.erfinv(2 * p - 1), x, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
 dist.inverseCumulativeProbability(-0.1);
 }
}