import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 1;
            }
        };
        Assert.assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        
        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);
        Assert.assertArrayEquals(new byte[]{0x12, 0x34, 0x56, 0x78}, bytes);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x80000000;
            }
        };
        Assert.assertEquals(-1.0, generator.nextDouble(), 0.0);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x40000000;
            }
        };
        Assert.assertEquals(2.0, generator.nextFloat(), 0.0);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x80000000;
            }
        };
        Assert.assertEquals(-1.0, generator.nextGaussian(), 0.0);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        Assert.assertEquals(305419896, generator.nextInt());
    }

    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        Assert.assertEquals(29756478, generator.nextInt(10000));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        generator.nextInt(-100);
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        Assert.assertEquals(81985529216486895L, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            protected int next(int bits) {
                return 0x12345678;
            }
        };
        generator.clear();
        Assert.assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}