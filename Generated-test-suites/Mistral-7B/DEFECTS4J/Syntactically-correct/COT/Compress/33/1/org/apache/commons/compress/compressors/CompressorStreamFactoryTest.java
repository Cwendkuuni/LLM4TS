import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.ZipFile;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;

    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(factory);
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.TEST_SIGNATURE.getData());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", inputStream);
        assertNotNull(compressorInputStream);
        assertEquals("bzip2", compressorInputStream.getName());
        IOUtils.closeQuietly(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownFormat() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        factory.createCompressorInputStream("foo", inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_GZIP() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.TEST_SIGNATURE.getData());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gzip", inputStream);
        assertNotNull(compressorInputStream);
        assertEquals("gzip", compressorInputStream.getName());
        IOUtils.closeQuietly(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Pack200() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Pack200CompressorInputStream.TEST_SIGNATURE.getData());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("pack200", inputStream);
        assertNotNull(compressorInputStream);
        assertEquals("pack200", compressorInputStream.getName());
        IOUtils.closeQuietly(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(XZCompressorInputStream.TEST_SIGNATURE.getData());
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("xz", inputStream);
        assertNotNull(compressorInputStream);
        assertEquals("xz", compressorInputStream.getName());
        IOUtils.closeQuietly(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        factory.createCompressorInputStream("bzip2", inputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("bzip2", outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals("bzip2", compressorOutputStream.getName());
        IOUtils.closeQuietly(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals("gzip", compressorOutputStream.getName());
        IOUtils.closeQuietly(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Pack200() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("pack200", outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals("pack200", compressorOutputStream.getName());
        IOUtils.closeQuietly(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("xz", outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals("xz", compressorOutputStream.getName());
        IOUtils.closeQuietly(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_DEFLATE() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("deflate", outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals("deflate", compressorOutputStream.getName());
        IOUtils.closeQuietly(compressorOutputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_InvalidName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("foo", outputStream);
    }

    @Test
    public void testDecompressConcatenated() {
        boolean decompressConcatenated = factory.getDecompressConcatenated();
        assertEquals(false, decompressConcatenated);

        factory = new CompressorStreamFactory(true);
        decompressConcatenated = factory.getDecompressConcatenated();
        assertEquals(true, decompressConcatenated);
    }
}