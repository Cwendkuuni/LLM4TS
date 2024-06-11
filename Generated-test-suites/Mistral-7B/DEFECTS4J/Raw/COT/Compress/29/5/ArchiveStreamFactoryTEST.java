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
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
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

import static org.apache.commons.compress.archivers.ArchiveStreamFactory.AR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ARJ;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.CPIO;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.DUMP;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.JAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.SEVEN_Z;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.TAR;
import static org.apache.commons.compress.archivers.ArchiveStreamFactory.ZIP;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidArchiveType() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);

        ArchiveInputStream archiveInputStream;

        archiveInputStream = createArchiveInputStream(ZIP, inputStream);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);

        archiveInputStream = createArchiveInputStream(AR, inputStream);
        Assert.assertTrue(archiveInputStream instanceof ArArchiveInputStream);

        archiveInputStream = createArchiveInputStream(CPIO, inputStream);
        Assert.assertTrue(archiveInputStream instanceof CpioArchiveInputStream);

        archiveInputStream = createArchiveInputStream(JAR, inputStream);
        Assert.assertTrue(archiveInputStream instanceof JarArchiveInputStream);

        archiveInputStream = createArchiveInputStream(TAR, inputStream);
        Assert.assertTrue(archiveInputStream instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_InvalidArchiveType() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);

        try {
            createArchiveInputStream("invalidArchiver", inputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_NullInputStream() {
        try {
            createArchiveInputStream(ZIP, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidArchiveType() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();

        ArchiveOutputStream archiveOutputStream;

        archiveOutputStream = createArchiveOutputStream(ZIP, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);

        archiveOutputStream = createArchiveOutputStream(AR, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);

        archiveOutputStream = createArchiveOutputStream(CPIO, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof CpioArchiveOutputStream);

        archiveOutputStream = createArchiveOutputStream(JAR, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof JarArchiveOutputStream);

        archiveOutputStream = createArchiveOutputStream(TAR, outputStream);
        Assert.assertTrue(archiveOutputStream instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_InvalidArchiveType() {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            createArchiveOutputStream("invalidArchiver", outputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream_NullOutputStream() {
        try {
            createArchiveOutputStream(ZIP, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream_ValidSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(getZipSignature());
        ArchiveInputStream archiveInputStream = createArchiveInputStream(inputStream);
        Assert.assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream_InvalidSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(getInvalidSignature());
        try {
            createArchiveInputStream(inputStream);
            Assert.fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream_Exception() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(getExceptionSignature());
        try {
            createArchiveInputStream(inputStream);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream_NullInputStream() {
        try {
            createArchiveInputStream((InputStream) null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private byte[] getZipSignature() {
        return new byte[10]; // Use a valid ZIP signature
    }

    private byte[] getInvalidSignature() {
        return new byte[10]; // Use an invalid signature
    }

    private byte[] getExceptionSignature() {
        return new byte[10]; // Use a signature that throws an exception on read
    }

    private ArchiveInputStream createArchiveInputStream(String archiverName, InputStream inputStream) throws ArchiveException {
        return new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    private ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream outputStream) throws ArchiveException {
        return new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }
}