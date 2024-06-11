import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_withValidBzip2Stream_returnsBzip2CompressorInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 'B', 'Z', 'h', '9', '1' });
        CompressorInputStream result = factory.createCompressorInputStream(in);
        assertTrue(result instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_withInvalidStream_throwsCompressorException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 'Z', 'I', 'P' });
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorInputStream(in);
        });
    }

    @Test
    public void testCreateCompressorInputStream_withValidBzip2NameAndStream_returnsBzip2CompressorInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 'B', 'Z', 'h', '9', '1' });
        CompressorInputStream result = factory.createCompressorInputStream("bzip2", in);
        assertTrue(result instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_withInvalidNameAndStream_throwsCompressorException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[] { 'Z', 'I', 'P' });
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorInputStream("invalid", in);
        });
    }

    @Test
    public void testCreateCompressorOutputStream_withValidBzip2NameAndStream_returnsBzip2CompressorOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorOutputStream result = factory.createCompressorOutputStream("bzip2", out);
        assertTrue(result instanceof BZip2CompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_withInvalidNameAndStream_throwsCompressorException() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorOutputStream("invalid", out);
        });
    }

    @Test
    public void testGetDecompressConcatenated_withDefaultConstructor_returnsFalse() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        boolean result = factory.getDecompressConcatenated();
        assertFalse(result);
    }

    @Test
    public void testGetDecompressConcatenated_withConstructorParameter_returnsTrue() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean result = factory.getDecompressConcatenated();
        assertTrue(result);
    }

}