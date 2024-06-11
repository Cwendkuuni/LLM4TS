package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.AR;
import org.apache.commons.compress.archivers.cpio.Cpio;
import org.apache.commons.compress.archivers.dump.Dump;
import org.apache.commons.compress.archivers.jar.Jar;
import org.apache.commons.compress.archivers.tar.Tar;
import org.apache.commons.compress.archivers.zip.Zip;

/**
 * Test class for {@link ArchiveStreamFactory}.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test createArchiveInputStream(String, InputStream) method with valid parameters.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamWithValidParameters() throws Exception {
        final String archiveType = Zip.TYPE;
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiveType, inputStream);
        assertNull(archiveInputStream.getNextEntry());
    }

    /**
     * Test createArchiveInputStream(String, InputStream) method with invalid archiver name.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("invalid", inputStream));
    }

    /**
     * Test createArchiveInputStream(InputStream) method with valid parameters.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamWithValidInputStream() throws Exception {
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNull(archiveInputStream.getNextEntry());
    }

    /**
     * Test createArchiveInputStream(InputStream) method with null input stream.
     */
    @org.junit.Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        assertThrows(IllegalArgumentException.class,
                () -> new ArchiveStreamFactory().createArchiveInputStream((InputStream) null));
    }

    /**
     * Test createArchiveOutputStream(String, OutputStream) method with valid parameters.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithValidParameters() throws Exception {
        final String archiveType = Zip.TYPE;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiveType, outputStream);
        assertNull(archiveOutputStream.getNextArchiveEntry());
    }

    /**
     * Test createArchiveOutputStream(String, OutputStream) method with invalid archiver name.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws Exception {
        final OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("invalid", outputStream));
    }

    /**
     * Test createArchiveOutputStream(String, OutputStream) method with null output stream.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        assertThrows(IllegalArgumentException.class,
                () -> new ArchiveStreamFactory().createArchiveOutputStream((OutputStream) null));
    }

    /**
     * Test createArchiveOutputStream(String, OutputStream) method with valid stream and
     * null output.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithValidStreamAndNullOutput() throws Exception {
        final String archiveType = Zip.TYPE;
        final OutputStream outputStream = null;
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiveType, outputStream);
        assertNull(archiveOutputStream);
    }

    /**
     * Test createArchiveOutputStream(String, OutputStream) method with valid stream and
     * non-null output.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithValidStreamAndNonnullOutput() throws Exception {
        final String archiveType = Zip.TYPE;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiveType, outputStream);
        assertNull(archiveOutputStream.getNextArchiveEntry());
    }

    /**
     * Test createArchiveOutputStream(InputStream) method with valid parameters.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithValidInputStream() throws Exception {
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(outputStream);
        assertNull(archiveOutputStream.getNextArchiveEntry());
    }

    /**
     * Test createArchiveOutputStream(InputStream) method with null input stream.
     */
    @org.junit.Test
    public void testCreateArchiveOutputStreamWithNullInputStream() {
        assertThrows(IllegalArgumentException.class,
                () -> new ArchiveStreamFactory().createArchiveOutputStream((InputStream) null));
    }
}