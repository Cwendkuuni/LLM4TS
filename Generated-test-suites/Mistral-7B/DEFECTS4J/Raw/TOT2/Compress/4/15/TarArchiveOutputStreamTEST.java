import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Tests for the TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private static final int BLOCK_SIZE = 512;
    private static final int RECORD_SIZE = 512;
    private static final int FILE_NAME_LENGTH = 12;

    private File tempFile;
    private ByteArrayOutputStream out;
    private TarArchiveOutputStream tarOut;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("test", null);
        out = new ByteArrayOutputStream(BLOCK_SIZE);
        tarOut = new TarArchiveOutputStream(out, BLOCK_SIZE, RECORD_SIZE);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.close();
        tempFile.delete();
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOut.putArchiveEntry(entry);
        byte[] fileContent = "This is a test file.".getBytes(StandardCharsets.UTF_8);
        tarOut.write(fileContent, 0, fileContent.length);
        tarOut.closeArchiveEntry();

        Assert.assertEquals(RECORD_SIZE, out.size());
        byte[] record = Arrays.copyOfRange(out.toByteArray(), 0, RECORD_SIZE);
        Assert.assertArrayEquals(TarConstants.MAGIC_NUMBER, Arrays.copyOfRange(record, 0, TarConstants.MAGIC_NUMBER.length), "Invalid magic number");
        Assert.assertEquals(TarConstants.TAR_FILETYPE_REGULAR, record[TarConstants.HEADER_FILETYPE], "Invalid file type");
        Assert.assertEquals(fileName.getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(record, TarConstants.HEADER_FILENAME_POINTER, TarConstants.HEADER_FILENAME_POINTER + TarConstants.NAMELEN), "Invalid file name");
        Assert.assertEquals((long) fileName.length(), BitConverter.getLong(record, TarConstants.HEADER_FILENAME_LENGTH), "Invalid file name length");
        Assert.assertEquals(fileContent.length, BitConverter.getLong(record, TarConstants.HEADER_FILESIZE), "Invalid file size");
        Assert.assertEquals(0, BitConverter.getLong(record, TarConstants.HEADER_MOD_TIME), "Invalid modification time");
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws IOException {
        String fileName = new String(new char[TarConstants.NAMELEN + 1]) + "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOut.putArchiveEntry(entry);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String fileName = "testFileWithLongName.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOut.putArchiveEntry(entry);
        byte[] fileContent = "This is a test file with a long name.".getBytes(StandardCharsets.UTF_8);
        tarOut.write(fileContent, 0, fileContent.length);
        tarOut.closeArchiveEntry();

        Assert.assertEquals(RECORD_SIZE * 2, out.size());
        byte[] record1 = Arrays.copyOfRange(out.toByteArray(), 0, RECORD_SIZE);
        Assert.assertArrayEquals(TarConstants.MAGIC_NUMBER, Arrays.copyOfRange(record1, 0, TarConstants.MAGIC_NUMBER.length), "Invalid magic number");
        Assert.assertEquals(TarConstants.TAR_FILETYPE_REGULAR, record1[TarConstants.HEADER_FILETYPE], "Invalid file type");
        Assert.assertEquals(fileName.getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(record1, TarConstants.HEADER_FILENAME_POINTER, TarConstants.HEADER_FILENAME_POINTER + TarConstants.NAMELEN), "Invalid file name");
        Assert.assertEquals((long) fileName.length(), BitConverter.getLong(record1, TarConstants.HEADER_FILENAME_LENGTH), "Invalid file name length");
        Assert.assertEquals(fileContent.length, BitConverter.getLong(record1, TarConstants.HEADER_FILESIZE), "Invalid file size");
        Assert.assertEquals(0, BitConverter.getLong(record1, TarConstants.HEADER_MOD_TIME), "Invalid modification time");

        byte[] record2 = Arrays.copyOfRange(out.toByteArray(), RECORD_SIZE, RECORD_SIZE * 2);
        Assert.assertArrayEquals(new byte[0], record2, "Invalid long file name record");
    }

    @Test
    public void testFinish() throws IOException {
        String fileName1 = "testFile1.txt";
        TarArchiveEntry entry1 = new TarArchiveEntry(fileName1);
        tarOut.putArchiveEntry(entry1);
        byte[] fileContent1 = "This is the first test file.".getBytes(StandardCharsets.UTF_8);
        tarOut.write(fileContent1, 0, fileContent1.length);
        tarOut.closeArchiveEntry();

        String fileName2 = "testFile2.txt";
        TarArchiveEntry entry2 = new TarArchiveEntry(fileName2);
        tarOut.putArchiveEntry(entry2);
        byte[] fileContent2 = "This is the second test file.".getBytes(StandardCharsets.UTF_8);
        tarOut.write(fileContent2, 0, fileContent2.length);
        tarOut.closeArchiveEntry();

        tarOut.finish();

        Assert.assertEquals(RECORD_SIZE * 3, out.size());
        byte[] record1 = Arrays.copyOfRange(out.toByteArray(), 0, RECORD_SIZE);
        Assert.assertArrayEquals(TarConstants.MAGIC_NUMBER, Arrays.copyOfRange(record1, 0, TarConstants.MAGIC_NUMBER.length), "Invalid magic number");
        Assert.assertEquals(TarConstants.TAR_FILETYPE_REGULAR, record1[TarConstants.HEADER_FILETYPE], "Invalid file type");
        Assert.assertEquals(fileName1.getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(record1, TarConstants.HEADER_FILENAME_POINTER, TarConstants.HEADER_FILENAME_POINTER + TarConstants.NAMELEN), "Invalid file name");
        Assert.assertEquals((long) fileName1.length(), BitConverter.getLong(record1, TarConstants.HEADER_FILENAME_LENGTH), "Invalid file name length");
        Assert.assertEquals(fileContent1.length, BitConverter.getLong(record1, TarConstants.HEADER_FILESIZE), "Invalid file size");
        Assert.assertEquals(0, BitConverter.getLong(record1, TarConstants.HEADER_MOD_TIME), "Invalid modification time");

        byte[] record2 = Arrays.copyOfRange(out.toByteArray(), RECORD_SIZE, RECORD_SIZE * 2);
        Assert.assertArrayEquals(TarConstants.MAGIC_NUMBER, Arrays.copyOfRange(record2, 0, TarConstants.MAGIC_NUMBER.length), "Invalid magic number");
        Assert.assertEquals(TarConstants.TAR_FILETYPE_REGULAR, record2[TarConstants.HEADER_FILETYPE], "Invalid file type");
        Assert.assertEquals(fileName2.getBytes(StandardCharsets.UTF_8), Arrays.copyOfRange(record2, TarConstants.HEADER_FILENAME_POINTER, TarConstants.HEADER_FILENAME_POINTER + TarConstants.NAMELEN), "Invalid file name");
        Assert.assertEquals((long) fileName2.length(), BitConverter.getLong(record2, TarConstants.HEADER_FILENAME_LENGTH), "Invalid file name length");
        Assert.assertEquals(fileContent2.length, BitConverter.getLong(record2, TarConstants.HEADER_FILESIZE), "Invalid file size");
        Assert.assertEquals(0, BitConverter.getLong(record2, TarConstants.HEADER_MOD_TIME), "Invalid modification time");

        byte[] record3 = Arrays.copyOfRange(out.toByteArray(), RECORD_SIZE * 2, out.size());
        Assert.assertArrayEquals(new byte[0], record3, "Invalid end of archive record");
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[0], 0, 0);
        tarOut.finish();
    }

    @Test
    public void testClose() throws IOException {
        String fileName1 = "testFile1.txt";
        TarArchiveEntry entry1 = new TarArchiveEntry(fileName1);
        tarOut.putArchiveEntry(entry1);
        byte[] fileContent1 = "This is the first test file.".getBytes(StandardCharsets.UTF_8);
        tarOut.write(fileContent1, 0, fileContent1.length);
        tarOut.closeArchiveEntry();
        tarOut.close();

        String fileName2 = "testFile2.txt";
        TarArchiveEntry entry2 = new TarArchiveEntry(fileName2);
        tarOut = new TarArchiveOutputStream(new FileOutputStream(tempFile), BLOCK_SIZE, RECORD_SIZE);
        tarOut.putArchiveEntry(entry2);
        byte[] fileContent2 = "This is the second test file.".getBytes(StandardCharsets.UTF_8);
        tarOut.write(fileContent2, 0, fileContent2.length);
        tarOut.closeArchiveEntry();
        tarOut.close();

        File tarFile = new File(tempFile.getAbsolutePath() + ".tar");
        Assert.assertTrue(tarFile.exists());
        Assert.assertFalse(tempFile.exists());

        TarArchiveInputStream tarIn = new TarArchiveInputStream(new FileInputStream(tarFile));
        TarArchiveEntry entry = tarIn.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(fileName1, entry.getName());
        byte[] fileContent = new ByteArrayInputStream(Arrays.copyOfRange(out.toByteArray(), 0, RECORD_SIZE)).readAllBytes();
        Assert.assertArrayEquals(fileContent, entry.getInputStream().readAllBytes(), "Invalid file content");
        tarIn.closeEntry();
        entry = tarIn.getNextEntry();
        Assert.assertEquals(fileName2, entry.getName());
        fileContent = new ByteArrayInputStream(Arrays.copyOfRange(out.toByteArray(), RECORD_SIZE, out.size())).readAllBytes();
        Assert.assertArrayEquals(fileContent, entry.getInputStream().readAllBytes(), "Invalid file content");
        tarIn.close();
    }
}