import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream archiveInputStream;

    @BeforeEach
    public void setup() throws IOException {
        String testFileContent = "Test Content";
        Path filePath = Files.createTempFile("test", ".ar");
        Files.write(filePath, testFileContent.getBytes(StandardCharsets.UTF_8));
        inputStream = new FileInputStream(filePath.toFile());
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextArEntry();
        Assertions.assertInstanceOf(ArArchiveEntry.class, entry);
        Assertions.assertEquals("Test", entry.getName());
        Assertions.assertEquals(testFileContent.length(), entry.getSize());
        Assertions.assertFalse(archiveInputStream.hasNextEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assertions.assertInstanceOf(ArArchiveEntry.class, entry);
        Assertions.assertEquals("Test", entry.getName());
        Assertions.assertEquals(testFileContent.length(), entry.getSize());
        Assertions.assertFalse(archiveInputStream.hasNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();
        Assertions.assertTrue(archiveInputStream.isClosed());
        Assertions.assertThrows(IOException.class, archiveInputStream::read);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = archiveInputStream.read(buffer);
        Assertions.assertEquals(testFileContent.charAt(0), (char) buffer[0]);
        Assertions.assertEquals(testFileContent.charAt(1), (char) buffer[1]);
        Assertions.assertNotEquals(testFileContent.length(), bytesRead);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int offset = 2;
        int length = 4;
        int bytesRead = archiveInputStream.read(buffer, offset, length);
        String subContent = new String(testFileContent.subSequence(2, 6));
        Assertions.assertEquals(subContent, new String(buffer));
        Assertions.assertEquals(length, bytesRead);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        Assertions.assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testInvalidArFile() throws IOException {
        InputStream invalidInputStream = Mockito.mock(InputStream.class);
        ArArchiveInputStream invalidArchiveInputStream = new ArArchiveInputStream(invalidInputStream);
        Assertions.assertFalse(invalidArchiveInputStream.hasNextEntry());
        Assertions.assertThrows(IOException.class, invalidArchiveInputStream::getNextArEntry);
    }
}