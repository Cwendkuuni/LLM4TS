package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamFromSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);

        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(inputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);

        inputStream = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(inputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);

        inputStream = new ByteArrayInputStream(XZCompressorInputStream.SIGNATURE);
        compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(inputStream);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);

        inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(inputStream);
            assertNull(compressorInputStream);
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(
                CompressorStreamFactory.BZIP2, outputStream);
        assertInstanceOf(BZip2CompressorOutputStream.class, compressorOutputStream);

        compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(
                CompressorStreamFactory.GZIP, outputStream);
        assertInstanceOf(GzipCompressorOutputStream.class, compressorOutputStream);

        compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(
                CompressorStreamFactory.XZ, outputStream);
        assertInstanceOf(XZCompressorOutputStream.class, compressorOutputStream);

        try {
            compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(
                    "unknown", outputStream);
            assertNull(compressorOutputStream);
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testDecompressConcatenated() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        CompressorStreamFactory factory = CompressorStreamFactory.newInstance();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);

        IOUtils.copy(inputStream, compressorOutputStream);
        compressorOutputStream.finish();
        compressorOutputStream.close();

        inputStream = new ByteArrayInputStream(IOUtils.toByteArray(outputStream));
        compressorInputStream = factory.createCompressorInputStream(inputStream);

        InputStream expectedInputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        byte[] expectedBytes = IOUtils.toByteArray(expectedInputStream);
        byte[] actualBytes = IOUtils.toByteArray(compressorInputStream);
        assertArrayEquals(expectedBytes, actualBytes);

        if (factory.getDecompressConcatenated()) {
            compressorInputStream = factory.createCompressorInputStream(inputStream);
            expectedInputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE.concat(BZip2CompressorInputStream.SIGNATURE));
            expectedBytes = IOUtils.toByteArray(expectedInputStream);
            actualBytes = IOUtils.toByteArray(compressorInputStream);
            assertArrayEquals(expectedBytes, actualBytes);
        }

        compressorInputStream.close();
    }
}