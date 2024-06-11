package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    public void testCreateArchiveInputStream() throws IOException {
        // Test valid archives
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream inputStream = new ByteArrayInputStream(getTestFileBytes(archiverName));
            ArchiveInputStream expectedArchiveInput = createExpectedArchiveInputStream(archiverName);
            ArchiveInputStream archiveInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assertNotNull(archiveInput);
            assertEquals(expectedArchiveInput, archiveInput);
        }

        // Test unknown archiver name
        assertThrows(ArchiveException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        // Test valid archives
        for (String archiverName : ArchiveStreamFactory.values()) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream expectedArchiveOutput = createExpectedArchiveOutputStream(archiverName);
            ArchiveOutputStream archiveOutput = ArchiveStreamFactory.getInstance().createArchiveOutputStream(archiverName, outputStream);
            assertNotNull(archiveOutput);
            assertEquals(expectedArchiveOutput, archiveOutput);
        }

        // Test null output stream
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveOutputStream(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testCreateArchiveInputStreamFromStream() throws IOException {
        // Test valid archives
        for (String archiverName : ArchiveStreamFactory.values()) {
            InputStream inputStream = new ByteArrayInputStream(getTestFileBytes(archiverName));
            ArchiveInputStream archiveInput = ArchiveStreamFactory.getInstance().createArchiveInputStream(archiverName, inputStream);
            assertNotNull(archiveInput);
            // Test closing the input stream does not affect the archive input stream
            inputStream.close();
            archiveInput.getNextEntry();
        }

        // Test null input stream
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(null, new ByteArrayInputStream(new byte[0])));

        // Test input stream that does not support mark/reset
        assertThrows(IllegalArgumentException.class, () -> ArchiveStreamFactory.getInstance().createArchiveInputStream(new ByteArrayInputStream(getTestFileBytes(Zip.getName())), new ByteArrayInputStream(new byte[0])));
    }

    private byte[] getTestFileBytes(String archiverName) {
        switch (archiverName) {
            case AR:
                return getArTestFileBytes();
            case CPIO:
                return getCpioTestFileBytes();
            case DUMP:
                return getDumpTestFileBytes();
            case JAR:
                return getJarTestFileBytes();
            case TAR:
                return getTarTestFileBytes();
            case ZIP:
                return getZipTestFileBytes();
            default:
                throw new RuntimeException("Unexpected archiver name: " + archiverName);
        }
    }

    private ArchiveInputStream createExpectedArchiveInputStream(String archiverName) throws IOException {
        switch (archiverName) {
            case AR:
                return new ArArchiveInputStream(new ByteArrayInputStream(getArTestFileBytes()));
            case CPIO:
                return new CpioArchiveInputStream(new ByteArrayInputStream(getCpioTestFileBytes()));
            case DUMP:
                return new DumpArchiveInputStream(new ByteArrayInputStream(getDumpTestFileBytes()));
            case JAR:
                return new JarArchiveInputStream(new ByteArrayInputStream(getJarTestFileBytes()));
            case TAR:
                return new TarArchiveInputStream(new ByteArrayInputStream(getTarTestFileBytes()));
            case ZIP:
                return new ZipArchiveInputStream(new ByteArrayInputStream(getZipTestFileBytes()));
            default:
                throw new RuntimeException("Unexpected archiver name: " + archiverName);
        }
    }

    private ArchiveOutputStream createExpectedArchiveOutputStream(String archiverName) throws IOException {
        switch (archiverName) {
            case AR:
                return new ArArchiveOutputStream(new ByteArrayOutputStream());
            case CPIO:
                return new CpioArchiveOutputStream(new ByteArrayOutputStream());
            case DUMP:
                return new DumpArchiveOutputStream(new ByteArrayOutputStream());
            case JAR:
                return new JarArchiveOutputStream(new ByteArrayOutputStream());
            case TAR:
                return new TarArchiveOutputStream(new ByteArrayOutputStream());
            case ZIP:
                return new ZipArchiveOutputStream(new ByteArrayOutputStream());
            default:
                throw new RuntimeException("Unexpected archiver name: " + archiverName);
        }
    }

    private byte[] getArTestFileBytes() {
        // Ar test file bytes go here
        return new byte[0];
    }

    private byte[] getCpioTestFileBytes() {
        // Cpio test file bytes go here
        return new byte[0];
    }

    private byte[] getDumpTestFileBytes() {
        // Dump test file bytes go here
        return new byte[0];
    }

    private byte[] getJarTestFileBytes() {
        // Jar test file bytes go here
        return new byte[0];
    }

    private byte[] getTarTestFileBytes() {
        // Tar test file bytes go here
        return new byte[0];
    }

    private byte[] getZipTestFileBytes() {
        // Zip test file bytes go here
        return new byte[0];
    }
}