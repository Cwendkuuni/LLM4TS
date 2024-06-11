import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        boolean result = generator.nextBoolean();
        Assert.assertTrue(result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        Assert.assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextDouble();
        Assert.assertNotEquals(Double.NaN, result);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        float result = generator.nextFloat();
        Assert.assertNotEquals(Float.NaN, result);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextGaussian();
        Assert.assertNotEquals(Double.NaN, result);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt();
        Assert.assertNotEquals(0, result);
    }

    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(10);
        Assert.assertTrue(result >= 0 && result < 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        long result = generator.nextLong();
        Assert.assertNotEquals(0L, result);
    }

    private class BitsStreamGeneratorImpl extends BitsStreamGenerator {

        @Override
        public void setSeed(int seed) {
            // Implementation not needed for testing
        }

        @Override
        public void setSeed(int[] seed) {
            // Implementation not needed for testing
        }

        @Override
        public void setSeed(long seed) {
            // Implementation not needed for testing
        }

        @Override
        protected int next(int bits) {
            return 0; // Dummy implementation for testing
        }
    }

}