package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

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
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException, IOException {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        // Test Dump
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        // Test invalid archiver name
        InputStream invalidInputStream = new ByteArrayInputStream(new byte[0]);
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(null, invalidInputStream));

        // Test null input stream
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);

        // Test Dump
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.DUMP, dumpOutputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);

        // Test invalid archiver name
        OutputStream invalidOutputStream = new ByteArrayOutputStream();
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, invalidOutputStream));

        // Test null output stream
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveInputStreamWithSignature() throws ArchiveException, IOException {
        // Test ZIP
        byte[] zipSignature = new byte[4];
        System.arraycopy(ZipArchiveInputStream.getMagic(), 0, zipSignature, 0, ZipArchiveInputStream.getMagic().length);
        InputStream zipInputStream = new ByteArrayInputStream(zipSignature);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);

        // Test JAR
        byte[] jarSignature = new byte[2];
        System.arraycopy(JarArchiveInputStream.getMagic(), 0, jarSignature, 0, JarArchiveInputStream.getMagic().length);
        InputStream jarInputStream = new ByteArrayInputStream(jarSignature);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);

        // Test AR
        byte[] arSignature = new byte[6];
        System.arraycopy(ArArchiveInputStream.getMagic(), 0, arSignature, 0, ArArchiveInputStream.getMagic().length);
        InputStream arInputStream = new ByteArrayInputStream(arSignature);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);

        // Test CPIO
        byte[] cpioSignature = new byte[7];
        System.arraycopy(CpioArchiveInputStream.getMagic(), 0, cpioSignature, 0, CpioArchiveInputStream.getMagic().length);
        InputStream cpioInputStream = new ByteArrayInputStream(cpioSignature);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);

        // Test TAR
        byte[] tarSignature = new byte[8];
        System.arraycopy(TarArchiveInputStream.getMagic(), 0, tarSignature, 0, TarArchiveInputStream.getMagic().length);
        InputStream tarInputStream = new ByteArrayInputStream(tarSignature);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);

        // Test DUMP
        byte[] dumpSignature = new byte[8];
        System.arraycopy(DumpArchiveInputStream.getMagic(), 0, dumpSignature, 0, DumpArchiveInputStream.getMagic().length);
        InputStream dumpInputStream = new ByteArrayInputStream(dumpSignature);
        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
    }
}