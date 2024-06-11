package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test AR archive
        byte[] arBytes = getArBytes();
        InputStream arInputStream = new ByteArrayInputStream(arBytes);
        ArchiveInputStream arArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, arInputStream);
        assertEquals(ArArchiveInputStream.class, arArchiveInputStream.getClass());

        // Test ARJ archive
        byte[] arjBytes = getArjBytes();
        InputStream arjInputStream = new ByteArrayInputStream(arjBytes);
        ArchiveInputStream arjArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, arjInputStream);
        assertEquals(ArjArchiveInputStream.class, arjArchiveInputStream.getClass());

        // Test CPIO archive
        byte[] cpioBytes = getCpioBytes();
        InputStream cpioInputStream = new ByteArrayInputStream(cpioBytes);
        ArchiveInputStream cpioArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, cpioInputStream);
        assertEquals(CpioArchiveInputStream.class, cpioArchiveInputStream.getClass());

        // Test Dump archive
        byte[] dumpBytes = getDumpBytes();
        InputStream dumpInputStream = new ByteArrayInputStream(dumpBytes);
        ArchiveInputStream dumpArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, dumpInputStream);
        assertEquals(DumpArchiveInputStream.class, dumpArchiveInputStream.getClass());

        // Test JAR archive
        byte[] jarBytes = getJarBytes();
        InputStream jarInputStream = new ByteArrayInputStream(jarBytes);
        ArchiveInputStream jarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, jarInputStream);
        assertEquals(JarArchiveInputStream.class, jarArchiveInputStream.getClass());

        // Test TAR archive
        byte[] tarBytes = getTarBytes();
        InputStream tarInputStream = new ByteArrayInputStream(tarBytes);
        ArchiveInputStream tarArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, tarInputStream);
        assertEquals(TarArchiveInputStream.class, tarArchiveInputStream.getClass());

        // Test ZIP archive
        byte[] zipBytes = getZipBytes();
        InputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        ArchiveInputStream zipArchiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, zipInputStream);
        assertEquals(ZipArchiveInputStream.class, zipArchiveInputStream.getClass());

        // Test unknown archive
        InputStream unknownInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", unknownInputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test AR archive
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
        assertEquals(ArArchiveOutputStream.class, arArchiveOutputStream.getClass());

        // Test JAR archive
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, jarOutputStream);
        assertEquals(JarArchiveOutputStream.class, jarArchiveOutputStream.getClass());

        // Test TAR archive
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarArchiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, tarOutputStream);
        assertEquals(TarArchiveOutputStream.class, tarArchiveOutputStream.getClass());

        // Test unknown archive
        OutputStream unknownOutputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", unknownOutputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
    }

    // Helper methods to create archive bytes

    private byte[] getArBytes() throws IOException {
        return createArBytes("test.txt", "Hello World!");
    }

    private byte[] getArBytes(String fileName, String contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);
        arOutputStream.putArchiveEntry(new ArArchiveEntry(fileName));
        IOUtils.copy(new ByteArrayInputStream(contents.getBytes()), arOutputStream);
        arOutputStream.closeArchiveEntry();
        arOutputStream.finish();
        arOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] getArjBytes() throws IOException {
        return createArjBytes("test.txt", "Hello World!");
    }

    private byte[] getArjBytes(String fileName, String contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjOutputStream = new ArjArchiveOutputStream(outputStream);
        arjOutputStream.putNextHeader(new ArjArchiveEntry(fileName));
        IOUtils.copy(new ByteArrayInputStream(contents.getBytes()), arjOutputStream);
        arjOutputStream.finish();
        arjOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] getCpioBytes() throws IOException {
        return createCpioBytes("test.txt", "Hello World!");
    }

    private byte[] getCpioBytes(String fileName, String contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.putNextEntry(new CpioArchiveEntry(fileName));
        IOUtils.copy(new ByteArrayInputStream(contents.getBytes()), cpioOutputStream);
        cpioOutputStream.closeEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] getDumpBytes() throws IOException {
        return createDumpBytes("test.txt", "Hello World!");
    }

    private byte[] getDumpBytes(String fileName, String contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpOutputStream = new DumpArchiveOutputStream(outputStream);
        dumpOutputStream.putNextEntry(new DumpArchiveEntry(fileName));
        IOUtils.copy(new ByteArrayInputStream(contents.getBytes()), dumpOutputStream);
        dumpOutputStream.closeEntry();
        dumpOutputStream.finish();
        dumpOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] getJarBytes() throws IOException {
        return createJarBytes("test.txt", "Hello World!");
    }

    private byte[] getJarBytes(String fileName, String contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = new JarArchiveOutputStream(outputStream);
        jarOutputStream.putNewArchiveEntry(new JarArchiveEntry(fileName));
        IOUtils.copy(new ByteArrayInputStream(contents.getBytes()), jarOutputStream);
        jarOutputStream.finish();
        jarOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] getTarBytes() throws IOException {
        return createTarBytes("test.txt", "Hello World!");
    }

    private byte[] getTarBytes(String fileName, String contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry(fileName);
        tarEntry.setSize(contents.length());
        tarEntry.setChecksum(IOUtils.toByteArray(IOUtils.toInputStream(IOUtils.toBytes(contents, "UTF-8"))));
        tarOutputStream.putNextEntry(tarEntry);
        IOUtils.copy(new ByteArrayInputStream(contents.getBytes()), tarOutputStream);
        tarOutputStream.finish();
        tarOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] getZipBytes() throws IOException {
        return createZipBytes("test.txt", "Hello World!");
    }

    private byte[] getZipBytes(String fileName, String contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream);
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(fileName);
        zipEntry.setSize(contents.length());
        zipOutputStream.putNextEntry(zipEntry);
        IOUtils.copy(new ByteArrayInputStream(contents.getBytes()), zipOutputStream);
        zipOutputStream.finish();
        zipOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] createArBytes(String fileName, String contents) throws IOException {
        return createArBytes(new ArArchiveEntry(fileName), contents.getBytes());
    }

    private byte[] createArBytes(ArArchiveEntry entry, byte[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOutputStream = new ArArchiveOutputStream(outputStream);
        arOutputStream.putArchiveEntry(entry);
        IOUtils.copy(new ByteArrayInputStream(contents), arOutputStream);
        arOutputStream.closeArchiveEntry();
        arOutputStream.finish();
        arOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] createArjBytes(String fileName, String contents) throws IOException {
        return createArjBytes(new ArjArchiveEntry(fileName), contents.getBytes());
    }

    private byte[] createArjBytes(ArjArchiveEntry entry, byte[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arjOutputStream = new ArjArchiveOutputStream(outputStream);
        arjOutputStream.putNextHeader(entry);
        IOUtils.copy(new ByteArrayInputStream(contents), arjOutputStream);
        arjOutputStream.finish();
        arjOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] createCpioBytes(String fileName, String contents) throws IOException {
        return createCpioBytes(new CpioArchiveEntry(fileName), contents.getBytes());
    }

    private byte[] createCpioBytes(CpioArchiveEntry entry, byte[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.putNextEntry(entry);
        IOUtils.copy(new ByteArrayInputStream(contents), cpioOutputStream);
        cpioOutputStream.closeEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] createDumpBytes(String fileName, String contents) throws IOException {
        return createDumpBytes(new DumpArchiveEntry(fileName), contents.getBytes());
    }

    private byte[] createDumpBytes(DumpArchiveEntry entry, byte[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream dumpOutputStream = new DumpArchiveOutputStream(outputStream);
        dumpOutputStream.putNextEntry(entry);
        IOUtils.copy(new ByteArrayInputStream(contents), dumpOutputStream);
        dumpOutputStream.closeEntry();
        dumpOutputStream.finish();
        dumpOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] createJarBytes(String fileName, String contents) throws IOException {
        return createJarBytes(new JarArchiveEntry(fileName), contents.getBytes());
    }

    private byte[] createJarBytes(JarArchiveEntry entry, byte[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOutputStream = new JarArchiveOutputStream(outputStream);
        jarOutputStream.putNewArchiveEntry(entry);
        IOUtils.copy(new ByteArrayInputStream(contents), jarOutputStream);
        jarOutputStream.finish();
        jarOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] createTarBytes(String fileName, String contents) throws IOException {
        return createTarBytes(new TarArchiveEntry(fileName), contents.getBytes());
    }

    private byte[] createTarBytes(TarArchiveEntry entry, byte[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry tarEntry = new TarArchiveEntry(fileName);
        tarEntry.setSize(contents.length());
        tarOutputStream.putNextEntry(tarEntry);
        IOUtils.copy(new ByteArrayInputStream(contents), tarOutputStream);
        tarOutputStream.finish();
        tarOutputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] createZipBytes(String fileName, String contents) throws IOException {
        return createZipBytes(new ZipArchiveEntry(fileName), contents.getBytes());
    }

    private byte[] createZipBytes(ZipArchiveEntry entry, byte[] contents) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream);
        zipOutputStream.putNextEntry(entry);
        IOUtils.copy(new ByteArrayInputStream(contents), zipOutputStream);
        zipOutputStream.finish();
        zipOutputStream.close();
        return outputStream.toByteArray();
    }
}