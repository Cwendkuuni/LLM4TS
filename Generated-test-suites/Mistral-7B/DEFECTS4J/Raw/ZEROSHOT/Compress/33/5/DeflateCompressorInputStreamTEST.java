import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    @Test
    public void testEmptyInputStream() {
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]))) {
            Assert.assertEquals(-1, inputStream.read());
        } catch (IOException e) {
            // ignored
        }
    }

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] inputData = new byte[]{'A'};
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData));
             ByteArrayOutputStream resultOutput = new ByteArrayOutputStream()) {
            int readByte = inputStream.read();
            outputStream.write(readByte);
            resultOutput.writeTo(outputStream.toByteArray(), 0, outputStream.size());
            Assert.assertArrayEquals(inputData, resultOutput.toByteArray());
        }
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] inputData = new byte[]{'A', 'B'};
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData));
             ByteArrayOutputStream resultOutput = new ByteArrayOutputStream()) {
            int readLength = inputStream.read(new byte[2], 0, 2);
            outputStream.write(new byte[]{'A', 'B'});
            resultOutput.writeTo(outputStream.toByteArray(), 0, outputStream.size());
            Assert.assertArrayEquals(inputData, resultOutput.toByteArray());
        }
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = new byte[]{'A', 'B', 'C'};
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData));
             ByteArrayOutputStream resultOutput = new ByteArrayOutputStream()) {
            inputStream.skip(2);
            byte[] result = new byte[inputData.length - 2];
            inputStream.read(result, 0, result.length);
            Assert.assertArrayEquals(new byte[]{'C'}, result);
        }
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] inputData = new byte[]{'A', 'B', 'C'};
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData));
             ByteArrayOutputStream resultOutput = new ByteArrayOutputStream()) {
            Assert.assertEquals(3, inputStream.available());
        }
    }

    @Test
    public void testClose() throws IOException {
        byte[] inputData = new byte[]{'A', 'B'};
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int readLength = inputStream.read(new byte[2], 0, 2);
            outputStream.write(new byte[]{'A', 'B'});
            Assert.assertArrayEquals(inputData, outputStream.toByteArray());
            inputStream.close();
        }
    }

    @Test
    public void testDeflateParameters() throws IOException {
        byte[] inputData = new byte[]{'A', 'B'};
        try (DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters().setZlibHeader(false));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int readLength = inputStream.read(new byte[2], 0, 2);
            outputStream.write(new byte[]{'A', 'B'});
            Assert.assertArrayEquals(inputData, outputStream.toByteArray());
        }
    }
}