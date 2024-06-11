import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithDirectoryEntry() throws IOException {
        // Given
        File testDir = new File("src/test/resources/testDirectory");
        TarArchiveEntry directoryEntry = new TarArchiveEntry(testDir, "testDirectory");
        directoryEntry.setSize(0);
        directoryEntry.setDirectory(true);

        // When
        tarOutputStream.putArchiveEntry(directoryEntry);

        // Then
        assertFalse(tarOutputStream.hasNextEntry());
        assertEquals(0, outputStream.size());
    }

    @Test
    public void testPutArchiveEntryWithFileEntry() throws IOException {
        // Given
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry fileEntry = new TarArchiveEntry(testFile, "testFile.txt");
        fileEntry.setSize(5);

        // When
        tarOutputStream.putArchiveEntry(fileEntry);
        tarOutputStream.write("Hello World".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // Then
        assertTrue(tarOutputStream.hasNextEntry());
        assertFalse(tarOutputStream.hasNextEntry());
        assertEquals(12, outputStream.size());

        byte[] expectedHeader = TarConstants.MAGIC_TAR + TarConstants.HEADER_VERSION +
                TarConstants.FIELD_NAME_LENGTH + "testFile.txt\0".getBytes(StandardCharsets.UTF_8) +
                TarConstants.FIELD_HEADER_TYPE_FILE + TarConstants.FIELD_HEADER_FLAG_UNCHANGED +
                TarConstants.FIELD_HEADER_SIZE_5 + TarConstants.FIELD_HEADER_MOD_TIME_NOW +
                TarConstants.FIELD_HEADER_CHECKSUM_NOCHECK + TarConstants.FIELD_HEADER_LINKNAME_NULL +
                TarConstants.FIELD_HEADER_MAGIC_NULL + "testFile.txt\0".getBytes(StandardCharsets.UTF_8) +
                TarConstants.FIELD_HEADER_SIZE_12 + TarConstants.FIELD_HEADER_CHECKSUM_NOCHECK;

        byte[] expectedData = "Hello World".getBytes(StandardCharsets.UTF_8);

        byte[] actualRecord = new byte[outputStream.size()];
        outputStream.read(actualRecord);

        // Verify that the header and data are written correctly
        assertArrayEquals(expectedHeader, actualRecord, 0, expectedHeader.length);
        assertArrayEquals(expectedData, new byte[actualRecord.length - expectedHeader.length], 0, expectedData.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        // Given
        String longFileName = "src/test/resources/testFile_____.txt";
        TarArchiveEntry fileEntry = new TarArchiveEntry(new File(longFileName), "testFile_____.txt");
        fileEntry.setSize(5);

        // When
        tarOutputStream.putArchiveEntry(fileEntry);

        // Then
        assertThrows(RuntimeException.class, () -> tarOutputStream.write("Hello World".getBytes(StandardCharsets.UTF_8)));
        assertEquals(5, outputStream.size());

        byte[] expectedHeader = TarConstants.MAGIC_TAR + TarConstants.HEADER_VERSION +
                TarConstants.FIELD_NAME_LENGTH + longFileName.getBytes(StandardCharsets.UTF_8) +
                TarConstants.FIELD_HEADER_TYPE_FILE + TarConstants.FIELD_HEADER_FLAG_UNCHANGED +
                TarConstants.FIELD_HEADER_SIZE_5 + TarConstants.FIELD_HEADER_MOD_TIME_NOW +
                TarConstants.FIELD_HEADER_CHECKSUM_NOCHECK + TarConstants.FIELD_HEADER_LINKNAME_NULL +
                TarConstants.FIELD_HEADER_MAGIC_NULL + longFileName.getBytes(StandardCharsets.UTF_8) +
                TarConstants.FIELD_HEADER_SIZE_5 + TarConstants.FIELD_HEADER_CHECKSUM_NOCHECK;

        byte[] actualRecord = new byte[outputStream.size()];
        outputStream.read(actualRecord);

        // Verify that the header is written correctly
        assertArrayEquals(expectedHeader, actualRecord, 0, expectedHeader.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameGNU() throws IOException {
        // Given
        String longFileName = "src/test/resources/testFile_____.txt";
        TarArchiveEntry fileEntry = new TarArchiveEntry(new File(longFileName), "testFile_____.txt");
        fileEntry.setSize(5);

        // When
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(fileEntry);

        // Then
        assertFalse(tarOutputStream.hasNextEntry());
        assertEquals(12, outputStream.size());

        byte[] expectedHeader = TarConstants.MAGIC_GNU_TAR + TarConstants.HEADER_VERSION +
                TarConstants.FIELD_NAME_LENGTH + longFileName.getBytes(StandardCharsets.UTF_8) +
                TarConstants.FIELD_HEADER_TYPE_LONGNAME +
                TarConstants.FIELD_HEADER_SIZE_5 + TarConstants.FIELD_HEADER_MOD_TIME_NOW +
                TarConstants.FIELD_HEADER_CHECKSUM_NOCHECK + TarConstants.FIELD_HEADER_LINKNAME_NULL +
                TarConstants.FIELD_HEADER_MAGIC_NULL + "testFile_____.txt\0".getBytes(StandardCharsets.UTF_8) +
                TarConstants.FIELD_HEADER_SIZE_12 + TarConstants.FIELD_HEADER_CHECKSUM_NOCHECK;

        byte[] expectedLinkName = longFileName.getBytes(StandardCharsets.UTF_8);
        byte[] expectedData = "Hello World".getBytes(StandardCharsets.UTF_8);

        byte[] actualRecord = new byte[outputStream.size()];
        outputStream.read(actualRecord);

        // Verify that the header, linkname, and data are written correctly
        assertArrayEquals(expectedHeader, actualRecord, 0, expectedHeader.length);
        assertEquals(expectedLinkName.length, actualRecord.length - expectedHeader.length);
        assertArrayEquals(expectedData, new byte[actualRecord.length - expectedHeader.length - expectedLinkName.length], 0, expectedData.length);
    }

    @Test
    public void testFinish() throws IOException {
        // Given
        TarArchiveEntry fileEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        fileEntry.setSize(5);
        tarOutputStream.putArchiveEntry(fileEntry);
        tarOutputStream.write("Hello World".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // When
        tarOutputStream.finish();

        // Then
        assertEquals(14, outputStream.size());
        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);
        byte[] actualEOFRecord = new byte[outputStream.size() - 5];
        outputStream.read(actualEOFRecord);
        assertArrayEquals(expectedEOFRecord, actualEOFRecord, 0, expectedEOFRecord.length);
    }

    @Test
    public void testClose() throws IOException {
        // Given
        TarArchiveEntry fileEntry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), "testFile.txt");
        fileEntry.setSize(5);
        tarOutputStream.putArchiveEntry(fileEntry);
        tarOutputStream.write("Hello World".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // When
        tarOutputStream.close();

        // Then
        assertNull(tarOutputStream.getArchiveEntry());
        assertNull(tarOutputStream.getNextEntryName());
        assertFalse(tarOutputStream.hasNextEntry());
        assertFalse(tarOutputStream.isOpen());
        assertEquals(14, outputStream.size());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameErrorMode() throws IOException {
        // Given
        String longFileName = "src/test/resources/testFile_____.txt";
        TarArchiveEntry fileEntry = new TarArchiveEntry(new File(longFileName), "testFile_____.txt");
        fileEntry.setSize(5);

        // When
        tarOutputStream.putArchiveEntry(fileEntry);

        // Then
        assertThrows(RuntimeException.class, () -> tarOutputStream.write("Hello World".getBytes(StandardCharsets.UTF_8)));
        assertEquals(5, outputStream.size());
    }
}