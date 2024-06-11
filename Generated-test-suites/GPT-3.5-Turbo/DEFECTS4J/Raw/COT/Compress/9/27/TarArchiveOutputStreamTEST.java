import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.finished);
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishUnclosedEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testCloseAlreadyClosed() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        assertEquals("test.txt", tarArchiveOutputStream.currName);
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameError() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("very_long_file_name.txt"));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncompleteEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] data = "test data".getBytes();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", data.length));
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] data = "test data".getBytes();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", data.length));
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(data.length, tarArchiveOutputStream.currBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteExceedSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        byte[] data = "test data".getBytes();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", 4));
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
    }
}