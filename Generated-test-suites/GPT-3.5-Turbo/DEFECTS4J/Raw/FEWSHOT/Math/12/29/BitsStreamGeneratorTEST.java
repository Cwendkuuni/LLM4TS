package org.apache.commons.math3.random;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BitsStreamGeneratorTest {
    @Test
    public void testNext() {
        // Test the next method with different values of bits
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(0, generator.next(1));
        assertEquals(0, generator.next(5));
        assertEquals(0, generator.next(10));
    }

    @Test
    public void testNextBoolean() {
        // Test the nextBoolean method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(false, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
        assertEquals(false, generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        // Test the nextBytes method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        byte[] bytes = new byte[10];
        byte[] expected = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        generator.nextBytes(bytes);
        assertEquals(expected, bytes);
        
        byte[] bytes2 = new byte[5];
        byte[] expected2 = {0, 0, 0, 0, 0};
        generator.nextBytes(bytes2);
        assertEquals(expected2, bytes2);
    }

    @Test
    public void testNextDouble() {
        // Test the nextDouble method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(0.0, generator.nextDouble(), 0.0);
        assertEquals(0.0, generator.nextDouble(), 0.0);
        assertEquals(0.0, generator.nextDouble(), 0.0);
    }

    @Test
    public void testNextFloat() {
        // Test the nextFloat method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
    }

    @Test
    public void testNextGaussian() {
        // Test the nextGaussian method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(0.0, generator.nextGaussian(), 0.0);
        assertEquals(0.0, generator.nextGaussian(), 0.0);
        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }

    @Test
    public void testNextInt() {
        // Test the nextInt method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(0, generator.nextInt());
        assertEquals(0, generator.nextInt());
        assertEquals(0, generator.nextInt());
    }

    @Test
    public void testNextIntN() {
        // Test the nextInt method with a parameter
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(0, generator.nextInt(1));
        assertEquals(0, generator.nextInt(5));
        assertEquals(0, generator.nextInt(10));
    }

    @Test
    public void testNextLong() {
        // Test the nextLong method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        assertEquals(0, generator.nextLong());
        assertEquals(0, generator.nextLong());
        assertEquals(0, generator.nextLong());
    }

    @Test
    public void testClear() {
        // Test the clear method
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        generator.nextGaussian();
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
        generator.clear();
        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }
}