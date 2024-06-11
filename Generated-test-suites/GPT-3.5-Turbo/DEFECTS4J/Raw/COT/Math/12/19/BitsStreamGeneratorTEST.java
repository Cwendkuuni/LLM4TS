import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        boolean result = generator.nextBoolean();
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextDouble();
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        float result = generator.nextFloat();
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        double result = generator.nextGaussian();
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt();
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testNextIntWithArgument() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(10);
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithArgumentInvalid() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        int result = generator.nextInt(-10);
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        long result = generator.nextLong();
        // Assert statement to check the functionality of the method
        // ...

    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
        generator.clear();
        // Assert statement to check the functionality of the method
        // ...

    }
}