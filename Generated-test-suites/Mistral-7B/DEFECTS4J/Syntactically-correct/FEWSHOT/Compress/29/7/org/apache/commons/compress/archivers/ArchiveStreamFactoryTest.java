package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ar() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(archiveInputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_cpio() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_dump() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_jar() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_tar() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertNotNull(archiveInputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_zip() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_sevenz() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, inputStream));
    }

    @Test
    public void testCreateArchiveOutputStream_ar() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_cpio() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_tar() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_zip() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_jar() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_sevenz() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, outputStream));
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
    }
}