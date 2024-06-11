import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BitsStreamGeneratorTest {

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

        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
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

        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertEquals(0, bytes[0]);
        assertEquals(0, bytes[1]);
        assertEquals(0, bytes[2]);
        assertEquals(0, bytes[3]);
        assertEquals(0, bytes[4]);
        assertEquals(0, bytes[5]);
        assertEquals(0, bytes[6]);
        assertEquals(0, bytes[7]);
        assertEquals(0, bytes[8]);
        assertEquals(0, bytes[9]);
    }

    @Test
    public void testNextDouble() {
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

        double result = generator.nextDouble();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextFloat() {
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

        float result = generator.nextFloat();
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void testNextGaussian() {
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

        double result = generator.nextGaussian();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testNextInt() {
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

        int result = generator.nextInt();
        assertEquals(0, result);
    }

    @Test
    public void testNextIntWithValidN() {
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

        int result = generator.nextInt(10);
        assertEquals(0, result);
    }


    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testNextIntWithInvalidN() {
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

        int result = generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
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

        long result = generator.nextLong();
        assertEquals(0L, result);
    }

    @Test
    public void testClear() {
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

        generator.clear();
        double result = generator.nextGaussian();
        assertNotNull(result);
    }
}