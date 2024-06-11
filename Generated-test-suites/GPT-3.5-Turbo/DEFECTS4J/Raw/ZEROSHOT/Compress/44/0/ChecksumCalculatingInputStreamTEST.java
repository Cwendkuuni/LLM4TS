import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {
    
    private ChecksumCalculatingInputStream inputStream;
    
    @Before
    public void setUp() {
        CRC32 checksum = new CRC32();
        byte[] data = { 1, 2, 3 };
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(-1, inputStream.read());
        assertEquals(6, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        assertEquals(3, inputStream.read(buffer));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        assertEquals(-1, inputStream.read(buffer));
        assertEquals(6, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[4];
        assertEquals(3, inputStream.read(buffer, 1, 3));
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
        assertEquals(3, buffer[3]);
        assertEquals(-1, inputStream.read(buffer, 1, 3));
        assertEquals(6, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(1, inputStream.skip(1));
        assertEquals(2, inputStream.skip(2));
        assertEquals(0, inputStream.skip(1));
        assertEquals(6, inputStream.getValue());
    }
}