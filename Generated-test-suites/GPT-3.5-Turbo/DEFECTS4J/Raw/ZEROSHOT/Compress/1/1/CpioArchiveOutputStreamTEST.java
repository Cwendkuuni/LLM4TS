import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
    
    private CpioArchiveOutputStream cpioOutputStream;
    
    @Before
    public void setUp() {
        cpioOutputStream = new CpioArchiveOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        });
    }
    
    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);
        
        cpioOutputStream.putNextEntry(cpioEntry);
        
        assertEquals(cpioEntry, cpioOutputStream.getCpioEntry());
        assertEquals(0, cpioOutputStream.getWritten());
    }
    
    @Test(expected = IOException.class)
    public void testPutNextEntryWhenAlreadyHasCurrentEntry() throws IOException {
        CpioArchiveEntry cpioEntry1 = new CpioArchiveEntry();
        cpioEntry1.setName("testfile1");
        cpioEntry1.setFileSize(5);
        
        CpioArchiveEntry cpioEntry2 = new CpioArchiveEntry();
        cpioEntry2.setName("testfile2");
        cpioEntry2.setFileSize(5);
        
        cpioOutputStream.putNextEntry(cpioEntry1);
        cpioOutputStream.putNextEntry(cpioEntry2); // should throw IOException
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);
        
        cpioOutputStream.putNextEntry(cpioEntry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
        
        assertNull(cpioOutputStream.getCpioEntry());
        assertEquals(0, cpioOutputStream.getWritten());
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWhenNoCurrentEntry() throws IOException {
        cpioOutputStream.closeArchiveEntry(); // should throw IOException
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithInvalidSize() throws IOException {
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(5);
        
        cpioOutputStream.putNextEntry(cpioEntry);
        
        cpioOutputStream.closeArchiveEntry(); // should throw IOException
    }
    
    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        
        assertNull(cpioOutputStream.getCpioEntry());
        assertEquals(0, cpioOutputStream.getWritten());
        
        // Check trailer entry
        CpioArchiveEntry trailerEntry = new CpioArchiveEntry();
        trailerEntry.setMode(0);
        trailerEntry.setName("TRAILER!!!");
        trailerEntry.setNumberOfLinks(1);
        
        assertEquals(trailerEntry, cpioOutputStream.getTrailerEntry());
    }
    
    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();
        assertTrue(cpioOutputStream.isClosed());
    }
    
}