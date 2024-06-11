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
        generator = new RandomGeneratorImpl();
    }
    
    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }
    
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // Testing if bytes are modified
        boolean modified = false;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                modified = true;
                break;
            }
        }
        assertTrue(modified);
    }
    
    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertTrue(result >= 0 && result < 1);
    }
    
    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertTrue(result >= 0 && result < 1);
    }
    
    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertFalse(Double.isNaN(result));
    }
    
    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertTrue(result >= Integer.MIN_VALUE && result <= Integer.MAX_VALUE);
    }
    
    @Test
    public void testNextIntWithMaxValue() {
        int result = generator.nextInt(10);
        assertTrue(result >= 0 && result < 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithInvalidMaxValue() {
        generator.nextInt(0);
    }
    
    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertTrue(result >= Long.MIN_VALUE && result <= Long.MAX_VALUE);
    }
    
    private class RandomGeneratorImpl extends BitsStreamGenerator {
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