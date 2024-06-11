import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.CompressorException;

public class CompressorStreamFactoryTest {

    // Test for createCompressorInputStream(InputStream in) method
    @Test
    public void testCreateCompressorInputStreamFromInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        // Testing with null input stream
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(null);
        });
        
        // Testing with unsupported mark
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(in);
        });
        
        // Further tests can be added to cover supported compression algorithms
    }

    // Test for createCompressorInputStream(String name, InputStream in) method
    @Test
    public void testCreateCompressorInputStreamFromNameAndInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        // Testing with null name
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(null, in);
        });
        
        // Testing with null input stream
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream("bzip2", null);
        });
        
        // Testing for unknown compressor
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorInputStream("unknown", in);
        });
        
        // Further tests can be added to cover other scenarios for different compression algorithms
    }

    // Test for createCompressorOutputStream(String name, OutputStream out) method
    @Test
    public void testCreateCompressorOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        
        // Testing with null name
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream(null, out);
        });
        
        // Testing with null output stream
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream("bzip2", null);
        });
        
        // Testing for unknown compressor
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorOutputStream("unknown", out);
        });
        
        // Further tests can be added to cover other scenarios for different compression algorithms
    }

    // Additional tests for edge cases, error scenarios, and typical use cases can be added here

}