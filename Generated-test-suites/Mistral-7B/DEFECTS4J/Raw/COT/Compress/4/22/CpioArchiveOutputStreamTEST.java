import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * JUnit test cases for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        OutputStream out = new FileOutputStream(new File("test.cpio"));
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getEntryFormat());
        cpioStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioStream.putArchiveEntry(entry);

        byte[] expectedHeader = {0x1, 0x1, 0x1, 0x1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] actualHeader = out.toByteArray();
        Assert.assertArrayEquals(expectedHeader, actualHeader);

        cpioStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, 123);
    }

    @Test
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        cpioStream.putArchiveEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        cpioStream.putArchiveEntry(entry2);

        cpioStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioStream.putArchiveEntry(entry);

        byte[] testData = new byte[]{'1', '2', '3', '4', '5'};
        cpioStream.write(testData, 0, testData.length);

        byte[] expectedData = new byte[]{'1', '2', '3', '4', '5'};
        byte[] actualData = out.toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);

        cpioStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioStream.putArchiveEntry(entry);

        byte[] testData = new byte[]{'1', '2', '3', '4', '5'};
        cpioStream.write(testData, 0, testData.length);

        cpioStream.finish();

        byte[] expectedTrailer = {0x2, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] actualTrailer = out.toByteArray();
        Assert.assertArrayEquals(expectedTrailer, actualTrailer);

        cpioStream.close();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioStream.putArchiveEntry(entry);

        byte[] testData = new byte[]{'1', '2', '3', '4', '5'};
        cpioStream.write(testData, 0, testData.length);

        cpioStream.finish();
        cpioStream.close();

        byte[] expectedOutput = new byte[14 + testData.length];
        System.arraycopy(getCpioHeader(CpioConstants.FORMAT_NEW), 0, expectedOutput, 0, 14);
        System.arraycopy(testData, 0, expectedOutput, 14, testData.length);

        byte[] actualOutput = out.toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteWithSwappedHalfWords() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_BINARY);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioStream.putArchiveEntry(entry);

        byte[] testData = new byte[]{'1', '2', '3', '4', '5'};
        cpioStream.write(testData, 0, testData.length);

        cpioStream.close();

        byte[] expectedOutput = new byte[14 + testData.length];
        System.arraycopy(getCpioHeader(CpioConstants.FORMAT_OLD_BINARY, true), 0, expectedOutput, 0, 14);
        System.arraycopy(testData, 0, expectedOutput, 14, testData.length);

        byte[] actualOutput = out.toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testfile.txt");
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(new FileOutputStream(file), CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = cpioStream.createArchiveEntry(file, "testfile.txt");
        cpioStream.putArchiveEntry(entry);
        cpioStream.finish();
        cpioStream.close();

        Assert.assertEquals("testfile.txt", entry.getName());
        Assert.assertEquals(file.length(), entry.getSize());
    }

    private byte[] getCpioHeader(short format) {
        return getCpioHeader(format, false);
    }

    private byte[] getCpioHeader(short format, boolean swapHalfWords) {
        byte[] header;
        if (format == CpioConstants.FORMAT_NEW) {
            header = new byte[]{0x1, 0x1, 0x1, 0x1, (byte) 0x0, 0x0, 0x0, 0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0};
        } else {
            header = new byte[]{0x1, 0x1, 0x1, 0x1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            if (swapHalfWords) {
                byte[] tmp = new byte[]{0, 0};
                for (int i = 0; i < header.length; i += 2) {
                    byte[] bytes = new byte[]{header[i], header[i + 1]};
                    CpioUtil.swapHalfWord(bytes);
                    header[i] = bytes[0];
                    header[i + 1] = bytes[1];
                }
            }
        }
        return header;
    }
}