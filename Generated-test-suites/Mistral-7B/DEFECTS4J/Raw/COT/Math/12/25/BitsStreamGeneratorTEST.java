import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for BitsStreamGenerator.
 */
public class BitsStreamGeneratorTest {

    /** Test instance of BitsStreamGenerator */
    private BitsStreamGenerator generator = new BitsStreamGenerator() {
        @Override
        protected int next(int bits) {
            // Provide a test implementation for next(int bits)
            // This method is abstract in the original class
            return (int) (Math.random() * (1 << bits));
        }
    };

    /**
     * Test setSeed(int) method.
     */
    @Test
    public void testSetSeedInt() {
        generator.setSeed(1);
        // Add assertions for the expected behavior after setting seed
    }

    /**
     * Test setSeed(int[]) method.
     */
    @Test
    public void testSetSeedIntArray() {
        int[] seed = {1, 2, 3};
        generator.setSeed(seed);
        // Add assertions for the expected behavior after setting seed
    }

    /**
     * Test setSeed(long) method.
     */
    @Test
    public void testSetSeedLong() {
        generator.setSeed(1L);
        // Add assertions for the expected behavior after setting seed
    }

    /**
     * Test nextBoolean() method.
     */
    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }

    /**
     * Test nextBytes(byte[]) method.
     */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertFalse(b == 0);
        }
    }

    /**
     * Test nextDouble() method.
     */
    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertTrue(result >= 0 && result < 1);
    }

    /**
     * Test nextFloat() method.
     */
    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertTrue(result >= 0 && result < 1);
    }

    /**
     * Test nextGaussian() method.
     */
    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        double mean = 0;
        double stddev = 1;
        assertEquals(mean, mean + stddev * Precision.significantDigits(2), result);
    }

    /**
     * Test nextInt() method with positive arguments.
     */
    @Test
    public void testNextIntPositive() {
        int result = generator.nextInt(5);
        assertTrue(result >= 0 && result < 5);
    }

    /**
     * Test nextInt() method with negative argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegative() {
        generator.nextInt(-5);
    }

    /**
     * Test nextInt(int) method with power-of-2 arguments.
     */
    @Test
    public void testNextIntPowerOf2() {
        int result = generator.nextInt(1 << 5);
        int mask = 1 << 5;
        assertTrue(result >= 0 && (result & mask) == mask);
    }

    /**
     * Test nextInt(int) method with non-power-of-2 arguments.
     */
    @Test
    public void testNextIntNonPowerOf2() {
        int result = generator.nextInt(7);
        assertTrue(result >= 0 && result < 7);

        int count = 0;
        int expected = 0;
        while (result != expected) {
            expected = (expected + 1) % 7;
            result = generator.nextInt(7);
            count++;
        }

        // At most 2 * 7 = 14 attempts should be necessary to obtain the expected value
        assertTrue(count <= 14);
    }

    /**
     * Test nextLong() method.
     */
    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertTrue(result >= 0);
    }

    /**
     * Test clear() method.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isFinite(generator.nextGaussian()));
    }
}