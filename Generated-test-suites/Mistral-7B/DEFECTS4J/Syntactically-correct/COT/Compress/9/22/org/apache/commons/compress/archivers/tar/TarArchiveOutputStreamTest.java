import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

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
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
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

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertNotEquals(-1, recordSize);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Object invalidArchiveEntry = new Object();
        tarArchiveOutputStream.putArchiveEntry(invalidArchiveEntry);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryInvalidArchiveEntryType() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Object invalidArchiveEntry = new Object();
        tarArchiveOutputStream.putArchiveEntry(invalidArchiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFile.txt";
        File file = new File(fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        Assert.assertEquals(archiveEntry.getName(), tarArchiveOutputStream.currName);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(),
                TarConstants.NAMELEN + 1, TarConstants.NAMELEN + 1);
        String fileName = "";
        for (int i = 0; i < TarConstants.NAMELEN + 1; i++) {
            fileName += "a";
        }
        File file = new File(fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFile.txt";
        File file = new File(fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFile.txt";
        File file = new File(fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        byte[] actualData = tarArchiveOutputStream.buffer.getBuffer();
        byte[] expectedData = new byte[tarArchiveOutputStream.buffer.getRecordSize()];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testWriteWithAssemledRecord() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length / 2);
        tarArchiveOutputStream.write(testData, testData.length / 2, testData.length / 2);
        byte[] actualData = tarArchiveOutputStream.buffer.getBuffer();
        byte[] expectedData = new byte[tarArchiveOutputStream.buffer.getRecordSize() * 2];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFile.txt";
        File file = new File(fileName);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length + archiveEntry.getSize());
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData, 0, testData.length);
        tarArchiveOutputStream.flush();
        byte[] actualData = tarArchiveOutputStream.buffer.getBuffer();
        byte[] expectedData = new byte[tarArchiveOutputStream.buffer.getRecordSize()];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("testFile.txt");
        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, "testFile.txt");
        Assert.assertEquals("testFile.txt", archiveEntry.getName());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryInvalidFile() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Object invalidFile = new Object();
        TarArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(invalidFile, "testFile.txt");
    }
}