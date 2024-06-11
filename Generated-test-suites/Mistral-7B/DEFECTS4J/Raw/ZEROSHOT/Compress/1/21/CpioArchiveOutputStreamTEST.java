import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        out.write(testData);
        out.closeArchiveEntry();
        out.finish();
        byte[] expectedOutput = getExpectedOutput("test1.bin");
        byte[] actualOutput = ((ByteArrayOutputStream) out.getOutputStream())
                .toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);

        Map<String, CpioArchiveEntry> entries = out.getEntries();
        Assert.assertEquals(1, entries.size());
        CpioArchiveEntry actualEntry = entries.values().iterator().next();
        Assert.assertEquals("testfile", actualEntry.getName());
        Assert.assertEquals(5, actualEntry.getSize());
        Assert.assertEquals(CpioConstants.FORMAT_OLD_ASCII, actualEntry.getFormat());

        InputStream inputStream = new ByteArrayInputStream(actualOutput);
        CpioArchiveEntry inputEntry = new CpioArchiveEntry();
        inputEntry.setName("testfile");
        ArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName("testfile");
        Assert.assertEquals(testEntry, out.getNextEntry());
        Assert.assertEquals(inputEntry, out.getNextEntry());
        Assert.assertNull(out.getNextEntry());

        out.close();
    }

    @Test(expected = IOException.class)
    public void testDuplicateEntry() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
        out.putNextEntry(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownFormat() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(-1);
        out.putNextEntry(entry);
    }

    @Test
    public void testWriteManyFiles() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        for (int i = 0; i < 10; i++) {
            CpioArchiveEntry entry = new CpioArchiveEntry();
            entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
            entry.setName("testfile" + i);
            entry.setSize(5 * (i + 1));
            out.putNextEntry(entry);
            byte[] testData = "12345".getBytes();
            out.write(testData);
            out.closeArchiveEntry();
        }
        out.finish();
        byte[] expectedOutput = getExpectedOutput("testManyFiles.bin");
        byte[] actualOutput = ((ByteArrayOutputStream) out.getOutputStream())
                .toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
        byte[] testData = "123456789".getBytes();
        out.write(testData);
    }

    @Test
    public void testWriteWithCustomFormat() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(), CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        out.write(testData);
        out.closeArchiveEntry();
        out.finish();
        byte[] expectedOutput = getExpectedOutput("test2.bin");
        byte[] actualOutput = ((ByteArrayOutputStream) out.getOutputStream())
                .toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidSwappedFormat() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(), CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII | CpioConstants.FORMAT_OLD_BINARY);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
    }

    @Test
    public void testWriteWithValidSwappedFormat() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(), CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY | CpioConstants.FORMAT_OLD_BINARY);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        out.write(testData);
        out.closeArchiveEntry();
        out.finish();
        byte[] expectedOutput = getExpectedOutput("test3.bin");
        byte[] actualOutput = ((ByteArrayOutputStream) out.getOutputStream())
                .toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteWithCRC() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_NEW | CpioConstants.FORMAT_NEW_CRC);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        out.write(testData);
        byte checksum = 0;
        for (byte b : testData) {
            checksum += b;
        }
        out.setCrc(checksum);
        out.closeArchiveEntry();
        out.finish();
        byte[] expectedOutput = getExpectedOutput("test4.bin");
        byte[] actualOutput = ((ByteArrayOutputStream) out.getOutputStream())
                .toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteWithInvalidSwappedFormatAndCRC() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII | CpioConstants.FORMAT_NEW_CRC);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
    }

    @Test
    public void testWriteWithValidSwappedFormatAndCRC() throws IOException {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY | CpioConstants.FORMAT_NEW_CRC);
        entry.setName("testfile");
        entry.setSize(5);
        out.putNextEntry(entry);
        byte[] testData = "12345".getBytes();
        out.write(testData);
        byte checksum = 0;
        for (byte b : testData) {
            checksum += b;
        }
        out.setCrc(checksum);
        out.closeArchiveEntry();
        out.finish();
        byte[] expectedOutput = getExpectedOutput("test5.bin");
        byte[] actualOutput = ((ByteArrayOutputStream) out.getOutputStream())
                .toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    private byte[] getExpectedOutput(String fileName) {
        InputStream expectedInput = CpioArchiveOutputStreamTest.class
                .getResourceAsStream("/expected/" + fileName);
        byte[] expectedOutput = new byte[expectedInput.available()];
        expectedInput.read(expectedOutput);
        return expectedOutput;
    }
}