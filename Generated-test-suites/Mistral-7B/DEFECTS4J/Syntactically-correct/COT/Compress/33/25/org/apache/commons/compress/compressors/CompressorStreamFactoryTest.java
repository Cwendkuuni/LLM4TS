import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBZip2() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{0x42, 0x5A, 0x68, 0x64, 0x6F, 0x67, 0x7A, 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x00, 0x1A, 0x00, 0x00, 0x00, 0x01, 0x02, 0x00, 0x42});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(BZip2CompressorInputStream.ID, input);
        Assert.assertEquals(BZip2CompressorInputStream.ID, compressorInputStream.getFormat().getId());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamGzip() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{0x47, 0x49, 0x46, 0x38, 0x37, 0x61, 0x32, 0x31, 0x31, 0x33, 0x34, 0x32, 0x01, 0x00, 0x40, 0x14, 0x00, 0x0F, 0x42, 0x5A, 0x68, 0x64, 0x6F, 0x67, 0x7A, 0x50, 0x4B, 0x05, 0x60, 0x82, 0x15, 0x00, 0x02, 0x1C, 0x00, 0x0F, 0x0B, 0x05, 0x08, 0x00, 0x00, 0x02, 0x00, 0x01, 0x0D, 0x1A, 0x00, 0x00, 0x02, 0x00, 0x00, 0x2E, 0x42});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(GzipCompressorInputStream.ID, input);
        Assert.assertEquals(GzipCompressorInputStream.ID, compressorInputStream.getFormat().getId());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamUnknown() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{0x78, 0x6D, 0x6C, 0x63, 0x68, 0x61, 0x72, 0x65, 0x20, 0x4D, 0x6F, 0x64, 0x65, 0x64, 0x69, 0x61, 0x62, 0x6C, 0x65, 0x2E, 0x63, 0x6F, 0x6D, 0x70, 0x65, 0x6E, 0x63, 0x61, 0x6D, 0x65});
        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("unknown", input);
            Assert.fail();
        } catch (CompressorException e) {
            // expected
        }
    }

    @Test
    public void testCreateCompressorInputStreamNullInputStream() throws Exception {
        try {
            new CompressorStreamFactory().createCompressorInputStream(BZip2CompressorInputStream.ID, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateCompressorOutputStreamBZip2() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(BZip2CompressorOutputStream.ID, output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.close();
        Assert.assertEquals(new String(new ByteArrayInputStream(output.toByteArray()).readAllBytes()), "Test\u0000");
    }

    @Test
    public void testCreateCompressorOutputStreamGzip() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(GzipCompressorOutputStream.ID, output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.close();
        Assert.assertEquals(new String(new ByteArrayInputStream(output.toByteArray()).readAllBytes()), "Test\n");
    }

    @Test
    public void testCreateCompressorOutputStreamUnknown() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        try {
            new CompressorStreamFactory().createCompressorOutputStream("unknown", output);
            Assert.fail();
        } catch (CompressorException e) {
            // expected
        }
    }

    @Test
    public void testCreateCompressorOutputStreamNullOutputStream() throws Exception {
        try {
            new CompressorStreamFactory().createCompressorOutputStream(BZip2CompressorOutputStream.ID, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        Assert.assertTrue(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory();
        Assert.assertFalse(factory.getDecompressConcatenated());
    }
}