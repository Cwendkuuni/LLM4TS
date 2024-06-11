import static org.junit.Assert.*;
import java.io.*;

import org.apache.commons.compress.archivers.tar.*;
import org.junit.*;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private TarArchiveOutputStream tarOut;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.close();
        out.close();
    }

    @Test
    public void testConstructor() {
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOut.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarOut.finish();
        assertTrue(tarOut.finished);
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        tarOut.finished = true;
        tarOut.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishUnclosedEntry() throws IOException {
        tarOut.haveUnclosedEntry = true;
        tarOut.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarOut.close();
        assertTrue(tarOut.closed);
    }

    @Test
    public void testCloseAlreadyClosed() throws IOException {
        tarOut.closed = true;
        tarOut.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        assertTrue(tarOut.haveUnclosedEntry);
        assertEquals("test.txt", tarOut.currName);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        tarOut.finished = true;
        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameError() throws IOException {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        String longName = generateLongName(TarConstants.NAMELEN + 1);
        tarOut.putArchiveEntry(new TarArchiveEntry(longName));
    }

    @Test
    public void testPutArchiveEntryLongFileNameTruncate() throws IOException {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longName = generateLongName(TarConstants.NAMELEN + 1);
        tarOut.putArchiveEntry(new TarArchiveEntry(longName));
        assertEquals(TarConstants.NAMELEN, tarOut.currName.length());
    }

    @Test
    public void testPutArchiveEntryLongFileNameGNU() throws IOException {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longName = generateLongName(TarConstants.NAMELEN + 1);
        tarOut.putArchiveEntry(new TarArchiveEntry(longName));
        assertEquals(TarConstants.NAMELEN + 1, tarOut.currBytes);
        assertTrue(tarOut.haveUnclosedEntry);
        assertEquals(TarConstants.GNU_LONGLINK, tarOut.currName);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.closeArchiveEntry();
        assertFalse(tarOut.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        tarOut.finished = true;
        tarOut.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
        tarOut.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncompleteEntry() throws IOException {
        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.closeArchiveEntry();
        tarOut.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes();
        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.write(data);
        assertEquals(data.length, tarOut.currBytes);
    }

    @Test(expected = IOException.class)
    public void testWriteExceedSize() throws IOException {
        byte[] data = "test data".getBytes();
        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt", data.length - 1));
        tarOut.write(data);
    }

    @Test
    public void testFlush() throws IOException {
        tarOut.flush();
        // No assertion, just check that no exception is thrown
    }

    private String generateLongName(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }
}