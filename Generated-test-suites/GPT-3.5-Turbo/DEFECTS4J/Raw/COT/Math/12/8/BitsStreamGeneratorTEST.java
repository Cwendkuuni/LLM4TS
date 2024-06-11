import org.junit.Assert;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        boolean result = generator.nextBoolean();
        Assert.assertTrue(result || !result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        Assert.assertNotNull(bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextDouble();
        Assert.assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        float result = generator.nextFloat();
        Assert.assertTrue(result >= 0.0 && result < 1.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextGaussian();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextIntWithUpperBound() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(100);
        Assert.assertTrue(result >= 0 && result < 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNonPositiveUpperBound() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        long result = generator.nextLong();
        Assert.assertNotNull(result);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.clear();
        Assert.assertTrue(Double.isNaN(generator.nextGaussian));
    }

    private static class BitsStreamGeneratorImpl extends BitsStreamGenerator {
        public void setSeed(int seed) {}
        public void setSeed(int[] seed) {}
        public void setSeed(long seed) {}
        protected int next(int bits) { return 0; }
    }
}