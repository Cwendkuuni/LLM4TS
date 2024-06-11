import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertEquals(1, stream.read());
        assertEquals(2, stream.read());
        assertEquals(3, stream.read());
        assertEquals(-1, stream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(data));
        byte[] buffer = new byte[3];
        assertEquals(3, stream.read(buffer));
        assertEquals(data, buffer);
        assertEquals(-1, stream.read(buffer));
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(data));
        byte[] buffer = new byte[5];
        assertEquals(3, stream.read(buffer, 1, 3));
        assertEquals(new byte[] { 0, 1, 2, 3, 0 }, buffer);
        assertEquals(-1, stream.read(buffer, 1, 3));
    }

    @Test
    public void testSkip() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        assertEquals(1, stream.skip(1));
        assertEquals(1, stream.read());
        assertEquals(1, stream.skip(2));
        assertEquals(-1, stream.read());
        assertEquals(0, stream.skip(1));
    }

    @Test
    public void testGetValue() throws IOException {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] { 1, 2, 3 }));
        stream.read();
        stream.read();
        assertEquals(0xCBF43926L, stream.getValue());
    }
}