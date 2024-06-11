import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryAndWritePositiveCase() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test content".getBytes(), 0, "test content".length());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedRecord = new byte[TarConstants.STANDARD_HEADER_LENGTH + "test.txt".length() + 1 + TarConstants.NAME_LENGTH];
        System.arraycopy(entry.getHeader(), 0, expectedRecord, 0, TarConstants.STANDARD_HEADER_LENGTH);
        System.arraycopy("test.txt".getBytes(), 0, expectedRecord, TarConstants.STANDARD_HEADER_LENGTH, "test.txt".length());
        expectedRecord[TarConstants.STANDARD_HEADER_LENGTH + "test.txt".length()] = (byte) 0;

        byte[] actualRecord = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedRecord, actualRecord, "The recorded header does not match the expected one.");
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNegativeCase() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish(); // This should throw an exception because an entry is still open
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNegativeCase2() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close(); // This should throw an exception because an entry is still open
    }

    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "test_long_name_for_archive_entry.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test content".getBytes(), 0, "test content".length());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedRecord;
        if (tarArchiveOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_GNU) {
            // Long file name in the archive will be stored as a separate entry
            // with the name "LongLink"
            expectedRecord = new byte[2 * TarConstants.STANDARD_HEADER_LENGTH + longFileName.length() + 1];
            System.arraycopy(TarConstants.GNU_LONGLINK_MAGIC, 0, expectedRecord, 0, TarConstants.GNU_LONGLINK_MAGIC.length);
            System.arraycopy("LongLink".getBytes(), 0, expectedRecord, TarConstants.GNU_LONGLINK_MAGIC.length, TarConstants.NAME_LENGTH);
            System.arraycopy(longFileName.getBytes(), 0, expectedRecord, TarConstants.GNU_LONGLINK_MAGIC.length + TarConstants.NAME_LENGTH, longFileName.length());
            expectedRecord[2 * TarConstants.GNU_LONGLINK_MAGIC.length + TarConstants.NAME_LENGTH + longFileName.length()] = (byte) 0;
        } else {
            expectedRecord = new byte[TarConstants.STANDARD_HEADER_LENGTH + longFileName.length() + 1 + TarConstants.NAME_LENGTH];
            System.arraycopy(entry.getHeader(), 0, expectedRecord, 0, TarConstants.STANDARD_HEADER_LENGTH);
            System.arraycopy(longFileName.getBytes(), 0, expectedRecord, TarConstants.STANDARD_HEADER_LENGTH, longFileName.length());
            expectedRecord[TarConstants.STANDARD_HEADER_LENGTH + longFileName.length()] = (byte) 0;
        }

        byte[] actualRecord = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedRecord, actualRecord, "The recorded header does not match the expected one.");
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameError() throws IOException {
        String longFileName = "test_long_name_for_archive_entry.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName, "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);

        if (tarArchiveOutputStream.longFileMode != TarArchiveOutputStream.LONGFILE_TRUNCATE) {
            throw new RuntimeException("file name '" + longFileName
                    + "' is too long ( > "
                    + TarConstants.NAMELEN + " bytes)");
        }
    }

    @Test
    public void testCloseArchiveEntryPositiveCase() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test content".getBytes(), 0, "test content".length());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedEOFRecord = new byte[2 * TarConstants.STANDARD_HEADER_LENGTH];
        System.arraycopy(TarConstants.STANDBY_MAGIC, 0, expectedEOFRecord, 0, TarConstants.STANDBY_MAGIC.length);
        System.arraycopy(TarConstants.STANDBY_MAGIC, 0, expectedEOFRecord, TarConstants.STANDBY_MAGIC.length, TarConstants.STANDBY_MAGIC.length);

        byte[] actualEOFRecord = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedEOFRecord, actualEOFRecord, "The EOF records do not match.");
    }

    @Test
    public void testWritePositiveCase() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test content".getBytes(), 0, "test content".length());
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedRecord = new byte[TarConstants.STANDARD_HEADER_LENGTH + "test.txt".length() + 1 + TarConstants.NAME_LENGTH];
        System.arraycopy(entry.getHeader(), 0, expectedRecord, 0, TarConstants.STANDARD_HEADER_LENGTH);
        System.arraycopy("test.txt".getBytes(), 0, expectedRecord, TarConstants.STANDARD_HEADER_LENGTH, "test.txt".length());
        expectedRecord[TarConstants.STANDARD_HEADER_LENGTH + "test.txt".length()] = (byte) 0;

        byte[] actualRecord = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedRecord, actualRecord, "The recorded header does not match the expected one.");
    }

    @Test(expected = IOException.class)
    public void testWriteNegativeCase() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test content".getBytes(), 0, "test content".length() + 1);
    }

    @Test(expected = IOException.class)
    public void testWriteNegativeCase2() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("test content".getBytes(), "test content".length(), 1);
    }
}