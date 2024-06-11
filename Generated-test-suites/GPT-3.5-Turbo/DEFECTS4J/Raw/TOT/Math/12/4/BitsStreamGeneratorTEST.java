import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    
    private static class MockBitsStreamGenerator extends BitsStreamGenerator {
        
        private int nextValue = 0;
        
        public void setNextValue(int nextValue) {
            this.nextValue = nextValue;
        }

        @Override
        protected int next(int bits) {
            return nextValue;
        }

        @Override
        public void setSeed(int seed) {
            
        }

        @Override
        public void setSeed(int[] seed) {
            
        }

        @Override
        public void setSeed(long seed) {
            
        }
    }
    
    @Test
    public void testNextBoolean() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        assertFalse(generator.nextBoolean());
        
        generator.setNextValue(1);
        assertTrue(generator.nextBoolean());
    }
    
    @Test
    public void testNextBytes() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        byte[] bytes = new byte[20];
        generator.setNextValue(0x11223344);
        
        generator.nextBytes(bytes);
        
        byte[] expected = new byte[] {(byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11,
                                      (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11,
                                      (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11,
                                      (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11,
                                      (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11};
        assertArrayEquals(expected, bytes);
    }
    
    @Test
    public void testNextDouble() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        assertEquals(0.0, generator.nextDouble(), 0.0);
        
        generator.setNextValue(0x00100000);
        assertEquals(1.1920928955078125E-7, generator.nextDouble(), 0.0);
        
        generator.setNextValue(0x7FFFFFFF);
        assertEquals(0.9999999981373549, generator.nextDouble(), 0.0);
    }
    
    @Test
    public void testNextFloat() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
        
        generator.setNextValue(0x00400000);
        assertEquals(1.1920929E-7f, generator.nextFloat(), 0.0f);
        
        generator.setNextValue(0x7FFFFFFF);
        assertEquals(0.9999999f, generator.nextFloat(), 0.0f);
    }
    
    @Test
    public void testNextGaussian() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        generator.clear();
        generator.setNextValue(1);
        
        assertEquals(0.0, generator.nextGaussian(), 0.0);
        
        generator.setNextValue(1000);
        generator.setNextValue(60000);
        
        assertEquals(0.8603597919801901, generator.nextGaussian(), 0.0);
    }
    
    @Test
    public void testNextInt() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        assertEquals(0, generator.nextInt());
        
        generator.setNextValue(0x7FFFFFFF);
        assertEquals(Integer.MAX_VALUE, generator.nextInt());
        
        generator.setNextValue(0x80000000);
        assertEquals(Integer.MIN_VALUE, generator.nextInt());
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.nextInt(-1);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroN() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.nextInt(0);
    }
    
    @Test
    public void testNextIntWithPowerOfTwoN() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        assertEquals(0, generator.nextInt(2));
        
        generator.setNextValue(0x7FFFFFFF);
        assertEquals(1, generator.nextInt(2));
    }
    
    @Test
    public void testNextIntWithNonPowerOfTwoN() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        generator.setNextValue(0);
        generator.setNextValue(0x80000001);
        generator.setNextValue(0);
        generator.setNextValue(0x7FFFFFFF);
        
        assertEquals(1, generator.nextInt(5));
    }
    
    @Test
    public void testNextLong() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.setNextValue(0);
        assertEquals(0L, generator.nextLong());
        
        generator.setNextValue(0x7FFFFFFF);
        generator.setNextValue(0x7FFFFFFF);
        assertEquals(0x800000007FFFFFFFL, generator.nextLong());
    }
    
    @Test
    public void testClear() {
        MockBitsStreamGenerator generator = new MockBitsStreamGenerator();
        generator.clear();
        generator.setNextValue(1);
        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }
    
}