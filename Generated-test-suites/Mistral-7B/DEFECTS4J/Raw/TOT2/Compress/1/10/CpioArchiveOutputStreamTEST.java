import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private Map<String, CpioArchiveEntry> names;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        names = new HashMap<>();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        // Test putting a new entry with default header format and valid data
        CpioArchiveEntry entry1 = createEntry("test1.txt", 10);
        outputStream.putNextEntry(entry1);
        writeTestData("Hello World!".getBytes());
        outputStream.finish();

        // Test reading the written entry
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Assert.assertEquals("test1.txt", readName(inputStream));
        Assert.assertEquals(10, readSize(inputStream));
        Assert.assertEquals("Hello World!\0", readContent(inputStream));

        // Test putting a new entry with a specific header format
        CpioArchiveEntry entry2 = createEntry("test2.txt", 20, CpioConstants.FORMAT_OLD_BINARY);
        outputStream.putNextEntry(entry2);
        writeTestData("Test entry 2 data".getBytes(), true);
        outputStream.finish();

        // Test reading the written entry
        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Assert.assertEquals("test2.txt", readName(inputStream));
        Assert.assertEquals(20, readSize(inputStream));
        Assert.assertEquals("Test entry 2 data", readContent(inputStream));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = createEntry("test.txt", 10);
        outputStream.putNextEntry(entry1);
        outputStream.putNextEntry(entry1);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = createEntry("test.txt", 10);
        outputStream.putNextEntry(entry);
        outputStream.setFormat((short) 0xFF); // Invalid format
        try {
            outputStream.putNextEntry(entry);
        } finally {
            outputStream.close();
        }
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        try {
            outputStream.putNextEntry(null);
        } finally {
            outputStream.close();
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = createEntry("test.txt", 10);
        outputStream.putNextEntry(entry);
        writeTestData("Test data".getBytes());
        outputStream.closeArchiveEntry();

        // Test reading the incomplete entry
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Assert.assertEquals("test.txt", readName(inputStream));
        Assert.assertEquals(10, readSize(inputStream));
        Assert.assertFalse(inputStream.read() > -1);
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = createEntry("test.txt", 10);
        outputStream.putNextEntry(entry);

        byte[] testData = "Test data to be written".getBytes();
        outputStream.write(testData, 0, testData.length);
        outputStream.finish();

        // Test reading the written data
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] readData = new byte[testData.length];
        inputStream.read(readData);
        Assert.assertArrayEquals(testData, readData);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        CpioArchiveEntry entry = createEntry("test.txt", 10);
        outputStream.putNextEntry(entry);
        outputStream.finish();

        byte[] testData = "Test data to be written".getBytes();
        outputStream.write(testData, 0, testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        CpioArchiveEntry entry = createEntry("test.txt", 10);
        outputStream.putNextEntry(entry);
        outputStream.close();

        byte[] testData = "Test data to be written".getBytes();
        outputStream.write(testData, 0, testData.length);
    }

    private void writeTestData(byte[] testData) throws IOException {
        outputStream.write(testData, 0, testData.length);
    }

    private void writeTestData(byte[] testData, boolean swapHalfWord) throws IOException {
        outputStream.setSwapBytes(swapHalfWord);
        outputStream.write(testData, 0, testData.length);
        outputStream.setSwapBytes(false);
    }

    private String readName(InputStream inputStream) throws IOException {
        byte[] nameBytes = new byte[NAME_LENGTH];
        inputStream.read(nameBytes);
        return new String(nameBytes);
    }

    private long readSize(InputStream inputStream) throws IOException {
        byte[] sizeBytes = new byte[SIZE_LENGTH];
        inputStream.read(sizeBytes);
        return CpioUtil.bytes2long(sizeBytes);
    }

    private String readContent(InputStream inputStream) throws IOException {
        byte[] contentBytes = new byte[CONTENT_LENGTH];
        inputStream.read(contentBytes);
        return new String(contentBytes);
    }

    private CpioArchiveEntry createEntry(String name, long size) {
        return createEntry(name, size, (short) 0);
    }

    private CpioArchiveEntry createEntry(String name, long size, short format) {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setSize(size);
        entry.setFormat(format);
        return entry;
    }
}