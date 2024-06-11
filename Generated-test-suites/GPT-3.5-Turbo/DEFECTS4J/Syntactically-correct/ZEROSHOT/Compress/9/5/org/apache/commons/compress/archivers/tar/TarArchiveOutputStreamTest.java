import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setup() {
        OutputStream outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(tarOutputStream);
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertTrue(tarOutputStream.isFinished());
    }
    
    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());
    }
    
    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        tarOutputStream.close();
        tarOutputStream.close();
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        assertTrue(tarOutputStream.hasUnclosedEntry());
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "This is a test file");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getContents());
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.hasUnclosedEntry());
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutWriteData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryDataNotFullyWritten() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "This is a test file");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "This is a test file");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(entry.getContents());
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.hasUnclosedEntry());
    }
    
    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        tarOutputStream.finish();
        byte[] data = "This is a test file".getBytes();
        tarOutputStream.write(data);
    }
    
    @Test(expected = IOException.class)
    public void testWriteDataExceedsEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "This is a test file");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "This is some additional data".getBytes();
        tarOutputStream.write(data);
    }
    
    @Test
    public void testFlush() throws IOException {
        tarOutputStream.flush();
        // No way to test the actual effect of flush()
    }
    
    @Test(expected = IOException.class)
    public void testCreateArchiveEntryAfterFinish() throws IOException {
        tarOutputStream.finish();
        File file = new File("test.txt");
        String entryName = "test.txt";
        tarOutputStream.createArchiveEntry(file, entryName);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        String entryName = "test.txt";
        TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(file, entryName);
        assertEquals(entryName, entry.getName());
    }
    
}