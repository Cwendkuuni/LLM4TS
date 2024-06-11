import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        Assert.assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishAlreadyFinished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseAlreadyClosed() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        Assert.assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertNotNull(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryNullEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        Object invalidEntry = new Object();
        tarArchiveOutputStream.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryTooLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        TarArchiveEntry entry = new TarArchiveEntry("test".repeat(TarConstants.NAMELEN + 1));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] actualData = ((ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream()).toByteArray();
        Assert.assertArrayEquals(data, actualData);
    }

    @Test(expected = IOException.class)
    public void testWriteInvalidOffset() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, -1, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteInvalidLength() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, -1);
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.flush();
        byte[] actualData = ((ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream()).toByteArray();
        Assert.assertArrayEquals(data, actualData);
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.write(new byte[10], 0, 10);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        File inputFile = new File("test.txt");
        File expectedEntryName = "test.txt";
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, expectedEntryName.getAbsolutePath());
        Assert.assertEquals(expectedEntryName.getAbsolutePath(), entry.getName());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryNonExistentFile() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        File inputFile = new File("nonExistentFile.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "test.txt");
    }

    @Test
    public void testWriteToOutput() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] actualData = ((ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream()).toByteArray();
        Assert.assertArrayEquals(data, actualData);
    }

    @Test
    public void testWriteToOutputWithLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        String longFileName = "test".repeat(TarConstants.NAMELEN + 1);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] actualData = ((ByteArrayInputStream) tarArchiveOutputStream.getInputStream()).readAllBytes();
        Assert.assertArrayEquals(data, actualData);
    }
}