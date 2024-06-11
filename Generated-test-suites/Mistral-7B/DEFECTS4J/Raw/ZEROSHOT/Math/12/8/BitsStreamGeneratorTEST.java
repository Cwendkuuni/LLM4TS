package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
            // not used in this test
        }

        @Override
        public void setSeed(int[] seed) {
            // not used in this test
        }

        @Override
        public void setSeed(long seed) {
            // not used in this test
        }

        @Override
        protected int next(int bits) {
            return (int) (Math.random() * ((1 << bits) - 1) + 1);
        }
    }

    /** Test instance of {@link TestBitsStreamGenerator}. */
    private TestBitsStreamGenerator generator;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        generator = new TestBitsStreamGenerator();
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBoolean()}.
     */
    @Test
    public void testNextBoolean() {
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    /**
     * Tests {@link BitsStreamGenerator#nextBytes(byte[])}.
     */
    @Test
    public void testNextBytes() {
        byte[] expected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        byte[] actual  = new byte[15];
        generator.nextBytes(actual);
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextDouble()}.
     */
    @Test
    public void testNextDouble() {
        double expected = Math.random();
        double actual   = generator.nextDouble();
        assertEquals(expected, actual, 1e-15);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextFloat()}.
     */
    @Test
    public void testNextFloat() {
        float expected = (float) Math.random();
        float actual   = generator.nextFloat();
        assertEquals(expected, actual, 1e-5f);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextGaussian()}.
     */
    @Test
    public void testNextGaussian() {
        double expected = FastMath.gauss(0.0, 1.0);
        double actual   = generator.nextGaussian();
        assertEquals(expected, actual, 1e-5);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative() {
        generator.nextInt(-1);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt()}.
     */
    @Test
    public void testNextInt() {
        int expected = (int) (Math.random() * (Integer.MAX_VALUE + 1));
        int actual   = generator.nextInt();
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegative2() {
        generator.nextInt(-1);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextInt(int)}.
     */
    @Test
    public void testNextInt2() {
        int expected = (int) (Math.random() * 10 + 1);
        int actual   = generator.nextInt(10);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#nextLong()}.
     */
    @Test
    public void testNextLong() {
        long expected = Math.random() * Long.MAX_VALUE;
        long actual   = generator.nextLong();
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link BitsStreamGenerator#clear()}.
     */
    @Test
    public void testClear() {
        double gaussian = generator.nextGaussian();
        generator.clear();
        assertFalse(Double.isFinite(generator.nextGaussian()));
    }

}