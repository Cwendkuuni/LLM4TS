import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testWriteEntryWithValidData() throws IOException {
        String fileName = "testFile.txt";
        String fileContent = "Test content";
        long fileSize = fileContent.length();

        CpioArchiveEntry entry = createEntry(fileName, fileSize, CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);
        outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        byte[] expectedHeader = createExpectedHeader(fileName, fileSize);
        byte[] actualHeader = extractHeaderFromByteArrayOutputStream(outputStream.getBuffer());

        Assert.assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        String fileName1 = "testFile1.txt";
        String fileContent1 = "Test content 1";
        long fileSize1 = fileContent1.length();

        String fileName2 = "testFile2.txt";
        String fileContent2 = "Test content 2";
        long fileSize2 = fileContent2.length();

        CpioArchiveEntry entry1 = createEntry(fileName1, fileSize1, CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry1);
        outputStream.write(fileContent1.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = createEntry(fileName2, fileSize2, CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry2);
        outputStream.write(fileContent2.getBytes(StandardCharsets.UTF_8));
        outputStream.closeArchiveEntry();

        outputStream.finish();
        outputStream.close();

        byte[] expectedTrailer = createExpectedTrailer();
        byte[] actualTrailer = extractTrailerFromByteArrayOutputStream(outputStream.getBuffer());

        Assert.assertArrayEquals(expectedTrailer, actualTrailer);
    }

    @Test(expected = IOException.class)
    public void testWriteEntryWithNegativeFileSize() throws IOException {
        String fileName = "testFile.txt";
        long fileSize = -1;

        CpioArchiveEntry entry = createEntry(fileName, fileSize, CpioConstants.C_ISREG);
        outputStream.putArchiveEntry(entry);

        // The test should fail with an IOException
        outputStream.write(new byte[0]);
        outputStream.closeArchiveEntry();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteEntryWithInvalidFormat() throws IOException {
        CpioArchiveOutputStream outputStreamWithInvalidFormat = new CpioArchiveOutputStream(byteArrayOutputStream, (short) 123);

        String fileName = "testFile.txt";
        long fileSize = 10;

        CpioArchiveEntry entry = createEntry(fileName, fileSize, CpioConstants.C_ISREG);
        outputStreamWithInvalidFormat.putArchiveEntry(entry);

        // The test should fail with an IllegalArgumentException
        outputStreamWithInvalidFormat.write("Test content".getBytes());
        outputStreamWithInvalidFormat.closeArchiveEntry();
        outputStreamWithInvalidFormat.close();
    }

    @Test
    public void testWriteEntryWithHeaderPadCount() throws IOException {
        String fileName = "testFile.txt";
        long fileSize = 15;
        int headerPadCount = 7;

        CpioArchiveEntry entry = createEntry(fileName, fileSize, CpioConstants.C_ISREG);
        entry.setHeaderPadCount(headerPadCount);

        outputStream.putArchiveEntry(entry);
        outputStream.write("Test content".getBytes());
        outputStream.closeArchiveEntry();

        byte[] expectedHeader = createExpectedHeaderWithPadCount(fileName, fileSize, headerPadCount);
        byte[] actualHeader = extractHeaderFromByteArrayOutputStream(outputStream.getBuffer());

        Assert.assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testWriteEntryWithDataPadCount() throws IOException {
        String fileName = "testFile.txt";
        long fileSize = 15;
        int dataPadCount = 7;

        CpioArchiveEntry entry = createEntry(fileName, fileSize, CpioConstants.C_ISREG);
        entry.setDataPadCount(dataPadCount);

        byte[] testData = new byte[fileSize + dataPadCount];
        outputStream.putArchiveEntry(entry);
        outputStream.write(testData);
        outputStream.closeArchiveEntry();

        byte[] expectedData = new byte[fileSize + dataPadCount];
        for (int i = 0; i < fileSize; i++) {
            expectedData[i] = (byte) ('A' + (i % 26));
        }

        Assert.assertArrayEquals(expectedData, extractEntryDataFromByteArrayOutputStream(outputStream.getBuffer(), fileName));
    }

    private CpioArchiveEntry createEntry(String fileName, long fileSize, short format) {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(fileSize);
        entry.setFormat(format);
        return entry;
    }

    private byte[] createExpectedHeader(String fileName, long fileSize) {
        byte[] magic = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        byte[] header = createNewEntryHeader(fileName, fileSize);
        return mergeByteArrays(magic, header);
    }

    private byte[] createExpectedHeaderWithPadCount(String fileName, long fileSize, int padCount) {
        byte[] magic = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        byte[] header = createNewEntryHeaderWithPadCount(fileName, fileSize, padCount);
        return mergeByteArrays(magic, header);
    }

    private byte[] createExpectedTrailer() {
        byte[] magic = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW_CRC);
        byte[] trailer = createTrailer();
        return mergeByteArrays(magic, trailer);
    }

    private byte[] extractHeaderFromByteArrayOutputStream(byte[] buffer) {
        int index = 0;
        byte[] header = new byte[512];
        for (int i = 0; i < header.length; i++) {
            header[i] = buffer[index++];
        }
        return header;
    }

    private byte[] extractTrailerFromByteArrayOutputStream(byte[] buffer) {
        int index = 0;
        byte[] trailer = new byte[512];
        for (int i = buffer.length - trailer.length; i < buffer.length; i++) {
            trailer[i - (buffer.length - trailer.length)] = buffer[i];
        }
        return trailer;
    }

    private byte[] extractEntryDataFromByteArrayOutputStream(byte[] buffer, String fileName) {
        int index = findEntryIndex(fileName, buffer);
        int entryDataStartIndex = findEntryDataStartIndex(buffer, index);
        int entryDataLength = findEntryDataLength(buffer, entryDataStartIndex);

        byte[] entryData = new byte[entryDataLength];
        for (int i = 0; i < entryData.length; i++) {
            entryData[i] = buffer[entryDataStartIndex + i];
        }
        return entryData;
    }

    private int findEntryIndex(String fileName, byte[] buffer) {
        for (int i = 0; i < buffer.length - fileName.length() - 1; i++) {
            if (compareStrings(CpioArchiveEntry.getName(buffer, i), fileName)) {
                return i;
            }
        }
        throw new IllegalStateException("Failed to find expected entry in the byte array");
    }

    private int findEntryDataStartIndex(byte[] buffer, int entryIndex) {
        return findIndexOfByte(buffer[entryIndex], buffer, entryIndex + 512);
    }

    private int findEntryDataLength(byte[] buffer, int entryDataStartIndex) {
        int sizeIndex = findIndexOfByte('S', buffer, entryDataStartIndex);
        int size = CpioUtil.bytes2Long(buffer, sizeIndex, false)[0];
        return (int) size;
    }

    private byte[] createNewEntryHeader(String fileName, long fileSize) {
        byte[] name = ArchiveUtils.toAsciiBytes(fileName);
        byte[] size = Ints.toByteArray(fileSize);
        byte[] mode = Ints.toByteArray((int) CpioConstants.C_ISREG);
        byte[] uid = Ints.toByteArray((int) 0);
        byte[] gid = Ints.toByteArray((int) 0);
        byte[] numberOfLinks = Ints.toByteArray((int) 1);
        byte[] time = Ints.toByteArray((int) (System.currentTimeMillis() / 1000));
        byte[] chksum = Ints.toByteArray((int) 0);

        return mergeByteArrays(name, size, mode, uid, gid, numberOfLinks, time, chksum);
    }

    private byte[] createNewEntryHeaderWithPadCount(String fileName, long fileSize, int padCount) {
        byte[] name = ArchiveUtils.toAsciiBytes(fileName);
        byte[] size = Ints.toByteArray(fileSize);
        byte[] mode = Ints.toByteArray((int) CpioConstants.C_ISREG);
        byte[] uid = Ints.toByteArray((int) 0);
        byte[] gid = Ints.toByteArray((int) 0);
        byte[] numberOfLinks = Ints.toByteArray((int) 1);
        byte[] time = Ints.toByteArray((int) (System.currentTimeMillis() / 1000));
        byte[] chksum = Ints.toByteArray((int) 0);
        byte[] headerPadCount = Ints.toByteArray((int) padCount);

        return mergeByteArrays(name, size, mode, uid, gid, numberOfLinks, time, chksum, headerPadCount);
    }

    private byte[] createTrailer() {
        byte[] name = ArchiveUtils.toAsciiBytes(CpioConstants.CPIO_TRAILER);
        byte[] numberOfLinks = Ints.toByteArray((int) 1);
        byte[] size = Ints.toByteArray(0);
        byte[] magic = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW_CRC);

        return mergeByteArrays(name, numberOfLinks, size, magic);
    }

    private byte[] mergeByteArrays(byte[]... byteArrays) {
        int length = 0;
        for (byte[] array : byteArrays) {
            length += array.length;
        }

        byte[] result = new byte[length];
        int index = 0;
        for (byte[] array : byteArrays) {
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }
        return result;
    }

    private int findIndexOfByte(byte targetByte, byte[] array, int startIndex) {
        for (int i = startIndex; i < array.length; i++) {
            if (array[i] == targetByte) {
                return i;
            }
        }
        throw new IllegalStateException("Failed to find expected byte in the byte array");
    }

    private boolean compareStrings(String str1, String str2) {
        return str1.equals(str2);
    }

    private boolean compareStrings(byte[] nameBytes, String fileName) {
        return new String(nameBytes, StandardCharsets.UTF_8).equals(fileName);
    }
}