package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateBZip2CompressorInputStream() {
        try {
            InputStream inputStream = IOUtils.toInputStream("testdata/test.bz2");
            CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
            assertNotNull(compressorInputStream);
            compressorInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateGzipCompressorInputStream() {
        try {
            InputStream inputStream = IOUtils.toInputStream("testdata/test.gz");
            CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
            assertNotNull(compressorInputStream);
            compressorInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateXZCompressorInputStream() {
        try {
            InputStream inputStream = IOUtils.toInputStream("testdata/test.xz");
            CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
            assertNotNull(compressorInputStream);
            compressorInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateCompressorInputStreamWithUnknownFormat() {
        try {
            InputStream inputStream = IOUtils.toInputStream("testdata/test.txt");
            CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("unknown", inputStream);
            assertNull(compressorInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateBZip2CompressorOutputStream() {
        try {
            OutputStream outputStream = IOUtils.toOutputStream("testdata/test.out.bz2");
            CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
            assertNotNull(compressorOutputStream);
            compressorOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateGzipCompressorOutputStream() {
        try {
            OutputStream outputStream = IOUtils.toOutputStream("testdata/test.out.gz");
            CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
            assertNotNull(compressorOutputStream);
            compressorOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateXZCompressorOutputStream() {
        try {
            OutputStream outputStream = IOUtils.toOutputStream("testdata/test.out.xz");
            CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
            assertNotNull(compressorOutputStream);
            compressorOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateCompressorOutputStreamWithUnknownFormat() {
        try {
            OutputStream outputStream = IOUtils.toOutputStream("testdata/test.out");
            CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("unknown", outputStream);
            assertNull(compressorOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean result = factory.getDecompressConcatenated();
        assertTrue(result);
    }
}