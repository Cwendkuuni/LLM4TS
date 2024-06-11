import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        tarOutputStream = new TarArchiveOutputStream(null);
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(0);
        assertEquals(0, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(1);
        assertEquals(1, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(2);
        assertEquals(2, tarOutputStream.longFileMode);
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry();
        tarOutputStream.putArchiveEntry(entry);
        assertTrue(tarOutputStream.haveUnclosedEntry);
        
        TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
        assertNotNull(tarEntry.getRecordBuf());
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);
        
        // Test exception if current bytes is less than current size
        ArchiveEntry entry = new TarArchiveEntry();
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.currBytes = 5;
        try {
            tarOutputStream.closeArchiveEntry();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("entry '' closed at '5' before the '10' bytes specified in the header were written", e.getMessage());
        }
    }
    
    @Test(expected = IOException.class)
    public void testWrite() throws IOException {
        byte[] wBuf = new byte[256];
        tarOutputStream.write(wBuf, 0, 256);
    }
    
    @Test
    public void testWriteEOFRecord() throws IOException {
        tarOutputStream.writeEOFRecord();
        byte[] recordBuf = tarOutputStream.recordBuf;
        for (byte b : recordBuf) {
            assertEquals(0, b);
        }
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutputStream.haveUnclosedEntry = true;
        try {
            tarOutputStream.finish();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(tarOutputStream.closed);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "test.txt");
        assertEquals(inputFile, ((TarArchiveEntry) entry).getInputFile());
        assertEquals("test.txt", ((TarArchiveEntry) entry).getEntryName());
    }

}