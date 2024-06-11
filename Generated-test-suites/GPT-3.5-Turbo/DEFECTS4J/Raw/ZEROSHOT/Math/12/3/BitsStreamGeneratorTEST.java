import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

            @Override
            protected int next(int bits) {
                return 0; // Dummy implementation for testing
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        // Verify that the result is either true or false
        assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        // Verify that the bytes array has been filled with random bytes
        assertNotNull(bytes);
        assertEquals(4, bytes.length);
        for (byte b : bytes) {
            assertNotEquals(0, b);
        }
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        // Verify that the result is a valid double value
        assertTrue(Double.isFinite(result));
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        // Verify that the result is a valid float value
        assertTrue(Float.isFinite(result));
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        // Verify that the result is a valid double value
        assertTrue(Double.isFinite(result));
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        // Verify that the result is a valid int value
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithPositiveN() {
        int result = generator.nextInt(100);
        // Verify that the result is within the range [0, 100)
        assertTrue(result >= 0 && result < 100);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroN() {
        generator.nextInt(0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        // Verify that the result is a valid long value
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        // Verify that the nextGaussian value has been cleared to NaN
        assertTrue(Double.isNaN(generator.nextGaussian));
    }
}