package org.apache.commons.math.distribution.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

public class NormalDistributionImplTest {

 private NormalDistribution dist;

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviation() {
 dist = new NormalDistributionImpl(2.5, 0.4);
 assertEquals(2.5, dist.getMean(), 0.01);
 assertEquals(0.4, dist.getStandardDeviation(), 0.01);
 }

 @Test
 public void testNormalDistributionImplConstructorWithMeanAndStandardDeviationZero() {
 dist = new NormalDistributionImpl();
 assertEquals(0.0, dist.getMean(), 0.01);
 assertEquals(1.0, dist.getStandardDeviation(), 0.01);
 }

 @Test
 public void testGetMean() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertEquals(0.0, dist.getMean(), 0.01);
 dist.setMean(1.5);
 assertEquals(1.5, dist.getMean(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMeanNegative() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 dist.setMean(-1.2);
 }

 @Test
 public void testSetMeanZero() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 dist.setMean(0.0);
 }

 @Test
 public void testGetStandardDeviation() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertEquals(1.0, dist.getStandardDeviation(), 0.01);
 dist.setStandardDeviation(2.2);
 assertEquals(2.2, dist.getStandardDeviation(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetStandardDeviationZero() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 dist.setStandardDeviation(0.0);
 }

 @Test
 public void testCumulativeProbabilityNormalDistribution() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertEquals(0.5, dist.cumulativeProbability(0.0), 0.01);
 assertEquals(0.841344746, dist.cumulativeProbability(1.2), 0.01);
 }

 @Test(expected = MathException.class)
 public void testCumulativeProbabilityTooFarFromMean() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertThrows(MathException.class, () -> dist.cumulativeProbability(10.0));
 }

 @Test
 public void testInverseCumulativeProbability() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertEquals(0.0, dist.inverseCumulativeProbability(0.0), 0.01);
 assertEquals(0.34134534, dist.inverseCumulativeProbability(0.841344746), 0.01);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testInverseCumulativeProbabilityLessThanZero() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertThrows(MathIllegalArgumentException.class, () -> dist.inverseCumulativeProbability(-0.1));
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testInverseCumulativeProbabilityGreaterThanOne() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertThrows(MathIllegalArgumentException.class, () -> dist.inverseCumulativeProbability(1.1));
 }

 @Test
 public void testInverseCumulativeProbabilityMaxIterationsExceededException() {
 dist = new NormalDistributionImpl(0.0, 1.0);
 assertThrows(MaxIterationsExceededException.class, () -> dist.inverseCumulativeProbability(10.0001));
 }
}