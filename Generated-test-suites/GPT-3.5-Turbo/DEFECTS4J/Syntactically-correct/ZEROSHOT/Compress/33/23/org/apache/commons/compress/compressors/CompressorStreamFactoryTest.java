import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_withValidSignature_shouldReturnCompressorInputStream() throws IOException {
        byte[] signature = new byte[] { (byte)0x1F, (byte)0x8B, (byte)0x08 };
        InputStream in = new ByteArrayInputStream(signature);
        CompressorStreamFactory factory = new CompressorStreamFactory();

        CompressorInputStream result = factory.createCompressorInputStream(in);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_withNullInputStream_shouldThrowIllegalArgumentException() throws IOException {
        InputStream in = null;
        CompressorStreamFactory factory = new CompressorStreamFactory();

        factory.createCompressorInputStream(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_withUnsupportedMark_shouldThrowIllegalArgumentException() throws IOException {
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }

            @Override
            public boolean markSupported() {
                return false;
            }
        };
        CompressorStreamFactory factory = new CompressorStreamFactory();

        factory.createCompressorInputStream(in);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_withUnknownSignature_shouldThrowCompressorException() throws IOException {
        byte[] signature = new byte[] { (byte)0x01, (byte)0x02, (byte)0x03 };
        InputStream in = new ByteArrayInputStream(signature);
        CompressorStreamFactory factory = new CompressorStreamFactory();

        factory.createCompressorInputStream(in);
    }

    @Test
    public void testCreateCompressorOutputStream_withValidName_shouldReturnCompressorOutputStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();

        CompressorOutputStream result = factory.createCompressorOutputStream("gzip", out);

        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStream_withNullName_shouldThrowIllegalArgumentException() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        String name = null;
        CompressorStreamFactory factory = new CompressorStreamFactory();

        factory.createCompressorOutputStream(name, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStream_withNullOutputStream_shouldThrowIllegalArgumentException() throws IOException {
        OutputStream out = null;
        String name = "gzip";
        CompressorStreamFactory factory = new CompressorStreamFactory();

        factory.createCompressorOutputStream(name, out);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_withUnknownName_shouldThrowCompressorException() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        String name = "unknown";
        CompressorStreamFactory factory = new CompressorStreamFactory();

        factory.createCompressorOutputStream(name, out);
    }

    @Test
    public void testGetDecompressConcatenated_withDefaultConfiguration_shouldReturnFalse() {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        boolean result = factory.getDecompressConcatenated();

        assertFalse(result);
    }
}