import org.junit.*;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    
    @Test
    public void testNext() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        int result = generator.next(5);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        boolean result = generator.nextBoolean();

        assertTrue(result);
    }
    
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 255;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        
        assertArrayEquals(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 }, bytes);
    }
    
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        double result = generator.nextDouble();
        
        assertEquals(0.0, result, 0.0);
    }
    
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        float result = generator.nextFloat();
        
        assertEquals(0.0f, result, 0.0f);
    }
    
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private boolean first = true;
            protected int next(int bits) {
                if (first) {
                    first = false;
                    return 0;
                } else {
                    return 1;
                }
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        double result = generator.nextGaussian();
        
        assertEquals(1.0, result, 0.0);
    }
    
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 12345;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        int result = generator.nextInt();
        
        assertEquals(12345, result);
    }
    
    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 12345;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        int result = generator.nextInt(10);
        
        assertEquals(5, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNonPositiveN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        generator.nextInt(-1);
    }
    
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 12345;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        long result = generator.nextLong();
        
        assertEquals(12345L, result);
    }
    
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        generator.clear();
        
        double result = generator.nextGaussian();
        
        assertEquals(0.0, result, 0.0);
    }
}