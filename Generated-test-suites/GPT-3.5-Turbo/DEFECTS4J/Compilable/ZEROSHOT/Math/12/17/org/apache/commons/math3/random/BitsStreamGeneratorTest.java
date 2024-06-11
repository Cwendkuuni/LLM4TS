package org.apache.commons.math3.random;
import static org.junit.Assert.*;
import org.junit.Test;


public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);

        assertArrayEquals(new byte[]{1, 0, 0, 0}, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 1072693248;
            }
        };

        assertEquals(0.5, generator.nextDouble(), 1e-6);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 8388608;
            }
        };

        assertEquals(0.5f, generator.nextFloat(), 1e-6);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 0;
            }
        };

        assertEquals(0.0, generator.nextGaussian(), 1e-6);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        assertEquals(1, generator.nextInt());
    }

    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 27;
            }
        };

        assertEquals(3, generator.nextInt(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        assertEquals(4294967296L, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}