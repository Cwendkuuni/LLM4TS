import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadByte() throws Exception {
        byte[] data = { 1, 2, 3, 4, 5 };
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int value = inputStream.read();

        assertEquals(1, value);
        assertEquals(1, inputStream.getValue());

        inputStream.close();
    }

    @Test
    public void testReadByteArray() throws Exception {
        byte[] data = { 1, 2, 3, 4, 5 };
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);

        assertEquals(3, bytesRead);
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        assertEquals(1680262837, inputStream.getValue());

        inputStream.close();
    }

    @Test
    public void testReadByteArrayWithOffset() throws Exception {
        byte[] data = { 1, 2, 3, 4, 5 };
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);

        assertEquals(3, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
        assertEquals(3, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(1680262837, inputStream.getValue());

        inputStream.close();
    }

    @Test
    public void testSkip() throws Exception {
        byte[] data = { 1, 2, 3, 4, 5 };
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        long skipped = inputStream.skip(3);

        assertEquals(3, skipped);
        assertEquals(1680262837, inputStream.getValue());

        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testSkipExhaustedStream() throws Exception {
        byte[] data = { 1, 2, 3, 4, 5 };
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        inputStream.skip(5); // Exhausting the stream

        inputStream.close();
    }

    @Test
    public void testGetValue() throws Exception {
        byte[] data = { 1, 2, 3, 4, 5 };
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        inputStream.read(new byte[5]);

        long value = inputStream.getValue();

        assertEquals(1680262837, value);

        inputStream.close();
    }
}