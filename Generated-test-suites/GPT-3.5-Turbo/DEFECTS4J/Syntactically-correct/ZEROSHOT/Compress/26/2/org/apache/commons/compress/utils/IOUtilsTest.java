import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static org.junit.Assert.*;

public class IOUtilsTest {

    // Test copy() method with default buffer size
    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new InputStream() {
                @Override
                public int read() throws IOException {
                    return -1;
                }
            };
            output = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // Do nothing
                }
            };
            long count = IOUtils.copy(input, output);
            assertEquals(0, count);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    // Test copy() method with custom buffer size
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new InputStream() {
                @Override
                public int read() throws IOException {
                    return -1;
                }
            };
            output = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // Do nothing
                }
            };
            long count = IOUtils.copy(input, output, 1024);
            assertEquals(0, count);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    // Test skip() method
    @Test
    public void testSkip() throws IOException {
        InputStream input = null;
        try {
            input = new InputStream() {
                private int count = 0;

                @Override
                public int read() throws IOException {
                    return -1;
                }

                @Override
                public long skip(long n) throws IOException {
                    if (n > 0 && count < 10) {
                        count++;
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };
            long skipped = IOUtils.skip(input, 10);
            assertEquals(10, skipped);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    // Test readFully() method
    @Test
    public void testReadFully() throws IOException {
        InputStream input = null;
        try {
            input = new InputStream() {
                private int count = 0;

                @Override
                public int read() throws IOException {
                    return -1;
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    if (count < 10) {
                        for (int i = 0; i < len; i++) {
                            b[off + i] = (byte) (count % 256);
                            count++;
                        }
                        return len;
                    } else {
                        return -1;
                    }
                }
            };
            byte[] buffer = new byte[10];
            int bytesRead = IOUtils.readFully(input, buffer);
            assertEquals(10, bytesRead);
            for (int i = 0; i < buffer.length; i++) {
                assertEquals(i % 256, buffer[i]);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    // Test toByteArray() method
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = null;
        try {
            input = new InputStream() {
                private int count = 0;

                @Override
                public int read() throws IOException {
                    if (count < 10) {
                        count++;
                        return count;
                    } else {
                        return -1;
                    }
                }
            };
            byte[] bytes = IOUtils.toByteArray(input);
            assertEquals(10, bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                assertEquals(i + 1, bytes[i]);
            }
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    // Test closeQuietly() method
    @Test
    public void testCloseQuietly() {
        Closeable closeable = null;
        try {
            closeable = new Closeable() {
                @Override
                public void close() throws IOException {
                    throw new IOException();
                }
            };
            IOUtils.closeQuietly(closeable);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}