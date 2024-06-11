package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test for {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        final boolean expectedTrue  = generator.nextBoolean();
        final boolean expectedFalse = generator.nextBoolean();
        assertEquals(true, expectedTrue);
        assertEquals(false, expectedFalse);
        assertEquals(true, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    /**
     * Test for {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] expectedBytes = new byte[] { 3, 117, -12, 72, 105, 12, 112, 117, 104, -107, 119, -112, -64, -111, 7 };
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        generator.nextBytes(expectedBytes);
        assertArrayEquals(expectedBytes, expectedBytes);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        final double expectedValue = FastMath.nextGaussian();
        final double generatedValue = generator.nextDouble();
        assertEquals(expectedValue, generatedValue, 1e-10);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        final float expectedValue = FastMath.nextGaussian();
        final float generatedValue = generator.nextFloat();
        assertEquals(expectedValue, generatedValue, 1e-8f);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        final double generatedValue1 = generator.nextGaussian();
        final double generatedValue2 = generator.nextGaussian();
        assertEquals(generatedValue1, generatedValue2, 1e-10);
        final double expectedMean = 0.0;
        final double expectedVariance = 1.0;
        final int numTests = 10000;
        double sum = 0.0;
        for (int i = 0; i < numTests; i++) {
            final double generatedValue = generator.nextGaussian();
            sum += generatedValue * generatedValue;
        }
        final double mean = sum / numTests;
        assertEquals(expectedMean, mean, 1e-6);
        final double variance = sum / numTests - expectedMean * expectedMean;
        assertEquals(expectedVariance, variance, 1e-6);
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        final int expectedValue = 25;
        final int generatedValue = generator.nextInt();
        assertEquals(expectedValue, generatedValue);
        assertEquals(Integer.MIN_VALUE, generator.nextInt());
        assertEquals(Integer.MAX_VALUE, generator.nextInt());
        assertEquals(23, generator.nextInt(23));
        assertEquals(23, generator.nextInt(23) % 23);
        try {
            generator.nextInt(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntWithMod() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        final int max = 15;
        final int min = 5;
        for (int i = 0; i < 1000; i++) {
            final int generatedValue = generator.nextInt(max) % max;
            assertEquals(min + (generatedValue % (max - min + 1)), generatedValue);
        }
    }

    /**
     * Test for {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        final long expectedValue = 1234567890123456789L;
        final long generatedValue = generator.nextLong();
        assertEquals(expectedValue, generatedValue);
        assertEquals(Long.MIN_VALUE, generator.nextLong());
        assertEquals(Long.MAX_VALUE, generator.nextLong());
    }

    /**
     * Test for {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new MersenneTwisterFast(12345L);
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }

}