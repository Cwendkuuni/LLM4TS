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
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setSeed(int[] seed) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void setSeed(long seed) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected int next(int bits) {
                // TODO Auto-generated method stub
                return 0;
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[5];
        generator.nextBytes(bytes);
        assertEquals(0, bytes[0]);
        assertEquals(0, bytes[1]);
        assertEquals(0, bytes[2]);
        assertEquals(0, bytes[3]);
        assertEquals(0, bytes[4]);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeN() {
        generator.nextInt(-5);
    }

    @Test
    public void testNextIntWithPositiveN() {
        int result = generator.nextInt(5);
        assertEquals(0, result);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertEquals(0L, result);
    }

    @Test
    public void testClear() {
        generator.clear();
        double result = generator.nextGaussian();
        assertEquals(0.0, result, 0.0);
    }
}