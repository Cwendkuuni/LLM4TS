import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() {
        // Test creating a compressor input stream with a null stream
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream(null);
        });

        // Test creating a compressor input stream with an unsupported compressor
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream("unsupported", System.in);
        });

        // Add more test cases as needed
    }

    @Test
    public void testCreateCompressorOutputStream() {
        // Test creating a compressor output stream with a null stream
        assertThrows(IllegalArgumentException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream("bzip2", null);
        });

        // Test creating a compressor output stream with an unsupported compressor
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream("unsupported", System.out);
        });

        // Add more test cases as needed
    }

    // Add more test methods for other public methods in CompressorStreamFactory

}