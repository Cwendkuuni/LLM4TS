import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testConstructor1() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
    }
    
    @Test
    public void testConstructor2() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null, 512);
    }
    
    @Test
    public void testConstructor3() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null, 512, 1024);
    }
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }
    
    @Test
    public void testFinish() throws Exception {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        tar.finish();
    }
    
    @Test
    public void testClose() throws Exception {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        tar.close();
    }
    
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        int recordSize = tar.getRecordSize();
        assertEquals(512, recordSize);
    }
    
    @Test
    public void testCreateArchiveEntry() throws Exception {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        File inputFile = new File("testFile");
        String entryName = "testEntry";
        ArchiveEntry entry = tar.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
        assertEquals(inputFile, entry.getFile());
        assertEquals(entryName, entry.getName());
    }
}