import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.compressors.CompressorException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class CompressorStreamFactoryTest {
    private CompressorStreamFactory compressorStreamFactory;

    @Before
    public void setUp() {
        compressorStreamFactory = new CompressorStreamFactory();
    }

    @After
    public void tearDown() {
        compressorStreamFactory = null;
    }

    @Test
    public void testCreateCompressorInputStream_Gzip_NormalCase() {
        try {
            InputStream in = getClass().getResourceAsStream("test.gzip");
            InputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gz", in);

            assertNotNull(compressorInputStream);
        } catch (CompressorException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStream_BZip2_NormalCase() {
        try {
            InputStream in = getClass().getResourceAsStream("test.bz2");
            InputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("bzip2", in);

            assertNotNull(compressorInputStream);
        } catch (CompressorException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStream_XZ_NormalCase() {
        try {
            InputStream in = getClass().getResourceAsStream("test.xz");
            InputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("xz", in);

            assertNotNull(compressorInputStream);
        } catch (CompressorException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip_NormalCase() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            OutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("gz", out);

            assertNotNull(compressorOutputStream);
        } catch (CompressorException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_BZip2_NormalCase() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            OutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("bzip2", out);

            assertNotNull(compressorOutputStream);
        } catch (CompressorException | IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_XZ_NormalCase() {
        try {
            OutputStream out = new ByteArrayOutputStream();
            OutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("xz", out);

            assertNotNull(compressorOutputStream);
        } catch (CompressorException | IOException e) {
            fail(e.getMessage());
        }
    }
}