package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test Ar Archive
        final InputStream inputStream = mock(InputStream.class);
        when(inputStream.readAllBytes()).thenReturn(new byte[]{"!<arch>\0"});
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ARCHIVE_TYPE_AR, inputStream);
        assertNotNull(archiveInputStream);
        assertNotNull(archiveInputStream.getFormat());
        archiveInputStream.close();

        // Test Jar Archive
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JarArchiveOutputStream jarArchiveOutputStream = new JarArchiveOutputStream(outputStream);
        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.jar!/test.txt"), new ByteArrayInputStream(("Hello World".getBytes())));
        jarArchiveOutputStream.finish();
        final InputStream jarInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ARCHIVE_TYPE_JAR, jarInputStream);
        assertNotNull(archiveInputStream);
        assertNotNull(archiveInputStream.getFormat());
        archiveInputStream.close();

        // Test Tar Archive
        final OutputStream tarOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(tarOutputStream);
        tarArchiveOutputStream.putNextEntry(new TarEntry("test.tar!/test.txt"), new ByteArrayInputStream(("Hello World".getBytes())));
        tarArchiveOutputStream.finish();
        final InputStream tarInputStream = new ByteArrayInputStream(tarOutputStream.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ARCHIVE_TYPE_TAR, tarInputStream);
        assertNotNull(archiveInputStream);
        assertNotNull(archiveInputStream.getFormat());
        archiveInputStream.close();

        // Test Zip Archive
        final ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipOutputStream);
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test.zip!/test.txt"), new ByteArrayInputStream(("Hello World".getBytes())));
        zipArchiveOutputStream.finish();
        final InputStream zipInputStream = new ByteArrayInputStream(zipOutputStream.toByteArray());
        archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ARCHIVE_TYPE_ZIP, zipInputStream);
        assertNotNull(archiveInputStream);
        assertNotNull(archiveInputStream.getFormat());
        archiveInputStream.close();

        // Test unknown archive
        final ByteArrayInputStream unknownArchiveInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            ArchiveStreamFactory.getInstance()
                    .createArchiveInputStream("unknown", unknownArchiveInputStream);
            // should not reach here
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test Ar Archive
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ARCHIVE_TYPE_AR, outputStream);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        assertNotNull(archiveOutputStream.getArchive());
        archiveOutputStream.close();

        // Test Jar Archive
        final ByteArrayOutputStream jarOutputstream = new ByteArrayOutputStream();
        JarArchiveOutputStream jarArchiveOutputStream = new JarArchiveOutputStream(jarOutputstream);
        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry("test.jar!/test.txt"), new ByteArrayInputStream(("Hello World".getBytes())));
        jarArchiveOutputStream.finish();
        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream2 = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ARCHIVE_TYPE_JAR, jarOutputStream);
        archiveOutputStream2.putArchiveEntry(new JarArchiveEntry("test.jar!/test.txt"), new ByteArrayInputStream(jarOutputstream.toByteArray()));
        archiveOutputStream2.finish();
        assertNotNull(archiveOutputStream2);
        assertNotNull(archiveOutputStream2.getArchive());
        archiveOutputStream2.close();

        // Test Tar Archive
        final OutputStream tarOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(tarOutputStream);
        tarArchiveOutputStream.putNextEntry(new TarEntry("test.tar!/test.txt"), new ByteArrayInputStream(("Hello World".getBytes())));
        tarArchiveOutputStream.finish();
        final OutputStream tarOutputStream2 = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream3 = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ARCHIVE_TYPE_TAR, tarOutputStream2);
        archiveOutputStream3.putArchiveEntry(new TarEntry("test.tar!/test.txt"), new ByteArrayInputStream(tarOutputStream.toByteArray()));
        archiveOutputStream3.finish();
        assertNotNull(archiveOutputStream3);
        assertNotNull(archiveOutputStream3.getArchive());
        archiveOutputStream3.close();

        // Test Zip Archive
        final ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipOutputStream);
        zipArchiveOutputStream.putNextEntry(new ZipEntry("test.zip!/test.txt"), new ByteArrayInputStream(("Hello World".getBytes())));
        zipArchiveOutputStream.finish();
        final OutputStream zipOutputStream2 = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream4 = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ARCHIVE_TYPE_ZIP, zipOutputStream2);
        archiveOutputStream4.putArchiveEntry(new ZipEntry("test.zip!/test.txt"), new ByteArrayInputStream(zipOutputStream.toByteArray()));
        archiveOutputStream4.finish();
        assertNotNull(archiveOutputStream4);
        assertNotNull(archiveOutputStream4.getArchive());
        archiveOutputStream4.close();

        // Test Ar Archive with InputStream
        final InputStream arInputStream = new ByteArrayInputStream(new byte[]{"!<arch>\0"});
        ArchiveOutputStream archiveOutputStream5 = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ARCHIVE_TYPE_AR, arInputStream);
        assertNotNull(archiveOutputStream5);
        archiveOutputStream5.finish();
        assertNotNull(archiveOutputStream5.getArchive());
        archiveOutputStream5.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.getInstance()
                .createArchiveInputStream("unknown", inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullStream() {
        ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ARCHIVE_TYPE_AR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() {
        ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(null, new ByteArrayOutputStream());
    }
}