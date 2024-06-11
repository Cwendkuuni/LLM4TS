import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZip2() throws CompressorException, IOException {
        byte[] data = { 66, 90, 104, 18, 24, 52 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream input = factory.createCompressorInputStream("bzip2", in);
        int byteRead = input.read();
        assertEquals(66, byteRead);
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws CompressorException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream output = factory.createCompressorOutputStream("xz", out);
        output.write(65);
        output.close();
        byte[] compressedData = out.toByteArray();
        assertEquals(65, compressedData[0]);
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() throws IOException {
        byte[] data = { 1, 2, 3 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorInputStream("unknown", in);
            fail("Expected CompressorException was not thrown");
        } catch (CompressorException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateCompressorOutputStream_NullName() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorOutputStream(null, out);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        } catch (CompressorException e) {
            fail("Unexpected CompressorException thrown");
        }
    }

    @Test
    public void testDecompressConcatenatedSetter() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(true);
        assertTrue(factory.getDecompressConcatenated());
    }
}