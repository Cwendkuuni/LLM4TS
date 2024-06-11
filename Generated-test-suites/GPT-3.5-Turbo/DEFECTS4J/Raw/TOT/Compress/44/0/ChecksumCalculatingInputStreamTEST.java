import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    @Mock
    private InputStream mockInputStream;

    private ChecksumCalculatingInputStream testStream;

    @Before
    public void setup() {
        // Initialize Mockito
        MockitoAnnotations.initMocks(this);
        
        // Create a CRC32 checksum
        CRC32 checksum = new CRC32();
        
        // Create a test byte array
        byte[] testData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        
        // Create a ByteArrayInputStream from the test data
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        
        // Create the ChecksumCalculatingInputStream
        testStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
    }

    @Test
    public void testRead_singleByte() throws IOException {
        // Verify that reading a single byte updates the checksum
        int singleByte = testStream.read();
        assertEquals(3, singleByte);
        assertEquals(3, testStream.getValue());
    }

    @Test
    public void testRead_byteArray() throws IOException {
        // Create a byte array to read into
        byte[] byteArray = new byte[3];
        
        // Read the byte array from the stream
        int bytesRead = testStream.read(byteArray);
        
        // Verify the read data and checksum
        assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}, byteArray);
        assertEquals(3, bytesRead);
        assertEquals(506250763, testStream.getValue());
    }
    
    @Test
    public void testRead_byteArray_offsetLength() throws IOException {
        // Create a byte array to read into
        byte[] byteArray = new byte[4];
        
        // Read the byte array (with offset and length) from the stream
        int bytesRead = testStream.read(byteArray, 1, 3);
        
        // Verify the read data and checksum
        assertArrayEquals(new byte[]{0, (byte) 0x01, (byte) 0x02, (byte) 0x03}, byteArray);
        assertEquals(3, bytesRead);
        assertEquals(506250763, testStream.getValue());
    }
    
    @Test
    public void testSkip() throws IOException {
        // Skip a byte
        long bytesSkipped = testStream.skip(1);
        
        // Verify the skipped bytes and checksum value
        assertEquals(1, bytesSkipped);
        assertEquals(506250763, testStream.getValue());
    }
    
    @Test
    public void testSkip_invalidSkip() throws IOException {
        // Skip when there is no data remaining
        long bytesSkipped = testStream.skip(4);
        
        // Verify skipped bytes and checksum value
        assertEquals(0, bytesSkipped);
        assertEquals(0, testStream.getValue());
    }

}