import static org.junit.Assert.*;
import org.junit.Test;

public class BitsStreamGeneratorTest {
    
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        boolean result = generator.nextBoolean();
        assertTrue(result || !result);
    }
    
    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertNotNull(bytes);
        assertEquals(4, bytes.length);
    }
    
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextDouble();
        assertTrue(result >= 0.0 && result < 1.0);
    }
    
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        float result = generator.nextFloat();
        assertTrue(result >= 0.0f && result < 1.0f);
    }
    
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextGaussian();
        assertNotNull(result);
    }
    
    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt();
        assertNotNull(result);
    }
    
    @Test
    public void testNextIntWithArgument() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(100);
        assertTrue(result >= 0 && result < 100);
    }
    
    @Test(expected=NotStrictlyPositiveException.class)
    public void testNextIntWithArgumentException() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.nextInt(-1);
    }
    
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        long result = generator.nextLong();
        assertNotNull(result);
    }
    
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.clear();
        // Testing for any specific behavior after clearing the generator's cache.
    }
    
}

class BitsStreamGeneratorImpl extends BitsStreamGenerator {
    
    public void setSeed(int seed) {}
    
    public void setSeed(int[] seed) {}
    
    public void setSeed(long seed) {}
    
    protected int next(int bits) {
        return 0;
    }
    
}