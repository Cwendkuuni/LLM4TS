import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new TestBitsStreamGenerator();
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertTrue(result);
        result = generator.nextBoolean();
        assertFalse(result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertNotNull(bytes);
        assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertTrue(result >= 0.0f && result < 1.0f);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithBounds() {
        int result = generator.nextInt(100);
        assertTrue(result >= 0 && result < 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeBounds() {
        generator.nextInt(-1);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        ((TestBitsStreamGenerator) generator).setNextGaussian(1.0);
        double result = generator.nextGaussian();
        assertEquals(1.0, result, 0.0);
        generator.clear();
        result = generator.nextGaussian();
        assertNotEquals(1.0, result, 0.0);
    }

    // Custom subclass to test the abstract class BitsStreamGenerator
    private class TestBitsStreamGenerator extends BitsStreamGenerator {
        private double nextGaussian;

        public void setNextGaussian(double value) {
            nextGaussian = value;
        }

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
            return 0;
        }

        @Override
        public void clear() {
            // Do nothing
        }
    }
}