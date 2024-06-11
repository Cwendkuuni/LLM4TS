package org.apache.commons.math3.random;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BitsStreamGeneratorTest {
    @Test
    public void testSetSeedInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        generator.setSeed(123);
    }

    @Test
    public void testSetSeedIntArray() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        generator.setSeed(new int[]{123, 456});
    }

    @Test
    public void testSetSeedLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        generator.setSeed(123L);
    }

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);

        for (byte b : bytes) {
            assertEquals(0, b);
        }
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        assertEquals(0.0, generator.nextDouble(), 0.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        assertEquals(0.0f, generator.nextFloat(), 0.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        assertEquals(0.0, generator.nextGaussian(), 0.0);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        assertEquals(0, generator.nextInt());
    }

    @Test
    public void testNextIntWithNegativeArgumentThrowsException() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };
        boolean thrown = false;
        try {
            generator.nextInt(-123);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void testNextIntWithPositiveArgument() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        assertEquals(0, generator.nextInt(123));
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        assertEquals(0L, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }

            protected int next(int bits) {
                return 0;
            }
        };

        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}