import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        // Test with small input size
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());

        // Test with larger input size
        byte[] bigInputBytes = new byte[4096];
        for (int i = 0; i < bigInputBytes.length; i++) {
            bigInputBytes[i] = (byte) (i % 256);
        }
        InputStream bigInput = new ByteArrayInputStream(bigInputBytes);
        ByteArrayOutputStream bigOutput = new ByteArrayOutputStream();
        long bigCount = IOUtils.copy(bigInput, bigOutput);
        assertEquals(bigInputBytes.length, bigCount);
        assertArrayEquals(bigInputBytes, bigOutput.toByteArray());
    }

    @Test
    public void testSkip() throws Exception {
        // Test with small numToSkip
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 2);
        assertEquals(2, skipped);
        assertEquals(3, input.read());

        // Test with larger numToSkip
        byte[] bigInputBytes = new byte[4096];
        InputStream bigInput = new ByteArrayInputStream(bigInputBytes);
        long bigSkipped = IOUtils.skip(bigInput, 2048);
        assertEquals(2048, bigSkipped);
        assertEquals(2049, bigInput.read());
    }

    @Test
    public void testReadFully() throws Exception {
        // Test with small length and offset
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = new byte[3];
        int bytesRead = IOUtils.readFully(input, output, 1, 2);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2}, output);

        // Test with larger length and offset
        byte[] bigInputBytes = new byte[4096];
        for (int i = 0; i < bigInputBytes.length; i++) {
            bigInputBytes[i] = (byte) (i % 256);
        }
        InputStream bigInput = new ByteArrayInputStream(bigInputBytes);
        byte[] bigOutput = new byte[2048];
        int bigBytesRead = IOUtils.readFully(bigInput, bigOutput, 256, 1024);
        assertEquals(1024, bigBytesRead);
        for (int i = 0; i < 1024; i++) {
            assertEquals((byte) (i + 256), bigOutput[i]);
        }
    }

    @Test
    public void testToByteArray() throws Exception {
        // Test with small input size
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, output);

        // Test with larger input size
        byte[] bigInputBytes = new byte[4096];
        for (int i = 0; i < bigInputBytes.length; i++) {
            bigInputBytes[i] = (byte) (i % 256);
        }
        InputStream bigInput = new ByteArrayInputStream(bigInputBytes);
        byte[] bigOutput = IOUtils.toByteArray(bigInput);
        assertArrayEquals(bigInputBytes, bigOutput);
    }

    @Test
    public void testCloseQuietly() throws Exception {
        // Test without exception
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        IOUtils.closeQuietly(c);

        // Test with exception
        Closeable c2 = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Close failed");
            }
        };

        IOUtils.closeQuietly(c2);
    }
}