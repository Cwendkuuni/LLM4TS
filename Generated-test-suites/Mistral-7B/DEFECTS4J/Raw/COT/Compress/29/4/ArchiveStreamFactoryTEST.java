package org.apache.commons.compress.archivers; // Import necessary packages

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.StreamingNotSupportedException;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test class for ArchiveStreamFactory
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertNotNull(factory);
    }

    /**
     * Test createArchiveInputStream method with valid arguments
     */
    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("ZIP", inputStream);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);
    }

    /**
     * Test createArchiveInputStream method with null archiverName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, inputStream);
    }

    /**
     * Test createArchiveInputStream method with null inputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream("ZIP", null);
    }

    /**
     * Test createArchiveInputStream method with unsupported archive name
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnsupportedArchiveName() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream("INVALID", inputStream);
    }

    /**
     * Test createArchiveOutputStream method with valid arguments
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("ZIP", outputStream);
        assertNotNull(archiveOutputStream);
        IOUtils.closeQuietly(archiveOutputStream);
    }

    /**
     * Test createArchiveOutputStream method with null archiverName
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, outputStream);
    }

    /**
     * Test createArchiveOutputStream method with null outputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream("ZIP", null);
    }

    /**
     * Test createArchiveOutputStream method with unsupported archive name
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnsupportedArchiveName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("INVALID", outputStream);
    }

    /**
     * Test createArchiveInputStream method with autodetection
     */
    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        IOUtils.closeQuietly(archiveInputStream);
    }

    /**
     * Test createArchiveOutputStream method with StreamingNotSupportedException
     */
    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStreamWithStreamingNotSupportedException() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream("SEVEN_Z", outputStream);
    }
}