import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] input = { 0x48 }; // ASCII value for 'H'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateStream.read();

        assertEquals(0x48, result);
        deflateStream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = { 0x48, 0x65, 0x6C, 0x6C, 0x6F }; // ASCII values for 'Hello'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[5];
        int bytesRead = deflateStream.read(buffer, 0, 5);

        assertEquals(5, bytesRead);
        assertArrayEquals(input, buffer);
        deflateStream.close();
    }

    @Test
    public void testSkipBytes() throws IOException {
        byte[] input = { 0x48, 0x65, 0x6C, 0x6C, 0x6F }; // ASCII values for 'Hello'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);

        long bytesSkipped = deflateStream.skip(3);

        assertEquals(3, bytesSkipped);
        assertEquals((int) 'l', deflateStream.read());
        deflateStream.close();
    }

    @Test
    public void testAvailableBytes() throws IOException {
        byte[] input = { 0x48, 0x65, 0x6C, 0x6C, 0x6F };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateStream.available();

        assertEquals(5, availableBytes);
        deflateStream.close();
    }

    @Test
    public void testCloseStream() throws IOException {
        byte[] input = { 0x48, 0x65, 0x6C, 0x6C, 0x6F };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);

        deflateStream.close();
        try {
            int result = deflateStream.read();
            fail("Stream should be closed");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testCheckZlibHeader() {
        byte[] signature = new byte[] { 0x78, 0x01 }; // zlib header
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(null);

        boolean isZlibHeader = deflateStream.checkZlibHeader(signature, 2);

        assertTrue(isZlibHeader);
    }
}