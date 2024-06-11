package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws ArchiveException {
        // AR
        String archiverName = ArchiveStreamFactory.AR;
        InputStream inputStream = new ByteArrayInputStream("AR archive header".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());

        // ARJ
        archiverName = ArchiveStreamFactory.ARJ;
        if (ArchiveStreamFactory.entryEncoding != null) {
            archiveInputStream = new ArchiveStreamFactory(ArchiveStreamFactory.entryEncoding)
                    .createArchiveInputStream(archiverName, inputStream);
        } else {
            archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        }
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(ArjArchiveInputStream.class, archiveInputStream.getClass());

        // JAR
        archiverName = ArchiveStreamFactory.JAR;
        inputStream = new ByteArrayInputStream("JAR archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());

        // ZIP
        archiverName = ArchiveStreamFactory.ZIP;
        inputStream = new ByteArrayInputStream("ZIP archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

        // TAR
        archiverName = ArchiveStreamFactory.TAR;
        inputStream = new ByteArrayInputStream("TAR archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());

        // CPIO
        archiverName = ArchiveStreamFactory.CPIO;
        inputStream = new ByteArrayInputStream("CPIO archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());

        // DUMP
        archiverName = ArchiveStreamFactory.DUMP;
        inputStream = new ByteArrayInputStream("DUMP archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        Assert.assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());

        // SEVEN_Z
        archiverName = ArchiveStreamFactory.SEVEN_Z;
        inputStream = new ByteArrayInputStream("7z archive header".getBytes());
        try {
            archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(archiverName, inputStream);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Invalid archiver name
        archiverName = "unknown";
        try {
            archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(archiverName, inputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        // Null archiver name or input stream
        archiverName = ArchiveStreamFactory.AR;
        inputStream = null;
        try {
            archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(archiverName, inputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        archiverName = null;
        try {
            archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(archiverName, inputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws ArchiveException, IOException {
        // AR
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass());

        // ARJ
        archiverName = ArchiveStreamFactory.ARJ;
        if (ArchiveStreamFactory.entryEncoding != null) {
            archiveOutputStream = new ArchiveStreamFactory(ArchiveStreamFactory.entryEncoding)
                    .createArchiveOutputStream(archiverName, outputStream);
        } else {
            archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        }
        assertNotNull(archiveOutputStream);
        assertEquals(ArjArchiveOutputStream.class, archiveOutputStream.getClass());

        // JAR
        archiverName = ArchiveStreamFactory.JAR;
        archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass());

        // ZIP
        archiverName = ArchiveStreamFactory.ZIP;
        archiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(archiverName, outputStream);
        assertNotNull(archiveOutputStream);
        assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());

        // TAR
        archiverName = ArchiveStreamFactory.TAR;
        if (ArchiveStreamFactory.entryEncoding != null) {
            archiveOutputStream = new ArchiveStreamFactory(ArchiveStreamFactory.entryEncoding)
                    .createArchiveOutputStream(archiverName, outputStream);
        } else {
            archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        }
        assertNotNull(archiveOutputStream);
        assertEquals(TarArchiveOutputStream.class, archiveOutputStream.getClass());

        // CPIO
        archiverName = ArchiveStreamFactory.CPIO;
        if (ArchiveStreamFactory.entryEncoding != null) {
            archiveOutputStream = new ArchiveStreamFactory(ArchiveStreamFactory.entryEncoding)
                    .createArchiveOutputStream(archiverName, outputStream);
        } else {
            archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        }
        assertNotNull(archiveOutputStream);
        assertEquals(CpioArchiveOutputStream.class, archiveOutputStream.getClass());

        // SEVEN_Z
        archiverName = ArchiveStreamFactory.SEVEN_Z;
        try {
            archiveOutputStream = new ArchiveStreamFactory()
                    .createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Invalid archiver name
        archiverName = "unknown";
        try {
            archiveOutputStream = new ArchiveStreamFactory()
                    .createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }

        // Null archiver name or output stream
        archiverName = ArchiveStreamFactory.AR;
        outputStream = null;
        try {
            archiveOutputStream = new ArchiveStreamFactory()
                    .createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        archiverName = null;
        try {
            archiveOutputStream = new ArchiveStreamFactory()
                    .createArchiveOutputStream(archiverName, outputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws ArchiveException, IOException {
        // AR
        InputStream inputStream = new ByteArrayInputStream("AR archive header".getBytes());
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(ArArchiveInputStream.class, archiveInputStream.getClass());

        // ARJ
        inputStream = new ByteArrayInputStream("ARJ archive header".getBytes());
        if (ArchiveStreamFactory.entryEncoding != null) {
            archiveInputStream = new ArchiveStreamFactory(ArchiveStreamFactory.entryEncoding)
                    .createArchiveInputStream(inputStream);
        } else {
            archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(inputStream);
        }
        assertNotNull(archiveInputStream);
        assertEquals(ArjArchiveInputStream.class, archiveInputStream.getClass());

        // JAR
        inputStream = new ByteArrayInputStream("JAR archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(JarArchiveInputStream.class, archiveInputStream.getClass());

        // ZIP
        inputStream = new ByteArrayInputStream("ZIP archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());

        // TAR
        inputStream = new ByteArrayInputStream("TAR archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(TarArchiveInputStream.class, archiveInputStream.getClass());

        // CPIO
        inputStream = new ByteArrayInputStream("CPIO archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(CpioArchiveInputStream.class, archiveInputStream.getClass());

        // DUMP
        inputStream = new ByteArrayInputStream("DUMP archive header".getBytes());
        archiveInputStream = new ArchiveStreamFactory()
                .createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertEquals(DumpArchiveInputStream.class, archiveInputStream.getClass());

        // SEVEN_Z
        inputStream = new ByteArrayInputStream("7z archive header".getBytes());
        try {
            archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(inputStream);
            Assert.fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Null input stream
        inputStream = null;
        try {
            archiveInputStream = new ArchiveStreamFactory()
                    .createArchiveInputStream(inputStream);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}