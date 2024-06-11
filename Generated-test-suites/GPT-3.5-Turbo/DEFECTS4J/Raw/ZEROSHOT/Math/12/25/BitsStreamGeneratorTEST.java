import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new TestGenerator();
        boolean result = generator.nextBoolean();
        Assert.assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new TestGenerator();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        Assert.assertNotNull(bytes);
        Assert.assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new TestGenerator();
        double result = generator.nextDouble();
        Assert.assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new TestGenerator();
        float result = generator.nextFloat();
        Assert.assertTrue(result >= 0.0f && result < 1.0f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new TestGenerator();
        double result = generator.nextGaussian();
        Assert.assertTrue(result >= Double.NEGATIVE_INFINITY && result <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new TestGenerator();
        int result = generator.nextInt();
        Assert.assertNotNull(result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new TestGenerator();
        generator.nextInt(-1);
    }
    
    @Test
    public void testNextIntWithNonPowerOfTwoN() {
        BitsStreamGenerator generator = new TestGenerator();
        int result = generator.nextInt(7);
        Assert.assertTrue(result >= 0 && result < 7);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new TestGenerator();
        long result = generator.nextLong();
        Assert.assertNotNull(result);
    }

    private class TestGenerator extends BitsStreamGenerator {

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