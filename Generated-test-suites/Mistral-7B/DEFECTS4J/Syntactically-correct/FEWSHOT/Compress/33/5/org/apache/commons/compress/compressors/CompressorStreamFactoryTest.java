package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.CompressorInputStream;
import org.apache.commons.compress.CompressorOutputStream;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorOutputStream;
import org.apache.commons.compress.test.utils.TestUtils;
import org.junit.Test;

/**
 * Test cases for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBZip2() throws Exception {
        InputStream inputStream = TestUtils.getTestDataResourceAsInputStream("testdata/test.bz2");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);

        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamGZip() throws Exception {
        InputStream inputStream = TestUtils.getTestDataResourceAsInputStream("testdata/test.gz");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);

        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamXZ() throws Exception {
        InputStream inputStream = TestUtils.getTestDataResourceAsInputStream("testdata/test.xz");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);

        compressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamUnsupported() throws Exception {
        InputStream inputStream = TestUtils.getTestDataResourceAsInputStream("testdata/test.txt");
        assertNull(new CompressorStreamFactory()
                .createCompressorInputStream("unknown", inputStream));
        inputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamBZip2() throws Exception {
        OutputStream outputStream = TestUtils.createTestDataFile("testdata/test.bz2");
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        compressorOutputStream.write("test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = TestUtils.getTestDataResourceAsInputStream("testdata/test.bz2");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        byte[] expected = new byte[]{"\u0000\u0000\u0000\u0004test data"};
        TestUtils.assertInputStreamEquals(expected, compressorInputStream);

        compressorInputStream.close();
        inputStream.close();
        TestUtils.deleteTestDataFile("testdata/test.bz2");
    }

    @Test
    public void testCreateCompressorOutputStreamGZip() throws Exception {
        OutputStream outputStream = TestUtils.createTestDataFile("testdata/test.gz");
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        compressorOutputStream.write("test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = TestUtils.getTestDataResourceAsInputStream("testdata/test.gz");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        byte[] expected = new byte[]{0x1F, 0x8B, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x3C, (byte) 0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x00, 't', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', 0x00};
        TestUtils.assertInputStreamEquals(expected, compressorInputStream);

        compressorInputStream.close();
        inputStream.close();
        TestUtils.deleteTestDataFile("testdata/test.gz");
    }

    @Test
    public void testCreateCompressorOutputStreamXZ() throws Exception {
        OutputStream outputStream = TestUtils.createTestDataFile("testdata/test.xz");
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
        compressorOutputStream.write("test data".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();

        InputStream inputStream = TestUtils.getTestDataResourceAsInputStream("testdata/test.xz");
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        byte[] expected = new byte[]{(byte) 0xDD, (byte) 0xDD, 0x00, 0x00, 0x00, 0x00, (byte) 0x1A, 0x01, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x00, 't', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', 0x00, 0x00, 0x00, 0x00, (byte) 0x1A, 0x00, 0x00};
        TestUtils.assertInputStreamEquals(expected, compressorInputStream);

        compressorInputStream.close();
        inputStream.close();
        TestUtils.deleteTestDataFile("testdata/test.xz");
    }

    @Test
    public void testCreateCompressorOutputStreamUnsupported() throws Exception {
        OutputStream outputStream = TestUtils.createTestDataFile("testdata/test.txt");
        assertNull(new CompressorStreamFactory()
                .createCompressorOutputStream("unknown", outputStream));
        TestUtils.deleteTestDataFile("testdata/test.txt");
    }
}