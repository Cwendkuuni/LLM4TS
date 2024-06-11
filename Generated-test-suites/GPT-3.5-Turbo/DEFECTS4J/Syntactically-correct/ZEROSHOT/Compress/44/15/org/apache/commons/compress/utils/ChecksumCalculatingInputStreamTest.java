import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream checksumStream;
    private Checksum checksum;
    private ByteArrayInputStream inputStream;

    @Before
    public void setup() {
        checksum = new CRC32();
        byte[] data = "Hello, World!".getBytes();
        inputStream = new ByteArrayInputStream(data);
        checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int bytesRead = checksumStream.read();
        assertEquals('H', bytesRead);
        long checksumValue = checksum.getValue();
        assertEquals(3211166983L, checksumValue);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = checksumStream.read(buffer);
        assertEquals(5, bytesRead);
        assertEquals("Hello", new String(buffer));
        long checksumValue = checksum.getValue();
        assertEquals(2116456139L, checksumValue);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = checksumStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertEquals("\u0000Hel", new String(buffer));
        long checksumValue = checksum.getValue();
        assertEquals(267652966L, checksumValue);
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = checksumStream.skip(7);
        assertEquals(7, bytesSkipped);
        long checksumValue = checksum.getValue();
        assertEquals(1534134666L, checksumValue);
    }

    @Test
    public void testGetValue() throws IOException {
        int bytesRead = checksumStream.read();
        assertEquals('H', bytesRead);
        long checksumValue = checksumStream.getValue();
        assertEquals(3211166983L, checksumValue);
    }

    @Test
    public void testEmptyStream() throws IOException {
        byte[] data = new byte[0];
        inputStream = new ByteArrayInputStream(data);
        checksumStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        int bytesRead = checksumStream.read();
        assertEquals(-1, bytesRead);
        long checksumValue = checksum.getValue();
        assertEquals(0, checksumValue);
    }

    @Test
    public void testMultipleReads() throws IOException {
        byte[] buffer = new byte[6];
        int bytesRead = checksumStream.read(buffer);
        assertEquals(6, bytesRead);
        assertEquals("Hello,", new String(buffer));
        long checksumValue = checksum.getValue();
        assertEquals(1329389967L, checksumValue);
        bytesRead = checksumStream.read(buffer);
        assertEquals(6, bytesRead);
        assertEquals(" World", new String(buffer));
        checksumValue = checksum.getValue();
        assertEquals(4141894244L, checksumValue);
    }

}