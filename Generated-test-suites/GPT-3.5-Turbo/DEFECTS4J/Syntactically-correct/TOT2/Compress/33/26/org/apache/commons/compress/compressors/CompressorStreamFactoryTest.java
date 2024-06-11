import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CompressorStreamFactoryTest {
    
    private CompressorStreamFactory factory;
    
    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }
    
    @Test
    public void testCreateCompressorInputStream_BZip2() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_Gzip() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.GZIP, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_Pack200() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.PACK200, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_XZ() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.XZ, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_LZMA() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.LZMA, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_SnappyFramed() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_FRAMED, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_SnappyRaw() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_Z() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.Z, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_Deflate() {
        assertNotNull(factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, null));
    }
    
    @Test
    public void testCreateCompressorInputStream_Unsupported() {
        try {
            factory.createCompressorInputStream("Unsupported", null);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // expected
        }
    }
    
    @Test
    public void testCreateCompressorOutputStream_Gzip() {
        assertNotNull(factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, null));
    }
    
    @Test
    public void testCreateCompressorOutputStream_BZip2() {
        assertNotNull(factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, null));
    }
    
    @Test
    public void testCreateCompressorOutputStream_XZ() {
        assertNotNull(factory.createCompressorOutputStream(CompressorStreamFactory.XZ, null));
    }
    
    @Test
    public void testCreateCompressorOutputStream_Pack200() {
        assertNotNull(factory.createCompressorOutputStream(CompressorStreamFactory.PACK200, null));
    }
    
    @Test
    public void testCreateCompressorOutputStream_Deflate() {
        assertNotNull(factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, null));
    }
    
    @Test
    public void testCreateCompressorOutputStream_Unsupported() {
        try {
            factory.createCompressorOutputStream("Unsupported", null);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // expected
        }
    }
}