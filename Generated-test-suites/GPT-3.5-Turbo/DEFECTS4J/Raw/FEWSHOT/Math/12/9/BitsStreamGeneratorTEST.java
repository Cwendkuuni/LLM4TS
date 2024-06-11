package org.apache.commons.math3.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
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
        double value = generator.nextDouble();
        assertEquals(0.0, value, 0.0);
    }

    @Test
    public void testNextFloat() {
        float value = generator.nextFloat();
        assertEquals(0.0f, value, 0.0f);
    }

    @Test
    public void testNextGaussian() {
        double value = generator.nextGaussian();
        assertEquals(0.0, value, 0.0);
    }

    @Test
    public void testNextInt() {
        int value = generator.nextInt();
        assertEquals(0, value);
    }

    @Test(expected=NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeValue() {
        generator.nextInt(-5);
    }

    @Test
    public void testNextIntWithPositiveValue() {
        int value = generator.nextInt(10);
        assertEquals(0, value);
    }

    @Test
    public void testNextLong() {
        long value = generator.nextLong();
        assertEquals(0L, value);
    }

    @Test
    public void testClear() {
        generator.clear();
        // Verify that clear() does not throw any exceptions
    }
}