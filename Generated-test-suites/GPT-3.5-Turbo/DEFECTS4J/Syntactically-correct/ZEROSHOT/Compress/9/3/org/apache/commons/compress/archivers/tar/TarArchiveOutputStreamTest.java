import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        tarArchiveOutputStream = new TarArchiveOutputStream(null);
    }

    @After
    public void tearDown() {
        tarArchiveOutputStream = null;
    }

    @Test
    public void testConstructor1() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructor2() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null, 512);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructor3() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null, 512, 512);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(0);
        assertEquals(0, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new ArchiveEntry());
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new ArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        assertTrue(tarArchiveOutputStream.hasUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFinishedStream() throws IOException {
        tarArchiveOutputStream.finish();
        ArchiveEntry archiveEntry = new ArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutFinish() throws IOException {
        ArchiveEntry archiveEntry = new ArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] buffer = new byte[512];
        tarArchiveOutputStream.write(buffer, 0, 512);
        assertEquals(512, tarArchiveOutputStream.getBytesWritten());
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingSize() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new ArchiveEntry());
        byte[] buffer = new byte[1024];
        tarArchiveOutputStream.write(buffer, 0, 1024);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File(".");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        assertNotNull(archiveEntry);
    }

}