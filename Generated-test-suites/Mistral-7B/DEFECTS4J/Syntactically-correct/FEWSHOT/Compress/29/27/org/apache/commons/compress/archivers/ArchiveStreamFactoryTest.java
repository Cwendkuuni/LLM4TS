package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream; // Unsupported as an output stream type
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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(IOUtils.toByteArray(getClass().getResourceAsStream("/test-data/test.zip")));
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertNotNull(archiveInputStream);
        JarArchiveInputStream jarInputStream = (JarArchiveInputStream) archiveInputStream;
        assertTrue(jarInputStream.getNextJarEntry() != null);
        archiveInputStream.close();

        // Test JAR
        InputStream jarInputStream1 = new ByteArrayInputStream(IOUtils.toByteArray(getClass().getResourceAsStream("/test-data/test.jar")));
        ArchiveInputStream archiveInputStream1 = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream1);
        assertNotNull(archiveInputStream1);
        JarArchiveInputStream jarInputStream2 = (JarArchiveInputStream) archiveInputStream1;
        assertTrue(jarInputStream2.getNextJarEntry() != null);
        archiveInputStream1.close();

        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(IOUtils.toByteArray(getClass().getResourceAsStream("/test-data/test.ar")));
        ArchiveInputStream archiveInputStream2 = factory.createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertNotNull(archiveInputStream2);
        ArArchiveInputStream arInput = (ArArchiveInputStream) archiveInputStream2;
        assertTrue(arInput.getNextEntry() != null);
        archiveInputStream2.close();

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(IOUtils.toByteArray(getClass().getResourceAsStream("/test-data/test.tar")));
        ArchiveInputStream archiveInputStream3 = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertNotNull(archiveInputStream3);
        TarArchiveInputStream tarInput = (TarArchiveInputStream) archiveInputStream3;
        assertTrue(tarInput.getNextEntry() != null);
        archiveInputStream3.close();

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(IOUtils.toByteArray(getClass().getResourceAsStream("/test-data/test.cpio")));
        ArchiveInputStream archiveInputStream4 = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertNotNull(archiveInputStream4);
        CpioArchiveInputStream cpioInput = (CpioArchiveInputStream) archiveInputStream4;
        assertTrue(cpioInput.getNextEntry() != null);
        archiveInputStream4.close();

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(IOUtils.toByteArray(getClass().getResourceAsStream("/test-data/test.dump")));
        ArchiveInputStream archiveInputStream5 = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertNotNull(archiveInputStream5);
        DumpArchiveInputStream dumpInput = (DumpArchiveInputStream) archiveInputStream5;
        assertTrue(dumpInput.getNextEntry() != null);
        archiveInputStream5.close();

        // Test SEVEN_Z
        InputStream sevenZInputStream = new ByteArrayInputStream(IOUtils.toByteArray(getClass().getResourceAsStream("/test-data/test.7z")));
        try {
            ArchiveInputStream archiveInputStream6 = factory.createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, sevenZInputStream);
            fail("Expected StreamingNotSupportedException for SEVEN_Z format.");
        } catch (StreamingNotSupportedException e) {
            // Expected
        }

        // Test null input stream
        InputStream nullInputStream = null;
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, nullInputStream);
            fail("Expected IllegalArgumentException for null input stream.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null archiver name
        String nullArchiverName = null;
        try {
            factory.createArchiveInputStream(nullArchiverName, zipInputStream);
            fail("Expected IllegalArgumentException for null archiver name.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test unknown archiver name
        String unknownArchiverName = "unknown";
        try {
            factory.createArchiveInputStream(unknownArchiverName, zipInputStream);
            fail("Expected ArchiveException for unknown archiver name.");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        assertNotNull(archiveOutputStream);
        JarArchiveOutputStream zipOutput = (JarArchiveOutputStream) archiveOutputStream;
        zipOutput.putNextEntry(new JarEntry("test.txt"));
        zipOutput.write("Hello World!".getBytes());
        zipOutput.closeArchiveEntry();
        zipOutputStream.close();
        byte[] zipBytes = ((ByteArrayOutputStream) zipOutputStream).toByteArray();
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(zipBytes));
        JarArchiveEntry jarEntry = (JarArchiveEntry) archiveInputStream.getNextJarEntry();
        assertNotNull(jarEntry);
        assertEquals("test.txt", jarEntry.getName());
        archiveInputStream.close();

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream1 = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        assertNotNull(archiveOutputStream1);
        JarArchiveOutputStream jarOutput = (JarArchiveOutputStream) archiveOutputStream1;
        jarOutput.putNextEntry(new JarEntry("test.txt"));
        jarOutput.write("Hello World!".getBytes());
        jarOutput.closeArchiveEntry();
        jarOutputStream.close();
        byte[] jarBytes = ((ByteArrayOutputStream) jarOutputStream).toByteArray();
        ArchiveInputStream archiveInputStream1 = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(jarBytes));
        JarArchiveEntry jarEntry1 = (JarArchiveEntry) archiveInputStream1.getNextJarEntry();
        assertNotNull(jarEntry1);
        assertEquals("test.txt", jarEntry1.getName());
        archiveInputStream1.close();

        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream2 = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        assertNotNull(archiveOutputStream2);
        ArArchiveOutputStream arOutput = (ArArchiveOutputStream) archiveOutputStream2;
        arOutput.putRecord(new ArHeader(null, ArHeader.FORMAT_AR, null, null, null, null));
        ArEntry arEntry = new ArEntry("test.txt");
        arOutput.putNextEntry(arEntry);
        arOutput.write("Hello World!".getBytes());
        arOutput.closeArchiveEntry();
        arOutputStream.close();
        byte[] arBytes = ((ByteArrayOutputStream) arOutputStream).toByteArray();
        ArchiveInputStream archiveInputStream2 = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(arBytes));
        ArEntry arEntry1 = (ArEntry) archiveInputStream2.getNextEntry();
        assertNotNull(arEntry1);
        assertEquals("test.txt", arEntry1.getName());
        archiveInputStream2.close();

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream3 = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        assertNotNull(archiveOutputStream3);
        TarArchiveOutputStream tarOutput = (TarArchiveOutputStream) archiveOutputStream3;
        tarOutput.putNextEntry(new TarEntry("test.txt"));
        tarOutput.write("Hello World!".getBytes());
        tarOutput.closeArchiveEntry();
        tarOutputStream.close();
        byte[] tarBytes = ((ByteArrayOutputStream) tarOutputStream).toByteArray();
        ArchiveInputStream archiveInputStream3 = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(tarBytes));
        TarEntry tarEntry = (TarEntry) archiveInputStream3.getNextEntry();
        assertNotNull(tarEntry);
        assertEquals("test.txt", tarEntry.getName());
        archiveInputStream3.close();

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream4 = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, cpioOutputStream);
        assertNotNull(archiveOutputStream4);
        CpioArchiveOutputStream cpioOutput = (CpioArchiveOutputStream) archiveOutputStream4;
        cpioOutput.putNextHeader(new CpioHeader("test.txt", null, 0, null, null, null, null, null, null));
        cpioOutput.write("Hello World!".getBytes());
        cpioOutput.closeArchiveEntry();
        cpioOutputStream.close();
        byte[] cpioBytes = ((ByteArrayOutputStream) cpioOutputStream).toByteArray();
        ArchiveInputStream archiveInputStream4 = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(cpioBytes));
        CpioArchiveEntry cpioEntry = (CpioArchiveEntry) archiveInputStream4.getNextEntry();
        assertNotNull(cpioEntry);
        assertEquals("test.txt", cpioEntry.getName());
        archiveInputStream4.close();

        // Test SEVEN_Z
        OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream5 = factory.createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, sevenZOutputStream);
        // SevenZFile is not supported as an output stream type, so we cannot test it here
        archiveOutputStream5.close();

        // Test null output stream
        OutputStream nullOutputStream = null;
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, nullOutputStream);
            fail("Expected IllegalArgumentException for null output stream.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null archiver name
        String nullArchiverName1 = null;
        try {
            factory.createArchiveOutputStream(nullArchiverName1, zipOutputStream);
            fail("Expected IllegalArgumentException for null archiver name.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test unknown archiver name
        String unknownArchiverName1 = "unknown";
        try {
            factory.createArchiveOutputStream(unknownArchiverName1, zipOutputStream);
            fail("Expected ArchiveException for unknown archiver name.");
        } catch (ArchiveException e) {
            // Expected
        }
    }
}