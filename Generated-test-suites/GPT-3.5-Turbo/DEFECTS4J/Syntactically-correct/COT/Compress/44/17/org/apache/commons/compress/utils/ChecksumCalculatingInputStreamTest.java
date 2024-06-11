import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        int byteRead = stream.read();
        
        assertEquals("Byte read is not matching", 1, byteRead);
        assertEquals("Checksum value is not matching", checksum.getValue(), 1);
    }
    
    @Test
    public void testReadArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        byte[] buffer = new byte[5];
        int bytesRead = stream.read(buffer);
        
        assertArrayEquals("Bytes read are not matching", data, buffer);
        assertEquals("Checksum value is not matching", checksum.getValue(), 15);
    }
    
    @Test
    public void testReadArrayWithOffset() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        byte[] buffer = new byte[5];
        int bytesRead = stream.read(buffer, 1, 3);
        
        byte[] expected = {0, 1, 2, 3, 0};
        assertArrayEquals("Bytes read are not matching", expected, buffer);
        assertEquals("Checksum value is not matching", checksum.getValue(), 6);
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        long skipped = stream.skip(3);
        int byteRead = stream.read();
        
        assertEquals("Number of skipped bytes is not matching", 3, skipped);
        assertEquals("Byte read after skipping is not matching", 4, byteRead);
        assertEquals("Checksum value is not matching", checksum.getValue(), 4);
    }
    
    @Test
    public void testGetValue() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);
        
        stream.read(new byte[5]);
        long value = stream.getValue();
        
        assertEquals("Checksum value is not matching", value, checksum.getValue());
    }
}