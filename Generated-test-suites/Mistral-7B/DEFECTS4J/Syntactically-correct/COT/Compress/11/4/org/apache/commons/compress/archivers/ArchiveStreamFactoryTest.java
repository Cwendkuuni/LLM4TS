package org.apache.commons.compress.archivers.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test valid archives
        for (String archiveType : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            InputStream inputStream = new ByteArrayInputStream(getArchiveBytes(archiveType));
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
            assertEquals(archiveType, archiveInputStream.getClass().getSimpleName());
            archiveInputStream.close();
        }

        // Test unknown archive type
        InputStream inputStream = new ByteArrayInputStream(getArchiveBytes("unknown"));
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();

        // Test null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test valid archives
        for (String archiveType : new String[]{"AR", "CPIO", "JAR", "TAR", "ZIP"}) {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveType, outputStream);
            assertEquals(archiveType, archiveOutputStream.getClass().getSimpleName());
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }

        // Test unknown archive type
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        outputStream.close();

        // Test null output stream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test valid archives
        for (String archiveType : new String[]{"AR", "CPIO", "DUMP", "JAR", "TAR", "ZIP"}) {
            InputStream inputStream = new ByteArrayInputStream(getArchiveBytes(archiveType));
            ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, inputStream);
            assertEquals(archiveType, archiveInputStream.getClass().getSimpleName());
            archiveInputStream.close();
            inputStream.close();
        }

        // Test unknown archive type
        InputStream inputStream = new ByteArrayInputStream(getArchiveBytes("unknown"));
        try {
            new ArchiveStreamFactory().createArchiveInputStream(inputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // Expected
        }
        inputStream.close();

        // Test null input stream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private byte[] getArchiveBytes(String archiveType) {
        switch (archiveType) {
            case "AR":
                return AR_ARCHIVE_BYTES;
            case "CPIO":
                return CPIO_ARCHIVE_BYTES;
            case "DUMP":
                return DUMP_ARCHIVE_BYTES;
            case "JAR":
                return JAR_ARCHIVE_BYTES;
            case "TAR":
                return TAR_ARCHIVE_BYTES;
            case "ZIP":
                return ZIP_ARCHIVE_BYTES;
            default:
                throw new IllegalStateException("Unexpected value: " + archiveType);
        }
    }

    private static final byte[] AR_ARCHIVE_BYTES = new byte[0];
    private static final byte[] CPIO_ARCHIVE_BYTES = new byte[0];
    private static final byte[] DUMP_ARCHIVE_BYTES = new byte[0];
    private static final byte[] JAR_ARCHIVE_BYTES = new byte[0];
    private static final byte[] TAR_ARCHIVE_BYTES = new byte[0];
    private static final byte[] ZIP_ARCHIVE_BYTES = new byte[0];
}