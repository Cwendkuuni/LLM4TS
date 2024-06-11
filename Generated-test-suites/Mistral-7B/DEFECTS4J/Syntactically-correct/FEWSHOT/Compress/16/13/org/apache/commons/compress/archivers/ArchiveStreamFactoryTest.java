package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.tar.TarHeader;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStreamFromNameAndInputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assertNull(archiveInputStream);
            inputStream.close();
        }

        String arArchiverName = ArchiveStreamFactory.AR;
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(arArchiverName, arInputStream);
        assertInstanceOf(ArArchiveInputStream.class, arArchiveInputStream);
        arInputStream.close();

        String zipArchiverName = ArchiveStreamFactory.ZIP;
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(zipArchiverName, zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipArchiveInputStream);
        zipInputStream.close();

        String tarArchiverName = ArchiveStreamFactory.TAR;
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(tarArchiverName, tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarArchiveInputStream);
        tarInputStream.close();

        String jarArchiverName = ArchiveStreamFactory.JAR;
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(jarArchiverName, jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarArchiveInputStream);
        jarInputStream.close();

        String cpioArchiverName = ArchiveStreamFactory.CPIO;
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(cpioArchiverName, cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioArchiveInputStream);
        cpioInputStream.close();

        String dumpArchiverName = ArchiveStreamFactory.DUMP;
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(dumpArchiverName, dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpArchiveInputStream);
        dumpInputStream.close();

        String unknownArchiverName = "unknown";
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream unknownArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(unknownArchiverName, unknownInputStream);
        assertNull(unknownArchiveInputStream);
        unknownInputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
            assertNull(archiveInputStream);
            inputStream.close();
        }

        String arArchiverName = ArchiveStreamFactory.AR;
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(arInputStream);
        assertInstanceOf(ArArchiveInputStream.class, arArchiveInputStream);
        arInputStream.close();

        String zipArchiverName = ArchiveStreamFactory.ZIP;
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(zipInputStream);
        assertInstanceOf(ZipArchiveInputStream.class, zipArchiveInputStream);
        zipInputStream.close();

        String tarArchiverName = ArchiveStreamFactory.TAR;
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(tarInputStream);
        assertInstanceOf(TarArchiveInputStream.class, tarArchiveInputStream);
        tarInputStream.close();

        String jarArchiverName = ArchiveStreamFactory.JAR;
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(jarInputStream);
        assertInstanceOf(JarArchiveInputStream.class, jarArchiveInputStream);
        jarInputStream.close();

        String cpioArchiverName = ArchiveStreamFactory.CPIO;
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(cpioInputStream);
        assertInstanceOf(CpioArchiveInputStream.class, cpioArchiveInputStream);
        cpioInputStream.close();

        String dumpArchiverName = ArchiveStreamFactory.DUMP;
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(dumpInputStream);
        assertInstanceOf(DumpArchiveInputStream.class, dumpArchiveInputStream);
        dumpInputStream.close();

        InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream unknownArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(unknownInputStream);
        assertNull(unknownArchiveInputStream);
        unknownInputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromNameAndOutputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertNull(archiveOutputStream);
            outputStream.close();
        }

        String arArchiverName = ArchiveStreamFactory.AR;
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(arArchiverName, arOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, arArchiveOutputStream);
        arOutputStream.close();

        String zipArchiverName = ArchiveStreamFactory.ZIP;
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(zipArchiverName, zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipArchiveOutputStream);
        zipOutputStream.close();

        String tarArchiverName = ArchiveStreamFactory.TAR;
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(tarArchiverName, tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, tarArchiveOutputStream);
        tarOutputStream.close();

        String jarArchiverName = ArchiveStreamFactory.JAR;
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(jarArchiverName, jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarArchiveOutputStream);
        jarOutputStream.close();

        String cpioArchiverName = ArchiveStreamFactory.CPIO;
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(cpioArchiverName, cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, cpioArchiveOutputStream);
        cpioOutputStream.close();

        String dumpArchiverName = ArchiveStreamFactory.DUMP;
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(dumpArchiverName, dumpOutputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, dumpArchiveOutputStream);
        dumpOutputStream.close();

        String unknownArchiverName = "unknown";
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream unknownArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(unknownArchiverName, unknownOutputStream);
        assertNull(unknownArchiveOutputStream);
        unknownOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamFromOutputStream() throws Exception {
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(outputStream);
            assertNull(archiveOutputStream);
            outputStream.close();
        }

        String arArchiverName = ArchiveStreamFactory.AR;
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(arArchiverName, arOutputStream);
        assertInstanceOf(ArArchiveOutputStream.class, arArchiveOutputStream);
        arOutputStream.close();

        String zipArchiverName = ArchiveStreamFactory.ZIP;
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(zipArchiverName, zipOutputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, zipArchiveOutputStream);
        zipOutputStream.close();

        String tarArchiverName = ArchiveStreamFactory.TAR;
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(tarArchiverName, tarOutputStream);
        assertInstanceOf(TarArchiveOutputStream.class, tarArchiveOutputStream);
        tarOutputStream.close();

        String jarArchiverName = ArchiveStreamFactory.JAR;
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(jarArchiverName, jarOutputStream);
        assertInstanceOf(JarArchiveOutputStream.class, jarArchiveOutputStream);
        jarOutputStream.close();

        String cpioArchiverName = ArchiveStreamFactory.CPIO;
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(cpioArchiverName, cpioOutputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, cpioArchiveOutputStream);
        cpioOutputStream.close();

        String dumpArchiverName = ArchiveStreamFactory.DUMP;
        OutputStream dumpOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(dumpArchiverName, dumpOutputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, dumpArchiveOutputStream);
        dumpOutputStream.close();

        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream unknownArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(unknownOutputStream);
        assertNull(unknownArchiveOutputStream);
        unknownOutputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithEntry() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        String tarArchiverName = ArchiveStreamFactory.TAR;
        ArchiveOutputStream tarArchiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(tarArchiverName, outputStream);

        TarArchiveEntry entry = new TarArchiveEntry("foo.txt", "foo content".getBytes());
        tarArchiveOutputStream.putNextEntry(entry);
        tarArchiveOutputStream.write(entry.getData().getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream tarArchiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(tarArchiverName, inputStream);
        TarArchiveEntry tarEntry = (TarArchiveEntry) tarArchiveInputStream.getNextEntry();
        assertInstanceOf(TarArchiveEntry.class, tarEntry);
        assertInstanceOf(ZipEntry.class, tarEntry.getSourcedFile());
        byte[] data = tarEntry.getData();
        assertNotNull(data);
        assertEquals("foo content", new String(data, "UTF-8"));

        inputStream.close();
        tarArchiveInputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveInputStreamWithInvalidSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertNull(archiveInputStream);
        inputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithInvalidArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String invalidArchiverName = "invalid";
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(invalidArchiverName, inputStream);
        assertNull(archiveInputStream);
        inputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        String arArchiverName = ArchiveStreamFactory.AR;
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(arArchiverName, null);
        assertNull(archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(null, inputStream);
        assertNull(archiveInputStream);
        inputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnsupportedSignature() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance().createArchiveInputStream(inputStream);
        assertNull(archiveInputStream);
        inputStream.close();
    }

    @Test
    public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        String invalidArchiverName = "invalid";
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(invalidArchiverName, outputStream);
        assertNull(archiveOutputStream);
        outputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        String arArchiverName = ArchiveStreamFactory.AR;
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(arArchiverName, null);
        assertNull(archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, outputStream);
        assertNull(archiveOutputStream);
        outputStream.close();
    }
}