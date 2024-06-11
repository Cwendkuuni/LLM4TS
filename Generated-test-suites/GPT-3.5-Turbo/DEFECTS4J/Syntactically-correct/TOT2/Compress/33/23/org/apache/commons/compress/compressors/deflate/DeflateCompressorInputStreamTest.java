import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = {65, 66, 67, 68};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        int output = deflateInputStream.read();
        assertEquals(65, output);
        deflateInputStream.close();
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] input = {65, 66, 67, 68};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        byte[] output = new byte[2];
        deflateInputStream.read(output, 0, 2);
        assertArrayEquals(new byte[]{65, 66}, output);
        deflateInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = {65, 66, 67, 68};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        long skipped = deflateInputStream.skip(2);
        assertEquals(2, skipped);
        deflateInputStream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = {65, 66, 67, 68};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        int available = deflateInputStream.available();
        assertEquals(4, available);
        deflateInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = {65, 66, 67, 68};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        deflateInputStream.close();
        assertEquals(-1, deflateInputStream.read());
    }
    
}