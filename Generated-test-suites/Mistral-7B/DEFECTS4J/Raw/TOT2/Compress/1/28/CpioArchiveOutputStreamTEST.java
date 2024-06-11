import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveStreamTestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new FileOutputStream(TEST_FILE_NAME);
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutNextEntryWithValidParameters() throws IOException {
        String fileName = "testfile.txt";
        String fileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(fileContent.length());
        cpioOutputStream.putNextEntry(entry);
        byte[] fileContentBytes = fileContent.getBytes();
        cpioOutputStream.write(fileContentBytes, 0, fileContentBytes.length);
        cpioOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        cpioOutputStream.putNextEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        String fileName = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        String fileName = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII + 1); // Invalid format
        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testWriteWithValidParameters() throws IOException {
        String fileName = "testfile.txt";
        String fileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(fileContent.length());
        cpioOutputStream.putNextEntry(entry);
        byte[] fileContentBytes = fileContent.getBytes();
        cpioOutputStream.write(fileContentBytes, 0, fileContentBytes.length);
        cpioOutputStream.closeArchiveEntry();

        byte[] testData = readFile(TEST_FILE_NAME);
        Assert.assertArrayEquals(fileContentBytes, testData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        byte[] data = null;
        cpioOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        byte[] data = new byte[10];
        cpioOutputStream.write(data, -5, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNegativeLength() throws IOException {
        byte[] data = new byte[10];
        cpioOutputStream.write(data, 0, -5);
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        Assert.assertTrue(cpioOutputStream.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();
        Assert.assertTrue(cpioOutputStream.isClosed());
    }

    @Test
    public void testCloseTwice() throws IOException {
        cpioOutputStream.close();
        cpioOutputStream.close();
        Assert.assertTrue(cpioOutputStream.isClosed());
    }

    @Test
    public void testCloseAfterFinish() throws IOException {
        cpioOutputStream.finish();
        cpioOutputStream.close();
        Assert.assertTrue(cpioOutputStream.isClosed());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String fileName = "testfile.txt";
        String fileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(fileContent.length());
        cpioOutputStream.putNextEntry(entry);
        byte[] fileContentBytes = fileContent.getBytes();
        cpioOutputStream.write(fileContentBytes, 0, fileContentBytes.length);
        cpioOutputStream.closeArchiveEntry();
        Assert.assertNull(cpioOutputStream.getCpioEntry());
    }

    @Test
    public void testCloseArchiveEntryTwice() throws IOException {
        String fileName = "testfile.txt";
        String fileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(fileContent.length());
        cpioOutputStream.putNextEntry(entry);
        byte[] fileContentBytes = fileContent.getBytes();
        cpioOutputStream.write(fileContentBytes, 0, fileContentBytes.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.closeArchiveEntry();
        Assert.assertNull(cpioOutputStream.getCpioEntry());
    }

    @Test
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        String fileName = "testfile.txt";
        String fileContent = "Test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setSize(fileContent.length());
        cpioOutputStream.putNextEntry(entry);
        byte[] fileContentBytes = fileContent.getBytes();
        cpioOutputStream.write(fileContentBytes, 0, fileContentBytes.length);
        cpioOutputStream.finish();
        cpioOutputStream.closeArchiveEntry();
        Assert.assertNull(cpioOutputStream.getCpioEntry());
    }

    @Test
    public void testCloseWithFinishedStream() throws IOException {
        cpioOutputStream.finish();
        cpioOutputStream.close();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        Assert.assertFalse(cpioOutputStream.isFinished());
    }

    @Test
    public void testCloseWithClosedStream() throws IOException {
        cpioOutputStream.close();
        cpioOutputStream.close();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        Assert.assertFalse(cpioOutputStream.isClosed());
    }

    @Test
    public void testCloseAfterFinishWithClosedStream() throws IOException {
        cpioOutputStream.finish();
        cpioOutputStream.close();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        cpioOutputStream.finish();
        cpioOutputStream.close();
        Assert.assertFalse(cpioOutputStream.isFinished());
        Assert.assertFalse(cpioOutputStream.isClosed());
    }

    private byte[] readFile(String fileName) throws IOException {
        return IOUtils.toByteArray(new FileOutputStream(fileName));
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        deleteFile(TEST_FILE_NAME);
    }

    private void deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}