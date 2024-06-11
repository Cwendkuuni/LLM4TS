import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
    }

    @Test
    public void testWriteSingleFileEntry() throws IOException {
        // Write a single file entry
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFile.txt", "Test file content.");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.write("Test file content.".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // Check the written data
        byte[] expectedData = "Test file content.\0".getBytes(StandardCharsets.UTF_8);
        byte[] actualData = Arrays.copyOfRange(outputStream.toByteArray(),
                tarOutputStream.getStartPosition(),
                tarOutputStream.getStartPosition() + TarConstants.HEADER_SIZE + expectedData.length);
        assertEquals("Unexpected data written to the archive.", expectedData, actualData);
    }

    @Test
    public void testWriteMultipleFileEntries() throws IOException {
        // Write multiple file entries
        TarArchiveEntry tarEntry1 = new TarArchiveEntry("testFile1.txt", "Test file content 1.");
        TarArchiveEntry tarEntry2 = new TarArchiveEntry("testFile2.txt", "Test file content 2.");
        tarOutputStream.putNextEntry(tarEntry1);
        tarOutputStream.write("Test file content 1.".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.putNextEntry(tarEntry2);
        tarOutputStream.write("Test file content 2.".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // Check the written data
        byte[] expectedData1 = "Test file content 1.\0".getBytes(StandardCharsets.UTF_8);
        byte[] actualData1 = Arrays.copyOfRange(outputStream.toByteArray(),
                tarOutputStream.getStartPosition(),
                tarOutputStream.getStartPosition() + TarConstants.HEADER_SIZE + expectedData1.length);
        assertEquals("Unexpected data written to the archive for entry 1.", expectedData1, actualData1);

        byte[] expectedData2 = "Test file content 2.\0".getBytes(StandardCharsets.UTF_8);
        byte[] actualData2 = Arrays.copyOfRange(outputStream.toByteArray(),
                tarOutputStream.getStartPosition(),
                tarOutputStream.getStartPosition() + TarConstants.HEADER_SIZE + expectedData2.length);
        assertEquals("Unexpected data written to the archive for entry 2.", expectedData2, actualData2);
    }

    @Test
    public void testLongFileName() throws IOException {
        // Write an entry with a long file name
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFile/with/long/name.txt", "Test file content.");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.write("Test file content.".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        // Check the written data
        byte[] expectedData = "Test file content.\0testFile/with/long/name.txt\0".getBytes(StandardCharsets.UTF_8);
        byte[] actualData = Arrays.copyOfRange(outputStream.toByteArray(),
                tarOutputStream.getStartPosition(),
                tarOutputStream.getStartPosition() + TarConstants.HEADER_SIZE + expectedData.length);
        assertEquals("Unexpected data written to the archive.", expectedData, actualData);
    }

    @Test
    public void testCloseEntryBeforeWrite() {
        // Try to close an entry before writing its data
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFile.txt", "Test file content.");
        tarOutputStream.putNextEntry(tarEntry);
        tarOutputStream.closeArchiveEntry();
        assertThrows(IOException.class, () -> tarOutputStream.write(new byte[0]));
    }

    @Test
    public void testLongFileMode() throws IOException {
        // Test long file name handling with different modes
        TarArchiveOutputStream longFileNameTarOutputStream = new TarArchiveOutputStream(outputStream);

        // Long file names are truncated
        longFileNameTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry tarEntry = new TarArchiveEntry("testFile/with/long/name.txt", "Test file content.");
        longFileNameTarOutputStream.putNextEntry(tarEntry);
        longFileNameTarOutputStream.write("Test file content.".getBytes(StandardCharsets.UTF_8));
        longFileNameTarOutputStream.closeArchiveEntry();
        byte[] actualData = Arrays.copyOfRange(outputStream.toByteArray(),
                0, outputStream.size());
        assertFalse("Unexpected long file name written to the archive.",
                actualData.toString(StandardCharsets.UTF_8).contains("testFile/with/long/name.txt"));

        // Long file names are truncated and an error is thrown
        longFileNameTarOutputStream = new TarArchiveOutputStream(outputStream);
        longFileNameTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarEntry = new TarArchiveEntry("testFile/with/long/name.txt", "Test file content.");
        assertThrows(IOException.class, () -> longFileNameTarOutputStream.putNextEntry(tarEntry));

        // Long file names are stored with GNU extensions
        longFileNameTarOutputStream = new TarArchiveOutputStream(outputStream);
        longFileNameTarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarEntry = new TarArchiveEntry("testFile/with/long/name.txt", "Test file content.");
        longFileNameTarOutputStream.putNextEntry(tarEntry);
        longFileNameTarOutputStream.write("Test file content.".getBytes(StandardCharsets.UTF_8));
        longFileNameTarOutputStream.closeArchiveEntry();
        actualData = Arrays.copyOfRange(outputStream.toByteArray(),
                0, outputStream.size());
        assertEquals("Unexpected data written to the archive.",
                "testFile/with/long/name.txt\0" +
                        TarConstants.GNU_LONGLINK + "\0" +
                        "testFile/with/long/name.txt\0".getBytes(StandardCharsets.UTF_8),
                actualData);
    }

    @Test
    public void testFinish() throws IOException {
        // Write multiple file entries and finish the archive
        TarArchiveEntry tarEntry1 = new TarArchiveEntry("testFile1.txt", "Test file content 1.");
        TarArchiveEntry tarEntry2 = new TarArchiveEntry("testFile2.txt", "Test file content 2.");
        tarOutputStream.putNextEntry(tarEntry1);
        tarOutputStream.write("Test file content 1.".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.putNextEntry(tarEntry2);
        tarOutputStream.write("Test file content 2.".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        // Check the whole archive data
        byte[] expectedData = new byte[outputStream.size()];
        System.arraycopy(outputStream.toByteArray(), 0, expectedData, 0, outputStream.size());

        byte[] actualData = Arrays.copyOfRange(outputStream.toByteArray(),
                0, outputStream.size());
        assertEquals("Unexpected data written to the archive.", expectedData, actualData);

        // Check the EOF records
        byte[] eofRecord1 = Arrays.copyOfRange(outputStream.toByteArray(),
                tarOutputStream.getStartPosition(),
                tarOutputStream.getStartPosition() + 2 * TarConstants.RECORD_SIZE);
        byte[] eofRecord2 = Arrays.copyOfRange(outputStream.toByteArray(),
                tarOutputStream.getStartPosition() + (tarOutputStream.size() - TarConstants.HEADER_SIZE),
                tarOutputStream.getStartPosition() + 2 * TarConstants.RECORD_SIZE);
        assertArrayEquals("Unexpected EOF records.", Arrays.copyOf(new byte[TarConstants.RECORD_SIZE], 0), eofRecord1);
        assertArrayEquals("Unexpected EOF records.", Arrays.copyOf(new byte[TarConstants.RECORD_SIZE], 0), eofRecord2);
    }
}