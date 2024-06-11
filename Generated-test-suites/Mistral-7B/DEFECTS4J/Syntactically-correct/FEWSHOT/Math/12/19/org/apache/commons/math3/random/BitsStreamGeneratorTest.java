package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the methods of {@link BitsStreamGenerator} class.
 */
public class BitsStreamGeneratorTest {

    /**
     * Test of abstract methods.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethods() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(12345);

        generator.setSeed(12345, new int[]{1, 2, 3, 4});

        generator.setSeed(12345L);

        generator.next(5);

        generator.nextBoolean();

        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);

        generator.nextDouble();

        generator.nextFloat();

        generator.nextGaussian();

        generator.nextInt();

        generator.nextInt(5);

        generator.nextLong();

        generator.clear();
    }

    /**
     * Test of {@link #next(int)} method with valid arguments.
     */
    @Test
    public void testNextValidArgs() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(12345);

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i, generator.next(i + 1));
        }
    }

    /**
     * Test of {@link #next(int)} method with invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNextInvalidArg() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.next(-1);
    }

    /**
     * Test of {@link #nextBoolean()} method.
     */
    @Test
    public void testNextBoolean() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(generator.nextBoolean(), (i % 2 == 0));
        }
    }

    /**
     * Test of {@link #nextBytes(byte[])} method.
     */
    @Test
    public void testNextBytes() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.setSeed(12345);

        byte[] bytes1 = new byte[1];
        byte[] bytes2 = new byte[10];

        generator.nextBytes(bytes1);
        Assert.assertArrayEquals(new byte[]{0x1}, bytes1);

        generator.nextBytes(bytes2);
        Assert.assertArrayEquals(new byte[0], bytes2);

        generator.nextBytes(bytes2);
        Assert.assertArrayEquals(new byte[10], bytes2);

        byte[] expected = new byte[]{0x9, 0x7, 0x8, 0x6, 0x4, 0x5, 0x3, 0x2, 0x1, 0xf};
        generator.nextBytes(bytes2);
        Assert.assertArrayEquals(expected, bytes2);
    }

    /**
     * Test of {@link #nextDouble()} method.
     */
    @Test
    public void testNextDouble() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 10; i++) {
            double actual = generator.nextDouble();
            double expected = (double) i / 10.0;
            Assert.assertEquals(expected, actual, 0.001);
        }
    }

    /**
     * Test of {@link #nextFloat()} method.
     */
    @Test
    public void testNextFloat() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 10; i++) {
            float actual = generator.nextFloat();
            float expected = (float) i / 10.0f;
            Assert.assertEquals(expected, actual, 0.001f);
        }
    }

    /**
     * Test of {@link #nextGaussian()} method.
     */
    @Test
    public void testNextGaussian() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 10; i++) {
            double actual = generator.nextGaussian();
            double expected = Math.sqrt(FastMath.RANDOM * 2.0) * Math.cos(2 * FastMath.PI * FastMath.RANDOM);
            Assert.assertEquals(expected, actual, 0.01);
        }
    }

    /**
     * Test of {@link #nextInt()} method.
     */
    @Test
    public void testNextInt() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i, generator.nextInt());
        }
    }

    /**
     * Test of {@link #nextInt(int)} method.
     */
    @Test
    public void testNextIntBounded() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int result = generator.nextInt(10);
                Assert.assertTrue(result >= 0 && result < 10);
            }
        }
    }

    /**
     * Test of {@link #nextInt(int)} method with negative argument.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeBound() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();
        generator.nextInt(-1);
    }

    /**
     * Test of {@link #nextLong()} method.
     */
    @Test
    public void testNextLong() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        for (int i = 0; i < 10; i++) {
            long actual = generator.nextLong();
            long expected = (long) (Math.random() * (Long.MAX_VALUE + 1));
            Assert.assertEquals(expected, actual);
        }
    }

    /**
     * Test of {@link #clear()} method.
     */
    @Test
    public void testClear() {
        final BitsStreamGenerator generator = new MersenneTwisterFastRandomGenerator();

        generator.nextGaussian();
        generator.clear();
        Assert.assertTrue(Double.isNaN(generator.nextGaussian()));
    }

}