import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Test class for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setup() throws IOException {
        String testData = "This is a test data.";
        inputStream = new ByteArrayInputStream(testData.getBytes());
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateCompressorInputStream_UnknownFormat() throws CompressorException {
        // Given
        InputStream testInputStream = new ByteArrayInputStream("UnknownFormat".getBytes());

        // When
        CompressorFactory compressorFactory = new CompressorStreamFactory();
        CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream("UnknownFormat", testInputStream);

        // Then
        Assert.fail("Expected CompressorException, but no exception was thrown.");
    }

    @Test
    public void testCreateCompressorInputStream_Gzip() throws CompressorException, IOException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory();

        // When
        CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream(GzipCompressorInputStream.IDENTIFIER, inputStream);

        // Then
        Assert.assertSame(GzipCompressorInputStream.class, compressorInputStream.getClass());
        Assert.assertEquals("gzip", compressorInputStream.getName());

        // Clean up
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_BZip2() throws CompressorException, IOException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory();

        // When
        CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream(BZip2CompressorInputStream.IDENTIFIER, inputStream);

        // Then
        Assert.assertSame(BZip2CompressorInputStream.class, compressorInputStream.getClass());
        Assert.assertEquals("bzip2", compressorInputStream.getName());

        // Clean up
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Deflate() throws CompressorException, IOException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory();

        // When
        CompressorInputStream compressorInputStream = compressorFactory.createCompressorInputStream(DeflateCompressorInputStream.IDENTIFIER, inputStream);

        // Then
        Assert.assertSame(DeflateCompressorInputStream.class, compressorInputStream.getClass());
        Assert.assertEquals("deflate", compressorInputStream.getName());

        // Clean up
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_UnknownFormat() throws CompressorException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory();

        // When
        CompressorOutputStream compressorOutputStream = compressorFactory.createCompressorOutputStream("UnknownFormat", outputStream);

        // Then
        Assert.fail("Expected CompressorException, but no exception was thrown.");
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws CompressorException, IOException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory();

        // When
        CompressorOutputStream compressorOutputStream = compressorFactory.createCompressorOutputStream(GzipCompressorOutputStream.IDENTIFIER, outputStream);

        // Then
        Assert.assertSame(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
        Assert.assertEquals("gzip", compressorOutputStream.getName());

        // Use the compressorOutputStream
        compressorFactory.createCompressorOutputStream(GzipCompressorOutputStream.IDENTIFIER, inputStream).transferTo(compressorOutputStream);
        compressorOutputStream.finish();

        // Then
        byte[] expectedBytes = "RïðåQXÓÿýD½vAãxØïðzM½s½sÑáÜx²qîîí³m".getBytes();
        byte[] actualBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedBytes, actualBytes);

        // Clean up
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_BZip2() throws CompressorException, IOException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory();

        // When
        CompressorOutputStream compressorOutputStream = compressorFactory.createCompressorOutputStream(BZip2CompressorOutputStream.IDENTIFIER, outputStream);

        // Then
        Assert.assertSame(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
        Assert.assertEquals("bzip2", compressorOutputStream.getName());

        // Use the compressorOutputStream
        compressorFactory.createCompressorOutputStream(BZip2CompressorOutputStream.IDENTIFIER, inputStream).transferTo(compressorOutputStream);
        compressorOutputStream.finish();

        // Then
        byte[] expectedBytes = new byte[0]; // BZip2 compression results in variable output length
        byte[] actualBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedBytes, actualBytes);

        // Clean up
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Deflate() throws CompressorException, IOException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory();

        // When
        CompressorOutputStream compressorOutputStream = compressorFactory.createCompressorOutputStream(DeflateCompressorOutputStream.IDENTIFIER, outputStream);

        // Then
        Assert.assertSame(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());
        Assert.assertEquals("deflate", compressorOutputStream.getName());

        // Use the compressorOutputStream
        compressorFactory.createCompressorOutputStream(DeflateCompressorOutputStream.IDENTIFIER, inputStream).transferTo(compressorOutputStream);
        compressorOutputStream.finish();

        // Then
        byte[] expectedBytes = "H4sIAAAAAAAAAFgRi8w8Cw0AAAA".getBytes();
        byte[] actualBytes = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedBytes, actualBytes);

        // Clean up
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_DecompressConcatenated() throws CompressorException, IOException {
        // Given
        CompressorFactory compressorFactory = new CompressorStreamFactory(true);

        byte[] testData = "This is a test data.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();

        // When
        compressorFactory.createCompressorOutputStream(DeflateCompressorOutputStream.IDENTIFIER, outputStream1).transferFrom(inputStream);
        compressorFactory.createCompressorOutputStream(DeflateCompressorOutputStream.IDENTIFIER, outputStream2).transferFrom(outputStream1);

        // Then
        byte[] expectedBytes = "H4sIAAAAAAAAAFgRi8w8Bw0AAAA".getBytes();
        byte[] actualBytes1 = outputStream1.toByteArray();
        byte[] actualBytes2 = outputStream2.toByteArray();

        Assert.assertArrayEquals(expectedBytes, actualBytes1);
        Assert.assertArrayEquals(expectedBytes, actualBytes2);

        // Clean up
        inputStream.close();
        outputStream1.close();
        outputStream2.close();
    }
}