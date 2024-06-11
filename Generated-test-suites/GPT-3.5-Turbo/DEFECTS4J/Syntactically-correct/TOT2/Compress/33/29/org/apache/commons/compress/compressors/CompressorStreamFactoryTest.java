import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamWithValidInput() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[100]);
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
            assertNotNull(compressorInputStream);
        } catch (CompressorException | IOException e) {
            fail("Exception caught while creating CompressorInputStream");
        }
    }

    @Test
    public void testCreateCompressorInputStreamWithInvalidInput() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorInputStream(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (CompressorException | IOException e) {
            fail("Unexpected exception caught while creating CompressorInputStream");
        }
    }

    @Test
    public void testCreateCompressorOutputStreamWithValidInput() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
            assertNotNull(compressorOutputStream);
        } catch (CompressorException | IOException e) {
            fail("Exception caught while creating CompressorOutputStream");
        }
    }

    @Test
    public void testCreateCompressorOutputStreamWithInvalidInput() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            factory.createCompressorOutputStream(null, null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (CompressorException | IOException e) {
            fail("Unexpected exception caught while creating CompressorOutputStream");
        }
    }

    @Test
    public void testDecompressConcatenatedOption() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
        factory.setDecompressConcatenated(true);
        assertTrue(factory.getDecompressConcatenated());
    }
}