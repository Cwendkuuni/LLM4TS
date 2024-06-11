package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

/**
 * TarArchiveOutputStreamTest.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out)) {
            // empty construction test
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128)) {
            // construction with block size test
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            // construction with block size and record size test
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetLongFileMode() {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.getLongFileMode());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            tarOut.finish();
        }
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            ArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.finish();
        }
    }

    @Test
    public void testClose() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            tarOut.close();
        }
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            tarOut.close();
            tarOut.close();
        }
    }

    @Test
    public void testGetRecordSize() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            assertEquals(512, tarOut.getRecordSize());
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            ArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            assertNotNull(entry);
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidArchiveEntry() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            ArchiveEntry entry = new Object() {};
            tarOut.putArchiveEntry(entry);
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            ArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
        }
    }

    @Test(expected = IOException.class)
    public void testWrite() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            ArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            byte[] testData = new byte[] { 1, 2, 3 };
            tarOut.write(testData, 0, testData.length);
            tarOut.closeArchiveEntry();
        }
    }

    @Test
    public void testWriteMultipleRecords() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            ArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            byte[] testData1 = new byte[] { 1, 2, 3 };
            byte[] testData2 = new byte[] { 4, 5, 6 };
            tarOut.write(testData1, 0, testData1.length);
            tarOut.write(testData2, 0, testData2.length);
            tarOut.closeArchiveEntry();
        }
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            ArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarOut.putArchiveEntry(entry);
            tarOut.closeArchiveEntry();
            byte[] testData = new byte[] { 1, 2, 3 };
            tarOut.write(testData, 0, testData.length);
        }
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            String longFileName = "test_entry_with_long_name_" + TarConstants.NAMELEN + "_characters.txt";
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.putArchiveEntry(entry);
            byte[] testData = new byte[] { 1, 2, 3 };
            tarOut.write(testData, 0, testData.length);
            tarOut.closeArchiveEntry();
        }
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileNameWithErrorLongFileMode() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            String longFileName = "test_entry_with_long_name_" + TarConstants.NAMELEN + "_characters.txt";
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.putArchiveEntry(entry);
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
            byte[] testData = new byte[] { 1, 2, 3 };
            tarOut.write(testData, 0, testData.length);
        }
    }

    @Test
    public void testWriteLongFileNameWithTruncateLongFileMode() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            String longFileName = "test_entry_with_long_name_" + TarConstants.NAMELEN + "_characters.txt";
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.putArchiveEntry(entry);
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            byte[] testData = new byte[] { 1, 2, 3 };
            tarOut.write(testData, 0, testData.length);
            tarOut.closeArchiveEntry();
        }
    }

    @Test
    public void testWriteLongFileNameWithGNULongFileMode() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(System.out, 128, 512)) {
            String longFileName = "test_entry_with_long_name_" + TarConstants.NAMELEN + "_characters.txt";
            TarArchiveEntry entry = new TarArchiveEntry(longFileName);
            tarOut.putArchiveEntry(entry);
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            byte[] testData = new byte[] { 1, 2, 3 };
            tarOut.write(testData, 0, testData.length);
            tarOut.closeArchiveEntry();
        }
    }
}