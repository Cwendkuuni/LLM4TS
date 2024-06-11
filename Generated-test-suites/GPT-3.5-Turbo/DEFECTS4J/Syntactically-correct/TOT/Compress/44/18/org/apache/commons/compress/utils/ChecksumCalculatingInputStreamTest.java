
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_SingleByte_ValidData() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = checksumStream.read();

        assertEquals(0x01, readByte);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testRead_SingleByte_EndOfStream() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte = checksumStream.read();

        assertEquals(-1, readByte);
        assertEquals(0, checksum.getValue());
    }

    @Test
    public void testRead_ByteArray_ValidData() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];

        int bytesRead = checksumStream.read(buffer);

        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, buffer);
        assertEquals(3, bytesRead);
        assertEquals(3, checksum.getValue());
    }

    @Test
    public void testRead_ByteArray_EndOfStream() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];

        int bytesRead = checksumStream.read(buffer);

        assertEquals(-1, bytesRead);
        assertEquals(0, checksum.getValue());
    }

    @Test
    public void testRead_ByteArray_WithOffset_ValidData() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[5];

        int bytesRead = checksumStream.read(buffer, 1, 3);

        assertArrayEquals(new byte[]{0x00, 0x01, 0x02, 0x03, 0x00}, buffer);
        assertEquals(3, bytesRead);
        assertEquals(3, checksum.getValue());
    }

    @Test
    public void testRead_ByteArray_WithOffset_EndOfStream() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[5];

        int bytesRead = checksumStream.read(buffer, 1, 3);

        assertEquals(-1, bytesRead);
        assertEquals(0, checksum.getValue());
    }

    @Test
    public void testSkip_ValidSkip() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long bytesSkipped = checksumStream.skip(3);

        assertEquals(3, bytesSkipped);
        assertEquals(3, checksum.getValue());
    }

    @Test
    public void testSkip_EndOfStream() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long bytesSkipped = checksumStream.skip(3);

        assertEquals(0, bytesSkipped);
        assertEquals(0, checksum.getValue());
    }

    @Test
    public void testGetValue_ValidChecksum() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumStream.read(new byte[5]);

        long calculatedChecksum = checksumStream.getValue();

        assertEquals(516026258, calculatedChecksum);
    }

    @Test
    public void testGetValue_EmptyStream() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long calculatedChecksum = checksumStream.getValue();

        assertEquals(0, calculatedChecksum);
    }
}
