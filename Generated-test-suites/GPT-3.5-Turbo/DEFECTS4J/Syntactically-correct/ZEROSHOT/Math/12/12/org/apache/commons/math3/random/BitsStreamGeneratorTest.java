import org.junit.Test;
import org.junit.Assert;

public class BitsStreamGeneratorTest {
    
    private BitsStreamGenerator generator;
    
    @Test
    public void testNextBoolean() {
        generator = new BitsStreamGeneratorImpl();
        boolean result = generator.nextBoolean();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testNextBytes() {
        generator = new BitsStreamGeneratorImpl();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        Assert.assertNotNull(bytes);
        Assert.assertEquals(10, bytes.length);
    }
    
    @Test
    public void testNextDouble() {
        generator = new BitsStreamGeneratorImpl();
        double result = generator.nextDouble();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testNextFloat() {
        generator = new BitsStreamGeneratorImpl();
        float result = generator.nextFloat();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testNextGaussian() {
        generator = new BitsStreamGeneratorImpl();
        double result = generator.nextGaussian();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testNextInt() {
        generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testNextIntWithParam() {
        generator = new BitsStreamGeneratorImpl();
        int param = 10;
        int result = generator.nextInt(param);
        Assert.assertNotNull(result);
        Assert.assertTrue(result >= 0 && result < param);
    }
    
    @Test
    public void testNextLong() {
        generator = new BitsStreamGeneratorImpl();
        long result = generator.nextLong();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testClear() {
        generator = new BitsStreamGeneratorImpl();
        generator.clear();
    }
}

class BitsStreamGeneratorImpl extends BitsStreamGenerator {
    
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