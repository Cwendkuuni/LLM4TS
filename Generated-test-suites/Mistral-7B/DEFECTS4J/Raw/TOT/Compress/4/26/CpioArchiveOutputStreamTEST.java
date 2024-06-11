import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(TEST_FILE_NAME), CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testWriteSimpleEntry() throws IOException {
        String entryName = "test-file.txt";
        byte[] testData = "This is a test file".getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setFileSize(testData.length);
        entry.setTime(System.currentTimeMillis());
        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();

        InputStream is = new FileInputStream(TEST_FILE_NAME);
        byte[] result = readBytesFromStream(is, testData.length + 1 + entryName.length() + 1); // +1 for each newline character and +1 for null character

        Assert.assertArrayEquals(testData, result, 0, testData.length);

        is.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteInvalidFormat() throws IOException {
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(TEST_FILE_NAME), (short) 99);
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        String entryName1 = "test-file1.txt";
        byte[] testData1 = "This is the first test file".getBytes();
        String entryName2 = "test-file2.txt";
        byte[] testData2 = "This is the second test file".getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(entryName1);
        entry1.setFileSize(testData1.length);
        entry1.setTime(System.currentTimeMillis());
        outputStream.putArchiveEntry(entry1);
        outputStream.write(testData1, 0, testData1.length);
        outputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(entryName2);
        entry2.setFileSize(testData2.length);
        entry2.setTime(System.currentTimeMillis());
        outputStream.putArchiveEntry(entry2);
        outputStream.write(testData2, 0, testData2.length);
        outputStream.closeArchiveEntry();

        outputStream.finish();
        outputStream.close();

        InputStream is = new FileInputStream(TEST_FILE_NAME);
        byte[] result = readBytesFromStream(is, testData1.length + 1 + entryName1.length() + 1); // +1 for each newline character and +1 for null character

        Assert.assertArrayEquals(testData1, result, 0, testData1.length);
        result = readBytesFromStream(is, testData2.length + 1 + entryName2.length() + 1); // +1 for each newline character and +1 for null character

        Assert.assertArrayEquals(testData2, result, 0, testData2.length);

        is.close();
    }

    private byte[] readBytesFromStream(InputStream is, int length) throws IOException {
        byte[] result = new byte[length];
        int bytesRead = is.read(result, 0, length);
        if (bytesRead < length) {
            throw new IOException("Expected to read " + length + " bytes but only read " + bytesRead);
        }
        return result;
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        File file = new File(TEST_FILE_NAME);
        file.delete();
    }
}