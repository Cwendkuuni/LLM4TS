package org.apache.commons.compress.compressors;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorException;
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
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test cases for the CompressorStreamFactory class.
 */
public class CompressorStreamFactoryTest {

    

    @org.junit.Test
    public void testCreateCompressorInputStream_BZIP2() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_GZIP() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
            fail("GZIP not supported as an input stream.");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_PACK200() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.PACK200, inputStream);
        assertEquals(Pack200CompressorInputStream.class, compressorInputStream.getClass());
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_XZ() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        assertEquals(XZCompressorInputStream.class, compressorInputStream.getClass());
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_LZMA() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.LZMA, inputStream);
            fail("LZMA not supported as an input stream.");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_SNAPPY_FRAMED() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.SNAPPY_FRAMED, inputStream);
            fail("SNAPPY_FRAMED not supported as an input stream.");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_SNAPPY_RAW() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, inputStream);
            fail("SNAPPY_RAW not supported as an input stream.");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_Z() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(CompressorStreamFactory.Z, inputStream);
            fail("Z not supported as an input stream.");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_DEFLATE() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, inputStream);
        assertEquals(DeflateCompressorInputStream.class, compressorInputStream.getClass());
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_Unknown() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new CompressorStreamFactory().createCompressorInputStream("Unknown", inputStream);
            fail("Unexpected success.");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_NullInputStream() throws Exception {
        try {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, null);
            fail("Unexpected success.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_MarkNotSupported() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]) {
            @Override
            public synchronized void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }
        };
        try {
            new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
            fail("Unexpected success.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // ... Add test cases for createCompressorOutputStream methods here ...

    @org.junit.After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(testInputStream);
        IOUtils.closeQuietly(testOutputStream);
    }

    private InputStream testInputStream;
    private OutputStream testOutputStream;

    @Before
    public void setUp() throws IOException {
        testInputStream = new ByteArrayInputStream(new byte[1024]);
        testOutputStream = new ByteArrayOutputStream(1024);
    }

    
}