import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = { 1, 2, 3 };
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);

        assertEquals(1, ccis.read());
        assertEquals(2, ccis.read());
        assertEquals(3, ccis.read());
        assertEquals(-1, ccis.read());

        assertEquals(3098877581L, ccis.getValue());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = { 1, 2, 3, 4, 5 };
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);
        
        byte[] buffer = new byte[3];
        assertEquals(3, ccis.read(buffer));
        assertArrayEquals(new byte[] { 1, 2, 3 }, buffer);
        
        assertEquals(2, ccis.read(buffer));
        assertArrayEquals(new byte[] { 4, 5, 3 }, buffer);
        
        assertEquals(-1, ccis.read());
        
        assertEquals(3111710304L, ccis.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = { 1, 2, 3 };
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);

        assertEquals(1, ccis.read());
        assertEquals(1, ccis.skip(10));
        assertEquals(-1, ccis.read());

        assertEquals(3903730729L, ccis.getValue());
    }

}