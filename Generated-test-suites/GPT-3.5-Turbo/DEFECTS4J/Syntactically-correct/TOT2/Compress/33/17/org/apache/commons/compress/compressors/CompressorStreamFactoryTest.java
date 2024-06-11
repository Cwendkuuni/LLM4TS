import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_unknownCompressor() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream("unknown", in);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_nullName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream(null, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_unknownCompressor() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream("unknown", out);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_nullName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(null, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_validName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        factory.createCompressorInputStream("bzip2", in);
    }

    @Test
    public void testCreateCompressorOutputStream_validName() throws CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("bzip2", out);
    }

    @Test
    public void testCreateCompressorInputStream_nullStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorInputStream("bzip2", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (CompressorException e) {
            fail("Unexpected CompressorException");
        }
    }

    @Test
    public void testCreateCompressorOutputStream_nullStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorOutputStream("bzip2", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (CompressorException e) {
            fail("Unexpected CompressorException");
        }
    }

}