package org.apache.commons.math3.random;

import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test class for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test instance. */
    private BitsStreamGenerator generator = new BitsStreamGenerator() {
        @Override
        public void setSeed(int seed) {
            // not used
        }

        @Override
        public void setSeed(int[] seed) {
            // not used
        }

        @Override
        public void setSeed(long seed) {
            // not used
        }

        @Override
        protected int next(int bits) {
            throw new UnsupportedOperationException();
        }
    };

    /** Test nextBoolean() method. */
    @Test
    public void testNextBoolean() {
        boolean result;
        for (int i = 0; i < 10; i++) {
            result = generator.nextBoolean();
            assertTrue("expected true or false", result instanceof Boolean);
            assertTrue("expected true or false", result);
        }
    }

    /** Test nextBytes() method. */
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertTrue("expected byte value", (b >= 0) && (b <= 255));
        }
    }

    /** Test nextDouble() method. */
    @Test
    public void testNextDouble() {
        double result;
        for (int i = 0; i < 10; i++) {
            result = generator.nextDouble();
            assertTrue("expected non-negative double", result >= 0.0);
        }
    }

    /** Test nextFloat() method. */
    @Test
    public void testNextFloat() {
        float result;
        for (int i = 0; i < 10; i++) {
            result = generator.nextFloat();
            assertTrue("expected non-negative float", result >= 0.0f);
        }
    }

    /** Test nextGaussian() method. */
    @Test
    public void testNextGaussian() {
        double result;
        for (int i = 0; i < 10; i++) {
            result = generator.nextGaussian();
            assertTrue("expected non-negative double", result >= 0.0);
            assertTrue("expected non-positive double", result <= 0.0 || FastMath.isFinite(result));
        }
    }

    /** Test nextInt() method. */
    @Test
    public void testNextInt() {
        int result;
        for (int i = 0; i < 10; i++) {
            result = generator.nextInt();
            assertTrue("expected int value", result >= 0);
        }
        try {
            generator.nextInt(-1);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /** Test nextInt() method with positive argument. */
    @Test
    public void testNextIntPositive() {
        int result;
        for (int n = 1; n <= 10; n++) {
            for (int i = 0; i < 1000; i++) {
                result = generator.nextInt(n);
                assertTrue("expected int value between 0 and given limit", result >= 0 && result < n);
            }
        }
    }

    /** Test nextLong() method. */
    @Test
    public void testNextLong() {
        long result;
        for (int i = 0; i < 10; i++) {
            result = generator.nextLong();
            assertTrue("expected long value", result >= 0);
        }
    }

    /** Test clear() method. */
    @Test
    public void testClear() {
        // no test case available for this method
    }

    /** Test setSeed() method with int argument. */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedInt() {
        generator.setSeed(123);
        fail("expected UnsupportedOperationException");
    }

    /** Test setSeed() method with int[] argument. */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedIntArray() {
        int[] seed = {1, 2, 3};
        generator.setSeed(seed);
        fail("expected UnsupportedOperationException");
    }

    /** Test setSeed() method with long argument. */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetSeedLong() {
        generator.setSeed(123L);
        fail("expected UnsupportedOperationException");
    }

    /** Test next() method. */
    @Test(expected = UnsupportedOperationException.class)
    public void testNext() {
        generator.next(1);
        fail("expected UnsupportedOperationException");
    }

}