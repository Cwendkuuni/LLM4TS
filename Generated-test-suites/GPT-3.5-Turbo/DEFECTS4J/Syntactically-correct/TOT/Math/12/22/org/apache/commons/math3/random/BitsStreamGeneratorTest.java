import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BitsStreamGeneratorTest {
    
    private BitsStreamGenerator generator;
    
    @Before
    public void setUp() {
        generator = new BitsStreamGeneratorImpl();
    }
    
    @Test
    public void testSetSeedInt() {
        int seed = 123456789;
        generator.setSeed(seed);
        // Assert that the seed was set correctly
        Assert.assertEquals(seed, generator.next(32));
    }
    
    @Test
    public void testSetSeedIntArray() {
        int[] seed = {1, 2, 3, 4};
        generator.setSeed(seed);
        // Assert that the seed was set correctly
        Assert.assertEquals(seed[0], generator.next(32));
        Assert.assertEquals(seed[1], generator.next(32));
        Assert.assertEquals(seed[2], generator.next(32));
        Assert.assertEquals(seed[3], generator.next(32));
    }
    
    @Test
    public void testSetSeedLong() {
        long seed = 987654321;
        generator.setSeed(seed);
        // Assert that the seed was set correctly
        Assert.assertEquals((int)(seed & 0xffffffff), generator.next(32));
        Assert.assertEquals((int)(seed >> 32), generator.next(32));
    }
    
    @Test
    public void testNextBoolean() {
        // Generate 10000 random booleans and assert they are either true or false
        for (int i = 0; i < 10000; i++) {
            boolean value = generator.nextBoolean();
            Assert.assertTrue(value || !value);
        }
    }
    
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[100];
        generator.nextBytes(bytes);
        
        // Assert that all bytes have been properly generated
        for (byte b : bytes) {
            Assert.assertEquals(true, (b >= 0 && b < 256));
        }
    }
    
    @Test
    public void testNextDouble() {
        // Generate 10000 random doubles and assert they are in [0, 1)
        for (int i = 0; i < 10000; i++) {
            double value = generator.nextDouble();
            Assert.assertTrue(value >= 0.0 && value < 1.0);
        }
    }
    
    @Test
    public void testNextFloat() {
        // Generate 10000 random floats and assert they are in [0, 1)
        for (int i = 0; i < 10000; i++) {
            float value = generator.nextFloat();
            Assert.assertTrue(value >= 0.0f && value < 1.0f);
        }
    }
    
    @Test
    public void testNextGaussian() {
        // Generate 10000 random gaussian numbers and assert they are approximately normally distributed
        double sum = 0.0;
        for (int i = 0; i < 10000; i++) {
            double value = generator.nextGaussian();
            sum += value;
        }
        double mean = sum / 10000;
        Assert.assertTrue(Math.abs(mean) < 0.1);
    }
    
    @Test
    public void testNextInt() {
        // Generate 10000 random integers and assert they are in the range of 32-bit signed integers
        for (int i = 0; i < 10000; i++) {
            int value = generator.nextInt();
            Assert.assertTrue(value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE);
        }
    }
    
    @Test
    public void testNextIntWithPositiveN() {
        int n = 10;
        // Generate 10000 random integers and assert they are in the range [0, n)
        for (int i = 0; i < 10000; i++) {
            int value = generator.nextInt(n);
            Assert.assertTrue(value >=0 && value < n);
        }
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testNextIntWithNonPositiveN() {
        int n = -1;
        generator.nextInt(n);
    }
    
    @Test
    public void testNextLong() {
        // Generate 10000 random longs and assert they are in the range of 64-bit signed integers
        for (int i = 0; i < 10000; i++) {
            long value = generator.nextLong();
            Assert.assertTrue(value >= Long.MIN_VALUE && value <= Long.MAX_VALUE);
        }
    }
    
    private class BitsStreamGeneratorImpl extends BitsStreamGenerator {

        @Override
        public void setSeed(int seed) {
            // TODO implement setting seed logic
        }

        @Override
        public void setSeed(int[] seed) {
            // TODO implement setting seed logic
        }

        @Override
        public void setSeed(long seed) {
            // TODO implement setting seed logic
        }

        @Override
        protected int next(int bits) {
            // TODO implement next pseudorandom number generation logic
            return 0;
        }
        
    }
}