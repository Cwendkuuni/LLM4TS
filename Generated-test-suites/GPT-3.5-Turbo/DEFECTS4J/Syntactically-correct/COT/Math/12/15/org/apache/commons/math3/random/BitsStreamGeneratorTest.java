
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    private BitsStreamGenerator generator;

    @Test
    public void testNextBoolean() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 1;
            }
        };
        assertTrue(generator.nextBoolean());

        generator = new BitsStreamGenerator() {  
            @Override
            protected int next(int bits) {
                return 0;
            }
        };
        assertFalse(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        final int len = 10;
        byte[] bytes = new byte[len];

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 123456789;
            }
        };
        generator.nextBytes(bytes);
        for (byte b : bytes) {
            assertEquals(123, b);
        }
    }

    @Test
    public void testNextDouble() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0.0, generator.nextDouble(), 0.0);

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };
        assertEquals(1.0, generator.nextDouble(), 0.0);

        // TODO: additional edge cases for nextDouble()

    }

    @Test
    public void testNextFloat() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0.0f, generator.nextFloat(), 0.0);

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };
        assertEquals(1.0f, generator.nextFloat(), 0.0);

        // TODO: additional edge cases for nextFloat()

    }

    @Test
    public void testNextGaussian() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0.0, generator.nextGaussian(), 0.0);

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };
        // TODO: additional edge cases for nextGaussian()

    }

    @Test
    public void testNextInt() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0, generator.nextInt());

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };
        assertEquals(Integer.MAX_VALUE, generator.nextInt());

        // TODO: additional edge cases for nextInt()

    }

    @Test
    public void testNextInt_int() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0, generator.nextInt(10));

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };
        assertEquals(3, generator.nextInt(10));

        // TODO: additional edge cases for nextInt(int)

    }

    @Test
    public void testNextLong() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0L, generator.nextLong());

        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return Integer.MAX_VALUE;
            }
        };
        assertEquals(2L, generator.nextLong());

        // TODO: additional edge cases for nextLong()

    }

    @Test
    public void testClear() {
        generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 0;
            }
        };

        // TODO: additional test cases for clear()

    }
}
