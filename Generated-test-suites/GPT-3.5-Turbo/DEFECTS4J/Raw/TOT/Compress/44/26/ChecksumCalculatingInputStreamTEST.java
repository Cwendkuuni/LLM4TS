import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() {
        Checksum checksum = new CRC32();
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));
    }

    @Test
    public void testReadByte() throws IOException {
        int readByte = inputStream.read();
        assertEquals(1, readByte);
        assertEquals(1, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        assertEquals(6, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
        assertEquals(3, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(6, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = inputStream.skip(3);
        assertEquals(3, bytesSkipped);
        assertEquals(3, inputStream.read());
        assertEquals(21, inputStream.getValue());
    }

    @Test
    public void testSkipEmptyStream() throws IOException {
        long bytesSkipped = inputStream.skip(10);
        assertEquals(0, bytesSkipped);
        assertEquals(-1, inputStream.read());
        assertEquals(0, inputStream.getValue());
    }

    @Test
    public void testGetValue() {
        assertEquals(0, inputStream.getValue());
    }
}