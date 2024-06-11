import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        try {
            assertEquals(0x00, stream.read());
            assertEquals(0x01, stream.read());
            assertEquals(0x02, stream.read());
            assertEquals(0x03, stream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadBuffer() {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        byte[] buffer = new byte[4];

        try {
            int bytesRead = stream.read(buffer, 0, 4);
            assertEquals(4, bytesRead);

            assertArrayEquals(data, buffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testSkip() {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        try {
            long skipped = stream.skip(2);
            assertEquals(2, skipped);
            assertEquals(0x02, stream.read());
            assertEquals(0x03, stream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testAvailable() {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        try {
            assertEquals(4, stream.available());
            stream.read();
            assertEquals(3, stream.available());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testClose() {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);

        try {
            stream.close();
            assertEquals(-1, stream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testZlibHeader() {
        byte[] data = {0x00, 0x01, 0x02, 0x03};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(true);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input, parameters);

        try {
            assertEquals(0x00, stream.read());
            assertEquals(0x01, stream.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}