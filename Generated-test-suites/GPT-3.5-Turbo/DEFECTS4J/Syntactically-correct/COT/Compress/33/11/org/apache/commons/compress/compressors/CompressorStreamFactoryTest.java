import org.junit.*;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInputStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_InputstreamDoesNotSupportMark() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullCompressorName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(null, new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInputStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("gzip", null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownCompressorName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStream_NullCompressorName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStream_NullOutputStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("gzip", null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnknownCompressorName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("unknown", new ByteArrayOutputStream());
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }
}