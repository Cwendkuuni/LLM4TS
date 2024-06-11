package org.apache.commons.math3.random;
import org.junit.Test;
import static org.junit.Assert.*;

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
                return 0;
            }
        };
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
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
                return 0;
            }
        };
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{0, 0, 0, 0}, bytes);
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
                return 0;
            }
        };
        assertEquals(0.0, generator.nextDouble(), 0.0);
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
                return 0;
            }
        };
        assertEquals(0.0f, generator.nextFloat(), 0.0f);
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
        assertEquals(0.0, generator.nextGaussian(), 0.0);
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
                return 0;
            }
        };
        assertEquals(0, generator.nextInt());
    }

    @Test
    public void testNextIntRange() {
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
        assertEquals(0, generator.nextInt(10));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNextIntNegativeRange() {
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
        generator.nextInt(-1);
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
                return 0;
            }
        };
        assertEquals(0L, generator.nextLong());
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
                return 0;
            }
        };
        generator.nextGaussian();
        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian(), 0.0);
    }
}