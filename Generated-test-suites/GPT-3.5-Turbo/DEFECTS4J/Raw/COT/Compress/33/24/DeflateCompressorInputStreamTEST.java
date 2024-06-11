import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = {0x78, 0x01, 0x65, 0x00, 0x00, (byte) 0xFD, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(0x65, deflateInputStream.read());
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] input = {0x78, 0x01, 0x65, 0x00, 0x00, (byte) 0xFD, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[10];
        int bytesRead = deflateInputStream.read(buffer);
        assertEquals(1, bytesRead);
        assertEquals(0x65, buffer[0]);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = {0x78, 0x01, 0x65, 0x00, 0x00, (byte) 0xFD, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long bytesSkipped = deflateInputStream.skip(3);
        assertEquals(3, bytesSkipped);
        assertEquals(0x00, deflateInputStream.read());
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = {0x78, 0x01, 0x65, 0x00, 0x00, (byte) 0xFD, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(7, deflateInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(mockInputStream);

        deflateInputStream.close();

        assertTrue(deflateInputStream.isClosed());
    }

    @Test
    public void testCheckSignature() throws IOException {
        byte[] signature = {0x78, 0x01};
        int length = 2;

        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));

        boolean result = deflateInputStream.checkSignature(signature, length);
        assertTrue(result);
    }
}