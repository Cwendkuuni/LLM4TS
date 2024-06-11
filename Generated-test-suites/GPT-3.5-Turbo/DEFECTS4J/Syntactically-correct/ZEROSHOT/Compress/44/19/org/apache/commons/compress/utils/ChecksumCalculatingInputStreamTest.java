import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream is;

    @Before
    public void setUp() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        CRC32 checksum = new CRC32();
        is = new ChecksumCalculatingInputStream(checksum, input);
    }

    @Test
    public void testRead() throws IOException {
        int expected = 0x01;
        int actual = is.read();
        assertEquals(expected, actual);
        assertEquals(1, is.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actual = new byte[3];
        is.read(actual);
        assertArrayEquals(expected, actual);
        assertEquals(1536928557, is.getValue());
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actual = new byte[5];
        is.read(actual, 1, 3);
        assertArrayEquals(expected, actual);
        assertEquals(1536928557, is.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long expected = 1;
        long actual = is.skip(1);
        assertEquals(expected, actual);
        assertEquals(1, is.getValue());
    }

    @Test
    public void testGetValue() {
        long expected = 1536928557;
        long actual = is.getValue();
        assertEquals(expected, actual);
    }
}