import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBZIP2() throws Exception {
        InputStream input = new FileInputStream("test.bz2");
        CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        byte[] expected = new byte[0]; // Add expected byte array for expected data if known
        Assert.assertArrayEquals("Invalid compressed data", expected, actual);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamGZIP() throws Exception {
        InputStream input = new FileInputStream("test.gz");
        CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        byte[] expected = new byte[0]; // Add expected byte array for expected data if known
        Assert.assertArrayEquals("Invalid compressed data", expected, actual);
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnknown() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        try {
            new CompressorStreamFactory().createCompressorInputStream("unknown", input);
        } catch (CompressorException e) {
            Assert.assertEquals("Unknown compressor: unknown", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testCreateCompressorOutputStreamBZIP2() throws Exception {
        OutputStream output = new FileOutputStream("test.bz2");
        CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        IOUtils.copy(input, compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertEquals("Test data", IOUtils.toString(new FileInputStream("test.bz2")));
    }

    @Test
    public void testCreateCompressorOutputStreamGZIP() throws Exception {
        OutputStream output = new FileOutputStream("test.gz");
        CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        InputStream input = new ByteArrayInputStream("Test data".getBytes());
        IOUtils.copy(input, compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
        Assert.assertEquals("Test data", IOUtils.toString(new GzipCompressorInputStream(new FileInputStream("test.gz"))));
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnknown() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        try {
            new CompressorStreamFactory().createCompressorOutputStream("unknown", output);
        } catch (CompressorException e) {
            Assert.assertEquals("Unknown compressor: unknown", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testDecompressConcatenated() throws Exception {
        InputStream input = new FileInputStream("test.bz2");
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        CompressorInputStream compressorInputStream =
                factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        byte[] expected = new byte[0]; // Add expected byte array for expected data if known
        Assert.assertArrayEquals("Invalid compressed data", expected, actual);
        compressorInputStream.close();
    }

    @Test
    public void testDecompressSingleStream() throws Exception {
        InputStream input = new FileInputStream("test.bz2");
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream compressorInputStream =
                factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        byte[] actual = IOUtils.toByteArray(compressorInputStream);
        byte[] expected = new byte[0]; // Add expected byte array for expected data if known
        Assert.assertArrayEquals("Invalid compressed data", expected, actual);
        compressorInputStream.close();
    }
}