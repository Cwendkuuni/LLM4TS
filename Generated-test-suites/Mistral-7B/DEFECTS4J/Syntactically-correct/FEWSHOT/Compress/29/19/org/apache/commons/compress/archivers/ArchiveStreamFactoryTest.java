package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.IOUtils;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test valid archives
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream;

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);
        assertNotNull(archiveInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, input);
        assertNotNull(archiveInputStream);
        assertInstanceOf(ArjArchiveInputStream.class, archiveInputStream);

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        assertNotNull(archiveInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
        assertNotNull(archiveInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        assertNotNull(archiveInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        assertNotNull(archiveInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertNotNull(archiveInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, input);
        assertThrows(StreamingNotSupportedException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, input));

        // Test null input stream
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.AR, null));

        // Test null archiver name
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, input));
    }

    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test valid archives
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream;

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, output);
        assertThrows(UnsupportedOperationException.class, () -> factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, output));

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
        assertThrows(UnsupportedOperationException.class, () -> factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, output));

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertNotNull(archiveOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, output);
        assertThrows(UnsupportedOperationException.class, () -> factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, output));

        // Test null output stream
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null));

        // Test null archiver name
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, output));
    }

    @Test
    public void testCreateArchiveInputStreamFromSignature() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test valid archives
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream;

        archiveInputStream = factory.createArchiveInputStream(input);
        assertNotNull(archiveInputStream);

        archiveInputStream = archiveInputStream.getNextEntry();
        assertNotNull(archiveInputStream);

        if (archiveInputStream instanceof JarArchiveEntry) {
            archiveInputStream = factory.createArchiveInputStream(JarArchiveInputStream.class, archiveInputStream);
            assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
        } else if (archiveInputStream instanceof TarArchiveEntry) {
            archiveInputStream = factory.createArchiveInputStream(TarArchiveInputStream.class, archiveInputStream);
            assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
        } else if (archiveInputStream instanceof ZipArchiveEntry) {
            archiveInputStream = factory.createArchiveInputStream(ZipArchiveInputStream.class, archiveInputStream);
            assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
        } else if (archiveInputStream instanceof ArArchiveEntry) {
            archiveInputStream = factory.createArchiveInputStream(ArArchiveInputStream.class, archiveInputStream);
            assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        } else if (archiveInputStream instanceof ArjArchiveEntry) {
            archiveInputStream = factory.createArchiveInputStream(ArjArchiveInputStream.class, archiveInputStream);
            assertInstanceOf(ArjArchiveInputStream.class, archiveInputStream);
        } else if (archiveInputStream instanceof CpioArchiveEntry) {
            archiveInputStream = factory.createArchiveInputStream(CpioArchiveInputStream.class, archiveInputStream);
            assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
        } else if (archiveInputStream instanceof DumpArchiveEntry) {
            archiveInputStream = factory.createArchiveInputStream(DumpArchiveInputStream.class, archiveInputStream);
            assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
        } else {
            assertNull(archiveInputStream);
        }

        // Test null input stream
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null));
    }
}