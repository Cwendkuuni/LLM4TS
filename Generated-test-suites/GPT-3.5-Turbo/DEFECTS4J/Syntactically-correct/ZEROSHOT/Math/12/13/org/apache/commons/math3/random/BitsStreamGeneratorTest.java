import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
        };
        
        assertFalse(generator.nextBoolean());
        
        generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        
        assertTrue(generator.nextBoolean());
    }
    
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x87654321;
            }
        };
        
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        
        byte[] expected = {(byte) 0x21, (byte) 0x43, (byte) 0x65, (byte) 0x87};
        assertArrayEquals(expected, bytes);
    }
    
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                if (bits == 26) {
                    return 0x03FFFFF;
                } else {
                    return 0x80000000;
                }
            }
        };
        
        double result = generator.nextDouble();
        
        assertEquals(Double.longBitsToDouble(0x4003FFFFF0000000L), result, 0);
    }
    
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x03FFFFFF;
            }
        };
        
        float result = generator.nextFloat();
        
        assertEquals(Float.intBitsToFloat(0x3F7FFFFF), result, 0);
    }
    
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private boolean firstCall = true;
            
            protected int next(int bits) {
                return 0x03FFFFFF;
            }
            
            public double nextDouble() {
                if (firstCall) {
                    firstCall = false;
                    return 0.3;
                } else {
                    return -0.5;
                }
            }
        };
        
        double result = generator.nextGaussian();
        
        assertEquals(0.6033610888942274, result, 0);
    }
    
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        
        int result = generator.nextInt();
        
        assertEquals(0x12345678, result);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZero() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
        };
        
        generator.nextInt(0);
    }
    
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        
        long result = generator.nextLong();
        
        assertEquals(0x1234567812345678L, result);
    }
    
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
        };
        
        generator.nextGaussian();
        
        generator.clear();
        
        assertTrue(Double.isNaN(generator.nextGaussian));
    }
}