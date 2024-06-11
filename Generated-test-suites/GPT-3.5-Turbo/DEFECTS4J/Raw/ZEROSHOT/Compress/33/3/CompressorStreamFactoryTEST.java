import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream inputStream = factory.createCompressorInputStream(in);
        assertNotNull(inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Exception() {
        try {
            InputStream in = null;
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorInputStream inputStream = factory.createCompressorInputStream(in);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected IllegalArgumentException to be thrown");
        }
    }

    @Test
    public void testCreateCompressorInputStream_IOException() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream inputStream = factory.createCompressorInputStream(in);
        assertNotNull(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            fail("Expected IOException to not be thrown");
        }
    }

    @Test
    public void testCreateCompressorOutputStream() {
        OutputStream out = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream outputStream = factory.createCompressorOutputStream("gzip", out);
        assertNotNull(outputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Exception() {
        try {
            OutputStream out = null;
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorOutputStream outputStream = factory.createCompressorOutputStream("gzip", out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Expected IllegalArgumentException to be thrown");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_IOException() {
        OutputStream out = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream outputStream = factory.createCompressorOutputStream("gzip", out);
        assertNotNull(outputStream);
        try {
            outputStream.close();
        } catch (IOException e) {
            fail("Expected IOException to not be thrown");
        }
    }

    @Test
    public void testDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean concatenated = factory.getDecompressConcatenated();
        assertTrue(concatenated);
    }

    @Test
    public void testDecompressConcatenated_Default() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        boolean concatenated = factory.getDecompressConcatenated();
        assertFalse(concatenated);
    }

    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(true);
        boolean concatenated = factory.getDecompressConcatenated();
        assertTrue(concatenated);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenated_Exception() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(true);
    }
}