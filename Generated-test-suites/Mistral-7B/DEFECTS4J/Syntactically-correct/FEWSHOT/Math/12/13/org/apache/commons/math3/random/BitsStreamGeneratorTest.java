package org.apache.commons.math3.random;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitsStreamGeneratorTest {

    abstract class TestBitsStreamGenerator extends BitsStreamGenerator {
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
        public int next(int bits) {
            fail("Abstract method 'next(int)' not implemented in subclass");
            return 0;
        }
    }

    @Test
    public void testNextBoolean() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            assertTrue(generator.nextBoolean());
        }
        for (int i = 0; i < 10; i++) {
            assertFalse(generator.nextBoolean());
        }
    }

    @Test
    public void testNextBytes() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        final byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            assertNotEquals((byte) 0, bytes[i]);
        }

        final byte[] bytes2 = new byte[10];
        generator.nextBytes(bytes2);
        assertArrayEquals(bytes, bytes2);
    }

    @Test
    public void testNextDouble() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator() {
            @Override
            public double nextDouble() {
                return super.nextDouble() * 2.0;
            }
        };
        for (int i = 0; i < 10; i++) {
            double d = generator.nextDouble();
            assertTrue(d >= 0.0);
            assertTrue(d <= 1.0);
        }
    }

    @Test
    public void testNextFloat() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator() {
            @Override
            public float nextFloat() {
                return (float) super.nextDouble();
            }
        };
        for (int i = 0; i < 10; i++) {
            float f = generator.nextFloat();
            assertTrue(f >= 0.0f);
            assertTrue(f <= 1.0f);
        }
    }

    @Test
    public void testNextGaussian() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            double mean = 0.0;
            double stdDev = 1.0;
            double gaussian = generator.nextGaussian(mean, stdDev);
            assertTrue(gaussian >= mean - 3 * stdDev);
            assertTrue(gaussian <= mean + 3 * stdDev);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntNegativeSize() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        generator.nextInt(-1);
    }

    @Test
    public void testNextInt() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator() {
            @Override
            public int nextInt() {
                return super.nextInt() & 0xff;
            }
        };
        for (int i = 0; i < 10; i++) {
            int n = generator.nextInt(10);
            assertTrue(n >= 0);
            assertTrue(n <= 9);
            assertEquals(n % 10, n);
        }
    }

    @Test
    public void testNextIntPowerOf2() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            int n = generator.nextInt(1 << 10);
            assertEquals(n % (1 << 10), n);
        }
    }

    @Test
    public void testNextIntNotPowerOf2() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        for (int i = 0; i < 10; i++) {
            int n = generator.nextInt(11);
            assertTrue(n >= 0);
            assertTrue(n <= 10);
            assertEquals(n % 11, n);
        }
    }

    @Test
    public void testNextLong() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        final long maxLong = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long l = generator.nextLong();
            assertTrue(l >= 0);
            assertTrue(l <= maxLong);
            assertEquals(l % (maxLong + 1), l);
        }
    }

    @Test
    public void testClear() {
        final TestBitsStreamGenerator generator = new TestBitsStreamGenerator();
        generator.nextInt();
        generator.clear();
        assertFalse(Double.isNaN(generator.nextGaussian()));
    }
}