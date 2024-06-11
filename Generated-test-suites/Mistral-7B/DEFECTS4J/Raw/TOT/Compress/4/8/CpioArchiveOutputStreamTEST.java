import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testWritingSingleEntry() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        // when
        String testFileName = "testFile";
        String testContent = "Test content";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes(), 0, testContent.length());
        cpioArchiveOutputStream.finish();

        // then
        Assert.assertEquals(testFileName, cpioArchiveOutputStream.getNextEntry().getName());
        Assert.assertEquals(testContent.length(), cpioArchiveOutputStream.getNextEntry().getSize());
        cpioArchiveOutputStream.close();

        // verify
        byte[] testFileContent = new byte[testContent.length()];
        File testFile = new File("test.cpio");
        FileInputStream testFileInputStream = new FileInputStream(testFile);
        testFileInputStream.read(testFileContent);
        testFileInputStream.close();

        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        CpioArchiveOutputStream expectedCpioArchiveOutputStream = new CpioArchiveOutputStream(expectedOutput, CpioConstants.FORMAT_NEW);
        String testExpectedFileName = "testFile";
        CpioArchiveEntry testExpectedEntry = new CpioArchiveEntry();
        testExpectedEntry.setName(testExpectedFileName);
        testExpectedEntry.setFileSize(testContent.length());
        testExpectedEntry.setMode(CpioConstants.C_ISREG); // regular file

        expectedCpioArchiveOutputStream.putArchiveEntry(testExpectedEntry);
        expectedCpioArchiveOutputStream.write(testContent.getBytes(), 0, testContent.length());
        expectedCpioArchiveOutputStream.finish();
        expectedCpioArchiveOutputStream.close();

        byte[] expectedOutputBytes = expectedCpioArchiveOutputStream.toByteArray();
        Assert.assertArrayEquals(expectedOutputBytes, testFileContent);
    }

    @Test
    public void testWritingMultipleEntries() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        // when
        String testFileName1 = "testFile1";
        String testContent1 = "Test content 1";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName1);
        entry1.setFileSize(testContent1.length());
        entry1.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(testContent1.getBytes(), 0, testContent1.length());

        String testFileName2 = "testFile2";
        String testContent2 = "Test content 2";

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName2);
        entry2.setFileSize(testContent2.length());
        entry2.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(testContent2.getBytes(), 0, testContent2.length());

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        // then
        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        expectedEntries.put(testFileName1, new CpioArchiveEntry(testFileName1, testContent1.length(), CpioConstants.C_ISREG));
        expectedEntries.put(testFileName2, new CpioArchiveEntry(testFileName2, testContent2.length(), CpioConstants.C_ISREG));

        byte[] testFileContent = new byte[0];
        File testFile = new File("test.cpio");
        FileInputStream testFileInputStream = new FileInputStream(testFile);

        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        CpioArchiveOutputStream expectedCpioArchiveOutputStream = new CpioArchiveOutputStream(expectedOutput, CpioConstants.FORMAT_NEW);

        for (Map.Entry<String, CpioArchiveEntry> entry : expectedEntries.entrySet()) {
            CpioArchiveEntry expectedEntry = entry.getValue();
            expectedCpioArchiveOutputStream.putArchiveEntry(expectedEntry);
            expectedCpioArchiveOutputStream.write(expectedEntry.getContent().getBytes(), 0, expectedEntry.getContent().length());
        }

        expectedCpioArchiveOutputStream.finish();
        expectedCpioArchiveOutputStream.close();

        testFileContent = expectedCpioArchiveOutputStream.toByteArray();
        testFileContent = testFileInputStream.readAllBytes();
        testFileInputStream.close();

        Assert.assertArrayEquals(testFileContent, expectedOutput.toByteArray());
    }

    @Test
    public void testWritingInvalidEntryFormat() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);

        // when & then
        Assert.assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry()));
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testWritingDuplicateEntry() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName);
        entry1.setFileSize(10);
        entry1.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(new byte[10], 0, 10);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName);
        entry2.setFileSize(10);
        entry2.setMode(CpioConstants.C_ISREG); // regular file

        // when
        Assert.assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry2));

        // then
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testWritingLargeFile() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testLargeFile";
        String testContent = new String(new byte[10 * 1024 * 1024]); // 10MB

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes(), 0, testContent.length());
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        // then
        byte[] testFileContent = new byte[testContent.length()];
        File testFile = new File("test.cpio");
        FileInputStream testFileInputStream = new FileInputStream(testFile);

        testFileContent = testFileInputStream.readAllBytes();
        testFileInputStream.close();

        Assert.assertArrayEquals(testContent.getBytes(), testFileContent);
    }

    @Test
    public void testWritingWithCrc() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW_CRC);

        String testFileName = "testFile";
        String testContent = "Test content";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes(), 0, testContent.length());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] testFileContent = new byte[0];
        File testFile = new File("test.cpio");
        FileInputStream testFileInputStream = new FileInputStream(testFile);

        byte[] expectedOutput = new byte[testContent.length() + CpioConstants.CPIO_HEADER_LENGTH + 8];

        System.arraycopy(CpioUtil.long2byteArray(CpioConstants.MAGIC_NEW_CRC, 2, false), 0, expectedOutput, 0, 8);
        CpioArchiveEntry expectedEntry = new CpioArchiveEntry();
        expectedEntry.setName(testFileName);
        expectedEntry.setFileSize(testContent.length());
        expectedEntry.setMode(CpioConstants.C_ISREG); // regular file
        expectedEntry.setFormat(CpioConstants.FORMAT_NEW_CRC);

        writeEntryHeader(expectedOutput, expectedEntry, false);
        expectedOutput = writeEntryContent(expectedOutput, testContent);

        testFileContent = testFileInputStream.readAllBytes();
        testFileInputStream.close();

        Assert.assertArrayEquals(expectedOutput, testFileContent);
    }

    @Test
    public void testWritingWithPadBytes() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testContent = "Test content";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setDataPadCount(10);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes(), 0, testContent.length());
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        // then
        byte[] testFileContent = new byte[testContent.length() + CpioArchiveEntry.CPIO_HEADER_LENGTH + 10];
        File testFile = new File("test.cpio");
        FileInputStream testFileInputStream = new FileInputStream(testFile);

        byte[] expectedOutput = new byte[testContent.length() + CpioArchiveEntry.CPIO_HEADER_LENGTH + 10];

        System.arraycopy(CpioUtil.long2byteArray(CpioConstants.MAGIC_NEW, 2, false), 0, expectedOutput, 0, 8);
        CpioArchiveEntry expectedEntry = new CpioArchiveEntry();
        expectedEntry.setName(testFileName);
        expectedEntry.setFileSize(testContent.length());
        expectedEntry.setMode(CpioConstants.C_ISREG); // regular file
        expectedEntry.setDataPadCount(10);

        writeEntryHeader(expectedOutput, expectedEntry, false);
        expectedOutput = writeEntryContent(expectedOutput, testContent);
        expectedOutput = writePadBytes(expectedOutput, 10);

        testFileContent = testFileInputStream.readAllBytes();
        testFileInputStream.close();

        Assert.assertArrayEquals(expectedOutput, testFileContent);
    }

    private void writeEntryHeader(byte[] output, CpioArchiveEntry entry, boolean swapHalfWord) throws IOException {
        writeBinaryLong(output, CpioConstants.MAGIC_NEW, false);
        writeBinaryLong(output, (short) entry.getFormat(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getInode(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getMode(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getUID(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getGID(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getNumberOfLinks(), swapHalfWord);
        writeBinaryLong(output, entry.getTime(), swapHalfWord);
        writeBinaryLong(output, entry.getFileSize(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getDeviceMaj(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getDeviceMin(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getRemoteDeviceMaj(), swapHalfWord);
        writeBinaryLong(output, (short) entry.getRemoteDeviceMin(), swapHalfWord);
        writeBinaryLong(output, (short) (entry.getName().length() + 1), swapHalfWord);
        writeBinaryLong(output, entry.getChksum(), swapHalfWord);
    }

    private byte[] writeEntryContent(byte[] output, String content) throws IOException {
        writeCString(output, content);
        pad(output, entry.getDataPadCount());

        return output;
    }

    private byte[] writePadBytes(byte[] output, int count) throws IOException {
        byte[] padBytes = new byte[count];

        for (int i = 0; i < count; i++) {
            padBytes[i] = (byte) 0;
        }

        System.arraycopy(padBytes, 0, output, output.length, count);

        return output;
    }

    @Test
    public void testWritingWithInvalidSwapHalfWord() throws IOException {
        // given
        OutputStream outputStream = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 42);

        // when & then
        Assert.assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry()));
        cpioArchiveOutputStream.close();
    }
}