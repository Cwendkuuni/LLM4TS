import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "testContent";

        File testFile = new File(testFileName);
        CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);
        entry.setSize(testFileContent.length());

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = createExpectedOutput(testFileName, testFileContent);
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, 123);

        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            cpioArchiveOutputStream.putArchiveEntry(entry);
        });

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        CpioArchiveEntry entry = new CpioArchiveEntry(testFileName);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(new byte[0]);
        cpioArchiveOutputStream.closeArchiveEntry();

        Assert.assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(entry);
        });

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        Object invalidEntry = new Object();
        Assert.assertThrows(ClassCastException.class, () -> {
            cpioArchiveOutputStream.putArchiveEntry(invalidEntry);
        });

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithInvalidHeaderFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_BINARY);

        String testFileName = "testFile";
        String testFileContent = "testContent";

        File testFile = new File(testFileName);
        CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedOutput = createExpectedOutputWithInvalidFormat(testFileName, testFileContent);
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName1 = "testFile1";
        String testFileContent1 = "testContent1";

        File testFile1 = new File(testFileName1);
        CpioArchiveEntry entry1 = new CpioArchiveEntry(testFile1, testFileName1);
        entry1.setSize(testFileContent1.length());

        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(testFileContent1.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        String testFileName2 = "testFile2";
        String testFileContent2 = "testContent2";

        File testFile2 = new File(testFileName2);
        CpioArchiveEntry entry2 = new CpioArchiveEntry(testFile2, testFileName2);
        entry2.setSize(testFileContent2.length());

        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(testFileContent2.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        byte[] expectedOutput = createExpectedOutputWithMultipleEntries(testFileName1, testFileContent1, testFileName2, testFileContent2);
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testFileContent = "testContent";

        File testFile = new File(testFileName);
        CpioArchiveEntry entry = new CpioArchiveEntry(testFile, testFileName);
        entry.setSize(testFileContent.length());

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.close();

        byte[] expectedOutput = createExpectedOutput(testFileName, testFileContent);
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testCloseAlreadyClosed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.close();

        Assert.assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.close();
        });

        cpioArchiveOutputStream.close();
    }

    private byte[] createExpectedOutput(String testFileName, String testFileContent) {
        byte[] headerBytes = ArchiveUtils.toByteArray(ArchiveUtils.ASCII, new String[]{
                MAGIC_NEW,
                Long.toString(1).getBytes(),
                Integer.toOctalString(CpioConstants.C_ISREG).getBytes(),
                Long.toBinaryString(0666).substring(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(0).getBytes(),
                Long.toString(0).getBytes(),
                Long.toString(0).getBytes(),
                Long.toString(0).getBytes(),
                Long.toString(testFileName.length() + 1).getBytes(),
                Long.toString(testFileContent.length()).getBytes(),
                Long.toString(0).getBytes(),
                testFileName.getBytes()
        });

        byte[] contentBytes = testFileContent.getBytes();
        byte[] trailerBytes = ArchiveUtils.toByteArray(ArchiveUtils.ASCII, new String[]{
                CPIO_TRAILER,
                Long.toString(1).getBytes(),
                Long.toString(1L).getBytes(),
                Long.toString(testFileName.length() + 1).getBytes()
        });

        Map<String, Byte[]> map = new HashMap<>();
        map.put(testFileName, contentBytes);

        return mergeBytes(headerBytes, map.values().toArray(new Byte[][]{}), trailerBytes);
    }

    private byte[] createExpectedOutputWithInvalidFormat(String testFileName, String testFileContent) {
        byte[] headerBytes = ArchiveUtils.toByteArray(ArchiveUtils.ASCII, new String[]{
                MAGIC_OLD_BINARY,
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(testFileName.length() + 1).getBytes(),
                Long.toString(testFileContent.length()).getBytes(),
                testFileName.getBytes(),
                new byte[0], // pad
                Long.toString(0).getBytes()
        });

        byte[] contentBytes = testFileContent.getBytes();

        return mergeBytes(headerBytes, new Byte[][]{contentBytes}, new byte[0]);
    }

    private byte[] createExpectedOutputWithMultipleEntries(String testFileName1, String testFileContent1, String testFileName2, String testFileContent2) {
        byte[] header1Bytes = ArchiveUtils.toByteArray(ArchiveUtils.ASCII, new String[]{
                MAGIC_NEW,
                Long.toString(1).getBytes(),
                Integer.toOctalString(CpioConstants.C_ISREG).getBytes(),
                Long.toBinaryString(0666).substring(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(testFileName1.length() + 1).getBytes(),
                Long.toString(testFileContent1.length()).getBytes(),
                Long.toString(0).getBytes(),
                testFileName1.getBytes()
        });

        byte[] content1Bytes = testFileContent1.getBytes();

        byte[] header2Bytes = ArchiveUtils.toByteArray(ArchiveUtils.ASCII, new String[]{
                MAGIC_NEW,
                Long.toString(1).getBytes(),
                Integer.toOctalString(CpioConstants.C_ISREG).getBytes(),
                Long.toBinaryString(0666).substring(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(1).getBytes(),
                Long.toString(testFileName2.length() + 1).getBytes(),
                Long.toString(testFileContent2.length()).getBytes(),
                Long.toString(0).getBytes(),
                testFileName2.getBytes()
        });

        byte[] content2Bytes = testFileContent2.getBytes();

        Map<String, Byte[]> map = new HashMap<>();
        map.put(testFileName1, content1Bytes);
        map.put(testFileName2, content2Bytes);

        byte[] trailerBytes = ArchiveUtils.toByteArray(ArchiveUtils.ASCII, new String[]{
                CPIO_TRAILER,
                Long.toString(2).getBytes(),
                Long.toString(1L).getBytes(),
                Long.toString(testFileName1.length() + 1 + testFileName2.length() + 1).getBytes()
        });

        return mergeBytes(header1Bytes, map.values().toArray(new Byte[][]{}), header2Bytes, map.values().toArray(new Byte[][]{}), trailerBytes);
    }

    private byte[] mergeBytes(byte[]... byteArrays) {
        int totalLength = 0;
        for (byte[] array : byteArrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];

        int index = 0;
        for (byte[] array : byteArrays) {
            System.arraycopy(array, 0, result, index, array.length);
            index += array.length;
        }

        return result;
    }
}