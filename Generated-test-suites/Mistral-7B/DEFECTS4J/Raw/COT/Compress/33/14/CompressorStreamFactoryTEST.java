package org.apache.commons.compress.compressors; // import necessary packages here

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.test.TestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;

    /**
     * Set up test environment
     */
    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNull(factory.getDecompressConcatenated());
    }

    /**
     * Test createCompressorInputStream method with null input stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullInputStream() {
        factory.createCompressorInputStream(null);
    }

    /**
     * Test createCompressorInputStream method with valid input stream
     */
    @Test
    public void testCreateCompressorInputStreamWithValidInputStream() {
        InputStream inputStream = TestUtils.createTestInputStream("Test Data");
        try {
            CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream.toString());
            assertSame(inputStream.getClass(), compressorInputStream.getInputStream().getClass());
            compressorInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test createCompressorInputStream method with invalid compressor name
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithInvalidCompressorName() {
        factory.createCompressorInputStream("InvalidCompressor");
    }

    /**
     * Test createCompressorOutputStream method with null output stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullOutputStream() {
        factory.createCompressorOutputStream(null);
    }

    /**
     * Test createCompressorOutputStream method with valid output stream
     */
    @Test
    public void testCreateCompressorOutputStreamWithValidOutputStream() {
        OutputStream outputStream = TestUtils.createTestOutputStream();
        try {
            CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(
                    CompressorStreamFactory.DEFLATE, outputStream);
            assertTrue(compressorOutputStream instanceof DeflateCompressorOutputStream);
            compressorOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test createCompressorOutputStream method with invalid compressor name
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWithInvalidCompressorName() {
        OutputStream outputStream = TestUtils.createTestOutputStream();
        factory.createCompressorOutputStream("InvalidCompressor", outputStream);
    }

    /**
     * Test getDecompressConcatenated method
     */
    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factoryWithConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithConcatenated.getDecompressConcatenated());
    }

    /**
     * Test setDecompressConcatenated method
     */
    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test setDecompressConcatenated method with null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetDecompressConcatenatedWithNullArgument() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(null);
    }
}