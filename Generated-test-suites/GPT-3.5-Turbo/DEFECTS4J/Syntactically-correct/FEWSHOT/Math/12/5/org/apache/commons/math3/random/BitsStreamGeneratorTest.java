import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Before
    public void setup() {
        generator = new TestBitsStreamGenerator();
    }

    @Test
    public void testNextBoolean() {
        boolean result1 = generator.nextBoolean();
        boolean result2 = generator.nextBoolean();
        boolean result3 = generator.nextBoolean();

        assertTrue(result1);
        assertFalse(result2);
        assertTrue(result3);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);

        assertEquals(10, bytes.length);
        for (byte b : bytes) {
            assertTrue(b >= 0);
            assertTrue(b <= 255);
        }
    }

    @Test
    public void testNextDouble() {
        double result1 = generator.nextDouble();
        double result2 = generator.nextDouble();
        double result3 = generator.nextDouble();

        assertTrue(result1 >= 0.0 && result1 < 1.0);
        assertTrue(result2 >= 0.0 && result2 < 1.0);
        assertTrue(result3 >= 0.0 && result3 < 1.0);
    }

    @Test
    public void testNextFloat() {
        float result1 = generator.nextFloat();
        float result2 = generator.nextFloat();
        float result3 = generator.nextFloat();

        assertTrue(result1 >= 0.0f && result1 < 1.0f);
        assertTrue(result2 >= 0.0f && result2 < 1.0f);
        assertTrue(result3 >= 0.0f && result3 < 1.0f);
    }

    @Test
    public void testNextGaussian() {
        double result1 = generator.nextGaussian();
        double result2 = generator.nextGaussian();
        double result3 = generator.nextGaussian();

        assertTrue(result1 >= Double.NEGATIVE_INFINITY && result1 <= Double.POSITIVE_INFINITY);
        assertTrue(result2 >= Double.NEGATIVE_INFINITY && result2 <= Double.POSITIVE_INFINITY);
        assertTrue(result3 >= Double.NEGATIVE_INFINITY && result3 <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNextInt() {
        int result1 = generator.nextInt();
        int result2 = generator.nextInt();
        int result3 = generator.nextInt();

        assertTrue(result1 >= Integer.MIN_VALUE && result1 <= Integer.MAX_VALUE);
        assertTrue(result2 >= Integer.MIN_VALUE && result2 <= Integer.MAX_VALUE);
        assertTrue(result3 >= Integer.MIN_VALUE && result3 <= Integer.MAX_VALUE);
    }

    @Test
    public void testNextIntWithLimit() {
        int result1 = generator.nextInt(10);
        int result2 = generator.nextInt(10);
        int result3 = generator.nextInt(10);

        assertTrue(result1 >= 0 && result1 < 10);
        assertTrue(result2 >= 0 && result2 < 10);
        assertTrue(result3 >= 0 && result3 < 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithInvalidLimit() {
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        long result1 = generator.nextLong();
        long result2 = generator.nextLong();
        long result3 = generator.nextLong();

        assertTrue(result1 >= Long.MIN_VALUE && result1 <= Long.MAX_VALUE);
        assertTrue(result2 >= Long.MIN_VALUE && result2 <= Long.MAX_VALUE);
        assertTrue(result3 >= Long.MIN_VALUE && result3 <= Long.MAX_VALUE);
    }

    private static class TestBitsStreamGenerator extends BitsStreamGenerator {
        @Override
        public void setSeed(int seed) {}

        @Override
        public void setSeed(int[] seed) {}

        @Override
        public void setSeed(long seed) {}

        @Override
        protected int next(int bits) {
            return 0;
        }
    }
}