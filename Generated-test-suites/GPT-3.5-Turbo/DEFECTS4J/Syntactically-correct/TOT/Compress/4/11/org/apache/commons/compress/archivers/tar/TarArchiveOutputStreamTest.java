import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream taos;
    private File testFile;
    
    @Before
    public void setup() throws Exception {
        testFile = new File("test.tar");
        FileOutputStream fos = new FileOutputStream(testFile);
        taos = new TarArchiveOutputStream(fos);
    }
    
    @Test
    public void testPutArchiveEntry_withNormalEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        
        taos.putArchiveEntry(entry);
        
        assertEquals("test.txt", taos.getCurrName());
        assertEquals(0, taos.getCurrBytes());
        assertEquals(10, taos.getCurrSize());
    }
    
    @Test
    public void testPutArchiveEntry_withDirectoryEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("dir/");
        entry.setDirectory(true);
        
        taos.putArchiveEntry(entry);
        
        assertTrue(entry.isDirectory());
        assertEquals("dir/", taos.getCurrName());
        assertEquals(0, taos.getCurrBytes());
        assertEquals(0, taos.getCurrSize());
    }
    
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_withLongFileName_TruncateMode() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("thisIsAVeryLongFileNameThatWillExceedTheMaxNameLengthAllowedByTarArchiveEntry.txt");
        
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        taos.putArchiveEntry(entry);
    }
    
    @Test
    public void testPutArchiveEntry_withLongFileName_GNUMode() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("thisIsAVeryLongFileNameThatWillExceedTheMaxNameLengthAllowedByTarArchiveEntry.txt");
        
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        taos.putArchiveEntry(entry);
        
        TarArchiveEntry longLinkEntry = (TarArchiveEntry) taos.getRecord(0);
        byte[] expectedNameBytes = entry.getName().getBytes();
        byte[] nameBytes = Arrays.copyOfRange(longLinkEntry.getLinkNameBytes(), 0, expectedNameBytes.length);
        
        assertArrayEquals(expectedNameBytes, nameBytes);
        assertEquals(expectedNameBytes.length, longLinkEntry.getSize());
        assertEquals(TarConstants.GNU_LONGLINK, longLinkEntry.getName());
        assertEquals(TarConstants.LF_GNUTYPE_LONGNAME, longLinkEntry.getLinkFlag());
    }
    
    @Test(expected = IOException.class)
    public void testFinish_withUnclosedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        taos.putArchiveEntry(entry);
        taos.finish();
    }
    
    @Test
    public void testFinish_withNoUnclosedEntry() throws Exception {
        taos.finish();
        
        assertEquals(0, taos.getCurrBytes());
        assertEquals(0, taos.getCurrSize());
    }
    
    @Test(expected = IOException.class)
    public void testClose_withUnclosedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        
        taos.putArchiveEntry(entry);
        taos.close();
    }
    
    @Test
    public void testClose_withNoUnclosedEntry() throws Exception {
        taos.close();
        
        assertEquals(0, taos.getCurrBytes());
        assertEquals(0, taos.getCurrSize());
        assertTrue(testFile.exists());
        testFile.delete();
    }
    
    @Test
    public void testWrite_withSmallBuffer() throws Exception {
        byte[] buffer = new byte[5];
        
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        taos.putArchiveEntry(entry);
        
        taos.write(buffer);
        
        assertEquals(5, taos.getCurrBytes());
    }
    
    @Test
    public void testWrite_withLargeBuffer() throws Exception {
        byte[] buffer = new byte[15];
        
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        taos.putArchiveEntry(entry);
        
        taos.write(buffer);
        
        assertEquals(10, taos.getCurrBytes());
    }
    
    @Test(expected = IOException.class)
    public void testWrite_withNumToWriteExceedingSize() throws Exception {
        byte[] buffer = new byte[15];
        
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        taos.putArchiveEntry(entry);
        
        taos.write(buffer, 0, 20);
    }
    
    @Test
    public void testCloseArchiveEntry_withAssemLenNonZero() throws Exception {
        byte[] buffer = new byte[5];
        
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(20);
        taos.putArchiveEntry(entry);
        taos.write(buffer);
        taos.closeArchiveEntry();
        
        assertEquals(20, taos.getCurrBytes());
    }
    
}