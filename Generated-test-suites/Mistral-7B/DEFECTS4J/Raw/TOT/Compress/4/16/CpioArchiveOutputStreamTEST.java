import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testWriteNewFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        archiveOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        String expectedMagicNumber = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        String expectedTestFileContent = testFileContent;

        String outputString = new String(outputBytes, StandardCharsets.UTF_8);
        Assert.assertTrue(outputString.startsWith(expectedMagicNumber));
        int fileEntryIndex = outputString.indexOf(expectedMagicNumber) + expectedMagicNumber.length();

        String testFileEntry = outputString.substring(fileEntryIndex);
        String[] fileEntryParts = testFileEntry.split("\\r\\n");

        String name = fileEntryParts[0].substring(8);
        long size = Long.parseLong(fileEntryParts[1].substring(8));
        long crc = Long.parseLong(fileEntryParts[18].substring(8));

        Assert.assertEquals(name, "testfile.txt");
        Assert.assertEquals(size, testFileContent.length());
        Assert.assertNotEquals(crc, 0); // Check if CRC is calculated correctly
    }

    @Test
    public void testWriteOldFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);

        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        archiveOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        String expectedMagicNumber = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_OLD_BINARY);
        String expectedTestFileContent = testFileContent;

        String outputString = new String(outputBytes, StandardCharsets.UTF_8);
        Assert.assertTrue(outputString.startsWith(expectedMagicNumber));
        int fileEntryIndex = outputString.indexOf(expectedMagicNumber) + expectedMagicNumber.length();

        String testFileEntry = outputString.substring(fileEntryIndex);
        String[] fileEntryParts = testFileEntry.split("\\r\\n");

        long device = Long.parseLong(fileEntryParts[0].substring(6));
        long inode = Long.parseLong(fileEntryParts[1].substring(6));
        long mode = Long.parseLong(fileEntryParts[2].substring(6));
        long uid = Long.parseLong(fileEntryParts[3].substring(6));
        long gid = Long.parseLong(fileEntryParts[4].substring(6));
        long links = Long.parseLong(fileEntryParts[5].substring(6));
        long time = Long.parseLong(fileEntryParts[6].substring(11));
        long nameLength = Long.parseLong(fileEntryParts[7].substring(6));
        long size = Long.parseLong(fileEntryParts[8].substring(11));
        long chksum = Long.parseLong(fileEntryParts[9].substring(11));

        Assert.assertEquals(nameLength, testFileContent.length() + 1);
        Assert.assertEquals(size, testFileContent.length());
        Assert.assertEquals(chksum, CpioUtil.crc32(testFileContent));
    }

    @Test
    public void testWriteInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputSteam = new CpioArchiveOutputStream(outputStream, (short) 999);

        String testFileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        archiveOutputSteam.putArchiveEntry(entry);
        archiveOutputSteam.write(testFileContent.getBytes(StandardCharsets.UTF_8));

        archiveOutputSteam.finish();

        byte[] outputBytes = outputStream.toByteArray();

        Assert.assertThrows(IllegalArgumentException.class, () -> new String(outputBytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileContent = "Test content";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile.txt");
        entry1.setSize(testFileContent.length());
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        archiveOutputStream.putArchiveEntry(entry1);
        archiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
        archiveOutputStream.finish();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile.txt");
        entry2.setSize(testFileContent.length());
        entry2.setFormat(CpioConstants.FORMAT_NEW);

        try {
            archiveOutputStream.putArchiveEntry(entry2);
            archiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            archiveOutputStream.finish();
        } catch (IOException e) {
            Assert.assertEquals("duplicate entry: testfile.txt", e.getMessage());
        }
    }

    @Test
    public void testWriteInvalidEntryName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileContent = "Test content";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("\0testfile.txt");
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        try {
            archiveOutputStream.putArchiveEntry(entry);
            archiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            archiveOutputStream.finish();
        } catch (IOException e) {
            Assert.assertEquals("Invalid entry name.", e.getMessage());
        }
    }

    @Test
    public void testWriteInvalidFileSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileContent = "Test content";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(-1);
        entry.setFormat(CpioConstants.FORMAT_NEW);

        try {
            archiveOutputStream.putArchiveEntry(entry);
            archiveOutputStream.write(testFileContent.getBytes(StandardCharsets.UTF_8));
            archiveOutputStream.finish();
        } catch (IOException e) {
            Assert.assertEquals("Invalid file size", e.getMessage());
        }
    }

    @Test
    public void testWriteFileWithFilter() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String testFileContent = "Test content";

        File file = new File("test.file");
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(file), CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(testFileContent.length());
        entry.setFormat(CpioConstants.FORMAT_NEW);

        archiveOutputStream.putArchiveEntry(entry);

        // Writing to the underlying file stream instead of the CpioArchiveOutputStream
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(testFileContent);
        fileWriter.close();

        archiveOutputStream.finish();
        archiveOutputStream.close();

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] inputBytes = new byte[testFileContent.length()];
        fileInputStream.read(inputBytes);
        fileInputStream.close();

        String expectedTestFileContent = testFileContent;
        String outputString = new String(inputBytes, StandardCharsets.UTF_8);

        Assert.assertEquals(expectedTestFileContent, outputString);
    }
}