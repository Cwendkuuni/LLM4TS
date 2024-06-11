package org.apache.commons.compress.archivers.test;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.ar.ARArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ARArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test for {@link ArchiveStreamFactory} class.
 */
public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiveName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamUnknownFormat() throws Exception {
        String archiveName = "unknown";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveStreamFactory factory = ArchiveStreamFactory.newInstance();
        factory.createArchiveInputStream(archiveName, inputStream);
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiveName = "zip";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, outputStream);
        archiveOutputStream.putNextEntry(new JarArchiveEntry("test.txt"));
        archiveOutputStream.write("Hello, World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        JarArchiveInputStream jarArchiveInputStream = (JarArchiveInputStream) ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, testInputStream);
        JarEntry testEntry = jarArchiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", testEntry.getName());
        Assert.assertNull(jarArchiveInputStream.getNextEntry());
        jarArchiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamUnknownFormat() throws Exception {
        String archiveName = "unknown";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, outputStream);
    }

    @Test
    public void testCreateArchiveInputStreamFromSignature() throws Exception {
        String archiveName = "tar";
        InputStream inputStream = new ByteArrayInputStream(new byte[512]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, inputStream);
        Assert.assertNotNull(archiveInputStream);
        archiveInputStream.close();
    }

    @Test
    public void testCreateZipArchiveInputStream() throws Exception {
        String archiveName = "zip";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, inputStream);
        ZipArchiveEntry zipEntry = (ZipArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", zipEntry.getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateJarArchiveInputStream() throws Exception {
        String archiveName = "jar";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, inputStream);
        JarArchiveEntry jarEntry = (JarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test/test.txt", jarEntry.getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArArchiveInputStream() throws Exception {
        String archiveName = "ar";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, inputStream);
        ARArchiveEntry arEntry = (ARArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", arEntry.getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateCpioArchiveInputStream() throws Exception {
        String archiveName = "cpio";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, inputStream);
        CpioArchiveEntry cpioEntry = (CpioArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", cpioEntry.getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateDumpArchiveInputStream() throws Exception {
        String archiveName = "dump";
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, inputStream);
        DumpArchiveEntry dumpEntry = (DumpArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", dumpEntry.getName());
        archiveInputStream.close();
    }

    @Test
    public void testCreateTarArchiveOutputStream() throws Exception {
        String archiveName = "tar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, outputStream);
        archiveOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        archiveOutputStream.write("Hello, World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, testInputStream);
        TarArchiveEntry tarEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", tarEntry.getName());
        Assert.assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateArArchiveOutputStream() throws Exception {
        String archiveName = "ar";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, outputStream);
        archiveOutputStream.putNextEntry(new ARArchiveEntry("test.txt"));
        archiveOutputStream.write("Hello, World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, testInputStream);
        ARArchiveEntry arEntry = (ARArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", arEntry.getName());
        Assert.assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }

    @Test
    public void testCreateCpioArchiveOutputStream() throws Exception {
        String archiveName = "cpio";
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.newInstance().createArchiveOutputStream(archiveName, outputStream);
        archiveOutputStream.putNextEntry(new CpioArchiveEntry("test.txt"));
        archiveOutputStream.write("Hello, World!".getBytes());
        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.newInstance().createArchiveInputStream(archiveName, testInputStream);
        CpioArchiveEntry cpioEntry = (CpioArchiveEntry) archiveInputStream.getNextEntry();
        Assert.assertEquals("test.txt", cpioEntry.getName());
        Assert.assertNull(archiveInputStream.getNextEntry());
        archiveInputStream.close();
    }
}