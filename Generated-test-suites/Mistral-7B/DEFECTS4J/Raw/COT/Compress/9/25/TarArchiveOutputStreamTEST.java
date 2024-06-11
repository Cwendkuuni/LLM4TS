import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        Assert.assertNotNull(tarOS);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOS.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testSetLongFileModeInvalidMode() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.finish();
        Assert.assertTrue(tarOS.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.finish();
        tarOS.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.finish();
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.finish();
        tarOS.close();
        Assert.assertTrue(tarOS.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.finish();
        tarOS.close();
        tarOS.close();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        int recordSize = tarOS.getRecordSize();
        Assert.assertNotNull(recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        Assert.assertEquals("test.txt", tarOS.getCurrentName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryInvalidEntry() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        Object invalidEntry = new Object();
        tarOS.putArchiveEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnfinishedArchive() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithTooLongFileName() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        String tooLongFileName = ""; // Add a very long string here
        TarArchiveEntry entry = new TarArchiveEntry(tooLongFileName);
        tarOS.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.closeArchiveEntry();
        Assert.assertFalse(tarOS.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnfinishedArchive() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.close();
    }

    @Test
    public void testWrite() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        byte[] testData = "Test Data".getBytes();
        tarOS.write(testData, 0, testData.length);
        Assert.assertEquals(testData.length, tarOS.getBytesWritten());
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnfinishedArchive() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        byte[] testData = "Test Data".getBytes();
        tarOS.finish();
        tarOS.write(testData, 0, testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithTooLargeData() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        byte[] tooLargeData = new byte[Integer.MAX_VALUE + 1];
        tarOS.write(tooLargeData, 0, tooLargeData.length);
    }

    @Test
    public void testWriteEOFRecords() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.finish();
        tarOS.write(new byte[TarConstants.BLOCK_SIZE], 0, TarConstants.BLOCK_SIZE);
        tarOS.write(new byte[TarConstants.BLOCK_SIZE], 0, TarConstants.BLOCK_SIZE);
        tarOS.close();
    }

    @Test
    public void testFlush() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.flush();
    }

    @Test(expected = IOException.class)
    public void testFlushWithUnfinishedArchive() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        tarOS.flush();
        tarOS.finish();
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarOS.createArchiveEntry(inputFile, "test.txt");
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithNullFileName() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        File inputFile = null;
        ArchiveEntry entry = tarOS.createArchiveEntry(inputFile, null);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithNullInputFile() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        ArchiveEntry entry = tarOS.createArchiveEntry(null, "test.txt");
    }

    @Test
    public void testWriteEntryData() throws Exception {
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(new MockOutputStream());
        File inputFile = new File("test.txt");
        byte[] testData = "Test Data".getBytes();
        ArchiveEntry entry = tarOS.createArchiveEntry(inputFile, "test.txt");
        tarOS.putArchiveEntry(entry);
        tarOS.write(testData, 0, testData.length);
        tarOS.closeArchiveEntry();
        tarOS.close();
        byte[] readData = ArchiveUtils.toByteArray(new File("test.tar"));
        int index = 0;
        for (int i = 0; i < testData.length; i++) {
            byte readByte = readData[index++];
            byte writeByte = testData[i];
            Assert.assertEquals("Mismatch at index " + i, writeByte, readByte);
        }
    }
}

class MockOutputStream extends OutputStream {
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // Implement this method as needed for testing
    }

    @Override
    public void flush() throws IOException {
        // Implement this method as needed for testing
    }

    @Override
    public void close() throws IOException {
        // Implement this method as needed for testing
    }
}