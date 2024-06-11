import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for the CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating a compressor input stream from a known signature
     */
    @Test
    public void testCreateCompressorInputStreamFromKnownSignature() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(
                "Gzip signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(GzipCompressorInputStream.class.getName(), inputStream);
        Assert.assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
        compressorInputStream.close();
    }

    /**
     * Test creating a compressor input stream from an unknown signature
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromUnknownSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Unknown signature".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("Unknown", inputStream);
    }

    /**
     * Test creating a compressor output stream with a known compressor name
     */
    @Test
    public void testCreateCompressorOutputStreamWithKnownCompressorName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(DeflateCompressorOutputStream.class.getName(), outputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertTrue(outputStream.size() > 0);
    }

    /**
     * Test creating a compressor output stream with an unknown compressor name
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWithUnknownCompressorName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("Unknown", outputStream);
    }

    /**
     * Test creating a decompressor input stream with a known compressor name
     */
    @Test
    public void testCreateDecompressorInputStreamWithKnownCompressorName() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(
                GzipCompressorInputStream.createGzipData());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(GzipCompressorInputStream.class.getName(), inputStream);
        byte[] expectedData = "Decompressed data".getBytes();
        byte[] actualData = new byte[inputStream.available()];
        compressorInputStream.read(actualData);
        compressorInputStream.close();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test creating a decompressor input stream with an unknown compressor name
     */
    @Test(expected = CompressorException.class)
    public void testCreateDecompressorInputStreamWithUnknownCompressorName() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Unknown signature".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("Unknown", inputStream);
    }

    /**
     * Test creating a compressor output stream with the default decompressUntilEOF value
     */
    @Test
    public void testCreateCompressorOutputStreamWithDefaultDecompressUntilEOF() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(
                DeflateCompressorOutputStream.class.getName(), outputStream);
        compressorOutputStream.write("Data to compress".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertTrue(outputStream.size() > 0);
    }

    /**
     * Test creating a compressor output stream with the decompressUntilEOF parameter set to false
     */
    @Test
    public void testCreateCompressorOutputStreamWithDecompressUntilEOFSetToFalse() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory(false)
                .createCompressorOutputStream(DeflateCompressorOutputStream.class.getName(), outputStream);
        compressorOutputStream.write("Data to compress".getBytes(0, 5));
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertEquals(5, outputStream.size());
    }

    /**
     * Test creating a compressor output stream with the decompressUntilEOF parameter set to true
     */
    @Test
    public void testCreateCompressorOutputStreamWithDecompressUntilEOFSetToTrue() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(
                DeflateCompressorOutputStream.class.getName(), outputStream);
        compressorOutputStream.write("Data to compress".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertTrue(outputStream.size() > 5);
    }
}