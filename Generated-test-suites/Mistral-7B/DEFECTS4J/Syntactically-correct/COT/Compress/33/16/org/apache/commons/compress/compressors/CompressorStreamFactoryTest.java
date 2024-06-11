import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() {
        InputStream input = IOUtils.toInputStream("Gzip signature");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(GzipStreamFactory.GZIP, input);
        assertEquals("GzipCompressorInputStream", compressorInputStream.getClass().getSimpleName());
        assertNull(compressorInputStream.getDecompressor());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() {
        InputStream input = IOUtils.toInputStream("Bzip2 signature");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertEquals("BZip2CompressorInputStream", compressorInputStream.getClass().getSimpleName());
        assertNull(compressorInputStream.getDecompressor());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_XZ() {
        InputStream input = IOUtils.toInputStream("XZ signature");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertEquals("XZCompressorInputStream", compressorInputStream.getClass().getSimpleName());
        assertNull(compressorInputStream.getDecompressor());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Pack200() {
        InputStream input = IOUtils.toInputStream("Pack200 signature");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.PACK200, input);
        assertEquals("Pack200CompressorInputStream", compressorInputStream.getClass().getSimpleName());
        assertNull(compressorInputStream.getDecompressor());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() {
        InputStream input = IOUtils.toInputStream("Unknown signature");
        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("Unknown", input);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_NullInputStream() {
        try {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_InputDoesNotSupportMark() {
        InputStream input = IOUtils.toInputStream("Input does not support mark");
        try {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_WrongSignatureLength() {
        InputStream input = IOUtils.toInputStream("Wrong signature length");
        try {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_AutoDetection() {
        InputStream input = IOUtils.toInputStream("Auto-detect signature");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(null, input);
        // The actual compressor type is not important for this test
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownCompressorName() {
        InputStream input = IOUtils.toInputStream("Unknown compressor name signature");
        new CompressorStreamFactory().createCompressorInputStream("Unknown", input);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() {
        OutputStream output = IOUtils.toOutputStream("Gzip signature");
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(GzipStreamFactory.GZIP, output);
        assertEquals("GzipCompressorOutputStream", compressorOutputStream.getClass().getSimpleName());
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    // Add test cases for other compressors here
}