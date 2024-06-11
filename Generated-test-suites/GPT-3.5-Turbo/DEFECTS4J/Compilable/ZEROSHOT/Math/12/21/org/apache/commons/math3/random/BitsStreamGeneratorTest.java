package org.apache.commons.math3.random;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator generator;

    @Before
    public void setUp() {
        generator = new BitsStreamGenerator() {
            @Override
            public void setSeed(int seed) {
                // Not implemented for testing purposes
            }

            @Override
            public void setSeed(int[] seed) {
                // Not implemented for testing purposes
            }

            @Override
            public void setSeed(long seed) {
                // Not implemented for testing purposes
            }

            @Override
            protected int next(int bits) {
                // Not implemented for testing purposes
                return 0;
            }
        };
    }

    @Test
    public void testNextBoolean() {
        boolean result = generator.nextBoolean();
        assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        generator.nextBytes(bytes);
        assertNotNull(bytes);
        assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        double result = generator.nextDouble();
        assertNotNull(result);
    }

    @Test
    public void testNextFloat() {
        float result = generator.nextFloat();
        assertNotNull(result);
    }

    @Test
    public void testNextGaussian() {
        double result = generator.nextGaussian();
        assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        int result = generator.nextInt();
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithValidArgument() {
        int result = generator.nextInt(10);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithInvalidArgument() {
        generator.nextInt(0);
    }

    @Test
    public void testNextLong() {
        long result = generator.nextLong();
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        generator.clear();
        double result = generator.nextGaussian();
        assertNotNull(result);
    }

}