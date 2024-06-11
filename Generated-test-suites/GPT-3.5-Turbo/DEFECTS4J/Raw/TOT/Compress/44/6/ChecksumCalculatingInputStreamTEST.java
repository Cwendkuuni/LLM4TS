import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ChecksumCalculatingInputStreamTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        Checksum checksum = new Adler32();
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(checksum, inputStream.getChecksum());
        assertEquals(in, inputStream.getInputStream());
    }

    // Test case for reading a single byte from the stream
    @Test
    public void testRead() throws IOException {
        Checksum checksum = new Adler32();
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int ret = inputStream.read();
        assertEquals(data[0], ret);
        assertEquals(checksum.getValue(), ret);

        ret = inputStream.read();
        assertEquals(data[1], ret);
        assertEquals(checksum.getValue(), ret);
    }

    // Test case for reading a byte array from the stream
    @Test
    public void testReadByteArray() throws IOException {
        Checksum checksum = new Adler32();
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[2];

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int bytesRead = inputStream.read(buffer);
        assertEquals(2, bytesRead);
        assertEquals(data[0], buffer[0]);
        assertEquals(data[1], buffer[1]);
        assertEquals(checksum.getValue(), bytesRead);

        bytesRead = inputStream.read(buffer);
        assertEquals(2, bytesRead);
        assertEquals(data[2], buffer[0]);
        assertEquals(data[3], buffer[1]);
        assertEquals(checksum.getValue(), bytesRead);

        bytesRead = inputStream.read(buffer);
        assertEquals(-1, bytesRead);
        assertEquals(0, inputStream.available());
    }

    // Test case for reading a byte array from the stream with offset and length
    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        Checksum checksum = new Adler32();
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[4];

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int bytesRead = inputStream.read(buffer, 1, 2);
        assertEquals(2, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(data[0], buffer[1]);
        assertEquals(data[1], buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(checksum.getValue(), bytesRead);

        bytesRead = inputStream.read(buffer, 2, 2);
        assertEquals(2, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(data[0], buffer[1]);
        assertEquals(data[1], buffer[2]);
        assertEquals(data[2], buffer[3]);
        assertEquals(checksum.getValue(), bytesRead);

        bytesRead = inputStream.read(buffer, 0, 4);
        assertEquals(-1, bytesRead);
        assertEquals(0, inputStream.available());
    }

    // Test case for skipping bytes in the stream
    @Test
    public void testSkip() throws IOException {
        Checksum checksum = new Adler32();
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        long skipped = inputStream.skip(2);
        assertEquals(2, skipped);
        assertEquals(data[2], inputStream.read());
        assertEquals(checksum.getValue(), skipped);

        skipped = inputStream.skip(0);
        assertEquals(0, skipped);
    }

    // Test case for getting the calculated checksum
    @Test
    public void testGetValue() {
        Checksum checksum = new Adler32();
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        try {
            while (inputStream.read() != -1) {
            }
        } catch (IOException e) {
            fail("IOException occurred while reading from stream.");
        }

        assertEquals(checksum.getValue(), inputStream.getValue());
    }
}