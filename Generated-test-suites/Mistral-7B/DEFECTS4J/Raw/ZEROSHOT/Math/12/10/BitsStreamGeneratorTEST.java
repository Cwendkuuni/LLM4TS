import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test of setSeed method, of class BitsStreamGenerator.
     */
    @Test
    public void testSetSeed() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        instance.setSeed(12345);
        assertEquals(12345, instance.nextInt());
    }

    /**
     * Test of setSeed method, with an array of integers, of class BitsStreamGenerator.
     */
    @Test
    public void testSetSeedWithIntArray() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        int[] seed = {1, 2, 3};
        instance.setSeed(seed);
        int[] expected = {1, 2, 3};
        int[] actual = new int[3];
        instance.nextInts(actual, 3);
        assertArrayEquals(expected, actual);
    }

    /**
     * Test of setSeed method, with a long, of class BitsStreamGenerator.
     */
    @Test
    public void testSetSeedWithLong() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        long seed = 67890123456L;
        instance.setSeed(seed);
        long expected = seed;
        long actual = instance.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Test of next method, of class BitsStreamGenerator.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNext() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        instance.next(10);
    }

    /**
     * Test of nextBoolean method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        boolean expResult = false;
        boolean result = instance.nextBoolean();
        assertEquals(expResult, result);
    }

    /**
     * Test of nextBytes method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextBytes() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        byte[] expResult = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};
        byte[] result = new byte[8];
        instance.nextBytes(result);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of nextDouble method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextDouble() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        double expResult = 0.123456789; // arbitrary value
        double result = instance.nextDouble();
        assertEquals(expResult, result, 0.000001);
    }

    /**
     * Test of nextFloat method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextFloat() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        float expResult = 0.123456f; // arbitrary value
        float result = instance.nextFloat();
        assertEquals(expResult, result, 0.00001f);
    }

    /**
     * Test of nextGaussian method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        double expResult = 0.123456; // arbitrary value
        double result = instance.nextGaussian();
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of nextInt method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextInt() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        int expResult = 42; // arbitrary value
        int result = instance.nextInt();
        assertEquals(expResult, result);
    }

    /**
     * Test of nextInts method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextInts() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        int[] expResult = new int[]{1, 2, 3, 4, 5};
        int[] result = new int[5];
        instance.nextInts(result, 5);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of nextLong method, of class BitsStreamGenerator.
     */
    @Test
    public void testNextLong() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        long expResult = 4294967295L; // Long.MAX_VALUE
        long result = instance.nextLong();
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class BitsStreamGenerator.
     */
    @Test
    public void testClear() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        instance.nextGaussian();
        instance.clear();
        assertFalse(instance.hasNextGaussian());
    }

    /**
     * Test of hasNextGaussian method, of class MyBitsStreamGenerator.
     */
    @Test
    public void testHasNextGaussian() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        assertFalse(instance.hasNextGaussian());
        instance.nextGaussian();
        assertTrue(instance.hasNextGaussian());
    }

    /**
     * Test of generateNextGaussian method, of class MyBitsStreamGenerator.
     */
    @Test
    public void testGenerateNextGaussian() {
        BitsStreamGenerator instance = new MyBitsStreamGenerator();
        double expResult = 0.123456; // arbitrary value
        double result = instance.generateNextGaussian();
        assertEquals(expResult, result, 0.00001);
    }

    /**
     * Test of generateNextGaussian method, with no cache, of class MyBitsStreamGenerator.
     */
    @Test
    public void testGenerateNextGaussianNoCache() {
        BitsStreamGenerator instance = new MyBitsStreamGeneratorNoCache();
        double expResult = 0.123456; // arbitrary value
        double result = instance.nextGaussian();
        assertEquals(expResult, result, 0.00001);
        double result2 = instance.nextGaussian();
        assertEquals(expResult, result2, 0.00001);
    }

}

class MyBitsStreamGenerator extends BitsStreamGenerator {

    @Override
    public void setSeed(int seed) {
        super.setSeed(seed);
    }

    @Override
    public void setSeed(int[] seed) {
        super.setSeed(seed);
    }

    @Override
    public void setSeed(long seed) {
        super.setSeed(seed);
    }

    @Override
    protected int next(int bits) {
        throw new UnsupportedOperationException();
    }

}

class MyBitsStreamGeneratorNoCache extends BitsStreamGenerator {

    @Override
    protected double nextGaussian() {
        generateNextGaussian();
        return super.nextGaussian();
    }

    private boolean hasNextGaussian = false;

    @Override
    protected void clear() {
        super.clear();
        hasNextGaussian = false;
    }

    @Override
    protected double generateNextGaussian() {
        double value = super.nextGaussian();
        hasNextGaussian = true;
        return value;
    }

}