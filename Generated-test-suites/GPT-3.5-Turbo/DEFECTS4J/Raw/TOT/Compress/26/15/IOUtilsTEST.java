import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy_DefaultBufferSize_InputStream_OutputStream() throws IOException {
        // Arrange
        byte[] testData = new byte[100];
        for (int i = 0; i < 100; i++) {
            testData[i] = (byte) (i % 256);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        long actualCount = IOUtils.copy(inputStream, outputStream);

        // Assert
        byte[] actualData = outputStream.toByteArray();
        Assert.assertArrayEquals(testData, actualData);
        Assert.assertEquals(100, actualCount);
    }

    @Test
    public void testCopy_CustomBufferSize_InputStream_OutputStream() throws IOException {
        // Arrange
        byte[] testData = new byte[100];
        for (int i = 0; i < 100; i++) {
            testData[i] = (byte) (i % 256);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;

        // Act
        long actualCount = IOUtils.copy(inputStream, outputStream, bufferSize);

        // Assert
        byte[] actualData = outputStream.toByteArray();
        Assert.assertArrayEquals(testData, actualData);
        Assert.assertEquals(100, actualCount);
    }

    @Test
    public void testSkip_Input_Stream() throws IOException {
        // Arrange
        byte[] testData = new byte[100];
        for (int i = 0; i < 100; i++) {
            testData[i] = (byte) (i % 256);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        // Act
        long actualSkipped = IOUtils.skip(inputStream, 50);

        // Assert
        Assert.assertEquals(50, actualSkipped);
    }

    @Test
    public void testReadFully_Input_Buffer() throws IOException {
        // Arrange
        byte[] testData = new byte[100];
        for (int i = 0; i < 100; i++) {
            testData[i] = (byte) (i % 256);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        byte[] buffer = new byte[50];

        // Act
        int actualCount = IOUtils.readFully(inputStream, buffer);

        // Assert
        Assert.assertArrayEquals(testData, buffer);
        Assert.assertEquals(50, actualCount);
    }

    @Test
    public void testReadFully_Input_Buffer_Offset_Length() throws IOException {
        // Arrange
        byte[] testData = new byte[100];
        for (int i = 0; i < 100; i++) {
            testData[i] = (byte) (i % 256);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        byte[] buffer = new byte[50];

        // Act
        int actualCount = IOUtils.readFully(inputStream, buffer, 10, 30);

        // Assert
        byte[] expectedData = new byte[50];
        System.arraycopy(testData, 0, expectedData, 10, 30);
        Assert.assertArrayEquals(expectedData, buffer);
        Assert.assertEquals(30, actualCount);
    }

    @Test
    public void testToByteArray_Input_Stream() throws IOException {
        // Arrange
        byte[] testData = new byte[100];
        for (int i = 0; i < 100; i++) {
            testData[i] = (byte) (i % 256);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);

        // Act
        byte[] actualData = IOUtils.toByteArray(inputStream);

        // Assert
        Assert.assertArrayEquals(testData, actualData);
    }

    @Test
    public void testCloseQuietly_NullInput() {
        // Arrange
        Closeable c = null;

        // Act
        IOUtils.closeQuietly(c);
    }

    @Test
    public void testCloseQuietly_Closeable() {
        // Arrange
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Error closing Closeable");
            }
        };

        // Act
        IOUtils.closeQuietly(c);
    }
}