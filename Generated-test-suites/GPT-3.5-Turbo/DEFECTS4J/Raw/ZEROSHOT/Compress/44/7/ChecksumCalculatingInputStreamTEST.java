import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] bytes = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int result = inputStream.read();

        assertEquals('t', result);
        assertEquals(9, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] bytes = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[4];
        int result = inputStream.read(buffer);

        assertEquals(4, result);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals(13, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] bytes = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[8];
        int result = inputStream.read(buffer, 1, 4);

        assertEquals(4, result);
        assertEquals(0, buffer[0]);
        assertEquals('t', buffer[1]);
        assertEquals('e', buffer[2]);
        assertEquals('s', buffer[3]);
        assertEquals('t', buffer[4]);
        assertEquals(13, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] bytes = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        long result = inputStream.skip(2);

        assertEquals(2, result);
        assertEquals(4, inputStream.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] bytes = "test data".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        inputStream.read();
        long result = inputStream.getValue();

        assertEquals(9, result);
    }

}