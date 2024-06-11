
package org.apache.commons.math3.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNext() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
        };
        assertEquals(0, generator.next(1));
        assertEquals(0, generator.next(10));
        assertEquals(0, generator.next(32));
        assertEquals(0, generator.next(64));
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
        };
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
            }
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
        };
        byte[] bytes = new byte[8];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7}, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
            }
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
        };
        assertEquals(0.0, generator.nextDouble(), 0.0);
        assertEquals(1.1102230246251565E-16, generator.nextDouble(), 0.0);
        assertEquals(2.220446049250313E-16, generator.nextDouble(), 0.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
            }
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
        };
        assertEquals(0.0, generator.nextFloat(), 0.0);
        assertEquals(5.8789064E-5, generator.nextFloat(), 0.0);
        assertEquals(1.17578128E-4, generator.nextFloat(), 0.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
            }
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
        };
        assertEquals(0.0, generator.nextGaussian(), 0.0);
        assertEquals(1.0, generator.nextGaussian(), 0.0);
        assertEquals(2.0, generator.nextGaussian(), 0.0);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
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
    }

    @Test
    public void testNextIntWithUpperBound() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
            }
            @Override
            public void setSeed(int seed) {}
            @Override
            public void setSeed(int[] seed) {}
            @Override
            public void setSeed(long seed) {}
        };
        assertEquals(0, generator.nextInt(1));
        assertEquals(1, generator.nextInt(2));
        assertEquals(2, generator.nextInt(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithInvalidUpperBound() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
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
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
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
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int n = 0;
            @Override
            protected int next(int bits) {
                return n++;
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
        generator.nextGaussian();
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}
