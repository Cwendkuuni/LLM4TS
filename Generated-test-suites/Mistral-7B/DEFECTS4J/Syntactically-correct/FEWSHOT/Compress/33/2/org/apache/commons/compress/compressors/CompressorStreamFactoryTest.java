package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_GZIP() throws Exception {
        String name = CompressorStreamFactory.GZIP;
        InputStream inputStream = IOUtils.toInputStream("testData.gz");
        CompressorInputStream compressorInputStream = CompressorStreamFactory.getInstance().createCompressorInputStream(name, inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2() throws Exception {
        String name = CompressorStreamFactory.BZIP2;
        InputStream inputStream = IOUtils.toInputStream("testData.bz2");
        CompressorInputStream compressorInputStream = CompressorStreamFactory.getInstance().createCompressorInputStream(name, inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws Exception {
        String name = CompressorStreamFactory.XZ;
        InputStream inputStream = IOUtils.toInputStream("testData.xz");
        CompressorInputStream compressorInputStream = CompressorStreamFactory.getInstance().createCompressorInputStream(name, inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_PACK200() throws Exception {
        String name = CompressorStreamFactory.PACK200;
        InputStream inputStream = IOUtils.toInputStream("testData.pac");
        CompressorInputStream compressorInputStream = CompressorStreamFactory.getInstance().createCompressorInputStream(name, inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_DEFLATE() throws Exception {
        String name = CompressorStreamFactory.DEFLATE;
        InputStream inputStream = IOUtils.toInputStream("testData.zip");
        CompressorInputStream compressorInputStream = CompressorStreamFactory.getInstance().createCompressorInputStream(name, inputStream);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidName() throws Exception {
        String name = "invalidName";
        InputStream inputStream = IOUtils.toInputStream("testData.gz");
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(false);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(name, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStream_NullInputStream() throws Exception {
        String name = CompressorStreamFactory.GZIP;
        InputStream inputStream = null;
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(false);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(name, inputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws Exception {
        String name = CompressorStreamFactory.GZIP;
        OutputStream outputStream = IOUtils.toOutputStream("output.gz");
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance().createCompressorOutputStream(name, outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws Exception {
        String name = CompressorStreamFactory.BZIP2;
        OutputStream outputStream = IOUtils.toOutputStream("output.bz2");
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance().createCompressorOutputStream(name, outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws Exception {
        String name = CompressorStreamFactory.XZ;
        OutputStream outputStream = IOUtils.toOutputStream("output.xz");
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance().createCompressorOutputStream(name, outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_PACK200() throws Exception {
        String name = CompressorStreamFactory.PACK200;
        OutputStream outputStream = IOUtils.toOutputStream("output.pac");
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance().createCompressorOutputStream(name, outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_DEFLATE() throws Exception {
        String name = CompressorStreamFactory.DEFLATE;
        OutputStream outputStream = IOUtils.toOutputStream("output.zip");
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.getInstance().createCompressorOutputStream(name, outputStream);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean result = factory.getDecompressConcatenated();
        assertTrue(result);
    }
}