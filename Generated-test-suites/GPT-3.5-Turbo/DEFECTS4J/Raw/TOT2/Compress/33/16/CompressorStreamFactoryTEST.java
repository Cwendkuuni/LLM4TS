import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            byte[] testData = "Test data for input stream".getBytes();
            InputStream in = new ByteArrayInputStream(testData);
            assertNotNull(factory.createCompressorInputStream(in));
        } catch (CompressorException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream() {
        try {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            assertNotNull(factory.createCompressorOutputStream("bzip2", out));
        } catch (CompressorException e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStreamWithNullName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = new ByteArrayInputStream("Test data for input stream".getBytes());
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(null, in);
        });
    }

    // Add more test cases for edge cases and exception handling scenarios

}