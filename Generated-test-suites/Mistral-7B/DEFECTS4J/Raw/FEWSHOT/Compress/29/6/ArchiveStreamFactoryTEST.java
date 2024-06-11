package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR
        final ArchiveInputStream arInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(arInputStream);
        assertNull(arInputStream.getNextEntry());
        arInputStream.close();

        // Test ARJ
        final ArchiveInputStream arjInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0]));
        assertNotNull(arjInputStream);
        assertNull(arjInputStream.getNextEntry());
        arjInputStream.close();

        // Test CPIO
        final ArchiveInputStream cpioInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertNotNull(cpioInputStream);
        assertNull(cpioInputStream.getNextEntry());
        cpioInputStream.close();

        // Test DUMP
        final ArchiveInputStream dumpInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(dumpInputStream);
        assertNull(dumpInputStream.getNextEntry());
        dumpInputStream.close();

        // Test JAR
        final InputStream jarInputStream = new ByteArrayInputStream(new byte[100]);
        final ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertNotNull(jarArchiveInputStream);
        assertNull(jarArchiveInputStream.getNextEntry());
        jarArchiveInputStream.close();

        // Test Jar with entry encoding
        final InputStream jarInputStreamWithEncoding = new ByteArrayInputStream(
                ("UTF-8\n" + "name\n" + "size\n" + "0\n").getBytes("UTF-8"));
        final ArchiveInputStream jarArchiveInputStreamWithEncoding = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStreamWithEncoding);
        assertNotNull(jarArchiveInputStreamWithEncoding);
        assertNull(jarArchiveInputStreamWithEncoding.getNextEntry());
        JarArchiveEntry entry = (JarArchiveEntry) jarArchiveInputStreamWithEncoding.getNextEntry();
        assertNotNull(entry);
        assertEquals("name", entry.getName());
        jarArchiveInputStreamWithEncoding.close();

        // Test TAR
        final ArchiveInputStream tarInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(tarInputStream);
        assertNull(tarInputStream.getNextEntry());
        tarInputStream.close();

        // Test ZIP
        final InputStream zipInputStream = new ByteArrayInputStream(new byte[100]);
        final ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertNotNull(zipArchiveInputStream);
        assertNull(zipArchiveInputStream.getNextEntry());
        zipArchiveInputStream.close();

        // Test SEVEN_Z
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, new ByteArrayInputStream(new byte[0]));
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR
        final OutputStream arOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream arArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, arOutputStream);
        arArchiveOutputStream.close();
        assertFalse(arOutputStream.toString().isEmpty());

        // Test ARJ
        try {
            new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.ARJ, new ByteArrayOutputStream());
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }

        // Test CPIO
        try {
            new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }

        // Test DUMP
        try {
            new ArchiveStreamFactory()
                    .createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream());
            fail("Expected StreamingNotSupportedException");
        } catch (StreamingNotSupportedException e) {
            // expected
        }

        // Test JAR
        final OutputStream jarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        jarArchiveOutputStream.putNextEntry(new JarArchiveEntry("name.txt"));
        jarArchiveOutputStream.write("Hello World!".getBytes());
        jarArchiveOutputStream.closeArchiveEntry();
        jarArchiveOutputStream.finish();
        assertFalse(jarOutputStream.toString().isEmpty());

        // Test TAR
        final OutputStream tarOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        final TarArchiveEntry tarEntry = new TarArchiveEntry("name.txt");
        tarEntry.setSize(10);
        tarArchiveOutputStream.putNextEntry(tarEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        assertFalse(tarOutputStream.toString().isEmpty());

        // Test ZIP
        final OutputStream zipOutputStream = new ByteArrayOutputStream();
        final ArchiveOutputStream zipArchiveOutputStream = new ArchiveStreamFactory()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutputStream);
        zipArchiveOutputStream.putNextEntry(new ZipArchiveEntry("name.txt"));
        zipArchiveOutputStream.write("Hello World!".getBytes());
        zipArchiveOutputStream.closeArchiveEntry();
        zipArchiveOutputStream.finish();
        assertFalse(zipOutputStream.toString().isEmpty());

        // Test SEVEN_Z
        final OutputStream sevenZOutputStream = new ByteArrayOutputStream();
        SevenZFile sevenZFile = new SevenZFile(sevenZOutputStream, CompressionMode.ENCODING, "test.7z");
        sevenZFile.createHeader(new SevenZEntry("name.txt").setSize(10).setMethod(CompressionMethod.STORED));
        sevenZFile.addEntry("name.txt", new ByteArrayInputStream(new byte[10]));
        sevenZFile.writeEntryDetails("name.txt", new byte[0]);
        sevenZFile.finish();
        assertFalse(sevenZOutputStream.toString().isEmpty());
    }
}