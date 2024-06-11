
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy_WithDefaultBufferSize() throws IOException {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new InputStreamMock();
            output = new OutputStreamMock();
            long result = IOUtils.copy(input, output);
            assertEquals(10, result);
            assertTrue(((OutputStreamMock) output).isWriteCalled());
            assertArrayEquals(((InputStreamMock) input).getData(), ((OutputStreamMock) output).getData());
        } finally {
            close(input);
            close(output);
        }
    }

    @Test
    public void testCopy_WithCustomBufferSize() throws IOException {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = new InputStreamMock();
            output = new OutputStreamMock();
            long result = IOUtils.copy(input, output, 5);
            assertEquals(10, result);
            assertTrue(((OutputStreamMock) output).isWriteCalled());
            assertArrayEquals(((InputStreamMock) input).getData(), ((OutputStreamMock) output).getData());
        } finally {
            close(input);
            close(output);
        }
    }

    @Test
    public void testCopy_NullInputStream() {
        OutputStream output = null;

        try {
            output = new OutputStreamMock();
            long result = IOUtils.copy(null, output);
            assertEquals(0, result);
            assertFalse(((OutputStreamMock) output).isWriteCalled());
        } finally {
            close(output);
        }
    }

    @Test
    public void testCopy_NullOutputStream() {
        InputStream input = null;

        try {
            input = new InputStreamMock();
            long result = IOUtils.copy(input, null);
            assertEquals(0, result);
            assertFalse(((InputStreamMock) input).isReadCalled());
        } finally {
            close(input);
        }
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = null;

        try {
            input = new InputStreamMock();
            long result = IOUtils.skip(input, 5);
            assertEquals(5, result);
            assertTrue(((InputStreamMock) input).isSkipCalled());
        } finally {
            close(input);
        }
    }

    @Test
    public void testSkip_WithInvalidSkipValue() throws IOException {
        InputStream input = null;

        try {
            input = new InputStreamMock();
            long result = IOUtils.skip(input, -1);
            assertEquals(0, result);
            assertFalse(((InputStreamMock) input).isSkipCalled());
        } finally {
            close(input);
        }
    }

    @Test
    public void testSkip_EndOfInputStream() throws IOException {
        InputStream input = null;

        try {
            input = new InputStreamMock();
            long result = IOUtils.skip(input, 15);
            assertEquals(10, result);
            assertTrue(((InputStreamMock) input).isSkipCalled());
        } finally {
            close(input);
        }
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = null;
        byte[] buffer = new byte[10];

        try {
            input = new InputStreamMock();
            int result = IOUtils.readFully(input, buffer);
            assertEquals(10, result);
            assertArrayEquals(((InputStreamMock) input).getData(), buffer);
        } finally {
            close(input);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_WithInvalidOffset() throws IOException {
        InputStream input = null;
        byte[] buffer = new byte[10];

        try {
            input = new InputStreamMock();
            IOUtils.readFully(input, buffer, -1, 10);
        } finally {
            close(input);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_WithInvalidLength() throws IOException {
        InputStream input = null;
        byte[] buffer = new byte[10];

        try {
            input = new InputStreamMock();
            IOUtils.readFully(input, buffer, 0, 11);
        } finally {
            close(input);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_WithInvalidLengthAndOffset() throws IOException {
        InputStream input = null;
        byte[] buffer = new byte[10];

        try {
            input = new InputStreamMock();
            IOUtils.readFully(input, buffer, -1, 11);
        } finally {
            close(input);
        }
    }

    @Test
    public void testReadFully_EndOfInputStream() throws IOException {
        InputStream input = null;
        byte[] buffer = new byte[10];

        try {
            input = new InputStreamMock();
            int result = IOUtils.readFully(input, buffer);
            assertEquals(10, result);
            assertArrayEquals(((InputStreamMock) input).getData(), buffer);
        } finally {
            close(input);
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = null;

        try {
            input = new InputStreamMock();
            byte[] result = IOUtils.toByteArray(input);
            assertArrayEquals(((InputStreamMock) input).getData(), result);
        } finally {
            close(input);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testToByteArray_NullInputStream() throws IOException {
        IOUtils.toByteArray(null);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = null;
        IOUtils.closeQuietly(closeable);

        closeable = new CloseableMock();
        try {
            IOUtils.closeQuietly(closeable);
        } catch (IOException e) {
            fail();
        }
    }

    private void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            fail();
        }
    }

    private class InputStreamMock extends InputStream {
        private byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        private int index = 0;
        private boolean readCalled = false;
        private boolean skipCalled = false;

        public byte[] getData() {
            return data;
        }

        public boolean isReadCalled() {
            return readCalled;
        }

        public boolean isSkipCalled() {
            return skipCalled;
        }

        @Override
        public int read() throws IOException {
            if (index >= data.length) {
                return -1;
            }
            readCalled = true;
            return data[index++];
        }

        @Override
        public long skip(long n) throws IOException {
            skipCalled = true;
            index += (int) n;
            if (index > data.length) {
                index = data.length;
            }
            return n;
        }
    }

    private class OutputStreamMock extends OutputStream {
        private byte[] data = new byte[10];
        private int index = 0;
        private boolean writeCalled = false;

        public byte[] getData() {
            return Arrays.copyOfRange(data, 0, index);
        }

        public boolean isWriteCalled() {
            return writeCalled;
        }

        @Override
        public void write(int b) throws IOException {
            writeCalled = true;
            data[index++] = (byte) b;
        }
    }

    private class CloseableMock implements Closeable {
        private boolean closeCalled = false;

        public boolean isCloseCalled() {
            return closeCalled;
        }

        @Override
        public void close() throws IOException {
            closeCalled = true;
        }
    }
}
