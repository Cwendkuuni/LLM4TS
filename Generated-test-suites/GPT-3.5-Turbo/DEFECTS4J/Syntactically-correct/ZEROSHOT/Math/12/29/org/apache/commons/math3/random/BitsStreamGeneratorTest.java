import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count;

            protected int next(int bits) {
                count++;
                return count;
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        assertEquals(2.220446049250313E-16, generator.nextDouble(), 1e-15);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        assertEquals(1.4012985E-7f, generator.nextFloat(), 1e-7);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private double[] values = {
                0.5, 0.4, 0.3, 0.2, 0.1
            };
            private int count;

            protected int next(int bits) {
                int index = count % values.length;
                count++;
                return (int) (values[index] * Math.pow(2, bits));
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        assertEquals(-0.9973002039367398, generator.nextGaussian(), 1e-15);
        assertEquals(-0.05259516340086898, generator.nextGaussian(), 1e-15);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count;

            protected int next(int bits) {
                count++;
                return count;
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        assertEquals(2, generator.nextInt());
    }

    @Test
    public void testNextIntInvalidInput() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        try {
            generator.nextInt(0);
            fail("An exception should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("The seed must be strictly positive.", e.getMessage());
        }
    }

    @Test
    public void testNextIntValidInput() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count;

            protected int next(int bits) {
                count++;
                return count;
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        assertEquals(2, generator.nextInt(3));
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count;

            protected int next(int bits) {
                count++;
                return count;
            }

            public void setSeed(int seed) {
            }

            public void setSeed(int[] seed) {
            }

            public void setSeed(long seed) {
            }
        };

        assertEquals(4294967298L, generator.nextLong());
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
        };

        generator.clear();

        assertEquals(Double.NaN, generator.nextGaussian(), 0);
    }
}