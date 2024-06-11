import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.BitsStreamGenerator;
import org.junit.Assert;
import org.junit.Test;

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
                return 1;
            }
        };

        Assert.assertTrue(generator.nextBoolean());
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
                return 1;
            }
        };

        byte[] bytes = new byte[4];
        generator.nextBytes(bytes);

        Assert.assertEquals(1, bytes[0]);
        Assert.assertEquals(1, bytes[1]);
        Assert.assertEquals(1, bytes[2]);
        Assert.assertEquals(1, bytes[3]);
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
                return 1;
            }
        };

        Assert.assertEquals(1.0 / (1L << 53), generator.nextDouble(), 0.0);
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
                return 1;
            }
        };

        Assert.assertEquals(1.0 / (1 << 24), generator.nextFloat(), 0.0);
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
                return 1;
            }
        };

        Assert.assertEquals(1.0, generator.nextGaussian(), 0.0);
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
                return 1;
            }
        };

        Assert.assertEquals(1, generator.nextInt());
    }

    @Test
    public void testNextIntWithPositiveN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        Assert.assertEquals(1 % 5, generator.nextInt(5));
        Assert.assertEquals(1 % 10, generator.nextInt(10));
        Assert.assertEquals(1 % 20, generator.nextInt(20));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithZeroN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {}

            @Override
            public void setSeed(int[] seed) {}

            @Override
            public void setSeed(long seed) {}

            @Override
            protected int next(int bits) {
                return 1;
            }
        };

        generator.nextInt(0);
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
                return 1;
            }
        };

        Assert.assertEquals(1L, generator.nextLong());
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
                return 1;
            }
        };

        generator.nextGaussian();

        generator.clear();
        Assert.assertTrue(Double.isNaN(generator.nextGaussian()));
    }
}