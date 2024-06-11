import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.*;
import java.util.zip.*;

public class TarArchiveOutputStreamTest {
    
    @Mock
    OutputStream os;
    TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tarOutputStream = new TarArchiveOutputStream(os);
    }
    
    @Test
    public void testPutArchiveEntry_ValidEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        assertTrue(tarOutputStream.haveUnclosedEntry);
        assertEquals("testFile.txt", tarOutputStream.currName);
    }
    
    @Test
    public void testPutArchiveEntry_LongFileName_GNU() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("verylongfilename1234567890morethannamelength");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntry_LongFileName_Error() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("verylongfilename1234567890morethannamelength");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testPutArchiveEntry_LongFileName_Truncate() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("verylongfilename1234567890morethannamelength");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(entry);
        
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void testCloseArchiveEntry_CompleteEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        tarOutputStream.write("This is a test".getBytes());
        tarOutputStream.closeArchiveEntry();
        
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_IncompleteEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite_ValidData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testWrite_InvalidData() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        
        byte[] data = "This is a test".getBytes();
        tarOutputStream.write(data, 0, data.length+1);
    }
    
    @Test
    public void testFinish_UnclosedEntry() {
        tarOutputStream.haveUnclosedEntry = true;
        try {
            tarOutputStream.finish();
            fail("IOException not thrown");
        } catch(IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }
    
    @Test
    public void testFinish_ValidArchive() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
        verify(os, times(2)).write(any(byte[].class));
    }
    
    @Test
    public void testClose_NotClosed() throws IOException {
        tarOutputStream.close();
        verify(os).close();
    }
    
    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutputStream.getRecordSize();
        assertEquals(TarConstants.DEFAULT_RCDSIZE, recordSize);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "testFile.txt");
        
        assertNotNull(entry);
        assertEquals(TarArchiveEntry.class, entry.getClass());
        assertEquals("testFile.txt", entry.getName());
    }
    
    @Test
    public void testFlush() throws IOException {
        tarOutputStream.flush();
        verify(os).flush();
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        verify(os).close();
        verify(buffer).close();
    }
}