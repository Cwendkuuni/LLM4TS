import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testBasicUsage() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        File fileToAdd = new File("testFile.txt");
        String fileNameInArchive = "testFile.txt";

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(fileToAdd, fileNameInArchive));
        byte[] fileContent = "Test content".getBytes();
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedTarContent = getExpectedTarContent(fileContent.length);

        Assert.assertArrayEquals(expectedTarContent, outputStream.toByteArray());
    }

    @Test
    public void testLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        TarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File fileToAdd = new File("test/longFileName/testFile.txt");
        String fileNameInArchive = "test/longFileName/testFile.txt";

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(fileToAdd, fileNameInArchive));
        byte[] fileContent = "Test content".getBytes();
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedTarContent = getExpectedTarContentForLongFileName(fileContent.length);

        Assert.assertArrayEquals(expectedTarContent, outputStream.toByteArray());
    }

    @Test
    public void testClosingUnclosedEntryThrowsException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        File fileToAdd = new File("testFile.txt");
        String fileNameInArchive = "testFile.txt";

        tarArchiveOutputStream.putNextEntry(new TarArchiveEntry(fileToAdd, fileNameInArchive));

        tarArchiveOutputStream.finish(); // Close the archive, but not the entry
        tarArchiveOutputStream.close();

        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        Assert.assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.closeArchiveEntry();
        });
    }

    private byte[] getExpectedTarContent(int fileContentLength) {
        // Header record + File record + EOF record
        int headerRecordLength = TarConstants.STD_MAGIC.length
                + Long.BYTES * 2
                + Short.BYTES * 2
                + Short.BYTES * 2
                + Short.BYTES * 2
                + fileContentLength.length + 1;

        byte[] headerRecord = TarConstants.STD_MAGIC;
        byte[] fileSize = TestUtils.intToBytes(headerRecordLength - TarConstants.STD_MAGIC.length);
        byte[] fileTypeFlag = TestUtils.shortToBytes((short) TarConstants.STD_FILETYPE);
        byte[] modificationTime = TestUtils.longToBytes(System.currentTimeMillis());
        byte[] fileNameLength = TestUtils.shortToBytes((short) (fileNameInArchive.length()));

        System.arraycopy(fileSize, 0, headerRecord, TarConstants.NAMELEN, fileSize.length);
        System.arraycopy(fileTypeFlag, 0, headerRecord, headerRecord.length - fileTypeFlag.length, fileTypeFlag.length);

        byte[] record = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(headerRecord, 0, record, 0, headerRecord.length);

        byte[] expectedTarContent = new byte[headerRecord.length + fileContentLength + 2 * TarConstants.RECORD_LENGTH];

        System.arraycopy(record, 0, expectedTarContent, 0, record.length);
        System.arraycopy(fileContent, 0, expectedTarContent, record.length, fileContent.length);
        System.arraycopy(record, 0, expectedTarContent, record.length + fileContent.length, record.length);

        return expectedTarContent;
    }

    private byte[] getExpectedTarContentForLongFileName(int fileContentLength) {
        // LongName record + Header record + File record + EOF record
        int headerRecordLength = TarConstants.LF_MAGIC.length
                + Long.BYTES * 2
                + Long.BYTES * 2
                + Short.BYTES * 2
                + Short.BYTES * 2
                + Short.BYTES * 2
                + (fileNameInArchive.length() + 1).length
                + fileContentLength.length + 1;

        byte[] longNameRecord = TarConstants.LF_NAME;
        byte[] longNameRecordLength = TestUtils.intToBytes(fileNameInArchive.length() + 1);
        byte[] fileNameInArchiveBytes = ArchiveUtils.toAsciiBytes(fileNameInArchive);

        byte[] headerRecord = new byte[headerRecordLength - longNameRecord.length - longNameRecordLength.length];
        byte[] fileSize = TestUtils.intToBytes(headerRecordLength - TarConstants.LF_MAGIC.length);
        byte[] fileTypeFlag = TestUtils.shortToBytes((short) TarConstants.STD_FILETYPE);
        byte[] modificationTime = TestUtils.longToBytes(System.currentTimeMillis());
        byte[] fileNameLength = TestUtils.shortToBytes((short) (fileNameInArchive.length()));

        System.arraycopy(fileSize, 0, headerRecord, 0, fileSize.length);
        System.arraycopy(fileTypeFlag, 0, headerRecord, headerRecord.length - fileTypeFlag.length, fileTypeFlag.length);

        byte[] record = new byte[TarConstants.RECORD_LENGTH];
        System.arraycopy(headerRecord, 0, record, 0, headerRecord.length);

        byte[] expectedTarContent = new byte[headerRecord.length + longNameRecord.length
                + longNameRecordLength.length + fileNameInArchiveBytes.length
                + fileContentLength.length + 2 * TarConstants.RECORD_LENGTH];

        System.arraycopy(longNameRecord, 0, expectedTarContent, 0, longNameRecord.length);
        System.arraycopy(longNameRecordLength, 0, expectedTarContent, longNameRecord.length, longNameRecordLength.length);
        System.arraycopy(fileNameInArchiveBytes, 0, expectedTarContent, longNameRecord.length + longNameRecordLength.length, fileNameInArchiveBytes.length);
        System.arraycopy(headerRecord, 0, expectedTarContent, longNameRecord.length + longNameRecordLength.length + fileNameInArchiveBytes.length, headerRecord.length);
        System.arraycopy(record, 0, expectedTarContent, headerRecord.length + headerRecord.length, record.length);
        System.arraycopy(record, 0, expectedTarContent, headerRecord.length + headerRecord.length + record.length, record.length);

        return expectedTarContent;
    }
}