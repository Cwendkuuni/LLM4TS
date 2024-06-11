import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        boolean result = generator.nextBoolean();
        assertTrue(result);

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };

        result = generator.nextBoolean();
        assertFalse(result);
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };

        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);

        byte[] expectedBytes = {0, 0, 0, 0};
        assertArrayEquals(expectedBytes, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE / 2;
            }
        };

        double result = generator.nextDouble();
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE / 2;
            }
        };

        float result = generator.nextFloat();
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private boolean isFirstCall = true;

            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE / 2;
            }

            @Override
            public double nextDouble() {
                return 0.5;
            }
        };

        double result = generator.nextGaussian();
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };

        int result = generator.nextInt();
        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testNextIntWithArgument() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };

        int result = generator.nextInt(10);
        assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNegativeArgument() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };

        generator.nextInt(-10);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };

        long result = generator.nextLong();
        assertEquals(Long.MAX_VALUE, result);
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };

        generator.clear();
        assertEquals(Double.NaN, generator.nextGaussian, 0.0001);
    }
}