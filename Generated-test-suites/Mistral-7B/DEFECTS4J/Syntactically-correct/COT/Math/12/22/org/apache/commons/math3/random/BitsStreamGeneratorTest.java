package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test instance. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test.
     */
    @SuppressWarnings("PMD.AssumeNull")
    @Before
    public void setUp() {
        generator = new BitsStreamGenerator();
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(generator);
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        generator.setSeed(1);
        // not testing the functionality as we don't have a way to check the
        // internal state of the generator
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        generator.setSeed(new int[]{1, 2, 3, 4});
        // not testing the functionality as we don't have a way to check the
        // internal state of the generator
    }

    /**
     * Tests {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        generator.setSeed(1L);
        // not testing the functionality as we don't have a way to check the
        // internal state of the generator
    }

    /**
     * Tests {@link BitsStreamGenerator#next(int)}.
     */
    @Test
    public void testNextInt() {
        final int bits = 8;
        int value = generator.next(bits);
        assertEquals(bits, Integer.size(value));
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 0xff);
        }
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        final double expectedMin = 0.0;
        final double expectedMax = 1.0;
        double result = generator.nextDouble();
        assertTrue(result >= expectedMin && result <= expectedMax);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        final float expectedMin = 0.0f;
        final float expectedMax = 1.0f;
        float result = generator.nextFloat();
        assertTrue(result >= expectedMin && result <= expectedMax);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertTrue(result >= -3.0 && result <= 3.0);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        final int expectedMin = Integer.MIN_VALUE;
        final int expectedMax = Integer.MAX_VALUE;
        int result = generator.nextInt();
        assertTrue(result >= expectedMin && result <= expectedMax);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntInt() {
        final int n = 5;
        final int expectedMin = 0;
        final int expectedMax = n - 1;
        int result = generator.nextInt(n);
        assertTrue(result >= expectedMin && result <= expectedMax);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        final long expectedMin = Long.MIN_VALUE;
        final long expectedMax = Long.MAX_VALUE;
        long result = generator.nextLong();
        assertTrue(result >= expectedMin && result <= expectedMax);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(generator.isNextGaussianSet());
    }

    /**
     * Tests exception handling in {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntIllegalArgumentException() {
        generator.nextInt(-1);
    }

    /**
     * Tests edge cases in {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextIntEdgeCases() {
        int result = generator.nextInt(0);
        assertEquals(0, result);

        result = generator.nextInt(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, result);

        result = generator.nextInt(Integer.MAX_VALUE + 1);
        assertEquals(Integer.MAX_VALUE, result);
    }
}