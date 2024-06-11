package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorInputStream() throws IOException, CompressorException {
        byte[] data = new byte[] { (byte) 0x42, (byte) 0x4A, (byte) 0x53, (byte) 0x42, (byte) 0x54, (byte) 0x53, (byte) 0x55, (byte) 0x43 };
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);

        assertNotNull(compressorInputStream);
        IOUtils.closeQuietly(compressorInputStream);
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithInvalidFormat() throws IOException, CompressorException {
        byte[] data = new byte[] { (byte) 0x42, (byte) 0x4A, (byte) 0x53, (byte) 0x42, (byte) 0x54, (byte) 0x53, (byte) 0x55, (byte) 0x43, (byte) 0x01 };
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        CompressorStreamFactory factory = new CompressorStreamFactory();

        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input));
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithNullInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, null));
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);

        assertNotNull(compressorOutputStream);
        IOUtils.closeQuietly(compressorOutputStream);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamWithInvalidFormat() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        CompressorStreamFactory factory = new CompressorStreamFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream(CompressorStreamFactory.INVALID_FORMAT, output));
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, null));
    }

    @org.junit.Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Boolean originalValue = factory.decompressConcatenated;

        factory.setDecompressConcatenated(true);

        assertTrue(factory.getDecompressConcatenated());

        factory.setDecompressConcatenated(originalValue);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenatedTwice() {
        CompressorStreamFactory factory = new CompressorStreamFactory(false);

        factory.setDecompressConcatenated(true);
        factory.setDecompressConcatenated(true);
    }
}