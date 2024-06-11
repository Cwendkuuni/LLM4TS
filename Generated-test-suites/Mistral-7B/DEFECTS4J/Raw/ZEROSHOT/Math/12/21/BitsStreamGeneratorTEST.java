package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

 private static final int N = 10;
 private static final int M = 32;
 private static final long MAX_LONG = Long.MAX_VALUE;
 private static final int MAX_INT = Integer.MAX_VALUE;

 @Test
 public void testNextBoolean() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 for (int i = 0; i < N; i++) {
 assertTrue(generator.nextBoolean());
 }
 }

 @Test(expected = AssertionError.class)
 public void testNextBooleanZero() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 assertEquals(0, generator.next(1));
 assertFalse(generator.nextBoolean());
 }

 @Test
 public void testNextBytes() {
 final byte[] expected = new byte[10];
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 generator.nextBytes(expected);
 for (int i = 0; i < expected.length; i++) {
 assertTrue(expected[i] >= 0);
 assertTrue(expected[i] <= 255);
 }
 }

 @Test
 public void testNextDouble() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 final double expectedMin = -2.0;
 final double expectedMax = 2.0;
 for (int i = 0; i < N; i++) {
 final double result = generator.nextDouble();
 assertTrue(result >= expectedMin);
 assertTrue(result <= expectedMax);
 }
 }

 @Test
 public void testNextFloat() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 final float expectedMin = 0.0f;
 final float expectedMax = 1.0f;
 for (int i = 0; i < N; i++) {
 final float result = generator.nextFloat();
 assertTrue(result >= expectedMin);
 assertTrue(result <= expectedMax);
 }
 }

 @Test
 public void testNextGaussian() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 final double expectedMean = 0.0;
 final double expectedStandardDeviation = 1.0;
 for (int i = 0; i < N; i++) {
 final double result = generator.nextGaussian();
 final double zScore = (result - expectedMean) / expectedStandardDeviation;
 assertTrue(zScore >= -3.0);
 assertTrue(zScore <= 3.0);
 }
 }

 @Test
 public void testNextInt() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 for (int i = 0; i < N; i++) {
 final int result = generator.nextInt();
 assertTrue(result >= 0);
 assertTrue(result <= MAX_INT);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextIntNegative() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 generator.nextInt(-1);
 }

 @Test
 public void testNextIntPowerOfTwo() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 for (int i = 0; i < 10; i++) {
 final int n = 1 << i;
 final int result = generator.nextInt(n);
 assertTrue(result >= 0);
 assertTrue(result < n);
 }
 }

 @Test
 public void testNextLong() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 final long expectedMin = 0L;
 final long expectedMax = MAX_LONG;
 for (int i = 0; i < N; i++) {
 final long result = generator.nextLong();
 assertTrue(result >= expectedMin);
 assertTrue(result <= expectedMax);
 }
 }

 @Test
 public void testSetSeed() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 final long seed = System.nanoTime();
 generator.setSeed(seed);
 final long result = generator.nextLong();
 assertEquals(seed, result);
 }

 @Test
 public void testClear() {
 final BitsStreamGenerator generator = new MyBitsStreamGenerator();
 generator.clear();
 assertFalse(Double.isNaN(generator.nextGaussian()));
 }

 private static class MyBitsStreamGenerator extends BitsStreamGenerator {
 @Override
 public void setSeed(int seed) {
 super.setSeed(seed);
 }

 @Override
 public void setSeed(int[] seed) {
 super.setSeed(seed);
 }

 @Override
 public void setSeed(long seed) {
 super.setSeed(seed);
 }

 @Override
 protected int next(int bits) {
 return super.next(bits);
 }
 }

}