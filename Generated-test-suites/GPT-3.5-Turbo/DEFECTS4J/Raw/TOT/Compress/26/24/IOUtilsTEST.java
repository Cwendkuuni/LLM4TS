import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {
    
    private final String TEST_STRING = "This is a test string";
    private final byte[] TEST_BYTES = TEST_STRING.getBytes();
    private InputStream testInputStream;
    private OutputStream testOutputStream;
    
    @Before
    public void setUp() {
        testInputStream = new ByteArrayInputStream(TEST_BYTES);
        testOutputStream = new ByteArrayOutputStream();
    }
    
    @After
    public void tearDown() {
        try {
            testInputStream.close();
            testOutputStream.close();
        } catch (IOException e) {
            fail("IOException occurred during teardown");
        }
    }
    
    @Test
    public void testCopyDefaultBufferSize() {
        try {
            long bytesCopied = IOUtils.copy(testInputStream, testOutputStream);
            byte[] outputBytes = ((ByteArrayOutputStream) testOutputStream).toByteArray();
            String outputString = new String(outputBytes);
            assertEquals(TEST_STRING.length(), bytesCopied);
            assertEquals(TEST_STRING, outputString);
        } catch (IOException e) {
            fail("IOException occurred during testCopyDefaultBufferSize");
        }
    }
    
    @Test
    public void testCopyCustomBufferSize() {
        int bufferSize = 1024; // Custom buffer size
        try {
            long bytesCopied = IOUtils.copy(testInputStream, testOutputStream, bufferSize);
            byte[] outputBytes = ((ByteArrayOutputStream) testOutputStream).toByteArray();
            String outputString = new String(outputBytes);
            assertEquals(TEST_STRING.length(), bytesCopied);
            assertEquals(TEST_STRING, outputString);
        } catch (IOException e) {
            fail("IOException occurred during testCopyCustomBufferSize");
        }
    }
    
    @Test
    public void testSkip() {
        long bytesToSkip = 10; // Arbitrary number of bytes to skip
        try {
            long bytesSkipped = IOUtils.skip(testInputStream, bytesToSkip);
            assertEquals(bytesToSkip, bytesSkipped);
        } catch (IOException e) {
            fail("IOException occurred during testSkip");
        }
    }
    
    @Test
    public void testReadFully() {
        int length = 10; // Arbitrary length of bytes to read
        byte[] buffer = new byte[length];
        try {
            int bytesRead = IOUtils.readFully(testInputStream, buffer);
            assertEquals(length, bytesRead);
            String outputString = new String(buffer);
            assertEquals(TEST_STRING.substring(0, length), outputString);
        } catch (IOException e) {
            fail("IOException occurred during testReadFully");
        }
    }
    
    @Test
    public void testReadFullyWithOffset() {
        int length = 10; // Arbitrary length of bytes to read
        int offset = 5; // Arbitrary offset
        byte[] buffer = new byte[offset + length];
        try {
            int bytesRead = IOUtils.readFully(testInputStream, buffer, offset, length);
            assertEquals(length, bytesRead);
            String outputString = new String(buffer, offset, length);
            assertEquals(TEST_STRING.substring(0, length), outputString);
        } catch (IOException e) {
            fail("IOException occurred during testReadFullyWithOffset");
        }
    }
    
    @Test
    public void testToByteArray() {
        try {
            byte[] outputBytes = IOUtils.toByteArray(testInputStream);
            String outputString = new String(outputBytes);
            assertEquals(TEST_STRING, outputString);
        } catch (IOException e) {
            fail("IOException occurred during testToByteArray");
        }
    }
    
    @Test
    public void testCloseQuietly() {
        IOUtils.closeQuietly(testInputStream);
        assert(true); // Ensure no exceptions occur during closing of input stream
    }
}