package org.apache.commons.compress.archivers;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;
    private ArchiveInputStream inputStream;
    private ArchiveOutputStream outputStream;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testCreateArchiveInputStream_ValidArchiveName() {
        String archiveName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream expectedArchiveInputStream = new ZipArchiveInputStream(inputStream);

        ArchiveInputStream actualArchiveInputStream = factory.createArchiveInputStream(archiveName, inputStream);

        assertNotNull(actualArchiveInputStream);
        Assert.assertEquals(expectedArchiveInputStream.getClass(), actualArchiveInputStream.getClass());
    }

    @Test
    public void testCreateArchiveInputStream_InvalidArchiveName() {
        String archiveName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Assert.assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(archiveName, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        String archiveName = "zip";

        Assert.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(archiveName, null));
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveName() {
        String archiveName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream expectedArchiveOutputStream = new JarArchiveOutputStream(outputStream);

        ArchiveOutputStream actualArchiveOutputStream = factory.createArchiveOutputStream(archiveName, outputStream);

        assertNotNull(actualArchiveOutputStream);
        Assert.assertEquals(expectedArchiveOutputStream.getClass(), actualArchiveOutputStream.getClass());
    }

    @Test
    public void testCreateArchiveOutputStream_InvalidArchiveName() {
        String archiveName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();

        Assert.assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream(archiveName, outputStream));
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        String archiveName = "zip";

        Assert.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(archiveName, null));
    }

    @Test
    public void testCreateArchiveInputStream_FromStream() throws IOException {
        String archiveName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ArchiveInputStream actualArchiveInputStream = factory.createArchiveInputStream(archiveName, inputStream);

        assertNotNull(actualArchiveInputStream);

        IOUtils.closeQuietly(actualArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidStream() {
        String archiveName = "zip";
        InputStream inputStream = null;

        factory.createArchiveInputStream(archiveName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_WriteToOutputStream() throws IOException {
        String archiveName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiveName, outputStream);

        assertNotNull(archiveOutputStream);

        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        outputStream.write("Test content".getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.close();

        InputStream inputStream = factory.createArchiveInputStream(archiveName, new ByteArrayInputStream(outputStream.toByteArray()));
        TarArchiveEntry entry = (TarArchiveEntry) inputStream.getNextEntry();

        assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test
    public void testCreateArchiveInputStream_NullArchiveName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Assert.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, inputStream));
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        String archiveName = "zip";

        Assert.assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(archiveName, null));
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_InvalidSignature() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        factory.createArchiveInputStream("unknown", inputStream);
    }

    @Test
    public void testGetEntryEncoding() {
        String encoding = "UTF-8";
        ArchiveStreamFactory factory = new ArchiveStreamFactory(encoding);

        String expectedEncoding = encoding;

        String actualEncoding = factory.getEntryEncoding();

        Assert.assertEquals(expectedEncoding, actualEncoding);
    }

    @Test
    public void testSetEntryEncoding() {
        String encoding = "UTF-8";
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.setEntryEncoding(encoding);

        String actualEncoding = factory.getEntryEncoding();

        Assert.assertEquals(encoding, actualEncoding);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetEntryEncoding_SetTwice() {
        String encoding = "UTF-8";
        ArchiveStreamFactory factory = new ArchiveStreamFactory(encoding);

        factory.setEntryEncoding(encoding);
        factory.setEntryEncoding("UTF-16");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEntryEncoding_NullEncoding() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        factory.setEntryEncoding(null);
    }
}