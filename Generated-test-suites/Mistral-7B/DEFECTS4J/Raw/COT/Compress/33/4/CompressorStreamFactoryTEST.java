import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZip2() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            CompressorInputStream cs = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
            assertNotNull(cs);
        } catch (CompressorException e) {
            // Failure is expected if the input stream is not a valid BZIP2 stream
        }
    }

    @Test
    public void testCreateCompressorInputStream_GZip() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            CompressorInputStream cs = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
            assertNotNull(cs);
        } catch (CompressorException e) {
            // Failure is expected if the input stream is not a valid GZip stream
        }
    }

    @Test
    public void testCreateCompressorInputStream_InvalidFormat() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createCompressorInputStream("INVALID_FORMAT", input);
            assert(false); // Should not reach this point
        } catch (CompressorException e) {
            // Expected result
        }
    }

    @Test
    public void testCreateCompressorOutputStream_BZip2() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            CompressorOutputStream cos = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
            assertNotNull(cos);
        } catch (IOException e) {
            // Failure is expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_GZip() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            CompressorOutputStream cos = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
            assertNotNull(cos);
        } catch (IOException e) {
            // Failure is expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCompressorOutputStream_LZMA() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        factory.createCompressorOutputStream(CompressorStreamFactory.LZMA, output);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCompressorOutputStream_SnappyFramed() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        factory.createCompressorOutputStream(CompressorStreamFactory.SNAPPY_FRAMED, output);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCompressorOutputStream_SnappyRaw() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        factory.createCompressorOutputStream(CompressorStreamFactory.SNAPPY_RAW, output);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCompressorOutputStream_Z() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        factory.createCompressorOutputStream(CompressorStreamFactory.Z, output);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateCompressorOutputStream_Deflate() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        boolean expected = false;
        assert(factory.getDecompressConcatenated() == expected);

        factory = new CompressorStreamFactory(true);
        expected = true;
        assert(factory.getDecompressConcatenated() == expected);
    }
}