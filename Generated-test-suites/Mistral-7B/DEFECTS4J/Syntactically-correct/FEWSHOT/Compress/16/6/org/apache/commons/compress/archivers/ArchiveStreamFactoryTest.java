package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStreamWithKSnown() throws Exception {
        for (final String archiver : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            final InputStream input = new ByteArrayInputStream(
                    (archiver.equals("TAR") ? new byte[512] : new byte[12]).clone());
            final ArchiveInputStream result = ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream(archiver, input);
            assertInstanceOf(ArchiveInputStream.class, result);
            if (archiver.equals("TAR")) {
                assertInstanceOf(TarArchiveInputStream.class, result);
            } else {
                assertInstanceOf(JarArchiveInputStream.class.isAssignableFrom(result.getClass()), result.getClass());
            }
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithUnknown() throws Exception {
        final InputStream input = new ByteArrayInputStream("unknown".getBytes());
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance()
                .createArchiveInputStream("unknown", input));
    }

    @Test
    public void testCreateArchiveOutputStreamWithKSnown() throws Exception {
        for (final String archiver : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            final OutputStream output = new ByteArrayOutputStream();
            final ArchiveOutputStream result = ArchiveStreamFactory.getInstance()
                    .createArchiveOutputStream(archiver, output);
            assertInstanceOf(ArchiveOutputStream.class, result);
            if (archiver.equals("AR")) {
                assertInstanceOf(ArArchiveOutputStream.class, result);
            } else if (archiver.equals("CPIO")) {
                assertInstanceOf(CpioArchiveOutputStream.class, result);
            } else if (archiver.equals("DUMP")) {
                assertInstanceOf(DumpArchiveOutputStream.class, result);
            } else if (archiver.equals("JAR")) {
                assertInstanceOf(JarArchiveOutputStream.class, result);
            } else if (archiver.equals("TAR")) {
                assertInstanceOf(TarArchiveOutputStream.class, result);
            } else if (archiver.equals("ZIP")) {
                assertInstanceOf(ZipArchiveOutputStream.class, result);
            }
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithUnknown() throws Exception {
        final OutputStream output = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream("unknown", output));
    }

    @Test
    public void testCreateArchiveInputStreamWithNull() {
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(null, new ByteArrayInputStream(new byte[0])));
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance()
                .createArchiveInputStream("AR", null));
    }

    @Test
    public void testCreateArchiveOutputStreamWithNull() {
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(null, new ByteArrayOutputStream()));
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream("AR", null));
    }
}