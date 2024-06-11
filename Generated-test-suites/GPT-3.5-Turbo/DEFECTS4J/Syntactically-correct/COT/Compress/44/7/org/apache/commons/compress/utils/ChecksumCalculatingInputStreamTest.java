import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() {
        byte[] data = {65, 66, 67};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(new Checksum(), bais);
        
        try {
            int value = csis.read();
            assertEquals(65, value);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] data = {65, 66, 67};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(new Checksum(), bais);
        
        try {
            byte[] buffer = new byte[2];
            int bytesRead = csis.read(buffer);
            assertEquals(2, bytesRead);
            assertArrayEquals(new byte[] {65, 66}, buffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() {
        byte[] data = {65, 66, 67};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(new Checksum(), bais);
        
        try {
            byte[] buffer = new byte[3];
            int bytesRead = csis.read(buffer, 1, 2);
            assertEquals(2, bytesRead);
            assertArrayEquals(new byte[] {0, 65, 66}, buffer);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testSkip() {
        byte[] data = {65, 66, 67};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(new Checksum(), bais);
        
        try {
            long skipped = csis.skip(1);
            assertEquals(1, skipped);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testGetValue() {
        byte[] data = {65, 66, 67};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream csis = new ChecksumCalculatingInputStream(new Checksum(), bais);
        
        long value = csis.getValue();
        
        // Assert that the initial value is 0
        assertEquals(0, value);
    }
}