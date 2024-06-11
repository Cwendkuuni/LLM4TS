package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test implementation of {@link BitsStreamGenerator}. */
    private static class TestBitsStreamGenerator extends BitsStreamGenerator {
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
            // Implementation for testing purposes.
            return (int) (Math.random() * (1 << bits));
        }
    }

    /** Test instance of {@link TestBitsStreamGenerator}. */
    private final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();

    /**
     * Test {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        for (int i = 0; i < 1000; ++i) {
            assertEquals("nextBoolean() returned false", false, generator.nextBoolean());
            assertEquals("nextBoolean() returned true", true, generator.nextBoolean());
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        final byte[] bytes1 = new byte[10];
        final byte[] bytes2 = new byte[10];
        final byte[] bytes3 = new byte[10];

        generator.nextBytes(bytes1);
        generator.nextBytes(bytes2);

        assertEquals("nextBytes() should generate different arrays",
                false, bytes1.equals(bytes2));

        generator.nextBytes(bytes3);
        assertArrayEquals("nextBytes() should generate different bytes",
                bytes1, bytes3);

        final byte[] expected = new byte[] {0x12, 0x34, 0x56, 0x78, 0x90, 0xAD, 0xFB, 0xDC, 0x21, 0x43};
        generator.nextBytes(bytes1);
        assertArrayEquals("nextBytes() should generate the same bytes for the same seed", expected, bytes1);

        generator.clear();
    }

    /**
     * Test {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        for (int i = 0; i < 1000; ++i) {
            final double d = generator.nextDouble();
            assertEquals("nextDouble() should generate a value between 0 and 1", 0.0, d, 0.0001);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        for (int i = 0; i < 1000; ++i) {
            final float f = generator.nextFloat();
            assertEquals("nextFloat() should generate a value between 0 and 1", 0.0f, f, 0.0001f);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        final double eps = 0.0001;

        for (int i = 0; i < 1000; ++i) {
            final double g = generator.nextGaussian();
            final double m = FastMath.sqrt(0.5);
            assertEquals("nextGaussian() should generate a value with mean 0 and standard deviation 1", 0.0, g - 0.0, eps);
            assertEquals("nextGaussian() should generate a value with mean 0 and positive standard deviation", m, FastMath.abs(g), eps);
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        for (int i = 0; i < 1000; ++i) {
            for (int n = 1; n <= 65536; ++n) {
                final int result = generator.nextInt(n);
                assertEquals("nextInt() should generate a value between 0 and " + n, 0, result, n - 1);
                assertEquals("nextInt() should generate a value between 0 and " + n, n - 1, result % n, 0);
            }
        }

        try {
            generator.nextInt(-1);
            fail("nextInt() should throw an exception for negative numbers");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            generator.nextInt(0);
            fail("nextInt() should throw an exception for zero");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            generator.nextInt((short) 0xFFFF);
            fail("nextInt() should throw an exception for negative numbers");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            generator.nextInt((short) 0xFFFF + 1);
            fail("nextInt() should throw an exception for values greater than Integer.MAX_VALUE");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        for (int i = 0; i < 1000; ++i) {
            final long l = generator.nextLong();
            final long max = 0xFFFFFFFFL;
            assertEquals("nextLong() should generate a 32-bit integer", max, l & max, 0);
        }

        try {
            final long l = generator.nextLong();
            final long max = 0xFFFFFFFFFL;
            assertEquals("nextLong() should generate a 64-bit integer", max, l & max, 0);
        } catch (IllegalStateException e) {
            // expected, as this is a 32-bit generator
        }
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int)}.
     */
    @Test
    public void testSetSeedInt() {
        final int seed1 = 0x12345678;
        final int seed2 = 0x87654321;

        generator.nextBoolean();
        generator.setSeed(seed1);
        generator.nextBoolean();
        generator.nextBoolean();

        generator.nextBytes(new byte[10]);
        generator.setSeed(seed2);
        generator.nextBytes(new byte[10]);

        assertArrayEquals("nextBytes() should generate different bytes for different seeds",
                new byte[] {0x12, 0x34, 0x56, 0x78, 0x90, 0xAD, 0xFB, 0xDC, 0x21, 0x43},
                new byte[] {0x87, 0x65, 0x43, 0x21, 0xAD, 0xDC, 0xFB, 0x90, 0x43, 0x21});
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(int[])}.
     */
    @Test
    public void testSetSeedIntArray() {
        final int[] seed = new int[] {1, 2, 3, 4, 5};

        generator.nextBoolean();
        generator.setSeed(seed);
        generator.nextBoolean();
        generator.nextBoolean();

        generator.nextBytes(new byte[10]);
        generator.setSeed(seed);
        generator.nextBytes(new byte[10]);

        assertArrayEquals("nextBytes() should generate the same bytes for the same seed",
                new byte[] {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55,
                        (byte) 0x66, (byte) 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA},
                new byte[] {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55,
                        (byte) 0x66, (byte) 0x77, (byte) 0x88, (byte) 0x99, (byte) 0xAA},
                "nextBytes() should generate the same bytes for the same seed");
    }

    /**
     * Test {@link BitsStreamGenerator#setSeed(long)}.
     */
    @Test
    public void testSetSeedLong() {
        final long seed1 = 0x1234567890ABCDEF1L;
        final long seed2 = 0x87654321FEDCBA98L;

        generator.nextBoolean();
        generator.setSeed(seed1);
        generator.nextBoolean();
        generator.nextBoolean();

        generator.nextBytes(new byte[10]);
        generator.setSeed(seed2);
        generator.nextBytes(new byte[10]);

        assertArrayEquals("nextBytes() should generate different bytes for different seeds",
                new byte[] {0x87, 0x65, 0x43, 0x21, 0xFB, 0xDC, 0x98, 0xBA, 0xC8, 0xAD},
                new byte[] {0x12, 0x34, 0x56, 0x78, 0x90, 0xAB, 0xCD, 0xEF, 0x11, 0x22},
                "nextBytes() should generate different bytes for different seeds");
    }

}