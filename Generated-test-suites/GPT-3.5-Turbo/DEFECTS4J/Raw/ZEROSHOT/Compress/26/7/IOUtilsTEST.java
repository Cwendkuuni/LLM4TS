import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.ByteArrayOutputStream;

public class IOUtilsTest {

    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new InputStream();
            output = new OutputStream();
            long count = IOUtils.copy(input, output);
            assertEquals("Incorrect number of bytes copied", expectedCount, count);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new InputStream();
            output = new OutputStream();
            int bufferSize = 8192;
            long count = IOUtils.copy(input, output, bufferSize);
            assertEquals("Incorrect number of bytes copied", expectedCount, count);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = null;
        try {
            input = new InputStream();
            long numToSkip = 100;
            long actualSkip = IOUtils.skip(input, numToSkip);
            assertEquals("Incorrect number of bytes skipped", expectedSkip, actualSkip);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = null;
        try {
            input = new InputStream();
            byte[] buffer = new byte[1024];
            int count = IOUtils.readFully(input, buffer);
            assertEquals("Incorrect number of bytes read", expectedCount, count);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = null;
        try {
            input = new InputStream();
            byte[] buffer = new byte[1024];
            int offset = 0;
            int length = 512;
            int count = IOUtils.readFully(input, buffer, offset, length);
            assertEquals("Incorrect number of bytes read", expectedCount, count);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = null;
        try {
            input = new InputStream();
            byte[] bytes = IOUtils.toByteArray(input);
            assertNotNull("Byte array is null", bytes);
            assertEquals("Byte array length is incorrect", expectedLength, bytes.length);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testCloseQuietly() throws IOException {
        Closeable closeable = null;
        try {
            closeable = new Closeable();
            IOUtils.closeQuietly(closeable);
            // No assertions as this is just testing if the method does not throw an exception
        } finally {
            IOUtils.closeQuietly(closeable);
        }
    }
}