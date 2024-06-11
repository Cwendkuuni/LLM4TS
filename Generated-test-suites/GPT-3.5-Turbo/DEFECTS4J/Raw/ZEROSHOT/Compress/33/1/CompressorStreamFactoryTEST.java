import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_validCompressorName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorInputStream gzipInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, null);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_nullName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(null, null);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_nullInputStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, null);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testCreateCompressorInputStream_unknownName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream("unknown", null);
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_validCompressorName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorOutputStream gzipOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStream_nullName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(null, null);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStream_nullOutputStream() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_unknownName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("unknown", null);
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception occurred");
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        boolean decompressConcatenated = factory.getDecompressConcatenated();
        assertFalse(decompressConcatenated);
    }

}