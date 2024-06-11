import org.junit.Test;

public class BitsStreamGeneratorTest {
    
    @Test
    public void testNextBoolean() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextBoolean returns the expected value
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }
    
    @Test
    public void testNextBytes() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextBytes fills the given byte array with expected values
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, bytes);
    }
    
    @Test
    public void testNextDouble() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextDouble returns the expected value
        assertEquals(0.0, generator.nextDouble(), 0.0);
    }
    
    @Test
    public void testNextFloat() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextFloat returns the expected value
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
    }
    
    @Test
    public void testNextGaussian() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextGaussian returns the expected value
        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }
    
    @Test
    public void testNextInt() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextInt returns the expected value
        assertEquals(0, generator.nextInt());
    }
    
    @Test
    public void testNextIntWithPositiveN() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextInt with positive n returns the expected value
        assertEquals(0, generator.nextInt(5));
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextInt with negative n throws an exception
        generator.nextInt(-5);
    }
    
    @Test
    public void testNextLong() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that nextLong returns the expected value
        assertEquals(0L, generator.nextLong());
    }
    
    @Test
    public void testClear() {
        // create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                // implement the next method
                return 0;
            }
            public void setSeed(int seed) {}
            public void setSeed(int[] seed) {}
            public void setSeed(long seed) {}
        };
        
        // test that clear sets nextGaussian to NaN
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian));
    }
}