import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.JarArchiveInputStream;
import org.apache.commons.compress.archivers.TarArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Testing the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_CONTENT = "Test content.";

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        // Test creating an ArchiveInputStream from a known archive format and a valid InputStream
        InputStream inputStream = new ByteArrayInputStream(TEST_CONTENT.getBytes(StandardCharsets.UTF_8));
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        ZipEntry entry = archiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals(TEST_FILE_NAME, entry.getName());
        assertArrayEquals(TEST_CONTENT.getBytes(StandardCharsets.UTF_8), IOUtils.toByteArray(archiveInputStream));

        // Test creating an ArchiveInputStream from an unsupported archive format
        inputStream = new ByteArrayInputStream("Unsupported format".getBytes(StandardCharsets.UTF_8));
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream("unsupported", inputStream));

        // Test creating an ArchiveInputStream from a null InputStream
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null));

        // Test creating an ArchiveInputStream from an InputStream without mark and reset support
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = new ByteArrayOutputStream();
        InputStream unsupportedStream = new ByteArrayInputStream(new byte[1024]); // arbitrary size
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, unsupportedStream));
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        // Test creating an ArchiveOutputStream from a known archive format and a valid OutputStream
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        archiveOutputStream.putArchiveEntry(new ZipEntry(TEST_FILE_NAME));
        IOUtils.write(TEST_CONTENT.getBytes(StandardCharsets.UTF_8), archiveOutputStream, TEST_CONTENT.length());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();
        assertArrayEquals(new byte[]{"PK\003\021", "Name: testFile.txt\000", " Len: 13\000", "-----------------------------1\000", TEST_CONTENT.getBytes(StandardCharsets.UTF_8)}, IOUtils.toByteArray(new ByteArrayInputStream(outputStream.toByteArray())));

        // Test creating an ArchiveOutputStream from an unsupported archive format
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream("unsupported", new ByteArrayOutputStream()));

        // Test creating an ArchiveOutputStream from a null OutputStream
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, null));
    }

    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test creating an ArchiveInputStream from an input stream with known archive format
        InputStream testInputStream = new FileInputStream("testFile.zip");
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, testInputStream);
        ZipEntry entry = archiveInputStream.getNextEntry();
        assertEquals(TEST_FILE_NAME, entry.getName());
        byte[] content = IOUtils.toByteArray(archiveInputStream);
        assertNotNull(content);
        testInputStream.close();

        // Test creating an ArchiveInputStream from an input stream with an unknown archive format
        InputStream unknownArchiveInputStream = new ByteArrayInputStream("Unknown format".getBytes(StandardCharsets.UTF_8));
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, unknownArchiveInputStream));

        // Test creating an ArchiveInputStream from a null InputStream
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, null));
    }
}