import org.junit.Test;
import java.io.*;

public class IOUtilsTest {
    
    private String TESTING_TEST = "Test String";
    private final int TESTING_SIZE = 1024;
    
    @Test
    public void testCopy() {
        ByteArrayInputStream input = new ByteArrayInputStream(TESTING_TEST.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        try {
            long result = IOUtils.copy(input, output);
            assertEquals(TESTING_TEST.length(), result);
            assertEquals(TESTING_TEST, output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testCopyWithBufferSize() {
        ByteArrayInputStream input = new ByteArrayInputStream(TESTING_TEST.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 10;
        
        try {
            long result = IOUtils.copy(input, output, bufferSize);
            assertEquals(TESTING_TEST.length(), result);
            assertEquals(TESTING_TEST, output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testCopyWithEmptyInputStream() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        try {
            long result = IOUtils.copy(input, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testCopyWithEmptyInputStreamAndBufferSize() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 10;
        
        try {
            long result = IOUtils.copy(input, output, bufferSize);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSkip() {
        ByteArrayInputStream input = new ByteArrayInputStream(TESTING_TEST.getBytes());
        long numToSkip = 5;
        
        try {
            long result = IOUtils.skip(input, numToSkip);
            assertEquals(numToSkip, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSkipWithEmptyInputStream() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        long numToSkip = 5;
        
        try {
            long result = IOUtils.skip(input, numToSkip);
            assertEquals(0, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testSkipWithNumToSkipGreaterThanAvailable() {
        ByteArrayInputStream input = new ByteArrayInputStream(TESTING_TEST.getBytes());
        long numToSkip = TESTING_SIZE;
        
        try {
            long result = IOUtils.skip(input, numToSkip);
            assertEquals(TESTING_TEST.length(), result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testReadFully() {
        ByteArrayInputStream input = new ByteArrayInputStream(TESTING_TEST.getBytes());
        byte[] buffer = new byte[TESTING_SIZE];
        
        try {
            int result = IOUtils.readFully(input, buffer);
            assertEquals(TESTING_TEST.length(), result);
            assertEquals(TESTING_TEST, new String(buffer));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testReadFullyWithEmptyInputStream() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        byte[] buffer = new byte[TESTING_SIZE];
        
        try {
            int result = IOUtils.readFully(input, buffer);
            assertEquals(0, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testReadFullyWithNumToReadGreaterThanAvailable() {
        ByteArrayInputStream input = new ByteArrayInputStream(TESTING_TEST.getBytes());
        byte[] buffer = new byte[TESTING_SIZE];
        
        try {
            int result = IOUtils.readFully(input, buffer, 0, TESTING_SIZE+1);
            assertEquals(TESTING_TEST.length(), result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testToByteArray() {
        ByteArrayInputStream input = new ByteArrayInputStream(TESTING_TEST.getBytes());
        
        try {
            byte[] result = IOUtils.toByteArray(input);
            assertEquals(TESTING_TEST, new String(result));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testToByteArrayWithEmptyInputStream() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        
        try {
            byte[] result = IOUtils.toByteArray(input);
            assertEquals("", new String(result));
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // do nothing
            }
        };
        
        try {
            IOUtils.closeQuietly(closeable);
            // if no exception thrown, test passes
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCloseQuietlyWithNullCloseable() {
        Closeable closeable = null;
        
        try {
            IOUtils.closeQuietly(closeable);
            // if no exception thrown, test passes
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
}