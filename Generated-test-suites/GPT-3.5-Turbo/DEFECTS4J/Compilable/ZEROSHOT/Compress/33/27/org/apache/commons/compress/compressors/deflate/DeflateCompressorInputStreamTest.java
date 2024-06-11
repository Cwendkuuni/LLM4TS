package org.apache.commons.compress.compressors.deflate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;

public class DeflateCompressorInputStreamTest {

    private byte[] testData;
    private DeflateCompressorInputStream compressor;

    @Before
    public void setup() {
        // Create test data
        testData = "This is a test".getBytes();
        
        // Create input stream from test data
        InputStream inputStream = new ByteArrayInputStream(testData);
        
        // Create compressor instance
        compressor = new DeflateCompressorInputStream(inputStream);
    }

    @Test
    public void testRead() {
        try {
            int c;
            int count = 0;
            
            // Read from compressor and check if it matches with test data
            while ((c = compressor.read()) != -1) {
                assertEquals(c, testData[count]);
                count++;
            }
            
            // Check if the total count matches the length of test data
            assertEquals(count, testData.length);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testReadArray() {
        try {
            byte[] buffer = new byte[10];
            int count = 0;
            
            // Read from compressor to buffer and check if it matches with test data
            int bytesRead;
            while ((bytesRead = compressor.read(buffer, 0, buffer.length)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    assertEquals(buffer[i], testData[count]);
                    count++;
                }
            }
            
            // Check if the total count matches the length of test data
            assertEquals(count, testData.length);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testSkip() {
        try {
            long skipCount = 5;
            
            // Skip bytes from compressor
            long skipped = compressor.skip(skipCount);
            
            // Check if the skipped count matches the expected skip count
            assertEquals(skipped, skipCount);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testAvailable() {
        try {
            // Get the number of available bytes from compressor
            int available = compressor.available();
            
            // Check if the available count matches the length of test data
            assertEquals(available, testData.length);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }

    @Test
    public void testClose() {
        try {
            // Close the compressor
            compressor.close();
            
            // Verify that compressor is closed
            // This is done by attempting to read from the closed compressor
            compressor.read();
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Expected IOException should be thrown when trying to read from closed compressor
        }
    }
}