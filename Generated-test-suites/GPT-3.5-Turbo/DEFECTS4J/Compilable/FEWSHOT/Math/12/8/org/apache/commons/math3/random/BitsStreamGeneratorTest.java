package org.apache.commons.math3.random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    
    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new TestBitsStreamGenerator();
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
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);

        for (byte b : bytes) {
            assertEquals(0, b);
        }
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertEquals(0, result, 0.0001);

        result = generator.nextDouble();
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertEquals(0, result, 0.0001);

        result = generator.nextFloat();
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertEquals(0, result, 0.0001);

        result = generator.nextGaussian();
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertEquals(0, result);

        result = generator.nextInt();
        assertEquals(1, result);
    }

    @Test
    public void testNextIntWithRange() {
        int result = generator.nextInt(10);
        assertEquals(0, result);

        result = generator.nextInt(10);
        assertEquals(1, result);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertEquals(0, result);

        result = generator.nextLong();
        assertEquals(1, result);
    }

    private class TestBitsStreamGenerator extends BitsStreamGenerator {
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
    }
}