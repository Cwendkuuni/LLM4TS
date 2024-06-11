import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;

    /**
     * Sets up the CompressorStreamFactory before each test case.
     */
    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }

    @Test
    public void testCreateCompressorInputStream_WithValidGzipStream_ReturnsGzipCompressorInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -47, 72, -51, 72, -55, 72, 4, 0, -59, 29, 0, 0, 0});

        // Act
        CompressorInputStream result = factory.createCompressorInputStream("gz", in);

        // Assert
        assertTrue(result instanceof GzipCompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_WithValidBzip2Stream_ReturnsBZip2CompressorInputStream() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{66, 90, 104, 49, -117, 8, 0, 0, 0, 0, 0, 15, 16, 104, 2, 28, 29, 57, 3, 0, 0, 0});

        // Act
        CompressorInputStream result = factory.createCompressorInputStream("bzip2", in);

        // Assert
        assertTrue(result instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_WithUnknownCompressorName_ThrowsCompressorException() {
        // Arrange
        InputStream in = new ByteArrayInputStream(new byte[]{66, 90, 49, 28, 57, 3, 0, 0, 0});

        // Act and Assert
        assertThrows(CompressorException.class, () -> {
            CompressorInputStream result = factory.createCompressorInputStream("unknown", in);
        });
    }

    @Test
    public void testCreateCompressorOutputStream_WithValidGzipCompressorName_ReturnsGzipCompressorOutputStream() {
        // Arrange
        OutputStream out = new ByteArrayOutputStream();

        // Act
        CompressorOutputStream result = factory.createCompressorOutputStream("gz", out);

        // Assert
        assertTrue(result instanceof GzipCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_WithUnknownCompressorName_ThrowsCompressorException() {
        // Arrange
        OutputStream out = new ByteArrayOutputStream();

        // Act and Assert
        assertThrows(CompressorException.class, () -> {
            CompressorOutputStream result = factory.createCompressorOutputStream("unknown", out);
        });
    }
}