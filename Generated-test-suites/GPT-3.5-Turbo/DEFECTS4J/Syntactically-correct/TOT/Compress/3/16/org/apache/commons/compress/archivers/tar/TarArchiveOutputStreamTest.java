import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        // Test case for constructor 1
        TarArchiveOutputStream t1 = new TarArchiveOutputStream(System.out);
        
        assertNotNull(t1);
        
        // Test case for constructor 2
        TarArchiveOutputStream t2 = new TarArchiveOutputStream(System.out, 512);
        
        assertNotNull(t2);
        
        // Test case for constructor 3
        TarArchiveOutputStream t3 = new TarArchiveOutputStream(System.out, 512, 1024);
        
        assertNotNull(t3);
    }

    @Test
    public void testSetLongFileMode() {
        // Test case for LONGFILE_ERROR mode
        TarArchiveOutputStream t = new TarArchiveOutputStream(System.out);
        t.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, t.getLongFileMode());
        
        // Test case for LONGFILE_TRUNCATE mode
        t.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, t.getLongFileMode());
        
        // Test case for LONGFILE_GNU mode
        t.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, t.getLongFileMode());
    }

    @Test
    public void testFinish() {
        // Test case for finish() method
        TarArchiveOutputStream t = new TarArchiveOutputStream(System.out);
        
        try {
            t.finish();
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    @Test
    public void testClose() {
        // Test case for close() method
        TarArchiveOutputStream t = new TarArchiveOutputStream(System.out);
        
        try {
            t.close();
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    @Test
    public void testPutArchiveEntry() {
        // Test case when entry name length is less than TarConstants.NAMELEN
        TarArchiveOutputStream t = new TarArchiveOutputStream(System.out);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        
        try {
            t.putArchiveEntry(entry);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test case when currBytes is equal to currSize
        TarArchiveOutputStream t = new TarArchiveOutputStream(System.out);
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);
        
        try {
            t.putArchiveEntry(entry);
            t.write(new byte[10]);
            t.closeArchiveEntry();
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        
        // Test case when currBytes is less than currSize
        entry.setSize(20);
        
        try {
            t.putArchiveEntry(entry);
            t.write(new byte[10]);
            t.closeArchiveEntry();
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        
        // Test case when currBytes is more than currSize
        entry.setSize(10);
        
        try {
            t.putArchiveEntry(entry);
            t.write(new byte[20]);
            t.closeArchiveEntry();
            fail("Expected IOException was not thrown");
        } catch (IOException ex) {
            assertEquals("entry 'file.txt' closed at '10' before the '10' bytes specified in the header were written", ex.getMessage());
        }
    }

    @Test
    public void testWrite() {
        // Test case when numToWrite is less than recordBuf.length
        TarArchiveOutputStream t = new TarArchiveOutputStream(System.out);
        
        try {
            t.write(new byte[512], 0, 256);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        
        // Test case when numToWrite is equal to recordBuf.length
        try {
            t.write(new byte[512], 0, 512);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        
        // Test case when numToWrite is more than recordBuf.length
        try {
            t.write(new byte[1024], 0, 1024);
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        // Test case for creating archive entry from file
        TarArchiveOutputStream t = new TarArchiveOutputStream(System.out);
        
        File file = new File("test.txt");
        
        try {
            ArchiveEntry entry = t.createArchiveEntry(file, "test.txt");
            
            assertEquals("test.txt", entry.getName());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }
}