import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++ % 2;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
    }

    @Test
    public void testNextBytes() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private byte[] bytes = {1, 2, 3, 4, 5};

            protected int next(int bits) {
                return 0;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        byte[] test = new byte[5];
        generator.nextBytes(test);

        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, test);
    }

    @Test
    public void testNextDouble() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++ % 10;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        assertEquals(0.0, generator.nextDouble(), 0.0001);
        assertEquals(0.1, generator.nextDouble(), 0.0001);
        assertEquals(0.2, generator.nextDouble(), 0.0001);
    }

    @Test
    public void testNextFloat() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++ % 100;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        assertEquals(0.0f, generator.nextFloat(), 0.0001f);
        assertEquals(0.01f, generator.nextFloat(), 0.0001f);
        assertEquals(0.02f, generator.nextFloat(), 0.0001f);
    }

    @Test
    public void testNextGaussian() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++ % 100;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        assertEquals(0.0, generator.nextGaussian(), 0.0001);
        assertEquals(0.01, generator.nextGaussian(), 0.0001);
        assertEquals(0.02, generator.nextGaussian(), 0.0001);
    }

    @Test
    public void testNextInt() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        assertEquals(0, generator.nextInt());
        assertEquals(1, generator.nextInt());
        assertEquals(2, generator.nextInt());
    }

    @Test
    public void testNextIntWithN() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        assertEquals(0, generator.nextInt(10));
        assertEquals(1, generator.nextInt(10));
        assertEquals(2, generator.nextInt(10));
    }

    @Test
    public void testNextLong() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        assertEquals(0L, generator.nextLong());
        assertEquals(1L, generator.nextLong());
        assertEquals(2L, generator.nextLong());
    }

    @Test
    public void testClear() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            private int count = 0;

            protected int next(int bits) {
                return count++;
            }

            public void setSeed(int seed) {

            }

            public void setSeed(int[] seed) {

            }

            public void setSeed(long seed) {

            }
        };

        generator.nextGaussian();
        generator.clear();
        Double.NaN expected = Double.NaN;
        assertEquals(expected, generator.nextGaussian());
    }
}