package org.apache.commons.math3.random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.MersenneTwisterFast;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

/**
 * Test cases for {@link BitsStreamGenerator}.
 */
public class BitsStreamGeneratorTest {

    /** Test random number generator. */
    @Test
    public void testBitsStreamGenerator() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        generator.setSeed(12345678L);
        assertEquals(12345678L, generator.nextLong());
        assertEquals(0, generator.next(0));
        assertEquals(1, generator.next(1));
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        // check for randomness
        for (int i = 0; i < bytes.length; i++) {
            assertTrue(bytes[i] >= 0 && bytes[i] <= 0xff);
        }
        // test nextInt()
        for (int i = 1; i <= 10; i++) {
            final int result = generator.nextInt(i);
            assertTrue(result >= 0 && result < i);
        }
        try {
            generator.nextInt(-1);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        // test nextGaussian()
        for (int i = 0; i < 100; i++) {
            final double gaussian = generator.nextGaussian();
            assertTrue(gaussian > -3.0);
            assertTrue(gaussian < 3.0);
        }
        // test clear()
        final double gaussian1 = generator.nextGaussian();
        generator.clear();
        final double gaussian2 = generator.nextGaussian();
        assertNotSame(gaussian1, gaussian2);
    }

    /** Test random number generator with different seeds. */
    @Test
    public void testBitsStreamGeneratorDifferentSeeds() {
        final BitsStreamGenerator generator1 = new MersenneTwisterFast();
        generator1.setSeed(12345678L);
        final BitsStreamGenerator generator2 = new MersenneTwisterFast();
        generator2.setSeed(987654321L);
        for (int i = 0; i < 10; i++) {
            final int int1 = generator1.nextInt();
            final int int2 = generator2.nextInt();
            assertFalse(int1 == int2);
        }
        final byte[] bytes1 = new byte[10];
        generator1.nextBytes(bytes1);
        final byte[] bytes2 = new byte[10];
        generator2.nextBytes(bytes2);
        assertArrayEquals(bytes1, bytes2);
        // test nextGaussian()
        final double gaussian1 = generator1.nextGaussian();
        final double gaussian2 = generator2.nextGaussian();
        assertNotSame(gaussian1, gaussian2);
    }

    /** Test random number generator for negative seed. */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBitsStreamGeneratorNegativeSeed() {
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        generator.setSeed(-12345678L);
    }

    /** Test random number generator for long seed array. */
    @Test
    public void testBitsStreamGeneratorLongSeedArray() {
        final long[] seed = new long[100];
        final Random random = new Random();
        random.nextBytes(seed);
        final BitsStreamGenerator generator = new MersenneTwisterFast();
        generator.setSeed(seed);
        for (int i = 0; i < 10; i++) {
            final int int1 = generator.nextInt();
            final int int2 = new Random(seed[i]).nextInt();
            assertEquals(int1, int2);
        }
    }

}