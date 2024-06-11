import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);

        int readData = inputStream.read();
        assertEquals(1, readData);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer);
        assertArrayEquals(data, buffer);
        assertEquals(5, bytesRead);
        assertEquals(25804800, checksum.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 0, 0}, buffer);
        assertEquals(16711910, checksum.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);

        long skipped = inputStream.skip(2);
        assertEquals(2, skipped);
        assertEquals(0, checksum.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
        inputStream.read();
        inputStream.read();
        inputStream.read();
        inputStream.read();
        inputStream.read();

        long calculatedChecksum = inputStream.getValue();
        assertEquals(2575, calculatedChecksum);
    }
}