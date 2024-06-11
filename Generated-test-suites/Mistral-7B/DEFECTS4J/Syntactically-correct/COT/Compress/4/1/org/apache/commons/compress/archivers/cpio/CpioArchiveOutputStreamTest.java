import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructors() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(new File("test.cpio")));
        Assert.assertEquals(CpioConstants.FORMAT_NEW, outputStream.getEntryFormat());

        CpioArchiveOutputStream outputStream2 = new CpioArchiveOutputStream(
                new FileOutputStream(new File("test.cpio")),
                CpioConstants.FORMAT_NEW_CRC);
        Assert.assertEquals(CpioConstants.FORMAT_NEW_CRC, outputStream2.getEntryFormat());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(new File("test.cpio"))) {
            @Override
            public void writeHeader(CpioArchiveEntry entry) throws IOException {
                // Do nothing, as this would write to a file
            }
        };

        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putArchiveEntry(entry);
        Assert.assertNull(outputStream.entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithException() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(new File("test.cpio"))) {
            @Override
            public void writeHeader(CpioArchiveEntry entry) throws IOException {
                throw new IOException("Test exception");
            }
        };

        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream);

        byte[] testData = "Test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putArchiveEntry(entry);
        outputStream.write(testData, 0, testData.length);

        inputTest(outputStream.toString(), testData);
    }

    @Test
    public void testWriteWithHeader() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream);

        byte[] testData = "Test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(testData.length);
        writeHeader(outputStream, entry);
        outputStream.write(testData, 0, testData.length);

        inputTest(outputStream.toString(), testData);
    }

    @Test
    public void testWriteWithHeaderAndCRC() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream, CpioConstants.FORMAT_NEW_CRC);

        byte[] testData = "Test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(testData.length);
        writeHeader(outputStream, entry);
        outputStream.write(testData, 0, testData.length);

        byte[] expectedOutput = createCpioFile("test.txt", testData);
        inputTest(outputStream.toString(), testData);
        Assert.assertArrayEquals(expectedOutput, outputStream.toString().getBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNonExistentEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream);

        writeHeader(outputStream, new CpioArchiveEntry());
        outputStream.write("Test data".getBytes(), 0, "Test data".length());

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("non-existent.txt");
        outputStream.putArchiveEntry(entry);
        outputStream.write("Test data 2".getBytes(), 0, "Test data 2".length());

        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toString().getBytes());
        Assert.assertThrows(IOException.class, () -> {
            CpioArchiveEntry entry2 = new CpioArchiveEntry(inputStream);
            inputStream.read(new byte[1]);
        });
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream);

        byte[] testData1 = "Test 1 data".getBytes();
        byte[] testData2 = "Test 2 data".getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(testData1.length);
        writeHeader(outputStream, entry1);
        outputStream.write(testData1, 0, testData1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(testData2.length);
        writeHeader(outputStream, entry2);
        outputStream.write(testData2, 0, testData2.length);

        inputTest(outputStream.toString(), testData1, testData2);
    }

    @Test
    public void testWriteMultipleEntriesWithCRC() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream, CpioConstants.FORMAT_NEW_CRC);

        byte[] testData1 = "Test 1 data".getBytes();
        byte[] testData2 = "Test 2 data".getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test1.txt");
        entry1.setSize(testData1.length);
        writeHeader(outputStream, entry1);
        outputStream.write(testData1, 0, testData1.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test2.txt");
        entry2.setSize(testData2.length);
        writeHeader(outputStream, entry2);
        outputStream.write(testData2, 0, testData2.length);

        byte[] expectedOutput = createCpioFile("test1.txt", testData1, "test2.txt", testData2);

        inputTest(outputStream.toString(), testData1, testData2);
        Assert.assertArrayEquals(expectedOutput, outputStream.toString().getBytes());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream);

        byte[] testData = "Test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        writeHeader(outputStream, entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.finish();

        byte[] expectedOutput = createCpioFile("test.txt", testData);
        inputTest(outputStream.toString(), testData);
        Assert.assertArrayEquals(expectedOutput, outputStream.toString().getBytes());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                byteArrayOutputStream);

        byte[] testData = "Test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        writeHeader(outputStream, entry);
        outputStream.write(testData, 0, testData.length);
        outputStream.close();

        byte[] expectedOutput = createCpioFile("test.txt", testData);
        inputTest(outputStream.toString(), testData);
        Assert.assertArrayEquals(expectedOutput, outputStream.toString().getBytes());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new FileOutputStream(new File("test.cpio")));
        CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, "testFile.txt");
        Assert.assertEquals("testFile.txt", entry.getName());

        String expectedOutput = createCpioFileHeader("testFile.txt") + "testFile.txt\0";
        inputTest(outputStream.toString(), "Test data".getBytes());
        Assert.assertArrayEquals(expectedOutput.getBytes(), outputStream.toString().getBytes());

        testFile.delete();
    }

    private void inputTest(String testData, byte[]... testDataArrays) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Map<String, CpioArchiveEntry> entries = new HashMap<>();

        CpioArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        CpioArchiveEntry currentEntry = null;
        while ((currentEntry = archiveInputStream.getNextEntry()) != null) {
            entries.put(currentEntry.getName(), currentEntry);
        }

        for (byte[] testDataArray : testDataArrays) {
            CpioArchiveEntry expectedEntry = entries.get("test.txt");
            Assert.assertNotNull(expectedEntry);
            Assert.assertEquals(testDataArray.length, expectedEntry.getSize());
            byte[] readData = new byte[testDataArray.length];
            archiveInputStream.read(readData);
            Assert.assertArrayEquals(testDataArray, readData);
        }

        archiveInputStream.close();
    }

    private void writeHeader(CpioArchiveOutputStream outputStream, CpioArchiveEntry entry) throws IOException {
        if (outputStream instanceof FilterOutputStream) {
            FilterOutputStream filterOutputStream = (FilterOutputStream) outputStream;
            OutputStream underlyingOutputStream = filterOutputStream.getOutputStream();
            CpioUtil.write(underlyingOutputStream, outputStream, entry);
        } else {
            outputStream.writeHeader(entry);
        }
    }

    private byte[] createCpioFile(String fileName, byte[] fileData) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        writeHeader(outputStream, entry);
        outputStream.write(fileData, 0, fileData.length);
        outputStream.finish();
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] createCpioFile(String fileName, byte[] fileData, String fileName2, byte[] fileData2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(fileName);
        writeHeader(outputStream, entry1);
        outputStream.write(fileData, 0, fileData.length);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(fileName2);
        writeHeader(outputStream, entry2);
        outputStream.write(fileData2, 0, fileData2.length);

        outputStream.finish();
        return byteArrayOutputStream.toByteArray();
    }

    private String createCpioFileHeader(String fileName) {
        return String.format("%s%s\0%d\0%d\0%d\0%d\0%d\0%d\0%d\0%d\0%d\0%d\0%d\0%s\0",
                CpioConstants.MAGIC_NEW,
                Long.toUnsignedLong(CpioConstants.FORMAT_NEW),
                (short) 1,
                Long.toUnsignedLong(System.currentTimeMillis()),
                (short) 1,
                Long.toUnsignedLong(0),
                Long.toUnsignedLong(0),
                Long.toUnsignedLong(fileName.length() + 1),
                Long.toUnsignedLong(fileName.length()),
                Long.toUnsignedLong(fileName.length()),
                (short) 0,
                (short) 0,
                fileName);
    }
}