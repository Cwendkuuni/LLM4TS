import static org.junit.Assert.*;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void createCompressorInputStreamTest() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = new InputStream();

        // Test for BZIP2 compression algorithm
        CompressorInputStream bzip2In = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, in);
        assertNotNull(bzip2In);
        assertTrue(bzip2In instanceof BZip2CompressorInputStream);

        // Test for GZIP compression algorithm
        CompressorInputStream gzipIn = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, in);
        assertNotNull(gzipIn);
        assertTrue(gzipIn instanceof GzipCompressorInputStream);

        // Test for PACK200 compression algorithm
        CompressorInputStream pack200In = factory.createCompressorInputStream(CompressorStreamFactory.PACK200, in);
        assertNotNull(pack200In);
        assertTrue(pack200In instanceof Pack200CompressorInputStream);

        // Test for XZ compression algorithm
        CompressorInputStream xzIn = factory.createCompressorInputStream(CompressorStreamFactory.XZ, in);
        assertNotNull(xzIn);
        assertTrue(xzIn instanceof XZCompressorInputStream);

        // Test for LZMA compression algorithm
        CompressorInputStream lzmaIn = factory.createCompressorInputStream(CompressorStreamFactory.LZMA, in);
        assertNotNull(lzmaIn);
        assertTrue(lzmaIn instanceof LZMACompressorInputStream);

        // Test for SNAPPY_FRAMED compression algorithm
        CompressorInputStream snappyFramedIn = factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_FRAMED, in);
        assertNotNull(snappyFramedIn);
        assertTrue(snappyFramedIn instanceof FramedSnappyCompressorInputStream);

        // Test for SNAPPY_RAW compression algorithm
        CompressorInputStream snappyRawIn = factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, in);
        assertNotNull(snappyRawIn);
        assertTrue(snappyRawIn instanceof SnappyCompressorInputStream);

        // Test for Z compression algorithm
        CompressorInputStream zIn = factory.createCompressorInputStream(CompressorStreamFactory.Z, in);
        assertNotNull(zIn);
        assertTrue(zIn instanceof ZCompressorInputStream);

        // Test for DEFLATE compression algorithm
        CompressorInputStream deflateIn = factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, in);
        assertNotNull(deflateIn);
        assertTrue(deflateIn instanceof DeflateCompressorInputStream);
    }

    @Test
    public void createCompressorOutputStreamTest() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream out = new OutputStream();

        // Test for GZIP compression algorithm
        CompressorOutputStream gzipOut = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
        assertNotNull(gzipOut);
        assertTrue(gzipOut instanceof GzipCompressorOutputStream);

        // Test for BZIP2 compression algorithm
        CompressorOutputStream bzip2Out = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
        assertNotNull(bzip2Out);
        assertTrue(bzip2Out instanceof BZip2CompressorOutputStream);

        // Test for XZ compression algorithm
        CompressorOutputStream xzOut = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, out);
        assertNotNull(xzOut);
        assertTrue(xzOut instanceof XZCompressorOutputStream);

        // Test for PACK200 compression algorithm
        CompressorOutputStream pack200Out = factory.createCompressorOutputStream(CompressorStreamFactory.PACK200, out);
        assertNotNull(pack200Out);
        assertTrue(pack200Out instanceof Pack200CompressorOutputStream);

        // Test for DEFLATE compression algorithm
        CompressorOutputStream deflateOut = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, out);
        assertNotNull(deflateOut);
        assertTrue(deflateOut instanceof DeflateCompressorOutputStream);
    }
}