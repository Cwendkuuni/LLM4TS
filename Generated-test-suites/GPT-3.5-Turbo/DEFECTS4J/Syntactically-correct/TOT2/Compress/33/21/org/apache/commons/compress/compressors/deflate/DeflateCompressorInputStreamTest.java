import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        int result = deflateInputStream.read();
        
        assertEquals(1, result);
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[3];
        int bytesRead = deflateInputStream.read(buffer, 0, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] { 1, 2, 3 }, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long bytesSkipped = deflateInputStream.skip(2);

        assertEquals(2, bytesSkipped);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateInputStream.available();

        assertEquals(5, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();
        assertTrue(deflateInputStream.in.closed());
    }

    @Test
    public void testDeflateParameters() {
        byte[] testData = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream, parameters);

        assertNotNull(deflateInputStream);
    }

    @Test
    public void testSignatureCheck() {
        byte[] signature = { 31, -117, 8 };
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(null);

        assertTrue(deflateInputStream.checkSignature(signature, signature.length));
    }

}