
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        // Verify that the longFileMode is set correctly
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutputStream.getRecordSize();
        // Verify that the record size is greater than 0
        assertTrue(recordSize > 0);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        // Verify that haveUnclosedEntry is set to true
        assertTrue(tarOutputStream.haveUnclosedEntry);
        // Verify that the current entry name is set correctly
        assertEquals("testFile.txt", tarOutputStream.currName);
        // Verify that the current entry size is initialized correctly
        assertEquals(0, tarOutputStream.currSize);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
        tarOutputStream.haveUnclosedEntry = false;
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncompleteEntry() throws IOException {
        tarOutputStream.haveUnclosedEntry = true;
        tarOutputStream.doingPutting = true;
        tarOutputStream.currBytes = tarOutputStream.currSize - 1;
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntryCompleteEntry() throws IOException {
        tarOutputStream.haveUnclosedEntry = true;
        tarOutputStream.doingPutting = true;
        tarOutputStream.currBytes = tarOutputStream.currSize;
        tarOutputStream.closeArchiveEntry();
        // Verify that haveUnclosedEntry is set to false
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Test data".getBytes();
        tarOutputStream.write(data);
        // Verify that the data is correctly written to the output stream
        assertEquals(data.length, outputStream.toByteArray().length);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        // Verify that the finish method writes the correct number of EOF records
        assertEquals(tarOutputStream.getRecordSize() * 2, outputStream.toByteArray().length);
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        // Verify that the output stream is closed
        assertTrue(tarOutputStream.closed);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        String entryName = "testFile.txt";
        TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(inputFile, entryName);
        // Verify that the entry name is set correctly
        assertEquals("testFile.txt", entry.getName());
    }

}

