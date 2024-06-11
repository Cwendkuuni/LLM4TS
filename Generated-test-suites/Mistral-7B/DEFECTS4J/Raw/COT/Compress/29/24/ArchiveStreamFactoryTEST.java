package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.assertNotNull;
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
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_AR_InvalidArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream("INVALID", new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveOutputStream_ARJ() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ARJ, output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test(expected = StreamingNotSupportedException.class)
    public void testCreateArchiveOutputStream_SEVEN_Z() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, output);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.jar!/test.txt"));
        IOUtils.write("test content", archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.tar!/test.txt"));
        IOUtils.write("test content", archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertNotNull(archiveOutputStream);
        archiveOutputStream.putNextEntry(new ZipArchiveEntry("test.zip!/test.txt"));
        IOUtils.write("test content", archiveOutputStream.getOutputStream());
        archiveOutputStream.finish();
        IOUtils.closeQuietly(archiveOutputStream);
    }

    @Test
    public void testCreateArchiveInputStream_AR() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidArchiverName() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("INVALID", inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_ARJ() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidSignatureLength() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[11]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_AR_ARJSignatureMismatch() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidTarSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[512]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidTarChecksum() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[512]);
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new ByteArrayInputStream(new byte[512]));
        TarArchiveEntry tarEntry = tarInputStream.getNextTarEntry();
        InputStream entryStream = tarInputStream.getInputStream();
        byte[] header = new byte[tarEntry.getSize()];
        IOUtils.readFully(entryStream, header);
        InputStream inputStreamWithInvalidChecksum = new ByteArrayInputStream(new byte[header.length + 512]);
        IOUtils.write(header, inputStreamWithInvalidChecksum, 0, header.length);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStreamWithInvalidChecksum));
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidZipSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[22]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidZipLocalHeaderSignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[30]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_AR_InvalidZipEndOfCentralDirectorySignature() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[18]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream));
    }

}