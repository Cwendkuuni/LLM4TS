import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;

public class DumpArchiveInputStreamTest {

    @Test
    public void testGetCount() throws ArchiveException, IOException {
        InputStream is = new FileInputStream("testFile");
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        int count = dis.getCount();
        assertEquals(0, count);
        dis.close();
    }
    
    @Test
    public void testGetBytesRead() throws ArchiveException, IOException {
        InputStream is = new FileInputStream("testFile");
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        long bytesRead = dis.getBytesRead();
        assertEquals(0, bytesRead);
        dis.close();
    }
    
    @Test
    public void testGetSummary() throws ArchiveException, IOException {
        InputStream is = new FileInputStream("testFile");
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        DumpArchiveSummary summary = dis.getSummary();
        assertNotNull(summary);
        dis.close();
    }
    
    @Test
    public void testGetNextDumpEntry() throws ArchiveException, IOException {
        InputStream is = new FileInputStream("testFile");
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        DumpArchiveEntry entry = dis.getNextDumpEntry();
        assertNull(entry);
        dis.close();
    }
    
    @Test
    public void testGetNextEntry() throws ArchiveException, IOException {
        InputStream is = new FileInputStream("testFile");
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        DumpArchiveEntry entry = dis.getNextEntry();
        assertNull(entry);
        dis.close();
    }
    
    @Test
    public void testRead() throws ArchiveException, IOException {
        InputStream is = new FileInputStream("testFile");
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        byte[] buffer = new byte[10];
        int bytesRead = dis.read(buffer, 0, 10);
        assertEquals(-1, bytesRead);
        dis.close();
    }
    
    @Test
    public void testClose() throws ArchiveException, IOException {
        InputStream is = new FileInputStream("testFile");
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        dis.close();
        assertTrue(dis.isClosed);
    }
}