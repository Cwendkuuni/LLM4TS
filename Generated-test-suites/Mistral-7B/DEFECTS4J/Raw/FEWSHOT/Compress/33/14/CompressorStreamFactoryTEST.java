package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBZip2() {
        final InputStream inputStream = IOUtils.toInputStream("testdata/test.bz2");
        CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressedInputStream);
        compressedInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamGZip() {
        final InputStream inputStream = IOUtils.toInputStream("testdata/test.gz");
        CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressedInputStream);
        compressedInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamXZ() {
        final InputStream inputStream = IOUtils.toInputStream("testdata/test.xz");
        CompressorInputStream compressedInputStream;
        try {
            compressedInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
            assertInstanceOf(XZCompressorInputStream.class, compressedInputStream);
            compressedInputStream.close();
        } catch (IOException e) {
            assertNull(e); //XZ not supported on all platforms
        }
    }

    @Test
    public void testCreateCompressorInputStreamPack200() {
        final InputStream inputStream = IOUtils.toInputStream("testdata/test.p20");
        CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.PACK200, inputStream);
        assertInstanceOf(Pack200CompressorInputStream.class, compressedInputStream);
        compressedInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamDeflate() {
        final InputStream inputStream = IOUtils.toInputStream("testdata/test.gz"); //gz is also deflate
        CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, inputStream);
        assertInstanceOf(DeflateCompressorInputStream.class, compressedInputStream);
        compressedInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnsupportedFormat() {
        final InputStream inputStream = IOUtils.toInputStream("testdata/test.xml");
        CompressorInputStream compressedInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("UNSUPPORTED", inputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamBZip2() {
        final OutputStream outputStream = IOUtils.toOutputStream("testOutput.bz2");
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        compressorOutputStream.write(new byte[] {1, 2, 3});
        compressorOutputStream.finish();
        compressorOutputStream.close();
        final InputStream inputStream = IOUtils.toInputStream("testOutput.bz2");
        final byte[] expected = new byte[] {(byte) 0x42, (byte) 0x5A, 0, 0, 1, 2, 3, (byte) 0x42, 0x5A};
        final byte[] actual = new byte[10];
        IOUtils.readFully(inputStream, actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCreateCompressorOutputStreamGZip() {
        final OutputStream outputStream = IOUtils.toOutputStream("testOutput.gz");
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        compressorOutputStream.write(new byte[] {1, 2, 3});
        compressorOutputStream.finish();
        compressorOutputStream.close();
        final InputStream inputStream = IOUtils.toInputStream("testOutput.gz");
        final byte[] expected = new byte[] {(byte) 0x1F, 0x8B, 0x08, 0x00, (byte) 0x00, 1, 2, 3};
        final byte[] actual = new byte[8];
        IOUtils.readFully(inputStream, actual);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnsupportedFormat() {
        final OutputStream outputStream = IOUtils.toOutputStream("testOutput.unsupported");
        new CompressorStreamFactory().createCompressorOutputStream("UNSUPPORTED", outputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    @Test
    public void testGetDecompressConcatenatedDefault() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }
}