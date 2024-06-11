import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    // Test case for read() method
    @Test
    public void testRead() throws IOException {
        CRC32 crc32 = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        assertEquals((byte) 1, (byte) inputStream.read());
        assertEquals((byte) 2, (byte) inputStream.read());
        
        inputStream.close();
    }

    // Test case for read(byte[] b) method
    @Test
    public void testReadByteArray() throws IOException {
        CRC32 crc32 = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        byte[] buffer = new byte[3];
        assertEquals(3, inputStream.read(buffer));
        assertArrayEquals(new byte[] {1, 2, 3}, buffer);
        
        inputStream.close();
    }

    // Test case for read(byte[] b, int off, int len) method
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        CRC32 crc32 = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        byte[] buffer = new byte[5];
        assertEquals(2, inputStream.read(buffer, 1, 2));
        assertArrayEquals(new byte[] {0, 1, 2, 0, 0}, buffer);
        
        inputStream.close();
    }

    // Test case for skip(long n) method
    @Test
    public void testSkip() throws IOException {
        CRC32 crc32 = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        assertEquals(1, inputStream.skip(2));
        assertEquals(1, inputStream.skip(1));
        
        inputStream.close();
    }

    // Test case for getValue() method
    @Test
    public void testGetValue() throws IOException {
        CRC32 crc32 = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        inputStream.read();
        inputStream.read();
        inputStream.read();
        inputStream.read();
        inputStream.read();
        
        assertEquals(0x8587f97fL, inputStream.getValue());
        
        inputStream.close();
    }

    // Test case for IOException in read() method
    @Test(expected = IOException.class)
    public void testIOExceptionInRead() throws IOException {
        CRC32 crc32 = new CRC32();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        inputStream.read();
    }

    // Test case for IOException in read(byte[] b) method
    @Test(expected = IOException.class)
    public void testIOExceptionInReadByteArray() throws IOException {
        CRC32 crc32 = new CRC32();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        inputStream.read(new byte[1]);
    }

    // Test case for IOException in read(byte[] b, int off, int len) method
    @Test(expected = IOException.class)
    public void testIOExceptionInReadByteArrayWithOffsetAndLength() throws IOException {
        CRC32 crc32 = new CRC32();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
        
        inputStream.read(new byte[1], 0, 1);
    }

}