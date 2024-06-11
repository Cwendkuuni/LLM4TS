package org.apache.commons.compress.archivers.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Tests for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveOutputStream().
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertEquals(ArchiveStreamFactory.ZIP, archiveOut.getName());
        archiveOut.finish();
        assertEquals(1, archiveOut.getNumberOfEntries());
        IOUtils.closeQuietly(archiveOut);
        InputStream testStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);
        assertEquals(ArchiveStreamFactory.ZIP, archiveIn.getName());
        archiveIn.close();
    }

    /**
     * Test createArchiveOutputStream with entry encoding.
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveOutputStreamWithEncoding() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory("UTF-8");
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOut = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertEquals(ArchiveStreamFactory.ZIP, archiveOut.getName());
        archiveOut.finish();
        assertEquals(1, archiveOut.getNumberOfEntries());
        IOUtils.closeQuietly(archiveOut);
        InputStream testStream = new ByteArrayInputStream(out.toByteArray());
        ArchiveInputStream archiveIn = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);
        assertEquals(ArchiveStreamFactory.ZIP, archiveIn.getName());
        assertEquals("UTF-8", archiveIn.getEncoding());
        archiveIn.close();
    }

    /**
     * Test createArchiveOutputStream throws exception for unsupported format.
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveOutputStreamUnsupportedFormat() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, out));
    }

    /**
     * Test createArchiveInputStream().
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveInputStream() {
        InputStream testStream = new ByteArrayInputStream(new byte[] { 0x0707, 0x0750, 0x4B41, 0x5082, 0x0301, 0x0100, 0x01, 0x00, 0x00, 0x02, 0x01, 0x00, 0x00, 0x01, 0x00 });
        ArchiveInputStream archiveIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);
        assertEquals(ArchiveStreamFactory.ZIP, archiveIn.getName());
        archiveIn.close();
    }

    /**
     * Test createArchiveInputStream().
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveInputStreamWithEncoding() {
        InputStream testStream = new ByteArrayInputStream(new byte[] { 0x0707, 0x0750, 0x4B41, 0x5082, 0x0301, 0x0100, 0x01, 0x00, 0x00, 0x02, 0x01, 0x00, 0x00, 0x01, 0x00 });
        ArchiveInputStream archiveIn = new ArchiveStreamFactory("UTF-8").createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);
        assertEquals(ArchiveStreamFactory.ZIP, archiveIn.getName());
        assertEquals("UTF-8", archiveIn.getEncoding());
        archiveIn.close();
    }

    /**
     * Test createArchiveInputStream throws exception for unsupported format.
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveInputStreamUnsupportedFormat() {
        InputStream testStream = new ByteArrayInputStream(new byte[] { 0x074C, 0x4D21, 0x50C2, 0x0101, 0x0100 });
        assertThrows(ArchiveException.class,
                () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, testStream));
    }

    /**
     * Test createArchiveInputStream with gzip compressed input.
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveInputStreamWithGzipCompression() throws IOException {
        InputStream testStream = new GzipCompressorInputStream(new ByteArrayInputStream(new byte[] { 0x0707, 0x0750, 0x4B41, 0x5082, 0x0301, 0x0100, 0x01, 0x00, 0x00, 0x02, 0x01, 0x00, 0x00, 0x01, 0x00 }));
        ArchiveInputStream archiveIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);
        assertEquals(ArchiveStreamFactory.ZIP, archiveIn.getName());
        archiveIn.close();
    }

    /**
     * Test createArchiveInputStream with gzip compressed input and encoding.
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveInputStreamWithGzipCompressionAndEncoding() throws IOException {
        InputStream testStream = new GzipCompressorInputStream(new ByteArrayInputStream(new byte[] { 0x0707, 0x0750, 0x4B41, 0x5082, 0x0301, 0x0100, 0x01, 0x00, 0x00, 0x02, 0x01, 0x00, 0x00, 0x01, 0x00 }));
        ArchiveInputStream archiveIn = new ArchiveStreamFactory("UTF-8").createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream);
        assertEquals(ArchiveStreamFactory.ZIP, archiveIn.getName());
        assertEquals("UTF-8", archiveIn.getEncoding());
        archiveIn.close();
    }

    /**
     * Test createArchiveInputStream with invalid gzip compressed input.
     */
    @org.junit.jupiter.api.Test
    public void testCreateArchiveInputStreamWithInvalidGzipCompression() {
        InputStream testStream = new ByteArrayInputStream(new byte[] { 0x07, 0x07, 0x4B, 0x41, 0x50, 0x82, 0x03, 0x01, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00 });
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testStream));
    }
}