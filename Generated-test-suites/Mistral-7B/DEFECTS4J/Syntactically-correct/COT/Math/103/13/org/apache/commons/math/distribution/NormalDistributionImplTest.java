package org.apache.commons.math.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

 private NormalDistributionImpl distribution;

 @Test
 public void testNormalDistributionImplConstructionWithMeanAndStandardDeviation() {
 distribution = new NormalDistributionImpl(1.0, 2.0);
 assertEquals(1.0, distribution.getMean(), 0.001);
 assertEquals(2.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testNormalDistributionImplConstructionWithMeanAndStandardDeviationZero() {
 distribution = new NormalDistributionImpl();
 assertEquals(0.0, distribution.getMean(), 0.001);
 assertEquals(1.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test
 public void testSetMean() {
 distribution = new NormalDistributionImpl();
 distribution.setMean(3.0);
 assertEquals(3.0, distribution.getMean(), 0.001);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testSetMeanNegative() {
 distribution = new NormalDistributionImpl();
 distribution.setMean(-1.0);
 }

 @Test
 public void testSetStandardDeviation() {
 distribution = new NormalDistributionImpl();
 distribution.setStandardDeviation(4.0);
 assertEquals(4.0, distribution.getStandardDeviation(), 0.001);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testSetStandardDeviationZeroOrNegative() {
 distribution = new NormalDistributionImpl();
 distribution.setStandardDeviation(0.0);
 distribution.setStandardDeviation(-1.0);
 }

 @Test
 public void testCumulativeProbability() throws MathException {
 NormalDistribution normalDistribution = distribution;
 double expected = 0.5;
 for (double x = -5.0; x <= 5.0; x += 0.1) {
 double result = normalDistribution.cumulativeProbability(x);
 assertEquals(expected, result, 0.001);
 expected += 0.01;
 }
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityOutOfBounds() throws MathException {
 distribution.cumulativeProbability(100.0);
 }

 @Test
 public void testInverseCumulativeProbability() throws MathException {
 NormalDistribution normalDistribution = distribution;
 double p = 0.2;
 double expected = -1.0 * Erf.erfInverse(p / 2.0) * standardDeviation * Math.sqrt(2.0) + mean;
 double result = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(expected, result, 0.001);
 p = 0.8;
 expected = mean + Erf.erfInverse((1.0 - p / 2.0) / 2.0) * standardDeviation * Math.sqrt(2.0);
 result = normalDistribution.inverseCumulativeProbability(p);
 assertEquals(expected, result, 0.001);
 }

 @Test(expected = MathException.class)
 public void testInverseCumulativeProbabilityOutOfBounds() throws MathException {
 distribution.inverseCumulativeProbability(1.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInverseCumulativeProbabilityOutOfProbabilityRange() {
 assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(1.0));
 assertThrows(IllegalArgumentException.class, () -> distribution.inverseCumulativeProbability(0.0));
 }

 @Test
 public void testGetDomainLowerBound() {
 NormalDistribution normalDistribution = distribution;
 double p = 0.1;
 double result = normalDistribution.getDomainLowerBound(p);
 assertEquals(-Double.MAX_VALUE, result, 0.0);
 p = 0.7;
 result = normalDistribution.getDomainLowerBound(p);
 assertEquals(getMean(), result, 0.001);
 }

 @Test
 public void testGetDomainUpperBound() {
 NormalDistribution normalDistribution = distribution;
 double p = 0.1;
 double result = normalDistribution.getDomainUpperBound(p);
 assertEquals(Double.MAX_VALUE, result, 0.0);
 p = 0.7;
 result = normalDistribution.getDomainUpperBound(p);
 assertEquals(getMean(), result, 0.001);
 }

 @Test
 public void testGetInitialDomain() {
 NormalDistribution normalDistribution = distribution;
 double p = 0.1;
 double result = normalDistribution.getInitialDomain(p);
 assertEquals(getMean() - getStandardDeviation(), result, 0.001);
 p = 0.9;
 result = normalDistribution.getInitialDomain(p);
 assertEquals(getMean() + getStandardDeviation(), result, 0.001);
 }
}