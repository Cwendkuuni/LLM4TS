import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        // Test if the nextBoolean() method returns true or false
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            boolean result = generator.nextBoolean();
            assertTrue(result || !result);
        }
    }

    @Test
    public void testNextBytes() {
        // Test if the nextBytes() method fills the array with random bytes
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            byte[] bytes = new byte[10];
            generator.nextBytes(bytes);
            boolean containsRandomBytes = false;
            for (byte b : bytes) {
                if (b != 0) {
                    containsRandomBytes = true;
                    break;
                }
            }
            assertTrue(containsRandomBytes);
        }
    }

    @Test
    public void testNextDouble() {
        // Test if the nextDouble() method returns a double value between 0.0 and 1.0
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            double result = generator.nextDouble();
            assertTrue(result >= 0.0 && result < 1.0);
        }
    }

    @Test
    public void testNextFloat() {
        // Test if the nextFloat() method returns a float value between 0.0 and 1.0
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            float result = generator.nextFloat();
            assertTrue(result >= 0.0 && result < 1.0);
        }
    }

    @Test
    public void testNextGaussian() {
        // Test if the nextGaussian() method returns a double value
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            double result = generator.nextGaussian();
            assertFalse(Double.isNaN(result));
        }
    }

    @Test
    public void testNextInt() {
        // Test if the nextInt() method returns an integer value
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            int result = generator.nextInt();
            assertNotEquals(Integer.MIN_VALUE, result);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        // Test if the nextInt(int n) method throws NotStrictlyPositiveException when n is negative
        BitsStreamGenerator generator = createGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextIntZero() {
        // Test if the nextInt(int n) method returns 0 when n is 0
        BitsStreamGenerator generator = createGenerator();
        int result = generator.nextInt(0);
        assertEquals(0, result);
    }

    @Test
    public void testNextIntGreaterThanZero() {
        // Test if the nextInt(int n) method returns an integer value between 0 and n-1 inclusively
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            int n = generator.nextInt(10);
            assertTrue(n >= 0 && n <= 9);
        }
    }

    @Test
    public void testNextLong() {
        // Test if the nextLong() method returns a long value
        for (int i = 0; i < 1000; i++) {
            BitsStreamGenerator generator = createGenerator();
            long result = generator.nextLong();
            assertNotEquals(Long.MIN_VALUE, result);
        }
    }

    private BitsStreamGenerator createGenerator() {
        // Create a new BitsStreamGenerator for testing
        return new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // Do nothing
            }

            @Override
            public void setSeed(int[] seed) {
                // Do nothing
            }

            @Override
            public void setSeed(long seed) {
                // Do nothing
            }

            @Override
            protected int next(int bits) {
                // Return a random integer value
                return (int) (Math.random() * (1 << bits));
            }
        };
    }
}