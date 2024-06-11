import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream stream;

    @Before
    public void setUp() {
        byte[] data = new byte[]{0x78, 0x01, 0x00, 0x00, 0x00, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        stream = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int value = stream.read();
        assertEquals(120, value);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = stream.read(buffer);
        assertEquals(1, bytesRead);
        assertEquals(120, buffer[0]);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = stream.read(buffer, 3, 5);
        assertEquals(1, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(120, buffer[3]);
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = stream.skip(3);
        assertEquals(3, skippedBytes);
    }

    @Test
    public void testAvailable() throws IOException {
        int availableBytes = stream.available();
        assertEquals(0, availableBytes);
    }

    @Test
    public void testClose() throws IOException {
        stream.close();
        assertTrue(stream.isClosed());
    }
}