import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private Checksum checksum;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        checksum = new CRC32();
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int data = inputStream.read();
        assertEquals(data, 1);
        assertEquals(inputStream.getValue(), 1);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);
        assertArrayEquals(buffer, new byte[]{1, 2, 3});
        assertEquals(bytesRead, 3);
        assertEquals(inputStream.getValue(), 75162681);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);
        assertArrayEquals(buffer, new byte[]{0, 1, 2, 3, 0});
        assertEquals(bytesRead, 3);
        assertEquals(inputStream.getValue(), 75162681);
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = inputStream.skip(2);
        assertEquals(bytesSkipped, 2);
        assertEquals(inputStream.getValue(), 55890098);
    }

    @Test
    public void testSkipZero() throws IOException {
        long bytesSkipped = inputStream.skip(0);
        assertEquals(bytesSkipped, 0);
        assertEquals(inputStream.getValue(), 0);
    }

    @Test
    public void testSkipExhaustedStream() throws IOException {
        byte[] buffer = new byte[5];
        inputStream.read(buffer);
        long bytesSkipped = inputStream.skip(1);
        assertEquals(bytesSkipped, 0);
        assertEquals(inputStream.getValue(), 615911671);
    }

}