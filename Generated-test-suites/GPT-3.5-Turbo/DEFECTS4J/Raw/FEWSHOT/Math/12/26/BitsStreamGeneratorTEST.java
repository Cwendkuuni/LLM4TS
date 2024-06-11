package org.apache.commons.math3.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
            }

            @Override
            public void setSeed(int[] seed) {
            }

            @Override
            public void setSeed(long seed) {
            }

            @Override
            protected int next(int bits) {
                return 0;
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result);
        result = generator.nextBoolean();
        assertFalse(result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertNotNull(bytes);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertEquals(0.0, result, 0.0);
        result = generator.nextDouble();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertEquals(0.0f, result, 0.0f);
        result = generator.nextFloat();
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertEquals(0.0, result, 0.0);
        result = generator.nextGaussian();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertEquals(0, result);
        result = generator.nextInt();
        assertEquals(0, result);
    }

    @Test
    public void testNextIntWithPositiveN() {
        int result = generator.nextInt(10);
        assertEquals(0, result);
        result = generator.nextInt(10);
        assertEquals(0, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertEquals(0L, result);
        result = generator.nextLong();
        assertEquals(0L, result);
    }
}