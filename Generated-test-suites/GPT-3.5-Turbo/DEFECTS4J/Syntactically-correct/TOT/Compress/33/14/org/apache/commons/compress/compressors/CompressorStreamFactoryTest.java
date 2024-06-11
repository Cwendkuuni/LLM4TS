import org.junit.Test;
import org.junit.Assert;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_GzipCompression_Success() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new FileInputStream("test.txt");
        
        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
            // Assert statements
            Assert.assertNotNull(compressorInputStream);
        } catch (CompressorException e) {
            Assert.fail("Unexpected exception occurred");
        }
    }
    
    @Test
    public void testCreateCompressorInputStream_UnknownCompression_Fail() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new FileInputStream("test.txt");
        
        try {
            factory.createCompressorInputStream("unknown", input);
            Assert.fail("Expected CompressorException was not thrown");
        } catch (CompressorException e) {
            // Assert statements
            Assert.assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateCompressorOutputStream_DeflateCompression_Success() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new FileOutputStream("test.deflate");

        try {
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
            // Assert statements
            Assert.assertNotNull(compressorOutputStream);
        } catch (CompressorException e) {
            Assert.fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_UnknownCompression_Fail() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new FileOutputStream("test.unknown");

        try {
            factory.createCompressorOutputStream("unknown", output);
            Assert.fail("Expected CompressorException was not thrown");
        } catch (CompressorException e) {
            // Assert statements
            Assert.assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStream_InvalidInputStream_Null() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = null;
        
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Assert statements
            Assert.assertEquals("Stream must not be null.", e.getMessage());
        } catch (CompressorException e) {
            Assert.fail("Unexpected exception occurred");
        }
    }
    
    @Test
    public void testCreateCompressorInputStream_InvalidInputStream_MarkNotSupported() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Assert statements
            Assert.assertEquals("Mark is not supported.", e.getMessage());
        } catch (CompressorException e) {
            Assert.fail("Unexpected exception occurred");
        }
    }
    
    @Test
    public void testCreateCompressorOutputStream_InvalidOutputStream_Null() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = null;

        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Assert statements
            Assert.assertEquals("Compressor name and stream must not be null.", e.getMessage());
        } catch (CompressorException e) {
            Assert.fail("Unexpected exception occurred");
        }
    }
}