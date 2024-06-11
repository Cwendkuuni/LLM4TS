import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() {
        // Test cases for createCompressorInputStream
    }

    @Test
    public void testCreateCompressorInputStreamWithNameAndStream() {
        // Test cases for createCompressorInputStream with name and stream
    }

    @Test
    public void testCreateCompressorOutputStream() {
        // Test cases for createCompressorOutputStream
    }

    // Additional test cases for potential edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream input = factory.createCompressorInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamNullName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream input = factory.createCompressorInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamNullStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream output = factory.createCompressorOutputStream("gzip", null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamInvalidName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream output = factory.createCompressorOutputStream("invalid", outputStream);
    }

    // Test cases for other public methods in the class

    // ...

}