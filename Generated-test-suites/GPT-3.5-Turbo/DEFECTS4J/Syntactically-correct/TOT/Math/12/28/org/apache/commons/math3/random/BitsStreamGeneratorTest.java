import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new TestBitsStreamGenerator();
    }

    // Test NextBoolean Method

    @Test
    public void testNextBooleanReturnsTrue() {
        boolean result = generator.nextBoolean();
        assertTrue(result);
    }

    @Test
    public void testNextBooleanReturnsFalse() {
        boolean result = generator.nextBoolean();
        assertFalse(result);
    }

    // Test NextBytes Method

    @Test
    public void testNextBytesWithEmptyArray() {
        byte[] bytes = new byte[0];
        generator.nextBytes(bytes);
        assertEquals(0, bytes.length);
    }

    @Test
    public void testNextBytesWithNonEmptyArray() {
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertNotNull(b);
        }
    }

    // Test NextDouble Method

    @Test
    public void testNextDoubleReturnsInRange() {
        double result = generator.nextDouble();
        assertTrue(result >= 0.0 && result < 1.0);
    }

    // Test NextFloat Method

    @Test
    public void testNextFloatReturnsInRange() {
        float result = generator.nextFloat();
        assertTrue(result >= 0.0 && result < 1.0);
    }

    // Test NextGaussian Method

    @Test
    public void testNextGaussianReturnsDifferentValues() {
        double result1 = generator.nextGaussian();
        double result2 = generator.nextGaussian();
        assertNotEquals(result1, result2);
    }

    // Test NextInt Method

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithBound() {
        int bound = 10;
        int result = generator.nextInt(bound);
        assertTrue(result >= 0 && result < bound);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeBound() {
        int bound = -10;
        generator.nextInt(bound);
    }

    // Test NextLong Method

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    // Test Clear Method

    @Test
    public void testClear() {
        generator.clear();
        double result = generator.nextGaussian();
        assertTrue(Double.isNaN(result));
    }

    private class TestBitsStreamGenerator extends BitsStreamGenerator {

        @Override
        public void setSeed(int seed) {
        }

        @Override
        public void setSeed(int[] seed) {
        }

        @Override
        public void setSeed(long seed) {
        }

        @Override
        protected int next(int bits) {
            return 0;
        }
    }
}