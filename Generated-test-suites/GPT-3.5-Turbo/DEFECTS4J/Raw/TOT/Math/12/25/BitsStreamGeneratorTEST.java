import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        // Instantiate the concrete class that extends BitsStreamGenerator
        generator = new ConcreteBitsStreamGenerator();
    }
    
    // Test case for setSeed(int seed)
    @Test
    public void testSetSeedWithInt() {
        int seed = 12345;
        
        generator.setSeed(seed);
        
        // Verify that the generator is initialized with the given seed
        // by generating and verifying the first few random numbers
        assertEquals(0, generator.nextInt());
        assertEquals(45234546, generator.nextInt());
        assertEquals(-15253528, generator.nextInt());
    }

    // Test case for setSeed(int[] seed)
    @Test
    public void testSetSeedWithIntArray() {
        int[] seed = {123, 456, 789};
        
        generator.setSeed(seed);
        
        // Verify that the generator is initialized with the given seed
        // by generating and verifying the first few random numbers
        assertEquals(0, generator.nextInt());
        assertEquals(4646098, generator.nextInt());
        assertEquals(-179019746, generator.nextInt());
    }
    
    // Test case for setSeed(long seed)
    @Test
    public void testSetSeedWithLong() {
        long seed = 1234567890L;
        
        generator.setSeed(seed);
        
        // Verify that the generator is initialized with the given seed
        // by generating and verifying the first few random numbers
        assertEquals(0, generator.nextInt());
        assertEquals(1296716897, generator.nextInt());
        assertEquals(-1755644430, generator.nextInt());
    }

    // Test case for nextBoolean()
    @Test
    public void testNextBoolean() {
        // Verify that the generated random booleans are distributed randomly
        int trueCount = 0;
        int falseCount = 0;

        for (int i = 0; i < 1000; i++) {
            if (generator.nextBoolean()) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        assertTrue(trueCount > 400 && trueCount < 600);
        assertTrue(falseCount > 400 && falseCount < 600);
    }

    // Test case for nextBytes(byte[] bytes)
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        
        generator.nextBytes(bytes);
        
        // Verify that the generated array contains random bytes
        boolean isRandom = false;
        
        for (int i = 1; i < bytes.length; i++) {
            if (bytes[i] != bytes[i - 1]) {
                isRandom = true;
                break;
            }
        }
        
        assertTrue(isRandom);
    }

    // Test case for nextDouble()
    @Test
    public void testNextDouble() {
        // Verify that the generated doubles are within the range [0, 1)
        for (int i = 0; i < 1000; i++) {
            double random = generator.nextDouble();
            assertTrue(random >= 0.0 && random < 1.0);
        }
    }

    // Test case for nextFloat()
    @Test
    public void testNextFloat() {
        // Verify that the generated floats are within the range [0, 1)
        for (int i = 0; i < 1000; i++) {
            float random = generator.nextFloat();
            assertTrue(random >= 0.0 && random < 1.0);
        }
    }

    // Test case for nextGaussian()
    @Test
    public void testNextGaussian() {
        // Verify that the generated Gaussian numbers have a mean of 0 and a standard deviation of 1
        double sum = 0.0;

        for (int i = 0; i < 1000; i++) {
            double random = generator.nextGaussian();
            sum += random;
        }

        double mean = sum / 1000;

        assertTrue(Math.abs(mean) < 0.01);
    }

    // Test case for nextInt()
    @Test
    public void testNextInt() {
        // Verify that the generated random numbers are uniformly distributed
        int[] counts = new int[10];

        for (int i = 0; i < 1000; i++) {
            int random = generator.nextInt(10);
            counts[random]++;
        }

        for (int count : counts) {
            assertTrue(count > 80 && count < 120);
        }
    }
    
    // Test case for nextInt(int n) with n = 0
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZero() {
        generator.nextInt(0);
    }
    
    // Test case for nextInt(int n) with n < 0
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegative() {
        generator.nextInt(-10);
    }

    // Test case for nextLong()
    @Test
    public void testNextLong() {
        // Verify that the generated random longs are uniformly distributed
        long[] counts = new long[10];

        for (int i = 0; i < 1000; i++) {
            long random = generator.nextLong() % 10;
            counts[(int) random]++;
        }

        for (long count : counts) {
            assertTrue(count > 80 && count < 120);
        }
    }

}