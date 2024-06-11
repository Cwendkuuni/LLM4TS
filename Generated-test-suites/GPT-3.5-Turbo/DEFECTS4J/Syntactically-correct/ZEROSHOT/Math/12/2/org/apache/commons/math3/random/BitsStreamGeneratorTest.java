import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
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
    public void testNextBooleanFalse() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
        };
        assertFalse(generator.nextBoolean());
    }
    
    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[4];
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;
            protected int next(int bits) {
                return ++count;
            }
        };
        generator.nextBytes(bytes);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, bytes);
    }
    
    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0.0, generator.nextDouble(), 0.0);
    }
    
    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0.0f, generator.nextFloat(), 0.0);
    }
    
    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private boolean firstCall = true;
            protected int next(int bits) {
                return 0;
            }
        };
        assertEquals(0.0, generator.nextGaussian(), 0.0);
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
    public void testNextIntWithParameter() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        assertEquals(1, generator.nextInt(100));
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithParameterNegativeValue() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        generator.nextInt(-1);
    }
    
    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        assertEquals(4294967297L, generator.nextLong());
    }
    
    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        generator.clear();
        assertTrue(Double.isNaN(generator.nextGaussian));
    }

}