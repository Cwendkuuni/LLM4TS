package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;

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
import org.apache.commons.compress.archivers.test.ArchiveTestHelper;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

    @Test
    public void testCreateArchiveInputStream() throws IOException {
        testCreateArchiveInputStream(ArchiveStreamFactory.AR, getArTestFileInput());
        testCreateArchiveInputStream(ArchiveStreamFactory.CPIO, getCpioTestFileInput());
        testCreateArchiveInputStream(ArchiveStreamFactory.DUMP, getDumpTestFileInput());
        testCreateArchiveInputStream(ArchiveStreamFactory.JAR, getJarTestFileInput());
        testCreateArchiveInputStream(ArchiveStreamFactory.TAR, getTarTestFileInput());
        testCreateArchiveInputStream(ArchiveStreamFactory.ZIP, getZipTestFileInput());
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        testCreateArchiveOutputStream(ArchiveStreamFactory.AR, getArTestFileOutput());
        testCreateArchiveOutputStream(ArchiveStreamFactory.CPIO, getCpioTestFileOutput());
        testCreateArchiveOutputStream(ArchiveStreamFactory.DUMP, getDumpTestFileOutput());
        testCreateArchiveOutputStream(ArchiveStreamFactory.JAR, getJarTestFileOutput());
        testCreateArchiveOutputStream(ArchiveStreamFactory.TAR, getTarTestFileOutput());
        testCreateArchiveOutputStream(ArchiveStreamFactory.ZIP, getZipTestFileOutput());
    }

    @Test
    public void testCreateArchiveInputStreamFromBytes() throws IOException {
        testCreateArchiveInputStreamFromBytes(ArchiveStreamFactory.AR, getArTestFileBytes());
        testCreateArchiveInputStreamFromBytes(ArchiveStreamFactory.CPIO, getCpioTestFileBytes());
        testCreateArchiveInputStreamFromBytes(ArchiveStreamFactory.DUMP, getDumpTestFileBytes());
        testCreateArchiveInputStreamFromBytes(ArchiveStreamFactory.JAR, getJarTestFileBytes());
        testCreateArchiveInputStreamFromBytes(ArchiveStreamFactory.TAR, getTarTestFileBytes());
        testCreateArchiveInputStreamFromBytes(ArchiveStreamFactory.ZIP, getZipTestFileBytes());
    }

    private void testCreateArchiveOutputStream(final String archiverName, final OutputStream expectedOutputStream)
            throws IOException {
        final File tempFile = File.createTempFile("test", null);
        try (OutputStream out = new FileOutputStream(tempFile)) {
            ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
            assertEquals(expectedOutputStream, archiveOutputStream);
            archiveOutputStream.close();
        }
        assertEquals(expectedOutputStream.getClass(), Files.newInputStream(tempFile.toPath()).getChannel().map(FileChannel::mapReadOnly).map(FileByteChannelInput::new).openStream().getClass());
        Files.delete(tempFile.toPath());
    }

    private void testCreateArchiveInputStream(final String archiverName, final InputStream expectedInputStream) throws IOException {
        try (InputStream input = getTestFileInput(archiverName)) {
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, input);
            assertEquals(expectedInputStream, archiveInputStream);
            archiveInputStream.close();
        }
    }

    private void testCreateArchiveInputStreamFromBytes(final String archiverName, final byte[] expectedBytes)
            throws IOException {
        try (InputStream input = new ByteArrayInputStream(expectedBytes)) {
            ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(archiverName, input);
            assertEquals(expectedBytes.length, input.available());
            byte[] actualBytes = new byte[expectedBytes.length];
            archiveInputStream.read(actualBytes);
            assertArrayEquals(expectedBytes, actualBytes);
            archiveInputStream.close();
        }
    }

    private InputStream getArTestFileInput() throws IOException {
        return new FileInputStream("test/resources/test-data/archive-test/test.ar");
    }

    private InputStream getCpioTestFileInput() throws IOException {
        return new FileInputStream("test/resources/test-data/archive-test/test.cpio");
    }

    private InputStream getDumpTestFileInput() throws IOException {
        return new FileInputStream("test/resources/test-data/archive-test/test.dump");
    }

    private InputStream getJarTestFileInput() throws IOException {
        return new FileInputStream("test/resources/test-data/archive-test/test.jar");
    }

    private InputStream getTarTestFileInput() throws IOException {
        return new FileInputStream("test/resources/test-data/archive-test/test.tar");
    }

    private InputStream getZipTestFileInput() throws IOException {
        return new FileInputStream("test/resources/test-data/archive-test/test.zip");
    }

    private OutputStream getArTestFileOutput() throws IOException {
        return new FileOutputStream("test/temp/archive-test/test.ar");
    }

    private OutputStream getCpioTestFileOutput() throws IOException {
        return new FileOutputStream("test/temp/archive-test/test.cpio");
    }

    private OutputStream getDumpTestFileOutput() throws IOException {
        return new FileOutputStream("test/temp/archive-test/test.dump");
    }

    private OutputStream getJarTestFileOutput() throws IOException {
        return new FileOutputStream("test/temp/archive-test/test.jar");
    }

    private OutputStream getTarTestFileOutput() throws IOException {
        return new FileOutputStream("test/temp/archive-test/test.tar");
    }

    private OutputStream getZipTestFileOutput() throws IOException {
        return new FileOutputStream("test/temp/archive-test/test.zip");
    }

    private byte[] getArTestFileBytes() throws IOException {
        try (InputStream input = new FileInputStream("test/resources/test-data/archive-test/test.ar")) {
            return input.readAllBytes();
        }
    }

    private byte[] getCpioTestFileBytes() throws IOException {
        try (InputStream input = new FileInputStream("test/resources/test-data/archive-test/test.cpio")) {
            return input.readAllBytes();
        }
    }

    private byte[] getDumpTestFileBytes() throws IOException {
        try (InputStream input = new FileInputStream("test/resources/test-data/archive-test/test.dump")) {
            return input.readAllBytes();
        }
    }

    private byte[] getJarTestFileBytes() throws IOException {
        try (InputStream input = new FileInputStream("test/resources/test-data/archive-test/test.jar")) {
            return input.readAllBytes();
        }
    }

    private byte[] getTarTestFileBytes() throws IOException {
        try (InputStream input = new FileInputStream("test/resources/test-data/archive-test/test.tar")) {
            return input.readAllBytes();
        }
    }

    private byte[] getZipTestFileBytes() throws IOException {
        try (InputStream input = new FileInputStream("test/resources/test-data/archive-test/test.zip")) {
            return input.readAllBytes();
        }
    }
}