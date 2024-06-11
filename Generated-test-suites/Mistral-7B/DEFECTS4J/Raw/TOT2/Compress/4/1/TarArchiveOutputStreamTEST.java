import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        // Given
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        // When
        TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Then
        byte[] expectedHeader = getExpectedHeader("testFile.txt", fileContent.length);
        assertEquals(expectedHeader, Arrays.copyOfRange(outputStream.toByteArray(), 0, expectedHeader.length));
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        // Given
        String fileName = "a".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName);

        // When
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // Then
        // Exception should be thrown
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        // Given
        TarArchiveEntry archiveEntry = null;

        // When
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // Then
        // IOException should be thrown
    }

    @Test
    public void testFinish() throws IOException {
        // Given
        String fileName1 = "testFile1.txt";
        String fileName2 = "testFile2.txt";

        // When
        TarArchiveEntry archiveEntry1 = new TarArchiveEntry(fileName1);
        TarArchiveEntry archiveEntry2 = new TarArchiveEntry(fileName2);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry1);
        tarArchiveOutputStream.write(new byte[0], 0, 0); // Write empty file content
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.putArchiveEntry(archiveEntry2);
        tarArchiveOutputStream.write(new byte[0], 0, 0); // Write empty file content
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        // Then
        byte[] expectedEOFRecord = getExpectedEOFRecord();
        assertEquals(expectedEOFRecord, Arrays.copyOfRange(outputStream.toByteArray(), outputStream.size() - expectedEOFRecord.length, outputStream.size()));
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        // Given
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        // When
        TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);

        // Then
        // IOException should be thrown
    }

    @Test
    public void testClose() throws IOException {
        // Given
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        // When
        TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        // Then
        // No exception should be thrown
    }

    @Test
    public void testWrite() throws IOException {
        // Given
        String fileName = "testFile.txt";
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        // When
        TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        // Then
        byte[] expectedContent = new byte[fileContent.length];
        assertArrayEquals(expectedContent, Arrays.copyOfRange(outputStream.toByteArray(), 0, fileContent.length));
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        // Given
        TarArchiveOutputStream tarArchiveOutputStream = Mockito.spy(new TarArchiveOutputStream(new ByteArrayOutputStream()));

        // When
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Then
        // No exception should be thrown
    }

    private byte[] getExpectedHeader(String fileName, long fileSize) {
        byte[] header = new byte[TarConstants.HEADER_SIZE];

        TarHeader headerInfo = new TarHeader();
        headerInfo.setName(fileName);
        headerInfo.setSize(fileSize);
        headerInfo.setMode(TarConstants.DEFAULT_FILE_MODE);
        headerInfo.setVersion(TarConstants.VERSION_100);
        headerInfo.setUid(0);
        headerInfo.setGid(0);
        headerInfo.setMtime(0);
        headerInfo.setChecksum();

        headerInfo.writeExternal(header, 0);

        return Arrays.copyOfRange(header, 0, TarConstants.HEADER_SIZE);
    }

    private byte[] getExpectedEOFRecord() {
        return new byte[2 * TarBuffer.getDefaultRecordSize()];
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        // Given
        Path inputFile = Files.createTempFile("test", "tar");
        String entryName = "testFile.txt";

        // When
        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile.toFile(), entryName);

        // Then
        assertEquals(entryName, archiveEntry.getName());
        assertEquals(inputFile.toFile().length(), archiveEntry.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        // Given
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // When
        tarArchiveOutputStream.createArchiveEntry(null, "testFile.txt");

        // Then
        // IllegalArgumentException should be thrown
    }
}