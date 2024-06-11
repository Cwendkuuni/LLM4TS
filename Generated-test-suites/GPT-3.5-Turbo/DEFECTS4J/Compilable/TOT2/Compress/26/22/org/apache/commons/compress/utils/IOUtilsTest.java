package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    private static final byte[] TEST_BYTES = {65, 66, 67, 68, 69};
    private static final int COPY_BUF_SIZE = 8024;
    private static final int SKIP_BUF_SIZE = 4096;

    @Test
    public void testCopyInputStreamToOutputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(TEST_BYTES);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output);
        
        assertArrayEquals(TEST_BYTES, output.toByteArray());
        assertEquals(TEST_BYTES.length, bytesCopied);
        
        input.close();
        output.close();
    }

    @Test
    public void testCopyInputStreamToOutputStreamWithBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(TEST_BYTES);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output, COPY_BUF_SIZE);
        
        assertArrayEquals(TEST_BYTES, output.toByteArray());
        assertEquals(TEST_BYTES.length, bytesCopied);
        
        input.close();
        output.close();
    }

    @Test
    public void testSkipInputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(TEST_BYTES);
        
        long bytesSkipped = IOUtils.skip(input, 3);
        
        assertEquals(3, bytesSkipped);
        
        input.close();
    }

    @Test
    public void testReadFullyToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(TEST_BYTES);
        byte[] buffer = new byte[TEST_BYTES.length];
        
        int bytesRead = IOUtils.readFully(input, buffer);
        
        assertArrayEquals(TEST_BYTES, buffer);
        assertEquals(TEST_BYTES.length, bytesRead);
        
        input.close();
    }

    @Test
    public void testReadFullyToByteArrayWithOffsetAndLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(TEST_BYTES);
        byte[] buffer = new byte[TEST_BYTES.length];
        
        int bytesRead = IOUtils.readFully(input, buffer, 1, TEST_BYTES.length - 2);
        
        assertArrayEquals(new byte[]{0, 65, 66, 0, 0}, buffer);
        assertEquals(TEST_BYTES.length - 2, bytesRead);
        
        input.close();
    }

    @Test
    public void testToByteArrayFromInputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(TEST_BYTES);
        
        byte[] result = IOUtils.toByteArray(input);
        
        assertArrayEquals(TEST_BYTES, result);
        
        input.close();
    }

    @Test
    public void testCloseQuietlyWithNull() {
        IOUtils.closeQuietly(null);
        // Just make sure no exceptions are thrown
    }
}