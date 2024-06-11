package org.apache.commons.math3.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

            @Override
            protected int next(int bits) {
                return 0;
            }
        };
    }

    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertEquals(0, b);
        }
    }

    @Test
    public void testNextDouble() {
        assertEquals(0.0, generator.nextDouble(), 0.0);
        assertEquals(0.0, generator.nextDouble(), 0.0);
    }

    @Test
    public void testNextFloat() {
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
    }

    @Test
    public void testNextGaussian() {
        assertEquals(0.0, generator.nextGaussian(), 0.0);
        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }

    @Test
    public void testNextInt() {
        assertEquals(0, generator.nextInt());
        assertEquals(0, generator.nextInt());
    }

    @Test
    public void testNextIntWithPositiveArgument() {
        assertEquals(0, generator.nextInt(10));
        assertEquals(0, generator.nextInt(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithZeroArgument() {
        generator.nextInt(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeArgument() {
        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        assertEquals(0L, generator.nextLong());
        assertEquals(0L, generator.nextLong());
    }

    @Test
    public void testClear() {
        generator.clear();
        assertEquals(0.0, generator.nextGaussian(), 0.0);
        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }
}