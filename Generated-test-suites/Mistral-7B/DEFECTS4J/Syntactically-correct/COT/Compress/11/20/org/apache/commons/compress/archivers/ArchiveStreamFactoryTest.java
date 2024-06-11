package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.AR;
import org.apache.commons.compress.archivers.jar.JAR;
import org.apache.commons.compress.archivers.test.testutils.TestInputStreams;
import org.apache.commons.compress.archivers.test.testutils.TestOutputStreams;
import org.junit.Test;

/**
 * Test cases for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating ArchiveInputStream with valid arguments.
     */
    @Test
    public void testCreateArchiveInputStreamWithValidArguments() {
        final InputStream inputStream = TestInputStreams.createTestInputStream(ArchiveStreamFactory.ZIP);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
    }

    /**
     * Test creating ArchiveInputStream with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArguments() {
        ArchiveStreamFactory.createArchiveInputStream(null, null);
    }

    /**
     * Test creating ArchiveInputStream with unsupported archiver name.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnsupportedArchiverName() {
        final InputStream inputStream = TestInputStreams.createTestInputStream(ArchiveStreamFactory.AR);
        ArchiveStreamFactory.createArchiveInputStream("unsupported", inputStream);
    }

    /**
     * Test creating ArchiveOutputStream with valid arguments.
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() {
        final OutputStream outputStream = TestOutputStreams.createTestOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    /**
     * Test creating ArchiveOutputStream with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArguments() {
        ArchiveStreamFactory.createArchiveOutputStream(null, null);
    }

    /**
     * Test creating ArchiveOutputStream with unsupported archiver name.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnsupportedArchiverName() {
        final OutputStream outputStream = TestOutputStreams.createTestOutputStream();
        ArchiveStreamFactory.createArchiveOutputStream("unsupported", outputStream);
    }

    /**
     * Test creating ArchiveInputStream from input stream with valid archiver name.
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithValidArchiverName() {
        final InputStream inputStream = TestInputStreams.createTestInputStream(ArchiveStreamFactory.JAR);
        final ArchiveInputStream archiveInputStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(archiveInputStream);
    }

    /**
     * Test creating ArchiveInputStream from input stream with unsupported archiver name.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInputStreamWithUnsupportedArchiverName() {
        final InputStream inputStream = TestInputStreams.createTestInputStream(ArchiveStreamFactory.AR);
        ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    }

    /**
     * Test creating ArchiveOutputStream from output stream with valid archiver name.
     */
    @Test
    public void testCreateArchiveOutputStreamFromOutputStreamWithValidArchiverName() {
        final OutputStream outputStream = TestOutputStreams.createTestOutputStream();
        final ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    /**
     * Test creating ArchiveOutputStream from output stream with unsupported archiver name.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamFromOutputStreamWithUnsupportedArchiverName() {
        final OutputStream outputStream = TestOutputStreams.createTestOutputStream();
        ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
    }
}