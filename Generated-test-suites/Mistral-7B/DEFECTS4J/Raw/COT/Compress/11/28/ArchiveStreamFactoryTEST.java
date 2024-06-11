package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.test.utils.TestUtils;

import org.junit.Test;

/**
 * Tests for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test valid archives
        for (String archiveType : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            final InputStream inputStream = new ByteArrayInputStream(TestUtils.getTestFileBytes(archiveType + ".test"));
            final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
            assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }

        // Test unknown archive
        final InputStream inputStream = new ByteArrayInputStream(TestUtils.getTestFileBytes("unknown.test"));
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream));
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test valid archives
        for (String archiveType : new String[]{"AR", "CPIO", "JAR", "TAR", "ZIP"}) {
            try (final OutputStream outputStream = new ByteArrayOutputStream()) {
                final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveType, outputStream);
                assertNotNull(archiveOutputStream);
                archiveOutputStream.finish();
            }
        }

        // Test unknown archive
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("unknown", new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() {
        // Test valid archives
        for (String archiveType : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            final byte[] inputBytes = TestUtils.getTestFileBytes(archiveType + ".test");
            final InputStream inputStream = new ByteArrayInputStream(inputBytes);
            final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
            assertNotNull(archiveInputStream);
            archiveInputStream.close();
        }

        // Test unknown archive
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null));
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }
}