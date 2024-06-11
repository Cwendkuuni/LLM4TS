import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
    }
    
    @Test
    public void testPutArchiveEntry() throws Exception {
        // Test case 1: Typical use case
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        
        assertEquals("testFile.txt", tarOutputStream.getCurrentEntry().getName());
        
        // Test case 2: Edge case - Long file name (>= TarConstants.NAMELEN) with longFileMode set to LONGFILE_TRUNCATE
        TarArchiveEntry longEntry = new TarArchiveEntry("ThisIsALongFileNameThatExceedsTarConstantsNAMELEN.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(longEntry);
        tarOutputStream.closeArchiveEntry();
        
        assertEquals("ThisIsALongFileNameThatExceedsTarConstantsN", tarOutputStream.getCurrentEntry().getName());
        
        // Test case 3: Error scenario - Long file name (>= TarConstants.NAMELEN) with longFileMode set to LONGFILE_ERROR
        TarArchiveEntry errorEntry = new TarArchiveEntry("ThisIsAnotherLongFileNameThatExceedsTarConstantsNAMELEN.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        
        try {
            tarOutputStream.putArchiveEntry(errorEntry);
        } catch (RuntimeException e) {
            assertEquals("file name 'ThisIsAnotherLongFileNameThatExceedsTarConstantsNAMELEN.txt' is too long ( > 100 bytes)", e.getMessage());
        }
    }
    
    @Test
    public void testCloseArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        // Test case 1: Typical use case - entry closed without any remaining data
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry());
        
        // Test case 2: Error scenario - entry closed before all data is written
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[100]);
        
        try {
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            assertEquals("entry 'testFile.txt' closed at '0' before the '100' bytes specified in the header were written", e.getMessage());
        }
    }
    
    @Test
    public void testFinish() throws Exception {
        // Test case 1: Typical use case
        tarOutputStream.finish();
        // Check that two EOF records are written
        assertEquals(2 * tarOutputStream.getRecordSize(), ((ByteArrayOutputStream)tarOutputStream.getOut()).size());
    }
    
    @Test
    public void testClose() throws Exception {
        // Test case 1: Typical use case
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());
    }
}