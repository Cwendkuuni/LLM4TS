import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZIP2_Success() throws CompressorException, IOException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Pack200CompressorInputStream compressorInputStream = (Pack200CompressorInputStream) factory.createCompressorInputStream("bzip2", inputStream);
        assertNotNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_UnknownCompressor_Exception() throws IOException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorInputStream("unknown", inputStream);
            fail("Exception expected");
        } catch (CompressorException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateCompressorInputStream_NullInput_Exception() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorInputStream("bzip2", null);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (CompressorException e) {
            fail("Incorrect exception type");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP_Success() throws CompressorException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("gz", outputStream);
        assertTrue(outputStream.size() == 0);
    }

    @Test
    public void testCreateCompressorOutputStream_UnknownCompressor_Exception() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorOutputStream("unknown", outputStream);
            fail("Exception expected");
        } catch (CompressorException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateCompressorOutputStream_NullOutput_Exception() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorOutputStream("gz", null);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (CompressorException e) {
            fail("Incorrect exception type");
        }
    }
}