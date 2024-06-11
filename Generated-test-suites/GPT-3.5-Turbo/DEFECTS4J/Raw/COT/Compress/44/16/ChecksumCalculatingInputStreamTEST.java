import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkstream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Read each byte and verify checksum
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], (byte) checkstream.read());
            assertEquals(checksum.getValue(), (i + 1)); // checksum value should be the sum of bytes read so far
        }

        // Verify EOF
        assertEquals(-1, checkstream.read());
        assertEquals(checksum.getValue(), 0x10); // final checksum value
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkstream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[data.length];
        checkstream.read(buffer);

        // Verify that the read bytes match the original data and checksum is updated
        assertArrayEquals(data, buffer);
        assertEquals(checksum.getValue(), 0x10); // final checksum value
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkstream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[data.length];
        checkstream.read(buffer, 1, 2);

        // Verify that the read bytes match the original data and checksum is updated
        assertArrayEquals(new byte[]{0x00, 0x01, 0x02, 0x00}, buffer);
        assertEquals(checksum.getValue(), 0x03); // final checksum value
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkstream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = checkstream.skip(2);

        // Verify that the correct number of bytes are skipped and checksum is updated
        assertEquals(2, skipped);
        assertEquals(checksum.getValue(), 0x06); // final checksum value
    }

    @Test
    public void testGetValue() {
        byte[] data = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream checkstream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Verify that the initial checksum value is returned correctly
        assertEquals(0, checkstream.getValue());

        // Read all bytes and verify the final checksum value
        for (byte b : data) {
            assertEquals(b, (byte) checkstream.read());
        }
        assertEquals(checksum.getValue(), 0x10);
    }
}