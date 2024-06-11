package org.apache.commons.compress.compressors;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    private CompressorStreamFactory compressorStreamFactory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        compressorStreamFactory = new CompressorStreamFactory();
        inputStream = Mockito.mock(InputStream.class);
        outputStream = Mockito.mock(OutputStream.class);
    }

    @After
    public void tearDown() throws Exception {
        if (inputStream != null) {
            Mockito.verifyNoMoreInteractions(inputStream);
        }
        if (outputStream != null) {
            Mockito.verifyNoMoreInteractions(outputStream);
        }
    }

    @Test
    public void testCreateCompressorInputStreamWithUnknownSignature() throws CompressorException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[10];
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        inputStream = new ByteArrayInputStream(bytes);
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(inputStream);
        assertNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamWithGzipSignature() throws CompressorException, IOException {
        inputStream = new GzipCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(inputStream);
        assertNotNull(compressorInputStream);
        assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamWithBZip2Signature() throws CompressorException, IOException {
        inputStream = new BZip2CompressorInputStream(new ByteArrayInputStream(new byte[0]));
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(inputStream);
        assertNotNull(compressorInputStream);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithUnsupportedSignature() throws CompressorException, IOException {
        inputStream = new ByteArrayInputStream(new byte[10]);
        compressorStreamFactory.createCompressorInputStream(inputStream);
    }

    @Test
    public void testCreateCompressorInputStreamWithCustomCompressor() throws CompressorException, IOException {
        String name = "custom";
        inputStream = Mockito.mock(InputStream.class);
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(name, inputStream);
        assertNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamWithGzipCompressor() throws CompressorException, IOException {
        outputStream = Mockito.mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream(GzipCompressorOutputStream.class.getCanonicalName(), outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamWithBZip2Compressor() throws CompressorException, IOException {
        outputStream = Mockito.mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream(BZip2CompressorOutputStream.class.getCanonicalName(), outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamWithXZCompressor() throws CompressorException, IOException {
        outputStream = Mockito.mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream(XZCompressorOutputStream.class.getCanonicalName(), outputStream);
        assertNotNull(compressorOutputStream);
        assertEquals(XZCompressorOutputStream.class, compressorOutputStream.getClass());
        Mockito.verify(outputStream, Mockito.times(XZCompressorOutputStream.MIN_INPUT_SIZE)).write(Mockito.anyByte());
    }

    @Test
    public void testCreateCompressorOutputStreamWithCustomCompressor() throws CompressorException, IOException {
        String name = "custom";
        outputStream = Mockito.mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream(name, outputStream);
        assertNull(compressorOutputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        assertFalse(compressorStreamFactory.getDecompressConcatenated());
    }
}