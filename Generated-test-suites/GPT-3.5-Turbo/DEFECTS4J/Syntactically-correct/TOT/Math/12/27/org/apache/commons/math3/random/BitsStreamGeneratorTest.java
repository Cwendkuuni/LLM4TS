import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BitsStreamGeneratorTest {

    private BitsStreamGenerator bitsStreamGenerator;

    @Before
    public void setUp() {
        bitsStreamGenerator = Mockito.mock(BitsStreamGenerator.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testNextBoolean() {
        boolean result = bitsStreamGenerator.nextBoolean();
        assertNotNull(result);
        verify(bitsStreamGenerator, times(1)).next(1);
    }

    @Test
    public void testNextBytes() {
        byte[] bytes = new byte[10];
        bitsStreamGenerator.nextBytes(bytes);
        verify(bitsStreamGenerator, times(bytes.length / 4 + 1)).next(32);
    }

    @Test
    public void testNextDouble() {
        double result = bitsStreamGenerator.nextDouble();
        assertNotNull(result);
        verify(bitsStreamGenerator, times(2)).next(26);
    }

    @Test
    public void testNextFloat() {
        float result = bitsStreamGenerator.nextFloat();
        assertNotNull(result);
        verify(bitsStreamGenerator, times(1)).next(23);
    }

    @Test
    public void testNextGaussian() {
        bitsStreamGenerator.clear();
        double result = bitsStreamGenerator.nextGaussian();
        assertNotNull(result);
        verify(bitsStreamGenerator, times(4)).next(26);

        bitsStreamGenerator.clear();
        bitsStreamGenerator.nextGaussian();
        double result2 = bitsStreamGenerator.nextGaussian();
        assertNotNull(result2);
        verify(bitsStreamGenerator, times(2)).next(26);
    }

    @Test
    public void testNextInt() {
        int result = bitsStreamGenerator.nextInt();
        assertNotNull(result);
        verify(bitsStreamGenerator, times(1)).next(32);
    }

    @Test
    public void testNextIntWithNPositive() {
        int n = 10;
        int result = bitsStreamGenerator.nextInt(n);
        assertNotNull(result);
        verify(bitsStreamGenerator, times(1)).next(31);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNZero() {
        int n = 0;
        bitsStreamGenerator.nextInt(n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextIntWithNNegative() {
        int n = -10;
        bitsStreamGenerator.nextInt(n);
    }

    @Test
    public void testNextLong() {
        long result = bitsStreamGenerator.nextLong();
        assertNotNull(result);
        verify(bitsStreamGenerator, times(2)).next(32);
    }

    @Test
    public void testClear() {
        bitsStreamGenerator.clear();
        verify(bitsStreamGenerator, times(1)).clear();
    }
}