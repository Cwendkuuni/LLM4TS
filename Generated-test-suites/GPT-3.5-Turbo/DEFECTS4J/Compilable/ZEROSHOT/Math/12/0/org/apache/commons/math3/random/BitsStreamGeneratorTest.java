package org.apache.commons.math3.random;
import static org.junit.Assert.*;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            int[] values = {0, 1};

            @Override
            public int next(int bits) {
                return values[nextInt(values.length)];
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            byte[] values = {1, 2, 3};

            @Override
            public int next(int bits) {
                return values[nextInt(values.length)];
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        byte[] bytes = new byte[12];
        generator.nextBytes(bytes);

        assertArrayEquals(new byte[]{1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3}, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            int[] values = {0, 1, 2, 3};

            @Override
            public int next(int bits) {
                return values[nextInt(values.length)];
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        assertEquals(0.0, generator.nextDouble(), 0.00001);
        assertEquals(0.25, generator.nextDouble(), 0.00001);
        assertEquals(0.5, generator.nextDouble(), 0.00001);
        assertEquals(0.75, generator.nextDouble(), 0.00001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            int[] values = {0, 1, 2, 3};

            @Override
            public int next(int bits) {
                return values[nextInt(values.length)];
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        assertEquals(0.0, generator.nextFloat(), 0.00001);
        assertEquals(0.5, generator.nextFloat(), 0.00001);
        assertEquals(1.0, generator.nextFloat(), 0.00001);
        assertEquals(1.5, generator.nextFloat(), 0.00001);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            double[] values = {0.0, 1.0, 2.0, 3.0};

            @Override
            public int next(int bits) {
                return nextInt(values.length);
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        assertEquals(0.0, generator.nextGaussian(), 0.00001);
        assertEquals(1.0, generator.nextGaussian(), 0.00001);
        assertEquals(2.0, generator.nextGaussian(), 0.00001);
        assertEquals(3.0, generator.nextGaussian(), 0.00001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            int[] values = {0, 1, 2, 3};

            @Override
            public int next(int bits) {
                return values[nextInt(values.length)];
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        assertEquals(0, generator.nextInt());
        assertEquals(1, generator.nextInt());
        assertEquals(2, generator.nextInt());
        assertEquals(3, generator.nextInt());
    }

    @Test
    public void testNextIntWithN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            int[] values = {0, 1, 2, 3};

            @Override
            public int next(int bits) {
                return values[nextInt(values.length)];
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        assertEquals(0, generator.nextInt(4));
        assertEquals(1, generator.nextInt(4));
        assertEquals(2, generator.nextInt(4));
        assertEquals(3, generator.nextInt(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNException() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            int[] values = {0, 1, 2, 3};

            @Override
            public int next(int bits) {
                return values[nextInt(values.length)];
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            long[] values = {0, 1, 2, 3};

            @Override
            public int next(int bits) {
                return nextInt(values.length);
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        assertEquals(0, generator.nextLong());
        assertEquals(1, generator.nextLong());
        assertEquals(2, generator.nextLong());
        assertEquals(3, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            long[] values = {0, 1, 2, 3};

            @Override
            public int next(int bits) {
                return nextInt(values.length);
            }

            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

        };

        generator.clear();

        assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}