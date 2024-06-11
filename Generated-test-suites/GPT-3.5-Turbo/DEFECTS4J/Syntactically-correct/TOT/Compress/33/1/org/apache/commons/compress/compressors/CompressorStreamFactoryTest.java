import static org.junit.Assert.*;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamForBZip2() {
        try {
            InputStream in = new FileInputStream("test.txt");
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorInputStream cInputStream = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, in);
            
            // typical use case
            assertNotNull(cInputStream);
            
            // edge case
            assertEquals("bzip2", cInputStream.getNestedCompressorName());
            
            // error scenario
            assertNull(factory.createCompressorInputStream("invalidCompressor", in));
            
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (CompressorException e) {
            fail("CompressorException occurred");
        }
    }

    @Test
    public void testCreateCompressorInputStreamForGzip() {
        try {
            InputStream in = new FileInputStream("test.txt");
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorInputStream cInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, in);
            
            // typical use case
            assertNotNull(cInputStream);
            
            // edge case
            assertEquals("gz", cInputStream.getNestedCompressorName());
            
            // error scenario
            assertNull(factory.createCompressorInputStream("invalidCompressor", in));
            
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (CompressorException e) {
            fail("CompressorException occurred");
        }
    }

    @Test
    public void testCreateCompressorInputStreamForPack200() {
        try {
            InputStream in = new FileInputStream("test.txt");
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorInputStream cInputStream = factory.createCompressorInputStream(CompressorStreamFactory.PACK200, in);
            
            // typical use case
            assertNotNull(cInputStream);
            
            // edge case
            assertEquals("pack200", cInputStream.getNestedCompressorName());
            
            // error scenario
            assertNull(factory.createCompressorInputStream("invalidCompressor", in));
            
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (CompressorException e) {
            fail("CompressorException occurred");
        }
    }

    @Test
    public void testCreateCompressorOutputStreamForXZ() {
        try {
            OutputStream out = new FileOutputStream("output.txt");
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorOutputStream cOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, out);
            
            // typical use case
            assertNotNull(cOutputStream);
            
            // edge case
            assertEquals("xz", cOutputStream.getNestedCompressorName());
            
            // error scenario
            assertNull(factory.createCompressorOutputStream("invalidCompressor", out));
            
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (CompressorException e) {
            fail("CompressorException occurred");
        }
    }

    @Test
    public void testCreateCompressorOutputStreamForDeflate() {
        try {
            OutputStream out = new FileOutputStream("output.txt");
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorOutputStream cOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, out);
            
            // typical use case
            assertNotNull(cOutputStream);
            
            // edge case
            assertEquals("deflate", cOutputStream.getNestedCompressorName());
            
            // error scenario
            assertNull(factory.createCompressorOutputStream("invalidCompressor", out));
            
        } catch (IOException e) {
            fail("IOException occurred");
        } catch (CompressorException e) {
            fail("CompressorException occurred");
        }
    }

}