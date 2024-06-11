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
        assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        assertEquals(0.0, generator.nextDouble(), 0.0);
        assertEquals(0.5, generator.nextDouble(), 0.0);
        assertEquals(1.0, generator.nextDouble(), 0.0);
    }

    @Test
    public void testNextFloat() {
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
        assertEquals(0.5f, generator.nextFloat(), 0.0f);
        assertEquals(1.0f, generator.nextFloat(), 0.0f);
    }

    @Test
    public void testNextGaussian() {
        assertEquals(0.0, generator.nextGaussian(), 0.0);
        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }

    @Test
    public void testNextInt() {
        assertEquals(0, generator.nextInt());
        assertEquals(1, generator.nextInt());
        assertEquals(2, generator.nextInt());
    }

    @Test
    public void testNextIntWithMaxValue() {
        assertEquals(0, generator.nextInt(0));
        assertEquals(0, generator.nextInt(1));
        assertEquals(1, generator.nextInt(2));
        assertEquals(2, generator.nextInt(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeMaxValue() {
        generator.nextInt(-1);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testNextLong() {
        assertEquals(0L, generator.nextLong());
        assertEquals(1L, generator.nextLong());
        assertEquals(2L, generator.nextLong());
    }

    @Test
    public void testClear() {
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }

}