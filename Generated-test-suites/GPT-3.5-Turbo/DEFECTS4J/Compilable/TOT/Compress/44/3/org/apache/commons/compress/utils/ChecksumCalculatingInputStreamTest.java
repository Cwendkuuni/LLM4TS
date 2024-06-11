package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream stream;

    @Before
    public void setUp() {
        // Create a mock input stream
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        // Create a Checksum object
        Checksum checksumObj = new CRC32();
        // Create the ChecksumCalculatingInputStream
        stream = new ChecksumCalculatingInputStream(checksumObj, input);
    }

    @Test
    public void testRead() throws IOException {
        // Read the first byte
        int firstByte = stream.read();

        // Assert that the first byte is correct
        assertEquals(1, firstByte);

        // Assert checksum is updated correctly
        assertEquals(1, stream.getValue());
        
        // Read the second byte
        int secondByte = stream.read();

        // Assert that the second byte is correct
        assertEquals(2, secondByte);

        // Assert checksum is updated correctly
        assertEquals(3, stream.getValue());
        
        // Read the third byte
        int thirdByte = stream.read();

        // Assert that the third byte is correct
        assertEquals(3, thirdByte);

        // Assert checksum is updated correctly
        assertEquals(6, stream.getValue());
        
        // Read beyond end of stream
        int endOfFile = stream.read();

        // Assert that end of stream is reached
        assertEquals(-1, endOfFile);

        // Assert checksum is not updated beyond end of stream
        assertEquals(6, stream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        // Create a byte array to read into
        byte[] byteArray = new byte[2];

        // Read the first two bytes into the byte array
        int bytesRead = stream.read(byteArray);

        // Assert that the number of bytes read is correct
        assertEquals(2, bytesRead);

        // Assert that the bytes read are correct
        assertArrayEquals(new byte[]{1, 2}, byteArray);

        // Assert checksum is updated correctly
        assertEquals(3, stream.getValue());
        
        // Read beyond end of stream
        bytesRead = stream.read(byteArray);

        // Assert that end of stream is reached
        assertEquals(-1, bytesRead);

        // Assert checksum is not updated beyond end of stream
        assertEquals(3, stream.getValue());
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        // Create a byte array to read into
        byte[] byteArray = new byte[4];

        // Read the first two bytes into the byte array starting from offset 1
        int bytesRead = stream.read(byteArray, 1, 2);

        // Assert that the number of bytes read is correct
        assertEquals(2, bytesRead);

        // Assert that the bytes read are correct
        assertArrayEquals(new byte[]{0, 1, 2, 0}, byteArray);

        // Assert checksum is updated correctly
        assertEquals(3, stream.getValue());
        
        // Read beyond end of stream
        bytesRead = stream.read(byteArray, 1, 2);

        // Assert that end of stream is reached
        assertEquals(-1, bytesRead);

        // Assert checksum is not updated beyond end of stream
        assertEquals(3, stream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        // Skip one byte
        long skipped = stream.skip(1);

        // Assert that one byte is skipped
        assertEquals(1, skipped);

        // Assert checksum is updated correctly
        assertEquals(1, stream.getValue());
        
        // Skip beyond end of stream
        skipped = stream.skip(2);

        // Assert that end of stream is reached
        assertEquals(0, skipped);

        // Assert checksum is not updated beyond end of stream
        assertEquals(1, stream.getValue());
    }
    
    @Test
    public void testErrorScenario() throws IOException {
        // Create a mock input stream
        ByteArrayInputStream input = null;
        // Create a Checksum object
        Checksum checksumObj = new CRC32();
        // Create the ChecksumCalculatingInputStream
        stream = new ChecksumCalculatingInputStream(checksumObj, input);
        
        // Try to read from the null input stream
        int byteRead = stream.read();
        
        // Assert that -1 is returned
        assertEquals(-1, byteRead);
        
        // Assert that checksum is not updated
        assertEquals(0, stream.getValue());
        
        // Try to skip bytes from the null input stream
        long skipped = stream.skip(1);
        
        // Assert that 0 is returned
        assertEquals(0, skipped);
        
        // Try to read byte array from the null input stream
        byte[] byteArray = new byte[2];
        int bytesRead = stream.read(byteArray);
        
        // Assert that -1 is returned
        assertEquals(-1, bytesRead);
        
        // Assert that checksum is not updated
        assertEquals(0, stream.getValue());
        
        // Try to read byte array from the null input stream with offset and length
        bytesRead = stream.read(byteArray, 1, 2);
        
        // Assert that -1 is returned
        assertEquals(-1, bytesRead);
        
        // Assert that checksum is not updated
        assertEquals(0, stream.getValue());
    }
}