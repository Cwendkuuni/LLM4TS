

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.compress.compressors.compressorstreamfactory.CompressorStreamFactory;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;
    private static final String BZIP2 = "bzip2";
    private static final String GZIP = "gz";
    private static final String PACK200 = "pack200";
    private static final String XZ = "xz";
    private static final String LZMA = "lzma";
    private static final String SNAPPY_FRAMED = "snappy-framed";
    private static final String SNAPPY_RAW = "snappy-raw";
    private static final String Z = "z";
    private static final String DEFLATE = "deflate";
	
    @Before
    public void setup() {
        factory = new CompressorStreamFactory();
    }
	
    @Test
    public void testCreateCompressorInputStreamWithSupportedCompressorName() {
        assertNotNull(factory.createCompressorInputStream(BZIP2));
        assertNotNull(factory.createCompressorInputStream(GZIP));
        assertNotNull(factory.createCompressorInputStream(PACK200));
        assertNotNull(factory.createCompressorInputStream(XZ));
        assertNotNull(factory.createCompressorInputStream(LZMA));
        assertNotNull(factory.createCompressorInputStream(SNAPPY_FRAMED));
        assertNotNull(factory.createCompressorInputStream(SNAPPY_RAW));
        assertNotNull(factory.createCompressorInputStream(Z));
        assertNotNull(factory.createCompressorInputStream(DEFLATE));
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullCompressorName() {
        factory.createCompressorInputStream(null);
    }
	
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithUnknownCompressorName() {
        factory.createCompressorInputStream("unknown");
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullInputStream() {
        factory.createCompressorInputStream(BZIP2, null);
    }
	
    @Test
    public void testCreateCompressorOutputStreamWithSupportedCompressorName() {
        assertNotNull(factory.createCompressorOutputStream(BZIP2));
        assertNotNull(factory.createCompressorOutputStream(GZIP));
        assertNotNull(factory.createCompressorOutputStream(PACK200));
        assertNotNull(factory.createCompressorOutputStream(XZ));
        assertNotNull(factory.createCompressorOutputStream(DEFLATE));
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullCompressorName() {
        factory.createCompressorOutputStream(null);
    }
	
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWithUnknownCompressorName() {
        factory.createCompressorOutputStream("unknown");
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullOutputStream() {
        factory.createCompressorOutputStream(BZIP2, null);
    }
}

