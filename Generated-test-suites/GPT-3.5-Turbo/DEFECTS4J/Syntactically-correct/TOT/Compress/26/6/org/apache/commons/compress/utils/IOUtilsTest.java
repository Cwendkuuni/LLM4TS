import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class IOUtilsTest {

    @Mock
    private InputStream mockInputStream;

    @Mock
    private OutputStream mockOutputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test copy(InputStream, OutputStream)
    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);

        IOUtils.copy(byteArrayInputStream, mockOutputStream);

        verify(mockOutputStream, times(1)).write(inputBytes);
    }

    // Test copy(InputStream, OutputStream, int)
    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);

        IOUtils.copy(byteArrayInputStream, mockOutputStream, 1024);

        verify(mockOutputStream, times(1)).write(inputBytes);
    }

    // Test skip(InputStream, long)
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Skip this".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);

        long actual = IOUtils.skip(byteArrayInputStream, 6);
        long expected = 6;

        assertEquals(expected, actual);
    }

    // Test readFully(InputStream, byte[])
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];

        IOUtils.readFully(byteArrayInputStream, outputBytes);

        assertArrayEquals(inputBytes, outputBytes);
    }

    // Test readFully(InputStream, byte[], int, int)
    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        IOUtils.readFully(byteArrayInputStream, outputBytes, 0, 5);

        byte[] expectedOutput = "Hello".getBytes();
        assertArrayEquals(expectedOutput, outputBytes);
    }

    // Test toByteArray(InputStream)
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(byteArrayInputStream);

        assertArrayEquals(inputBytes, outputBytes);
    }

    // Test closeQuietly(Closeable)
    @Test
    public void testCloseQuietly() throws IOException {
        Closeable closeableMock = mock(Closeable.class);
        doThrow(new IOException()).when(closeableMock).close();
        
        IOUtils.closeQuietly(closeableMock);

        verify(closeableMock, times(1)).close();
    }

}