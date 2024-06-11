import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
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
     * Test reading from the input stream.
     */
    @Test
    public void testRead() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[data.length];
        int bytesRead = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(data.length, bytesRead);
        Assert.assertArrayEquals(data, buffer);

        testStream.close();
    }

    /**
     * Test reading from the input stream with a small buffer.
     */
    @Test
    public void testReadWithSmallBuffer() throws IOException {
        byte[] data = "Lorem ipsum dolor".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[2];
        int bytesRead = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(2, bytesRead);
        Assert.assertArrayEquals(new byte[] {'L', 'o'}, buffer);

        testStream.close();
    }

    /**
     * Test reading an empty input stream.
     */
    @Test
    public void testReadEmpty() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        int bytesRead = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, bytesRead);
        testStream.close();
    }

    /**
     * Test reading from a defective input stream.
     */
    @Test(expected = IOException.class)
    public void testReadDefective() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[1]) {
            @Override
            public int read() throws IOException {
                throw new IOException("Defective input stream");
            }
        };
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        testStream.read(buffer, 0, buffer.length);
        testStream.close();
    }

    /**
     * Test reading with a larger buffer.
     */
    @Test
    public void testReadWithLargeBuffer() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[data.length * 2];
        int bytesRead = testStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(data.length, bytesRead);
        testStream.close();
    }

    /**
     * Test skipping bytes from the input stream.
     */
    @Test
    public void testSkip() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        long skipped = testStream.skip(data.length - 1);
        Assert.assertEquals(data.length - 1, skipped);

        testStream.close();
    }

    /**
     * Test skipping to the end of the input stream.
     */
    @Test
    public void testSkipToEnd() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        long skipped = testStream.skip(data.length);
        Assert.assertEquals(0, skipped);

        testStream.close();
    }

    /**
     * Test skipping more bytes than available.
     */
    @Test(expected = IOException.class)
    public void testSkipMoreThanAvailable() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        testStream.skip(data.length + 1);
    }

    /**
     * Test availability of bytes in the input stream.
     */
    @Test
    public void testAvailable() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        int available = testStream.available();
        Assert.assertEquals(data.length, available);

        testStream.close();
    }

    /**
     * Test closing the input stream.
     */
    @Test
    public void testClose() throws IOException {
        byte[] data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        testStream.close();

        try {
            testStream.read();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test compressing and decompressing a file.
     */
    @Test
    public void testCompressAndDecompress() throws IOException {
        String original = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
        String compressed = IOUtils.toString(getClass().getResourceAsStream("/test-data/lorem-ipsum.txt"), "UTF-8");

        InputStream inputStream = new ByteArrayInputStream(original.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DeflateCompressorInputStream compressor = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[1024];

        int len;
        while ((len = compressor.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        compressor.close();
        outputStream.close();

        InputStream testStream = new ByteArrayInputStream(outputStream.toByteArray());
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

        byte[] resultBuffer = new byte[1024];
        while ((len = testStream.read(resultBuffer, 0, resultBuffer.length)) > 0) {
            resultStream.write(resultBuffer, 0, len);
        }

        testStream.close();
        resultStream.close();

        Assert.assertEquals(original, new String(resultStream.toByteArray(), "UTF-8"));
    }
}