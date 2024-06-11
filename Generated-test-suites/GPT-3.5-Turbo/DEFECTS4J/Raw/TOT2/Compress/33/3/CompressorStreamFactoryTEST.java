import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_withValidInput_shouldReturnInputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            assertNotNull(factory.createCompressorInputStream(new ByteArrayInputStream(new byte[0])));
        } catch (CompressorException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateCompressorInputStream_withInvalidInput_shouldThrowException() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorInputStream(null);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (CompressorException e) {
            fail("Incorrect exception thrown");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withValidInput_shouldReturnOutputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            assertNotNull(factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, new ByteArrayOutputStream()));
        } catch (CompressorException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withInvalidInput_shouldThrowException() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorOutputStream(null, null);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (CompressorException e) {
            fail("Incorrect exception thrown");
        }
    }

    @Test
    public void testDecompressConcatenated_whenSet_shouldReturnTrue() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(true);
        assertTrue(factory.getDecompressConcatenated());
    }

}