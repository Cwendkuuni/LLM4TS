import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        assertEquals(inputBytes.length, count);
        assertEquals(inputBytes, output.toByteArray());
    }
    
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output, 10);
        
        assertEquals(inputBytes.length, count);
        assertEquals(inputBytes, output.toByteArray());
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        long skipped = IOUtils.skip(input, 3);
        
        assertEquals(3, skipped);
        assertEquals(4, input.read()); // Verify that we skipped the correct number of bytes
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];
        
        int bytesRead = IOUtils.readFully(input, outputBytes);
        
        assertEquals(inputBytes.length, bytesRead);
        assertEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] outputBytes = IOUtils.toByteArray(input);
        
        assertEquals(inputBytes, outputBytes);
    }
    
    @Test
    public void testCloseQuietly() {
        CloseableMock closeable = new CloseableMock();
        
        IOUtils.closeQuietly(closeable);
        
        assertEquals(true, closeable.isClosed());
    }
    
    @Test
    public void testCloseQuietlyWithNull() {
        IOUtils.closeQuietly(null); // Should not throw any exception
    }
    
    @Test
    public void testCopyThrowsIOException() {
        InputStreamMock input = new InputStreamMock(true);
        OutputStreamMock output = new OutputStreamMock();
        
        assertThrows(IOException.class, () -> IOUtils.copy(input, output));
    }
    
    @Test
    public void testSkipThrowsIOException() {
        InputStreamMock input = new InputStreamMock(true);
        
        assertThrows(IOException.class, () -> IOUtils.skip(input, 3));
    }
    
    @Test
    public void testReadFullyThrowsIOException() {
        InputStreamMock input = new InputStreamMock(true);
        byte[] outputBytes = new byte[5];
        
        assertThrows(IOException.class, () -> IOUtils.readFully(input, outputBytes));
    }
    
    @Test
    public void testToByteArrayThrowsIOException() {
        InputStreamMock input = new InputStreamMock(true);
        
        assertThrows(IOException.class, () -> IOUtils.toByteArray(input));
    }
    
    class CloseableMock implements Closeable {
        private boolean closed = false;
        
        @Override
        public void close() throws IOException {
            closed = true;
        }
        
        public boolean isClosed() {
            return closed;
        }
    }
    
    class InputStreamMock extends InputStream {
        private boolean throwException = false;
        
        public InputStreamMock(boolean throwException) {
            this.throwException = throwException;
        }
        
        @Override
        public int read() throws IOException {
            if (throwException) {
                throw new IOException();
            } else {
                return 0;
            }
        }
    }
    
    class OutputStreamMock extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            // Do nothing
        }
    }

}