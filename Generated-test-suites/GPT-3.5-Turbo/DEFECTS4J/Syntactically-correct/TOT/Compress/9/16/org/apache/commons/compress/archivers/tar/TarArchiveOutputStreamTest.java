import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private ArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (archiveOutputStream != null) {
            archiveOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntry_typicalUseCase() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        
        // Act
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();

        // Assert
        assertEquals(1024, outputStream.toByteArray().length); // Check if the record is written correctly
    }

    @Test
    public void testPutArchiveEntry_longFileName_error() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("verylongfilenameoverlimitof100characters.txt");

        // Act and Assert
        try {
            archiveOutputStream.putArchiveEntry(entry);
            fail("An IOException should have been thrown.");
        } catch (IOException e) {
            // IOException is expected
        }
    }

    @Test
    public void testPutArchiveEntry_longFileName_truncate() throws IOException {
        // Arrange
        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        ArchiveEntry entry = new TarArchiveEntry("verylongfilenameoverlimitof100characters.txt");

        // Act
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();

        // Assert
        assertEquals(1024, outputStream.toByteArray().length); // Check if the record is written correctly
    }

    @Test
    public void testPutArchiveEntry_longFileName_gnu() throws IOException {
        // Arrange
        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry entry = new TarArchiveEntry("verylongfilenameoverlimitof100characters.txt");

        // Act
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();

        // Assert
        assertEquals(2048, outputStream.toByteArray().length); // Check if the record is written correctly
    }

    @Test
    public void testCloseArchiveEntry_noCurrentEntryToClose() {
        // Assert
        try {
            archiveOutputStream.closeArchiveEntry();
            fail("An IOException should have been thrown.");
        } catch (IOException e) {
            // IOException is expected
        }
    }

    @Test
    public void testWrite_typicalUseCase() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("file.txt");
        archiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, world!".getBytes();

        // Act
        archiveOutputStream.write(data);
        archiveOutputStream.closeArchiveEntry();

        // Assert
        assertEquals(1024, outputStream.toByteArray().length); // Check if the record is written correctly
    }

    @Test
    public void testWrite_bytesExceedSizeInHeader() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("file.txt", 10);
        archiveOutputStream.putArchiveEntry(entry);

        byte[] data = "Hello, world!".getBytes();

        // Act and Assert
        try {
            archiveOutputStream.write(data);
            fail("An IOException should have been thrown.");
        } catch (IOException e) {
            // IOException is expected
        }
    }
}