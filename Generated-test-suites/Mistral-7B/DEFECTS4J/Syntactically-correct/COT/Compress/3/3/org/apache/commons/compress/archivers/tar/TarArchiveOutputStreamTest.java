import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.finish();
        byte[] expectedRecord = new byte[512];
        Arrays.fill(expectedRecord, (byte) 0);
        Assert.assertArrayEquals(expectedRecord, tarOutputStream.recordBuf);
        Assert.assertArrayEquals(expectedRecord, tarOutputStream.buffer.getBuffer());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.close();
        Assert.assertTrue(tarOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String entryName = "testEntry";
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"), entryName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] record = entry.getEntryHeader();
        tarOutputStream.write(record, 0, record.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedRecord = new byte[512];
        System.arraycopy(entry.getEntryHeader(), 0, expectedRecord, 0, record.length);
        Arrays.fill(expectedRecord, record.length, expectedRecord.length, (byte) 0);
        Assert.assertArrayEquals(expectedRecord, tarOutputStream.buffer.getBuffer());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"), "testEntry");
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String entryName = "testEntry";
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"), entryName);
        byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        byte[] expectedRecord = new byte[512];
        System.arraycopy(entry.getEntryHeader(), 0, expectedRecord, 0, entry.getEntryHeader().length);
        Arrays.fill(expectedRecord, entry.getEntryHeader().length, expectedRecord.length, (byte) 0);
        Assert.assertArrayEquals(expectedRecord, tarOutputStream.buffer.getBuffer());
        Assert.assertArrayEquals(expectedData, Arrays.copyOfRange(tarOutputStream.recordBuf, 512, 512 + testData.length));
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"), "testEntry");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testData, 0, testData.length);
    }

    @Test
    public void testWriteWithLargeFileName() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String longFileName = "testEntryWithLongName";
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"), longFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        byte[] record = entry.getEntryHeader();
        tarOutputStream.write(record, 0, record.length);
        byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedRecord = new byte[512 + longFileName.length() + 1];
        System.arraycopy(entry.getEntryHeader(), 0, expectedRecord, 0, record.length);
        int nameIndex = 512;
        for (int i = 0; i < longFileName.length(); i++) {
            expectedRecord[nameIndex++] = (byte) longFileName.charAt(i);
        }
        expectedRecord[nameIndex] = (byte) 0;
        Arrays.fill(expectedRecord, record.length + longFileName.length(), expectedRecord.length, (byte) 0);
        Assert.assertArrayEquals(expectedRecord, tarOutputStream.buffer.getBuffer());
        Assert.assertArrayEquals(testData, Arrays.copyOfRange(tarOutputStream.recordBuf, 512 + longFileName.length(), 512 + longFileName.length() + testData.length));
    }

    @Test(expected = IOException.class)
    public void testWriteWithLongFileNameWithoutLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String longFileName = "testEntryWithLongName";
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"), longFileName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] testData = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testData, 0, testData.length);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.close();
    }
}