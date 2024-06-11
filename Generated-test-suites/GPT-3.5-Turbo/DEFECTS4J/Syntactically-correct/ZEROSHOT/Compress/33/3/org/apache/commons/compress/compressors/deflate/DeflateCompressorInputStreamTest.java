import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setUp() {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(data);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int b = deflateCompressorInputStream.read();
        assertEquals(1, b);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buf = new byte[3];
        int bytesRead = deflateCompressorInputStream.read(buf, 0, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3}, buf);
    }

    @Test
    public void testSkipBytes() throws IOException {
        long bytesSkipped = deflateCompressorInputStream.skip(2);
        assertEquals(2, bytesSkipped);
    }

    @Test
    public void testAvailableBytes() throws IOException {
        int bytesAvailable = deflateCompressorInputStream.available();
        assertEquals(5, bytesAvailable);
    }

    @Test
    public void testCloseStream() throws IOException {
        deflateCompressorInputStream.close();
        assertTrue(deflateCompressorInputStream.isClosed());
    }

    @Test
    public void testCheckSignatureForValidData() throws Exception {
        byte[] signature = {31, -117, 8, 0, 0, 0, 0, 0, 0, 0};
        boolean result = deflateCompressorInputStream.checkSignature(signature, 10);
        assertTrue(result);
    }

    @Test
    public void testCheckSignatureForInvalidData() throws Exception {
        byte[] signature = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        boolean result = deflateCompressorInputStream.checkSignature(signature, 10);
        assertFalse(result);
    }
}