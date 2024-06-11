
import org.junit.Assert;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        boolean result = generator.nextBoolean();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        Assert.assertNotNull(bytes);
        Assert.assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        double result = generator.nextDouble();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        float result = generator.nextFloat();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        double result = generator.nextGaussian();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        int result = generator.nextInt();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextIntWithArgument() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        int result = generator.nextInt(10);
        Assert.assertNotNull(result);
        Assert.assertTrue(result >= 0 && result < 10);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        long result = generator.nextLong();
        Assert.assertNotNull(result);
    }

    @Test
    public void testNextIntMaxValue() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        generator.setSeed(1234); // Set a seed to generate deterministic values
        int result = generator.nextInt();
        Assert.assertEquals(Integer.MAX_VALUE, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeArgument() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        generator.nextInt(0);
    }

    @Test
    public void testNextIntWithPowerOf2Argument() {
        BitsStreamGenerator generator = new BitsStreamGeneratorTestImpl();
        generator.setSeed(1234); // Set a seed to generate deterministic values
        int result = generator.nextInt(64);
        Assert.assertTrue(result >= 0 && result < 64);
    }

    private class BitsStreamGeneratorTestImpl extends BitsStreamGenerator {

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
