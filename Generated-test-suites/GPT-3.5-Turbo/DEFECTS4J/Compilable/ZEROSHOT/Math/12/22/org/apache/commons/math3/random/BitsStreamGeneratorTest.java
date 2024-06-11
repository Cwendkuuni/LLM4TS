package org.apache.commons.math3.random;
import static org.junit.Assert.*;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertTrue(generator.nextBoolean());
    }
    
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            int[] random = { 1, 2, 3, 4 };
            
            @Override
            protected int next(int bits) {
                return random[count++];
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
        };
        
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bytes);
    }
    
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            double[] random = { 0.5, 0.8 };
            
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertEquals(0.8, generator.nextDouble(), 0.0);
    }
    
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            float[] random = { 0.3f, 0.6f };
            
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertEquals(0.6f, generator.nextFloat(), 0.0);
    }
    
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            double[] random = { 0.1, 0.2, 0.3, 0.4 };
            
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertEquals(0.2, generator.nextGaussian(), 0.0);
    }
    
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            int[] random = { 10, 20, 30, 40 };
            
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertEquals(20, generator.nextInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNextInt_withIllegalArgument() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        generator.nextInt(-1);
    }
    
    @Test
    public void testNextInt_withPowerOfTwoN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            int[] random = { 1, 2 };
            
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertEquals(0, generator.nextInt(2));
    }
    
    @Test
    public void testNextInt_withNonPowerOfTwoN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            int[] random = { 1, 2, 3, 4 };
            
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertEquals(2, generator.nextInt(3));
    }
    
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            long[] random = { 100, 200, 300, 400 };
            
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        assertEquals(200, generator.nextLong());
    }
    
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 1;
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
        };
        
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
    }
}