package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {"A", "B", "C"});
        OutputStream output = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(input, output);
        assertEquals(3, copiedBytes);

        byte[] expectedOutputBytes = output.toByteArray();
        byte[] actualOutputBytes = new byte[] {"A", "B", "C"};
        assertArrayEquals(actualOutputBytes, expectedOutputBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {"A", "B", "C"});
        OutputStream output = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(input, output, 1);
        assertEquals(3, copiedBytes);

        byte[] expectedOutputBytes = output.toByteArray();
        byte[] actualOutputBytes = new byte[] {"A", "B", "C"};
        assertArrayEquals(actualOutputBytes, expectedOutputBytes);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {"A", "B", "C"});

        long skippedBytes = IOUtils.skip(input, 2);
        assertEquals(2, skippedBytes);

        byte[] expectedRemainingBytes = new byte[] {"C"};
        byte[] actualRemainingBytes = new byte[1];
        int read = 0;
        while ((read = input.read(actualRemainingBytes)) != -1) {
            // Consume remaining bytes
            assertArrayEquals(new byte[] {actualRemainingBytes[0]}, expectedRemainingBytes);
        }
        assertArrayEquals(expectedRemainingBytes, actualRemainingBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {"A", "B", "C"});
        byte[] buffer = new byte[3];

        int readBytes = IOUtils.readFully(input, buffer);
        assertEquals(3, readBytes);

        byte[] expectedBytes = new byte[] {"A", "B", "C"};
        assertArrayEquals(expectedBytes, buffer);
    }

    @Test
    public void testReadFullyWithBufferSizeAndOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {"A", "B", "C", "D", "E"});
        byte[] buffer = new byte[5];

        int readBytes = IOUtils.readFully(input, buffer, 2, 3);
        assertEquals(3, readBytes);

        byte[] expectedBytes = new byte[] {"C", "D", "E"};
        assertArrayEquals(expectedBytes, new byte[] {buffer[2], buffer[3], buffer[4]});
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {"A", "B", "C"});
        byte[] expectedBytes = new byte[] {"A", "B", "C"};

        byte[] actualBytes = IOUtils.toByteArray(input);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Closeable closeable = outputStream;

        IOUtils.closeQuietly(closeable);
    }
}