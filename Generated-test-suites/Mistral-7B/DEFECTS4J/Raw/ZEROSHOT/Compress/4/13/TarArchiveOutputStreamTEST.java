import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "testLongFileName" + TarConstants.NAMELEN + "A";
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Check that the long file name was truncated
        byte[] headerRecord = tarArchiveOutputStream.getRecordBuffer();
        String expectedFileName = TarConstants.NAMELEN + "testLongFileName";
        assertEquals(expectedFileName, new String(Arrays.copyOfRange(headerRecord, 51, 51 + TarConstants.NAMELEN), StandardCharsets.UTF_8));

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws IOException {
        String longFileName = "testLongFileName" + TarConstants.NAMELEN + "A";
        TarArchiveEntry entry = new TarArchiveEntry(new File("src/test/resources/testFile.txt"), longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] headerRecord = tarArchiveOutputStream.getRecordBuffer();
        assertEquals("testFileName.txt", new String(Arrays.copyOfRange(headerRecord, 51, 51 + TarConstants.NAMELEN), StandardCharsets.UTF_8));

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntryWithUnwrittenData() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "testData".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedData = Arrays.copyOf(data, data.length + TarConstants.MAGIC_HEADER_SIZE + TarConstants.HEADER_SIZE);
        byte[] record = tarArchiveOutputStream.getRecordBuffer();

        // Comparing the first 512 bytes of the expected data record with the actual record
        Arrays.sort(expectedData);
        Arrays.sort(record);
        for (int i = 0; i < 512; i++) {
            assertEquals("Mismatch at offset " + i, expectedData[i], record[i]);
        }
    }

    @Test
    public void testCloseArchiveEntryWithWrittenData() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "testData".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedData = Arrays.copyOf(data, data.length + TarConstants.MAGIC_HEADER_SIZE + TarConstants.HEADER_SIZE * 2);
        byte[] record1 = tarArchiveOutputStream.getRecordBuffer();
        byte[] record2 = tarArchiveOutputStream.getRecordBuffer();

        // Comparing the first 512 bytes of the expected data record with the actual records
        Arrays.sort(expectedData);
        Arrays.sort(record1);
        for (int i = 0; i < 512; i++) {
            assertEquals("Mismatch at offset " + i, expectedData[i], record1[i]);
        }

        // Comparing the second header record with the expected record
        Arrays.sort(expectedData, TarConstants.HEADER_SIZE, expectedData.length);
        Arrays.sort(record2);
        for (int i = TarConstants.HEADER_SIZE; i < expectedData.length; i++) {
            assertEquals("Mismatch at offset " + i, expectedData[i], record2[i]);
        }
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = "testData".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);

        // Should not be able to write more data with an open entry
        try {
            tarArchiveOutputStream.write(data, 0, data.length);
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testCloseArchiveEntryWithEmptyData() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.closeArchiveEntry();

        byte[] record = tarArchiveOutputStream.getRecordBuffer();
        byte[] expectedData = new byte[TarConstants.HEADER_SIZE];

        // Comparing the header record with the expected record
        Arrays.sort(expectedData);
        Arrays.sort(record);
        for (int i = 0; i < TarConstants.HEADER_SIZE; i++) {
            assertEquals("Mismatch at offset " + i, expectedData[i], record[i]);
        }

        tarArchiveOutputStream.finish();
    }

    @Test
    public void testFinishWithUnclosedEntry() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        // Should throw an exception if there is an unclosed entry
        try {
            tarArchiveOutputStream.finish();
            Assert.fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }

        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
    }

    @Test
    public void testHasUnclosedEntryAfterClose() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        assertFalse("Should not have an unclosed entry", tarArchiveOutputStream.hasUnclosedEntry());
    }

    @Test
    public void testHasUnclosedEntryBeforeClose() throws IOException {
        File testFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.finish();

        assertTrue("Should have an unclosed entry", tarArchiveOutputStream.hasUnclosedEntry());
    }
}