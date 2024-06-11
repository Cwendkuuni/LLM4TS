import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        byte[] expected = {1, 1, 1, 1};
        assertArrayEquals(expected, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0b00111111111111111111111111111111;
            }
        };
        assertEquals(1.0, generator.nextDouble(), 0.0);
    }
    
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0b00111111111111111111111;
            }
        };
        assertEquals(1.0f, generator.nextFloat(), 0.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0b00111111111111111111111111111111;
            }
        };
        assertEquals(1.0, generator.nextGaussian(), 0.0);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        assertEquals(1, generator.nextInt());
    }

    @Test
    public void testNextIntWithBound() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        assertEquals(1, generator.nextInt(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithInvalidBound() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        assertEquals(1L, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}