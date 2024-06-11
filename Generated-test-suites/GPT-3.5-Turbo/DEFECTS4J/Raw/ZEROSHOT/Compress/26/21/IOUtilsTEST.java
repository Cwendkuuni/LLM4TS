import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy_defaultBufferSize() throws IOException {
        // Arrange
        InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };

        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };

        // Act
        long result = IOUtils.copy(input, output);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCopy_customBufferSize() throws IOException {
        // Arrange
        InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };

        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        };

        int bufferSize = 1024;

        // Act
        long result = IOUtils.copy(input, output, bufferSize);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSkip() throws IOException {
        // Arrange
        InputStream input = new InputStream() {
            @Override
            public long skip(long n) throws IOException {
                return 0;
            }
        };

        long numToSkip = 100;

        // Act
        long result = IOUtils.skip(input, numToSkip);

        // Assert
        Assert.assertEquals(numToSkip, result);
    }

    @Test
    public void testReadFully() throws IOException {
        // Arrange
        InputStream input = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return -1;
            }
        };

        byte[] b = new byte[10];
        int offset = 0;
        int len = 10;

        // Act
        int result = IOUtils.readFully(input, b, offset, len);

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testToByteArray() throws IOException {
        // Arrange
        InputStream input = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };

        // Act
        byte[] result = IOUtils.toByteArray(input);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testCloseQuietly() {
        // Arrange
        Closeable c = null;

        // Act
        IOUtils.closeQuietly(c);

        // Assert (no exception is thrown)
    }
}