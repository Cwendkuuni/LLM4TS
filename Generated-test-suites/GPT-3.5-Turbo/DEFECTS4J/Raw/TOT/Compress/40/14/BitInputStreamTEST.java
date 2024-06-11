import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private InputStream mockedInputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        mockedInputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(mockedInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() {
        try {
            bitInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBitsCountLessThanZero() throws IOException {
        try {
            bitInputStream.readBits(-1);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadBitsCountGreaterThanMaximumCacheSize() throws IOException {
        try {
            bitInputStream.readBits(64);
            Assert.fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        when(mockedInputStream.read()).thenReturn(-1);
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(-1L, result);
    }

    @Test
    public void testReadBitsLittleEndian() throws IOException {
        byte[] inputBytes = {0b00000001, 0b00000010, 0b00000011};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(16);

        // Expected result: 0b0000001000000001
        Assert.assertEquals(259L, result);
    }

    @Test
    public void testReadBitsBigEndian() throws IOException {
        byte[] inputBytes = {0b00000001, 0b00000010, 0b00000011};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(16);

        // Expected result: 0b0000000100000010
        Assert.assertEquals(514L, result);
    }
}