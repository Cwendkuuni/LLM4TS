package org.junit.tests;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory;

    @Before
    public void setUp() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }

    @Test
    public void testCreateArchiveInputStreamWithValidArguments() throws Exception {
        String archiverName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        String archiverName = null;
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        String archiverName = "zip";
        InputStream inputStream = null;
        archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
        String archiverName = "invalid";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        archiveStreamFactory.createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidArguments() throws Exception {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
        Assert.assertNotNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        String archiverName = null;
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        String archiverName = "zip";
        OutputStream outputStream = null;
        archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws Exception {
        String archiverName = "invalid";
        OutputStream outputStream = new ByteArrayOutputStream();
        archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithValidSignature() throws Exception {
        String archiverName = "zip";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] zipSignature = new byte[0]; // valid ZIP signature
        OutputStream signatureOutputStream = new ByteArrayOutputStream();
        IOUtils.write(zipSignature, signatureOutputStream, null);
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(archiverName, os);
        aos.putNextEntry(new JarArchiveEntry("test.jar"));
        IOUtils.copy(signatureOutputStream.toByteArray(), aos, null);
        aos.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(archiverName, is);
        Assert.assertNotNull(ais);
        Assert.assertEquals("test.jar", ais.getNextEntry().getName());
        ais.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInputStreamWithInvalidSignature() throws Exception {
        String archiverName = "zip";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] invalidZipSignature = new byte[0]; // invalid ZIP signature
        OutputStream signatureOutputStream = new ByteArrayOutputStream();
        IOUtils.write(invalidZipSignature, signatureOutputStream, null);
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(archiverName, os);
        aos.putNextEntry(new JarArchiveEntry("test.jar"));
        IOUtils.copy(signatureOutputStream.toByteArray(), aos, null);
        aos.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(archiverName, is);
    }

    @Test
    public void testCreateArchiveInputStreamFromInputSteamWithNullSignature() throws Exception {
        String archiverName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        OutputStream signatureOutputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(archiverName, os);
        aos.putNextEntry(new JarArchiveEntry("test.jar"));
        aos.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(archiverName, is);
        Assert.assertNotNull(ais);
        ais.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStreamWithSupportedFormat() throws Exception {
        String archiverName = "tar";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(archiverName, os);
        aos.putNextEntry(new TarArchiveEntry("test.tar"));
        aos.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(archiverName, is);
        Assert.assertNotNull(ais);
        ais.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromInputStreamWithUnsupportedFormat() throws Exception {
        String archiverName = "ar"; // unsupported format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(archiverName, os);
        aos.putNextEntry(new ArArchiveEntry("test.ar"));
        aos.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        archiveStreamFactory.createArchiveInputStream(archiverName, is);
    }

    @Test
    public void testCreateArchiveOutputStreamWithValidSignature() throws Exception {
        String archiverName = "zip";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] zipSignature = new byte[0]; // valid ZIP signature
        OutputStream signatureOutputStream = new ByteArrayOutputStream();
        IOUtils.write(zipSignature, signatureOutputStream, null);
        OutputStream os = new ByteArrayOutputStream();
        ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(archiverName, os);
        aos.setComment("test comment");
        IOUtils.copy(signatureOutputStream.toByteArray(), aos, null);
        aos.putNextEntry(new JarArchiveEntry("test.jar"));
        aos.close();
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(archiverName, is);
        Assert.assertNotNull(ais);
        Assert.assertEquals("test comment", ais.getArchiveComment());
        ais.close();
    }
}