import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(5L, bytesCopied);
        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long bytesCopied = IOUtils.copy(inputStream, outputStream, 10);
        Assert.assertEquals(5L, bytesCopied);
        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long numToSkip = 2;
        long skipped = IOUtils.skip(inputStream, numToSkip);
        Assert.assertEquals(3L, skipped);
        byte[] remainingBytes = inputStream.readAllBytes();
        Assert.assertArrayEquals(new byte[]{3, 4, 5}, remainingBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5, 6, 7});
        byte[] buffer = new byte[5];
        int offset = 2;
        int length = 3;
        int bytesRead = IOUtils.readFully(inputStream, buffer, offset, length);
        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(new byte[]{3, 4, 5}, buffer, offset);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] expectedByteArray = new byte[]{1, 2, 3, 4, 5};
        byte[] output = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(expectedByteArray, output);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }
}