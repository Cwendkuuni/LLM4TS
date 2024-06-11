import static org.junit.Assert.*;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        // Test valid input stream
        try {
            InputStream in = new FileInputStream("test.txt");
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(in);
            assertNotNull(compressorInputStream);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        } catch (CompressorException e) {
            fail("Compressor exception");
        }
        // Test null input stream
        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (CompressorException e) {
            fail("Compressor exception occurred");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testCreateCompressorInputStreamWithName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        // Test valid input stream and valid name
        try {
            InputStream in = new FileInputStream("test.txt");
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gz", in);
            assertNotNull(compressorInputStream);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        } catch (CompressorException e) {
            fail("Compressor exception");
        }
        // Test null input stream
        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gz", null);
            fail("Expected IllegalArgumentException");
        } catch (CompressorException e) {
            fail("Compressor exception occurred");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        // Test null name
        try {
            InputStream in = new FileInputStream("test.txt");
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(null, in);
            fail("Expected IllegalArgumentException");
        } catch (CompressorException e) {
            fail("Compressor exception occurred");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testCreateCompressorOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        // Test valid output stream and valid name
        try {
            OutputStream out = new FileOutputStream("test.txt");
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gz", out);
            assertNotNull(compressorOutputStream);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        } catch (CompressorException e) {
            fail("Compressor exception");
        }
        // Test null output stream
        try {
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gz", null);
            fail("Expected IllegalArgumentException");
        } catch (CompressorException e) {
            fail("Compressor exception occurred");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        // Test null name
        try {
            OutputStream out = new FileOutputStream("test.txt");
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(null, out);
            fail("Expected IllegalArgumentException");
        } catch (CompressorException e) {
            fail("Compressor exception occurred");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertEquals(false, factory.getDecompressConcatenated());
    }
}