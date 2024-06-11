import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test reading data from DeflateCompressorInputStream.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testReadData() throws IOException {
        // Given
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream decompressorInputStream = new DeflateCompressorInputStream(inputStream);

        // When
        byte[] buffer = new byte[testData.length()];
        int bytesRead = decompressorInputStream.read(buffer);

        // Then
        Assert.assertEquals(testData.length(), bytesRead);
        Assert.assertArrayEquals(testData.getBytes(), buffer);

        // Clean up
        decompressorInputStream.close();
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Test reading data from DeflateCompressorInputStream with empty input.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testReadDataEmpty() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream decompressorInputStream = new DeflateCompressorInputStream(inputStream);

        // When
        byte[] buffer = new byte[1];
        int bytesRead = decompressorInputStream.read(buffer);

        // Then
        Assert.assertEquals(-1, bytesRead);

        // Clean up
        decompressorInputStream.close();
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Test reading data from DeflateCompressorInputStream with zlib header.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testReadDataWithZlibHeader() throws IOException {
        // Given
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        byte[] zlibHeader = new byte[]{0x78, 0x5E, 0x42, 0x69, 0x66}; // zlib header
        InputStream inputStream = new ByteArrayInputStream(zlibHeader.concat(testData.getBytes()));
        DeflateCompressorInputStream decompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        // When
        byte[] buffer = new byte[testData.length() + zlibHeader.length];
        int bytesRead = decompressorInputStream.read(buffer);

        // Then
        Assert.assertEquals(testData.length() + zlibHeader.length, bytesRead);
        Assert.assertArrayEquals(testData.getBytes().concat(zlibHeader), buffer, 0, testData.length() + zlibHeader.length);

        // Clean up
        decompressorInputStream.close();
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Test reading data from DeflateCompressorInputStream with incorrect zlib header.
     *
     * @throws IOException when an error occurs
     */
    @Test(expected = IOException.class)
    public void testReadDataWithIncorrectZlibHeader() throws IOException {
        // Given
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        byte[] zlibHeader = new byte[]{0x78, 0x5E, 0x43, 0x69, 0x66}; // incorrect zlib header
        InputStream inputStream = new ByteArrayInputStream(zlibHeader.concat(testData.getBytes()));
        DeflateCompressorInputStream decompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        // When
        byte[] buffer = new byte[testData.length() + zlibHeader.length];
        int bytesRead = decompressorInputStream.read(buffer);

        // Then - expected failure as it should throw an IOException

        // Clean up
        IOUtils.closeQuietly(decompressorInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    /**
     * Test closing DeflateCompressorInputStream.
     *
     * @throws IOException when an error occurs
     */
    @Test
    public void testClose() throws IOException {
        // Given
        String testData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        DeflateCompressorInputStream decompressorInputStream = new DeflateCompressorInputStream(inputStream);

        // When
        decompressorInputStream.close();

        // Then
        // The close method should not return a value, so we cannot really test it.
        // However, we can check that the underlying input stream was closed.
        Assert.assertFalse(inputStream.isOpen());

        // Clean up
        IOUtils.closeQuietly(decompressorInputStream);
        IOUtils.closeQuietly(inputStream);
    }
}