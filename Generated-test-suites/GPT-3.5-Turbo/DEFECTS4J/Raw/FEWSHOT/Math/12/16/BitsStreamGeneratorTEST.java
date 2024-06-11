
package org.apache.commons.math3.random;

import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    
    @Test
    public void testNext() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test with 1 bit
        assertTrue(generator.next(1) == 0 || generator.next(1) == 1);
        
        // Test with 4 bits
        assertTrue(generator.next(4) >= 0 && generator.next(4) <= 15);
        
        // Test with 8 bits
        assertTrue(generator.next(8) >= 0 && generator.next(8) <= 255);
    }
    
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test multiple calls return different values
        boolean b1 = generator.nextBoolean();
        boolean b2 = generator.nextBoolean();
        boolean b3 = generator.nextBoolean();
        assertNotEquals(b1, b2);
        assertNotEquals(b2, b3);
        assertNotEquals(b1, b3);
    }
    
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        byte[] bytes = new byte[10];
        
        generator.nextBytes(bytes);
        
        // Check that all bytes are within the range 0-255
        for (byte b : bytes) {
            assertTrue(b >= 0 && b <= 255);
        }
    }
    
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test multiple calls return different values
        double d1 = generator.nextDouble();
        double d2 = generator.nextDouble();
        double d3 = generator.nextDouble();
        assertNotEquals(d1, d2, 0.0001);
        assertNotEquals(d2, d3, 0.0001);
        assertNotEquals(d1, d3, 0.0001);
        
        // Check that values are within the range 0-1
        assertTrue(d1 >= 0 && d1 <= 1);
        assertTrue(d2 >= 0 && d2 <= 1);
        assertTrue(d3 >= 0 && d3 <= 1);
    }
    
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test multiple calls return different values
        float f1 = generator.nextFloat();
        float f2 = generator.nextFloat();
        float f3 = generator.nextFloat();
        assertNotEquals(f1, f2, 0.0001);
        assertNotEquals(f2, f3, 0.0001);
        assertNotEquals(f1, f3, 0.0001);
        
        // Check that values are within the range 0-1
        assertTrue(f1 >= 0 && f1 <= 1);
        assertTrue(f2 >= 0 && f2 <= 1);
        assertTrue(f3 >= 0 && f3 <= 1);
    }
    
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test multiple calls return different values
        double g1 = generator.nextGaussian();
        double g2 = generator.nextGaussian();
        double g3 = generator.nextGaussian();
        assertNotEquals(g1, g2, 0.0001);
        assertNotEquals(g2, g3, 0.0001);
        assertNotEquals(g1, g3, 0.0001);
    }
    
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test multiple calls return different values
        int i1 = generator.nextInt();
        int i2 = generator.nextInt();
        int i3 = generator.nextInt();
        assertNotEquals(i1, i2);
        assertNotEquals(i2, i3);
        assertNotEquals(i1, i3);
    }
    
    @Test
    public void testNextIntWithBound() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test with bound = 0
        try {
            generator.nextInt(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
        
        // Test with bound = 1
        // Since the bound is 1, the result should always be 0
        assertEquals(0, generator.nextInt(1));
        
        // Test with bound > 1 and < 100
        int bound = 50;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < 100; i++) {
            int val = generator.nextInt(bound);
            min = Math.min(min, val);
            max = Math.max(max, val);
        }
        
        assertTrue(min >= 0 && min < bound);
        assertTrue(max >= 0 && max < bound);
    }
    
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Test multiple calls return different values
        long l1 = generator.nextLong();
        long l2 = generator.nextLong();
        long l3 = generator.nextLong();
        assertNotEquals(l1, l2);
        assertNotEquals(l2, l3);
        assertNotEquals(l1, l3);
    }
    
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        
        // Generate a gaussian number
        double g1 = generator.nextGaussian();
        
        // Clear the generator
        generator.clear();
        
        // Generate another gaussian number
        double g2 = generator.nextGaussian();
        
        // The two generated numbers should be different
        assertNotEquals(g1, g2, 0.0001);
    }
}

class BitsStreamGeneratorImpl extends BitsStreamGenerator {

    @Override
    public void setSeed(int seed) {
        // TODO: Implement setSeed method for the BitsStreamGeneratorImpl
    }

    @Override
    public void setSeed(int[] seed) {
        // TODO: Implement setSeed method for the BitsStreamGeneratorImpl
    }

    @Override
    public void setSeed(long seed) {
        // TODO: Implement setSeed method for the BitsStreamGeneratorImpl
    }

    @Override
    protected int next(int bits) {
        // TODO: Implement next method for the BitsStreamGeneratorImpl
    }
}
