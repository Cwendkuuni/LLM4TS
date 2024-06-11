import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutput;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutput.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry_Directory() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testDir/");
        entry.setDirectory(true);

        // Act
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();

        // Assert
        assertEquals(0, outputStream.toByteArray().length);
    }

    @Test
    public void testPutArchiveEntry_File() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setDirectory(false);
        entry.setSize(10);

        // Act
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("Test data".getBytes());
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();

        // Assert
        byte[] expectedBytes = "Test data".getBytes();
        byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileName_ExceedsMax() throws IOException {
        // Arrange
        String longFileName = generateLongFileNameExceedingMax();
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);

        // Act
        tarOutput.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_TruncateMode() throws IOException {
        // Arrange
        String longFileName = generateLongFileNameExceedingMax();
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Act
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();

        // Assert
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, entry.getLongFileMode());
        assertFalse(outputStream.toByteArray().length > 100);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_GNUMode() throws IOException {
        // Arrange
        String longFileName = generateLongFileNameExceedingMax();
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // Act
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();

        // Assert
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, entry.getLongFileMode());
        String expectedLongLinkEntryName = "././@LongLink";
        assertTrue(outputStream.toString().contains(expectedLongLinkEntryName));
    }

    @Test
    public void testSetLongFileMode() {
        // Arrange
        int longFileMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;

        // Act
        tarOutput.setLongFileMode(longFileMode);

        // Assert
        assertEquals(longFileMode, tarOutput.getLongFileMode());
    }

    private String generateLongFileNameExceedingMax() {
        StringBuilder longFileName = new StringBuilder("longFileName/");
        while (longFileName.length() < 150) {
            longFileName.append("a");
        }
        return longFileName.toString();
    }

}