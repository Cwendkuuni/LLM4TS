import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.IOUtils;

public class IOUtilsTest {

    @Test
    public void testCopyInputStreamToOutputStream() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();

        assertEquals(5, bytesCopied);
        assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testSkipInputStreamBytes() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(inputData);

        long bytesSkipped = IOUtils.skip(inputStream, 2);

        assertEquals(2, bytesSkipped);
    }

    @Test
    public void testReadFullyIntoByteArray() throws IOException {
        byte[] sourceArray = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(sourceArray);
        byte[] targetArray = new byte[3];

        int bytesRead = IOUtils.readFully(inputStream, targetArray);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3}, targetArray);
    }

    @Test
    public void testReadFullyIntoOffsetLengthInByteArray() throws IOException {
        byte[] sourceArray = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(sourceArray);
        byte[] targetArray = new byte[5];

        int bytesRead = IOUtils.readFully(inputStream, targetArray, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0}, targetArray);
    }

    @Test
    public void testToByteArrayFromInputStream() throws IOException {
        byte[] sourceArray = {1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(sourceArray);

        byte[] byteArray = IOUtils.toByteArray(inputStream);

        assertArrayEquals(sourceArray, byteArray);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Dummy IOException");
            }
        };

        IOUtils.closeQuietly(closeable);
        // If no exception is thrown, the test passes
    }
}