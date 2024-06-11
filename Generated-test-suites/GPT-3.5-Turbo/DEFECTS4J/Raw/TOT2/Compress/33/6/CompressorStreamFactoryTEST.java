import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorException;

public class CompressorStreamFactoryTest {
    
    private CompressorStreamFactory factory;
    
    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }
    
    @After
    public void tearDown() {
        factory = null;
    }
    
    @Test
    public void testCreateCompressorInputStreamWithValidInput() {
        try {
            byte[] data = "Test data".getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            CompressorInputStream inputStream = factory.createCompressorInputStream("gzip", input);
            assertNotNull(inputStream);
        } catch (CompressorException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateCompressorOutputStreamWithValidInput() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            CompressorOutputStream outputStream = factory.createCompressorOutputStream("bzip2", output);
            assertNotNull(outputStream);
        } catch (CompressorException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateCompressorInputStreamWithInvalidInput() {
        try {
            ByteArrayInputStream input = null;
            CompressorInputStream inputStream = factory.createCompressorInputStream("invalid", input);
            fail("Expected CompressorException was not thrown");
        } catch (CompressorException | IOException e) {
            // Expected exception, test passes
        }
    }
    
    @Test
    public void testCreateCompressorOutputStreamWithInvalidInput() {
        try {
            ByteArrayOutputStream output = null;
            CompressorOutputStream outputStream = factory.createCompressorOutputStream("invalid", output);
            fail("Expected CompressorException was not thrown");
        } catch (CompressorException | IOException e) {
            // Expected exception, test passes
        }
    }
    
    @Test
    public void testCreateCompressorInputStreamWithNullInput() {
        try {
            CompressorInputStream inputStream = factory.createCompressorInputStream("gzip", null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (CompressorException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Expected exception, test passes
        }
    }
    
    @Test
    public void testCreateCompressorOutputStreamWithNullInput() {
        try {
            CompressorOutputStream outputStream = factory.createCompressorOutputStream("bzip2", null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (CompressorException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Expected exception, test passes
        }
    }

}