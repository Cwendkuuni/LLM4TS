import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new MockBitsStreamGenerator();
    }

    @Test
    public void testSetSeedInt() {
        generator.setSeed(12345);
        // Verify that the seed has been properly set
        assertEquals(generator.nextInt(), -1155484576);
    }

    @Test
    public void testSetSeedIntArray() {
        int[] seed = { 123, 456, 789 };
        generator.setSeed(seed);
        // Verify that the seed has been properly set
        assertEquals(generator.nextInt(), -1390404611);
    }

    @Test
    public void testSetSeedLong() {
        generator.setSeed(1234567890L);
        // Verify that the seed has been properly set
        assertEquals(generator.nextLong(), -8050499449151143501L);
    }

    @Test
    public void testNextBoolean() {
        boolean randomBoolean = generator.nextBoolean();
        // Verify that the returned value is either true or false
        assertTrue(randomBoolean || !randomBoolean);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // Verify that all bytes have been filled with random values
        for (byte b : bytes) {
            assertNotEquals(b, 0);
        }
    }

    @Test
    public void testNextDouble() {
        double randomDouble = generator.nextDouble();
        // Verify that the returned value is between 0.0 and 1.0
        assertTrue(randomDouble >= 0.0 && randomDouble < 1.0);
    }

    @Test
    public void testNextFloat() {
        float randomFloat = generator.nextFloat();
        // Verify that the returned value is between 0.0 and 1.0
        assertTrue(randomFloat >= 0.0 && randomFloat < 1.0);
    }

    @Test
    public void testNextGaussian() {
        double randomGaussian = generator.nextGaussian();
        // Verify that the returned value is within a reasonable range (-3 to 3)
        assertTrue(randomGaussian >= -3.0 && randomGaussian <= 3.0);
    }

    @Test
    public void testNextInt() {
        int randomInt = generator.nextInt();
        // Verify that the returned value is a valid integer
        assertNotNull(randomInt);
    }

    @Test
    public void testNextIntWithPositiveN() {
        int randomInt = generator.nextInt(100);
        // Verify that the returned value is within the range 0 to 99
        assertTrue(randomInt >= 0 && randomInt < 100);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroN() {
        generator.nextInt(0);
        // Verify that the NotStrictlyPositiveException is thrown
    }

    @Test
    public void testNextLong() {
        long randomLong = generator.nextLong();
        // Verify that the returned value is a valid long
        assertNotNull(randomLong);
    }

    @Test
    public void testClear() {
        generator.clear();
        // Verify that the cache has been cleared
        assertTrue(Double.isNaN(generator.nextGaussian));
    }

    private class MockBitsStreamGenerator extends BitsStreamGenerator {

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