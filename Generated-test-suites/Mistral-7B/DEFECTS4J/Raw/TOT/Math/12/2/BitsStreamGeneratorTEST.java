package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** The instance under test. */
    private BitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp() {
        generator = new XorShiftRandomGenerator(); // use default implementation
    }

    /**
     * Tests the {@code nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        Assert.assertFalse("false expected", generator.nextBoolean());
        Assert.assertTrue("true expected", generator.nextBoolean());
        Assert.assertFalse("false expected", generator.nextBoolean());
        Assert.assertFalse("false expected", generator.nextBoolean());
        Assert.assertFalse("false expected", generator.nextBoolean());
        Assert.assertFalse("false expected", generator.nextBoolean());
        Assert.assertTrue("true expected", generator.nextBoolean());
        Assert.assertTrue("true expected", generator.nextBoolean());
        Assert.assertFalse("false expected", generator.nextBoolean());
    }

    /**
     * Tests the {@code nextBytes(byte[])} method with a byte array of length 1.
     */
    @Test
    public void testNextBytesByteArraySingleByte() {
        byte[] bytes = new byte[1];
        generator.nextBytes(bytes);
        Assert.assertArrayEquals("single byte expected", new byte[] { (byte) 0xaa }, bytes);
    }

    /**
     * Tests the {@code nextBytes(byte[])} method with a byte array of length 4.
     */
    @Test
    public void testNextBytesByteArrayFourBytes() {
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; ++i) {
            Assert.assertArrayEquals("expected random byte", new byte[] { (byte) 0xaa, 0x01, 0x03, 0xab }, bytes);
            // repeat test with same seed to check deterministic behavior
            generator.setSeed(bytes[0] << 24 | bytes[1] << 16 | bytes[2] << 8 | bytes[3]);
            generator.nextBytes(bytes);
            Assert.assertArrayEquals("expected same random bytes", new byte[] { (byte) 0xaa, 0x01, 0x03, 0xab }, bytes);
        }
    }

    /**
     * Tests the {@code nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        Assert.assertEquals("0.0 expected", 0.0, generator.nextDouble(), 1e-15);
        Assert.assertEquals("1.0 expected", 1.0, generator.nextDouble(), 1e-15);
        Assert.assertEquals("0.5 expected", 0.5, generator.nextDouble(), 1e-15);
    }

    /**
     * Tests the {@code nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        Assert.assertEquals("0.0 expected", 0.0f, generator.nextFloat(), 1e-6f);
        Assert.assertEquals("1.0 expected", 1.0f, generator.nextFloat(), 1e-6f);
        Assert.assertEquals("0.5 expected", 0.5f, generator.nextFloat(), 1e-6f);
    }

    /**
     * Tests the {@code nextGaussian()} method.
     */
    @Test
    public void testNextGaussian() {
        Assert.assertEquals("0.0 expected", 0.0, generator.nextGaussian(), 1e-15);
        Assert.assertEquals("1.0 expected", 1.0, generator.nextGaussian(), 1e-15);
        Assert.assertEquals("0.5 expected", 0.5, generator.nextGaussian(), 1e-15);
        Assert.assertEquals("-0.5 expected", -0.5, generator.nextGaussian(), 1e-15);
    }

    /**
     * Tests the {@code nextInt()} method with byte range.
     */
    @Test
    public void testNextIntByteRange() {
        Assert.assertEquals("expected 1", (byte) 1, generator.nextInt(128), 1);
        Assert.assertEquals("expected 0", (byte) 0, generator.nextInt(128), 1);
    }

    /**
     * Tests the {@code nextInt()} method with short range.
     */
    @Test
    public void testNextIntShortRange() {
        Assert.assertEquals("expected 1", (short) 1, generator.nextInt(Short.MAX_VALUE), 1);
        Assert.assertEquals("expected 0", (short) 0, generator.nextInt(Short.MAX_VALUE), 1);
    }

    /**
     * Tests the {@code nextInt()} method with int range.
     */
    @Test
    public void testNextIntIntRange() {
        Assert.assertEquals("expected 1", 1, generator.nextInt(Integer.MAX_VALUE), 1);
        Assert.assertEquals("expected 0", 0, generator.nextInt(Integer.MAX_VALUE), 1);
    }

    /**
     * Tests the {@code nextInt()} method with long range.
     */
    @Test
    public void testNextIntLongRange() {
        Assert.assertEquals("expected 1", 1L, generator.nextInt(Long.MAX_VALUE), 1);
        Assert.assertEquals("expected 0", 0L, generator.nextInt(Long.MAX_VALUE), 1);
    }

    /**
     * Tests the {@code nextLong()} method.
     */
    @Test
    public void testNextLong() {
        Assert.assertEquals("expected 0", 0L, generator.nextLong());
        Assert.assertEquals("expected -1", -1L, generator.nextLong());
        Assert.assertEquals("expected 1", 1L, generator.nextLong());
    }

    /**
     * Tests the {@code clear()} method.
     */
    @Test
    public void testClear() {
        generator.nextGaussian();
        generator.clear();
        Assert.assertFalse("expected NaN", Double.isNaN(generator.nextGaussian()));
    }

    /**
     * Tests the {@code nextInt()} method with negative range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntNegativeRange() {
        generator.nextInt(-1);
    }

    /**
     * Tests the {@code nextInt()} method with zero range.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntZeroRange() {
        generator.nextInt(0);
    }

    /**
     * Tests the {@code nextInt()} method with range larger than int.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextIntRangeLargerThanInt() {
        generator.nextInt(Integer.MAX_VALUE + 1);
    }

    /**
     * Tests the {@code nextBytes(byte[])} method with null array argument.
     */
    @Test(expected = NullPointerException.class)
    public void testNextBytesNullByteArray() {
        generator.nextBytes(null);
    }

}