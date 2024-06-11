import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            assertNotNull(factory.createCompressorInputStream("gz", null));
        } catch (CompressorException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStreamNull() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorInputStream(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Compressor name and stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStreamUnknown() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorInputStream("unknown", null);
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            assertNotNull(factory.createCompressorOutputStream("gz", null));
        } catch (CompressorException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStreamNull() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorOutputStream(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Compressor name and stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStreamUnknown() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorOutputStream("unknown", null);
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(true);
        assertTrue(factory.getDecompressConcatenated());
    }
}