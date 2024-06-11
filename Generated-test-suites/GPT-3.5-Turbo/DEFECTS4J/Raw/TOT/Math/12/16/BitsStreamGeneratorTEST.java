import org.junit.Test;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BitsStreamGeneratorTest {

    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };
      
        // Test setting a positive seed
        generator.setSeed(1);
      
        // Test setting a negative seed
        generator.setSeed(-1);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test getting a true boolean
        boolean result = generator.nextBoolean();
        assertTrue(result);

        // Test getting a false boolean
        boolean result = generator.nextBoolean();
        assertFalse(result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test byte array with length 0
        byte[] bytes = new byte[0];
        generator.nextBytes(bytes);

        // Test byte array with length 1
        byte[] bytes1 = new byte[1];
        generator.nextBytes(bytes1);

        // Test byte array with length 4
        byte[] bytes2 = new byte[4];
        generator.nextBytes(bytes2);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test getting a random double
        double result = generator.nextDouble();
        assert(result >= 0 && result < 1);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test getting a random float
        float result = generator.nextFloat();
        assert(result >= 0 && result < 1);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test getting a random gaussian number
        double result = generator.nextGaussian();
        assert(!Double.isNaN(result));
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test getting a positive integer
        int result = generator.nextInt();
        assertTrue(result >= 0);

        // Test getting a negative integer
        int result = generator.nextInt();
        assertFalse(result < 0);
    }

    @Test
    public void testNextIntWithN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test getting a positive integer with n > 0
        int result1 = generator.nextInt(10);
        assertTrue(result1 >= 0 && result1 < 10);

        // Test getting a negative integer with n < 0
        int result2 = generator.nextInt(-10);
        assertFalse(result2 >= 0 && result2 < -10);

        // Test getting a positive integer with n = 0
        int result3 = generator.nextInt(0);
        assertFalse(result3 >= 0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test getting a positive long
        long result = generator.nextLong();
        assertTrue(result >= 0);

        // Test getting a negative long
        long result = generator.nextLong();
        assertFalse(result < 0);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
            public void setSeed(int seed) {
            }
            public void setSeed(int[] seed) {
            }
            public void setSeed(long seed) {
            }
        };

        // Test clearing the nextGaussian cache
        generator.clear();
        assert(generator.nextGaussian == Double.NaN);
    }
}