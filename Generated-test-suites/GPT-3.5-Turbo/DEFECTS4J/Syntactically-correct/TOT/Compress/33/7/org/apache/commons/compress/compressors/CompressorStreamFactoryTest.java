import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZip2() throws Exception {
        // Test typical use case
        InputStream in = new FileInputStream("file.txt");
        CompressorInputStream cis = CompressorStreamFactory.createCompressorInputStream("bzip2", in);
        assertNotNull(cis);
        
        // Test edge case: empty file
        InputStream emptyIn = new FileInputStream("empty.txt");
        CompressorInputStream emptyCis = CompressorStreamFactory.createCompressorInputStream("bzip2", emptyIn);
        assertNotNull(emptyCis);
        
        // Test error scenario: unknown compressor name
        try {
            InputStream in = new FileInputStream("file.txt");
            CompressorInputStream cis = CompressorStreamFactory.createCompressorInputStream("invalid", in);
            fail("CompressorException not thrown");
        } catch (CompressorException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws Exception {
        // Test typical use case
        OutputStream out = new FileOutputStream("file.gz");
        CompressorOutputStream cos = CompressorStreamFactory.createCompressorOutputStream("gz", out);
        assertNotNull(cos);
        
        // Test error scenario: unsupported output stream type
        try {
            OutputStream out = new FileOutputStream("file.txt");
            CompressorOutputStream cos = CompressorStreamFactory.createCompressorOutputStream("gz", out);
            fail("CompressorException not thrown");
        } catch (CompressorException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateCompressorInputStream_AutoDetect() throws Exception {
        // Test typical use case
        InputStream in = new FileInputStream("file.bz2");
        CompressorInputStream cis = CompressorStreamFactory.createCompressorInputStream(in);
        assertNotNull(cis);
        
        // Test edge case: small file
        InputStream smallIn = new FileInputStream("small.txt");
        CompressorInputStream smallCis = CompressorStreamFactory.createCompressorInputStream(smallIn);
        assertNotNull(smallCis);
        
        // Test error scenario: invalid input stream
        try {
            InputStream in = new FileInputStream("invalidFile");
            CompressorInputStream cis = CompressorStreamFactory.createCompressorInputStream(in);
            fail("CompressorException not thrown");
        } catch (CompressorException e) {
            // Expected exception
        }
    }
}