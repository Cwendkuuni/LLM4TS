package org.apache.commons.compress.compressors;

import static org.apache.commons.compress.compressors.CompressorStreamFactory.BZIP2;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.DEFLATE;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.GZIP;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.PACK200;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.XZ;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;
    private byte[] inputData;

    @Before
    public void setup() {
        this.inputData = "This is a test string".getBytes();
        this.factory = new CompressorStreamFactory();
    }

    @Test
    public void testCreateCompressorInputStream_ValidNameAndInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(inputData);

        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(DEFLATE, inputStream);
        assertNotNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_InvalidName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        assertThrows(CompressorException.class, () -> factory.createCompressorInputStream("INVALID", inputStream));
    }

    @Test
    public void testCreateCompressorInputStream_NullInputStream() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorInputStream(DEFLATE, null));
    }

    @Test
    public void testCreateCompressorInputStream_Autodetect() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(inputData);
        inputStream.mark(12);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
        assertEquals(CompressorStreamFactory.GZIP, compressorInputStream.getName());
        inputStream.reset();
    }

    @Test
    public void testCreateCompressorInputStream_AutodetectWithInvalidSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
        assertNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_ValidNameAndOutputStream() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(XZ, outputStream);
        assertNotNull(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_InvalidName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> factory.createCompressorOutputStream("INVALID", outputStream));
    }

    @Test
    public void testCreateCompressorOutputStream_NullOutputStream() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> factory.createCompressorOutputStream(DEFLATE, null));
    }

    @Test
    public void testGetDecompressConcatenated() {
        assertTrue(factory.getDecompressConcatenated());
    }

    @Test
    public void testSetAndGetDecompressConcatenated() {
        factory.setDecompressConcatenated(false);
        assertFalse(factory.getDecompressConcatenated());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenated_AfterConstruction() {
        factory.setDecompressConcatenated(false);
    }
}