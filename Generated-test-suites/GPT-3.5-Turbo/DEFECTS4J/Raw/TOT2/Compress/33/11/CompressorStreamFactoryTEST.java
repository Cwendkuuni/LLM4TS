import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.*;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_withValidStream() throws IOException {
        byte[] data = new byte[]{0x42, 0x5A, 0x68, 0x31};
        InputStream inputStream = new ByteArrayInputStream(data);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);
        assertNotNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_withInvalidStream() {
        InputStream inputStream = null;
        try {
            new CompressorStreamFactory().createCompressorInputStream(inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withValidStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        assertNotNull(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_withInvalidStream() {
        OutputStream outputStream = null;
        try {
            new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_withUnknownStream() {
        byte[] data = new byte[]{0x00, 0x00, 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        try {
            new CompressorStreamFactory().createCompressorInputStream(inputStream);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withUnknownStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            new CompressorStreamFactory().createCompressorOutputStream("invalid", outputStream);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_withNullName() {
        byte[] data = new byte[]{0x00, 0x00, 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        try {
            new CompressorStreamFactory().createCompressorInputStream(null, inputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_withNullName() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            new CompressorStreamFactory().createCompressorOutputStream(null, outputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompressorStreamFactoryConstructor_withDecompressUntilEOF() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(true);
        assertTrue(compressorStreamFactory.getDecompressConcatenated());
    }

}