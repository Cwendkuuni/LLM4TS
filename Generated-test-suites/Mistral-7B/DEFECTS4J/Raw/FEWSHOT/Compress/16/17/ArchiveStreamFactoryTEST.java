package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        // Test with known archive types
        InputStream knownArchiveStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.ZIP, knownArchiveStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.TAR, knownArchiveStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.JAR, knownArchiveStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.AR, knownArchiveStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.CPIO, knownArchiveStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(ArchiveStreamFactory.DUMP, knownArchiveStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
        archiveInputStream.close();

        // Test with unknown archive type
        InputStream unknownArchiveStream = new ByteArrayInputStream(new byte[0]);
        assertNull(ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", unknownArchiveStream));
        unknownArchiveStream.close();
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test with known archive types
        OutputStream knownArchiveStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.ZIP, knownArchiveStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.finish();
        knownArchiveStream.close();

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.TAR, knownArchiveStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.finish();
        knownArchiveStream.close();

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.JAR, knownArchiveStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.finish();
        knownArchiveStream.close();

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.AR, knownArchiveStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.finish();
        knownArchiveStream.close();

        archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(ArchiveStreamFactory.CPIO, knownArchiveStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
        archiveOutputStream.finish();
        knownArchiveStream.close();

        // Test with unknown archive type
        OutputStream unknownArchiveStream = new ByteArrayOutputStream();
        assertNull(ArchiveStreamFactory.getInstance().createArchiveOutputStream("unknown", unknownArchiveStream));
        unknownArchiveStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromFirstBytes() throws IOException {
        // Test with known archive types
        InputStream knownArchiveStream = new ByteArrayInputStream(new byte[0]);

        // Zip
        ByteArrayOutputStream zipOutput = new ByteArrayOutputStream();
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(zipOutput);
        zipOutputStream.putNextEntry(new ZipEntry("test.txt"));
        zipOutputStream.write("Test content.".getBytes());
        zipOutputStream.finish();
        zipOutput.close();
        InputStream zipInputStream = new ByteArrayInputStream(zipOutput.toByteArray());

        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
        TarEntry zipEntry = (TarEntry) archiveInputStream.getNextEntry();
        assertInstanceOf(ZipEntry.class, zipEntry);
        assertEquals("test.txt", zipEntry.getName());
        assertInstanceOf(InputStream.class, archiveInputStream.getInputStream());
        archiveInputStream.close();
        zipInputStream.close();

        // Tar
        ByteArrayOutputStream tarOutput = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(tarOutput);
        TarEntry tarEntry = new TarEntry("test.txt");
        tarEntry.setSize(13);
        tarEntry.setName("test.txt");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.writeTo(new ByteArrayOutputStream(13), 0, 13);
        tarOutputStream.finish();
        tarOutput.close();
        InputStream tarInputStream = new ByteArrayInputStream(tarOutput.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
        TarEntry tarArchiveEntry = (TarEntry) archiveInputStream.getNextEntry();
        assertInstanceOf(TarEntry.class, tarArchiveEntry);
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertInstanceOf(InputStream.class, archiveInputStream.getInputStream());
        archiveInputStream.close();
        tarInputStream.close();

        // Jar
        ByteArrayOutputStream jarOutput = new ByteArrayOutputStream();
        JarArchiveOutputStream jarOutputStream = new JarArchiveOutputStream(jarOutput);
        JarEntry jarEntry = new JarEntry("test.txt");
        jarEntry.setSize(13);
        jarEntry.setName("test.txt");
        jarOutputStream.putNextEntry(jarEntry);
        jarOutputStream.writeTo(new ByteArrayOutputStream(13), 0, 13);
        jarOutputStream.finish();
        jarOutput.close();
        InputStream jarInputStream = new ByteArrayInputStream(jarOutput.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
        ZipEntry jarArchiveEntry = (ZipEntry) archiveInputStream.getNextEntry();
        assertInstanceOf(ZipEntry.class, jarArchiveEntry);
        assertEquals("test.txt", jarArchiveEntry.getName());
        assertInstanceOf(InputStream.class, archiveInputStream.getInputStream());
        archiveInputStream.close();
        jarInputStream.close();

        // AR
        ByteArrayOutputStream arOutput = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutputStream = new ArArchiveOutputStream(arOutput);
        arOutputStream.putNextEntry(new ArArchiveEntry("test.txt"));
        arOutputStream.writeTo(new ByteArrayOutputStream(13), 0, 13);
        arOutputStream.finish();
        arOutput.close();
        InputStream arInputStream = new ByteArrayInputStream(arOutput.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(arInputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
        ArArchiveEntry arArchiveEntry = (ArArchiveEntry) archiveInputStream.getNextEntry();
        assertInstanceOf(ArArchiveEntry.class, arArchiveEntry);
        assertEquals("test.txt", arArchiveEntry.getName());
        assertInstanceOf(InputStream.class, archiveInputStream.getInputStream());
        archiveInputStream.close();
        arInputStream.close();

        // CPIO
        ByteArrayOutputStream cpioOutput = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(cpioOutput);
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry("test.txt", "test.txt", null, 13);
        cpioOutputStream.putNextEntry(cpioEntry);
        cpioOutputStream.writeTo(new ByteArrayOutputStream(13), 0, 13);
        cpioOutputStream.finish();
        cpioOutput.close();
        InputStream cpioInputStream = new ByteArrayInputStream(cpioOutput.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
        CpioArchiveEntry cpioArchiveEntry = (CpioArchiveEntry) archiveInputStream.getNextEntry();
        assertInstanceOf(CpioArchiveEntry.class, cpioArchiveEntry);
        assertEquals("test.txt", cpioArchiveEntry.getName());
        assertEquals("test.txt", cpioArchiveEntry.getFileName());
        assertInstanceOf(InputStream.class, archiveInputStream.getInputStream());
        archiveInputStream.close();
        cpioInputStream.close();

        // Dump
        ByteArrayOutputStream dumpOutput = new ByteArrayOutputStream();
        DumpArchiveOutputStream dumpOutputStream = new DumpArchiveOutputStream(dumpOutput, new File("test.txt").getAbsolutePath());
        dumpOutputStream.putNextEntry(new DumpEntry(new File("test.txt").getAbsolutePath(), false));
        dumpOutputStream.writeTo(new ByteArrayOutputStream(13), 0, 13);
        dumpOutputStream.finish();
        dumpOutput.close();
        InputStream dumpInputStream = new ByteArrayInputStream(dumpOutput.toByteArray());

        archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
        DumpArchiveEntry dumpArchiveEntry = (DumpArchiveEntry) archiveInputStream.getNextEntry();
        assertInstanceOf(DumpArchiveEntry.class, dumpArchiveEntry);
        assertEquals("test.txt", dumpArchiveEntry.getName());
        assertEquals("test.txt", dumpArchiveEntry.getSourceFile());
        assertInstanceOf(InputStream.class, archiveInputStream.getInputStream());
        archiveInputStream.close();
        dumpInputStream.close();

        knownArchiveStream.close();
    }
}