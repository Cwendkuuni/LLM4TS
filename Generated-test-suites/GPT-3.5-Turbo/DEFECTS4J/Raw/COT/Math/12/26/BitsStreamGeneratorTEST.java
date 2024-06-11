
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        boolean result = generator.nextBoolean();
        // write assertion here
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // write assertion here
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextDouble();
        // write assertion here
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        float result = generator.nextFloat();
        // write assertion here
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextGaussian();
        // write assertion here
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt();
        // write assertion here
    }

    @Test
    public void testNextIntWithN() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(10);
        // write assertion here
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        long result = generator.nextLong();
        // write assertion here
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.clear();
        // write assertion here
    }
}
