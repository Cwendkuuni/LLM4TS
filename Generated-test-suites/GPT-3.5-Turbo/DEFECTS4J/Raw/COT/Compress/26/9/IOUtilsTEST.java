import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long count = IOUtils.copy(inputStream, outputStream);
        String output = new String(outputStream.toByteArray());
        
        assertEquals(input.length, count);
        assertEquals("Hello, World!", output);
        
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long count = IOUtils.copy(inputStream, outputStream, 8);
        String output = new String(outputStream.toByteArray());
        
        assertEquals(input.length, count);
        assertEquals("Hello, World!", output);
        
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        long skipped = IOUtils.skip(inputStream, 7);
        byte[] buffer = new byte[7];
        int read = inputStream.read(buffer);
        
        assertEquals(7, skipped);
        assertEquals(7, read);
        assertEquals("World!", new String(buffer));
        
        inputStream.close();
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        byte[] buffer = new byte[5];
        int read = IOUtils.readFully(inputStream, buffer);
        
        assertEquals(5, read);
        assertEquals("Hello", new String(buffer));
        
        inputStream.close();
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        byte[] buffer = new byte[10];
        int read = IOUtils.readFully(inputStream, buffer, 3, 5);
        
        assertEquals(5, read);
        assertEquals("\u0000\u0000\u0000Hello", new String(buffer));
        
        inputStream.close();
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] input = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        byte[] output = IOUtils.toByteArray(inputStream);
        
        assertArrayEquals(input, output);
        
        inputStream.close();
    }

    @Test
    public void testCloseQuietly() throws IOException {
        // Test closing a Closeable that is null
        IOUtils.closeQuietly(null);

        // Test closing a Closeable that throws an exception
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(closeable);
    }

}