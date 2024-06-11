import static org.junit.Assert.*;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_withNullStream_shouldThrowIllegalArgumentException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testCreateCompressorInputStream_withUnknownCompressor_shouldThrowCompressorException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            InputStream mockInputStream = new ByteArrayInputStream(new byte[0]);
            factory.createCompressorInputStream("unknown", mockInputStream);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Success
        }
    }

    @Test
    public void testCreateCompressorInputStream_withKnownCompressor_shouldCreateInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            InputStream mockInputStream = new ByteArrayInputStream(new byte[0]);
            CompressorInputStream inputStream = factory.createCompressorInputStream(CompressorStreamFactory.XZ, mockInputStream);
            assertNotNull(inputStream);
        } catch (CompressorException e) {
            fail("Unexpected CompressorException");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withNullStream_shouldThrowIllegalArgumentException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorOutputStream("unknown", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withUnknownCompressor_shouldThrowCompressorException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            OutputStream mockOutputStream = new ByteArrayOutputStream();
            factory.createCompressorOutputStream("unknown", mockOutputStream);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Success
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withKnownCompressor_shouldCreateOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            OutputStream mockOutputStream = new ByteArrayOutputStream();
            CompressorOutputStream outputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, mockOutputStream);
            assertNotNull(outputStream);
        } catch (CompressorException e) {
            fail("Unexpected CompressorException");
        }
    }

    @Test
    public void testSetDecompressConcatenated_shouldThrowIllegalStateException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.setDecompressConcatenated(true);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Success
        }
    }

}