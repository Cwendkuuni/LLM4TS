import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_GZIP() throws IOException, CompressorException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String name = CompressorStreamFactory.GZIP;
        OutputStream out = outputStream;
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, out);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, inputStream);

        // Then
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
        compressorOutputStream.finish();
        out.close();
        Assert.assertEquals("GZIP signature", CompressorStreamFactory.GZIP, IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2() throws IOException, CompressorException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String name = CompressorStreamFactory.BZIP2;
        OutputStream out = outputStream;
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, out);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, inputStream);

        // Then
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
        compressorOutputStream.finish();
        out.close();
        Assert.assertEquals("BZIP2 signature", CompressorStreamFactory.BZIP2, IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray())));
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws IOException, CompressorException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String name = CompressorStreamFactory.XZ;
        OutputStream out = outputStream;
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, out);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, inputStream);

        // Then
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
        compressorOutputStream.finish();
        out.close();
        // XZ signature testing is not possible in JUnit, as there is no way to check for XZ signature in a test
    }

    @Test
    public void testCreateCompressorInputStream_UnsupportedFormat() throws IOException, CompressorException {
        // Given
        String name = "UnsupportedFormat";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        // When & Then
        try {
            new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            Assert.assertEquals("Unsupported format name: UnsupportedFormat", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String name = CompressorStreamFactory.GZIP;
        InputStream in = inputStream;
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, in);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);

        // Then
        Assert.assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        outputStream.close();
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String name = CompressorStreamFactory.BZIP2;
        InputStream in = inputStream;
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, in);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);

        // Then
        Assert.assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        outputStream.close();
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String name = CompressorStreamFactory.XZ;
        InputStream in = inputStream;
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, in);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);

        // Then
        Assert.assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        outputStream.close();
        Assert.assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_UnsupportedFormat() throws IOException {
        // Given
        String name = "UnsupportedFormat";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(name, inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        try {
            CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
            Assert.fail("Expected CompressorException");
        } catch (CompressorException e) {
            Assert.assertEquals("Unsupported format name: UnsupportedFormat", e.getMessage());
        }

        // Then
        compressorInputStream.close();
    }
}