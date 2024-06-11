package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test abstract methods
        generator.setSeed(0);
        generator.setSeed(new int[] { 0 });
        generator.setSeed(1L);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextBoolean()
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);

        // Test nextBytes() with byte array length > 0
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, bytes);

        // Test nextBytes() with null argument
        generator.nextBytes(null);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextDouble()
        assertEquals(0.0, generator.nextDouble(), 0.0);
        assertEquals(1.0, generator.nextDouble(), 0.0);
        double maxValue = Double.MAX_VALUE;
        assertEquals(maxValue, generator.nextDouble(), maxValue);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextFloat()
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
        assertEquals(1.0f, generator.nextFloat(), 0.0f);
        float maxValue = Float.MAX_VALUE;
        assertEquals(maxValue, generator.nextFloat(), maxValue);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextGaussianNegativeSeed() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.clear();

        // Test nextGaussian() with negative seed
        generator.setSeed(-1);
        generator.nextGaussian();
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextInt() with 0 arguments
        assertEquals(0, generator.nextInt());

        // Test nextInt() with positive arguments
        assertEquals(1, generator.nextInt(1));
        assertEquals(1, generator.nextInt(10));
        assertEquals(5, generator.nextInt(10));
        assertEquals(Integer.MAX_VALUE, generator.nextInt(Integer.MAX_VALUE));

        // Test nextInt() with negative arguments
        try {
            generator.nextInt(-1);
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test nextInt() with non-power-of-2 arguments
        int i = 0;
        while (i < 100) {
            int random = generator.nextInt(i + 1);
            assertEquals(random % (i + 1), random);
            i++;
        }
    }

    @Test
    public void testNextIntPowerOfTwo() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextInt() with power-of-2 arguments
        int i = 0;
        while (i <= 31) {
            int random = generator.nextInt(1 << i);
            assertEquals(random & ((1 << i) - 1), random);
            i++;
        }
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test nextLong()
        assertEquals(0L, generator.nextLong());
        assertEquals(1L, generator.nextLong());
        long maxValue = Long.MAX_VALUE;
        assertEquals(maxValue, generator.nextLong(), maxValue);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        // Test clear()
        generator.nextGaussian();
        generator.clear();
        assertNull(generator.nextGaussian());
    }
}