import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBZip2() throws IOException, CompressorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = CompressorStreamFactory.BZIP2;

        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);

        IOUtils.copy(compressorOutputStream, compressorInputStream);

        CompressorInputStream testInputStream = new CompressorStreamFactory().createCompressorInputStream(name, compressorOutputStream);
        byte[] expected = new byte[7];
        int bytesRead = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(bytesRead, expected.length);
        Assert.assertArrayEquals(new byte[]{13, 78, 91, -128, 0, 0, 1}, expected);

        testInputStream.close();
        compressorOutputStream.close();
        compressorInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamGZip() throws IOException, CompressorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = CompressorStreamFactory.GZIP;

        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);

        IOUtils.copy(compressorOutputStream, compressorInputStream);

        CompressorInputStream testInputStream = new CompressorStreamFactory().createCompressorInputStream(name, compressorOutputStream);
        byte[] expected = new byte[3];
        int bytesRead = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(bytesRead, expected.length);
        Assert.assertArrayEquals(new byte[]{31, 139, 8}, expected);

        testInputStream.close();
        compressorOutputStream.close();
        compressorInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamPack200() throws IOException, CompressorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = CompressorStreamFactory.PACK200;

        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);

        IOUtils.copy(compressorOutputStream, compressorInputStream);

        CompressorInputStream testInputStream = new CompressorStreamFactory().createCompressorInputStream(name, compressorOutputStream);
        byte[] expected = new byte[1];
        int bytesRead = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(bytesRead, expected.length);
        Assert.assertEquals(0, expected[0]);

        testInputStream.close();
        compressorOutputStream.close();
        compressorInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamXZ() throws IOException, CompressorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = CompressorStreamFactory.XZ;

        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);

        IOUtils.copy(compressorOutputStream, compressorInputStream);

        CompressorInputStream testInputStream = new CompressorStreamFactory().createCompressorInputStream(name, compressorOutputStream);
        byte[] expected = new byte[4];
        int bytesRead = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(bytesRead, expected.length);
        Assert.assertArrayEquals(new byte[]{0, 0, 37, 7}, expected);

        testInputStream.close();
        compressorOutputStream.close();
        compressorInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamUnknown() throws IOException, CompressorException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = "UNKNOWN";

        try {
            CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);

            IOUtils.copy(compressorOutputStream, compressorInputStream);

            CompressorInputStream testInputStream = new CompressorStreamFactory().createCompressorInputStream(name, compressorOutputStream);
            byte[] expected = new byte[1];
            int bytesRead = testInputStream.read(expected, 0, expected.length);
            Assert.assertEquals(bytesRead, -1);

            testInputStream.close();
            compressorOutputStream.close();
            compressorInputStream.close();
            inputStream.close();
            outputStream.close();
        } catch (CompressorException e) {
            Assert.assertEquals("Compressor: UNKNOWN not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStreamBZip2() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = CompressorStreamFactory.BZIP2;

        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);

        IOUtils.copy(compressorInputStream, compressorOutputStream);

        CompressorInputStream testInputStream = new CompressorStreamFactory().createCompressorInputStream(name, outputStream.toByteArray());
        byte[] expected = new byte[7];
        int bytesRead = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(bytesRead, expected.length);
        Assert.assertArrayEquals(new byte[]{13, 78, 91, -128, 0, 0, 1}, expected);

        testInputStream.close();
        compressorOutputStream.close();
        compressorInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamGZip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = CompressorStreamFactory.GZIP;

        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);

        IOUtils.copy(compressorInputStream, compressorOutputStream);

        CompressorInputStream testInputStream = new CompressorStreamFactory().createCompressorInputStream(name, outputStream.toByteArray());
        byte[] expected = new byte[3];
        int bytesRead = testInputStream.read(expected, 0, expected.length);
        Assert.assertEquals(bytesRead, expected.length);
        Assert.assertArrayEquals(new byte[]{31, 139, 8}, expected);

        testInputStream.close();
        compressorOutputStream.close();
        compressorInputStream.close();
        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamUnknown() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String name = "UNKNOWN";

        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);
            CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, outputStream);
            IOUtils.copy(compressorInputStream, compressorOutputStream);
            compressorOutputStream.close();
            compressorInputStream.close();
            inputStream.close();
            outputStream.close();
        } catch (CompressorException e) {
            Assert.assertEquals("Compressor: UNKNOWN not found.", e.getMessage());
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Assert.assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryConcatenated = new CompressorStreamFactory(true);
        Assert.assertTrue(factoryConcatenated.getDecompressConcatenated());
    }
}