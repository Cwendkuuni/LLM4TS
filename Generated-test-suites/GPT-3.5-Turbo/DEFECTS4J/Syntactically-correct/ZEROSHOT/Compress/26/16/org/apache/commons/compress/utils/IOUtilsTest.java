import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    private static final int COPY_BUF_SIZE = 8024;
    private static final int SKIP_BUF_SIZE = 4096;

    @Test
    public void testCopy_DefaultBufferSize() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long count = IOUtils.copy(inputStream, outputStream);

        Assert.assertEquals(data.length, count);
        Assert.assertArrayEquals(data, outputStream.toByteArray());
    }

    @Test
    public void testCopy_CustomBufferSize() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long count = IOUtils.copy(inputStream, outputStream, COPY_BUF_SIZE);

        Assert.assertEquals(data.length, count);
        Assert.assertArrayEquals(data, outputStream.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        long skipped = IOUtils.skip(inputStream, 5);

        Assert.assertEquals(5, skipped);
        Assert.assertEquals(6, inputStream.available());
    }

    @Test
    public void testReadFully_FillBuffer() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        byte[] buffer = new byte[10];

        int bytesRead = IOUtils.readFully(inputStream, buffer);

        Assert.assertEquals(data.length, bytesRead);
        Assert.assertArrayEquals(data, buffer);
    }

    @Test
    public void testReadFully_PartialBuffer() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        byte[] buffer = new byte[5];

        int bytesRead = IOUtils.readFully(inputStream, buffer);

        Assert.assertEquals(buffer.length, bytesRead);
        Assert.assertArrayEquals("Test ".getBytes(), buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        byte[] result = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals(data, result);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);

        IOUtils.closeQuietly(inputStream);

        try {
            inputStream.read();
            Assert.fail("InputStream should be closed");
        } catch (IOException e) {
            Assert.assertNotNull(e);
        }
    }
}