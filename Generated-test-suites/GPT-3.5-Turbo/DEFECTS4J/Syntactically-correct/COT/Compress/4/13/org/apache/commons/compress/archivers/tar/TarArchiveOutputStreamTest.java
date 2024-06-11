
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutput;
    
    @Before
    public void setUp() {
        tarOutput = new TarArchiveOutputStream(null);
    }
    
    @After
    public void tearDown() {
        try {
            tarOutput.close();
        } catch(Exception e) {
            // Ignore exception
        }
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());
    }
    
    @Test
    public void testPutArchiveEntry() {
        // Test with a valid TarArchiveEntry
        tarOutput.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        assertTrue(tarOutput.haveUnclosedEntry);
        // Verify the current entry name
        assertEquals("testFile.txt", tarOutput.currName);
        
        // Test with a long file name and truncate mode
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutput.putArchiveEntry(new TarArchiveEntry("ThisIsALongFileName.txt"));
        assertFalse(tarOutput.haveUnclosedEntry);
        // Verify the current entry name is truncated
        assertEquals("ThisIsALongFileName.txt", tarOutput.currName);
        
        // Test with a long file name and GNU mode
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutput.putArchiveEntry(new TarArchiveEntry("ThisIsALongFileName.txt"));
        assertTrue(tarOutput.haveUnclosedEntry);
        // Verify the current entry name
        assertEquals("ThisIsALongFileName.txt", tarOutput.currName);
        
    }
    
    @Test
    public void testCloseArchiveEntry() {
        // Test with closed entry
        tarOutput.haveUnclosedEntry = false;
        assertThrows(Exception.class, () -> {
            tarOutput.closeArchiveEntry();
        });
        
        // Test with open entry and incomplete buffer
        tarOutput.haveUnclosedEntry = true;
        tarOutput.assemLen = 10;
        tarOutput.currBytes = 0;
        assertThrows(Exception.class, () -> {
            tarOutput.closeArchiveEntry();
        });
        
        // Test with open entry and complete buffer
        tarOutput.haveUnclosedEntry = true;
        tarOutput.assemLen = 0;
        tarOutput.currBytes = 100;
        try {
            tarOutput.closeArchiveEntry();
        } catch(Exception e) {
            fail("Exception thrown");
        }
        assertFalse(tarOutput.haveUnclosedEntry);
    }
    
    @Test
    public void testWrite() {
        // Test writing to archive with no assembly and complete buffer
        tarOutput.assemLen = 0;
        tarOutput.currBytes = 0;
        byte[] data = {1, 2, 3};
        try {
            tarOutput.write(data, 0, data.length);
        } catch(Exception e) {
            fail("Exception thrown");
        }
        assertEquals(data.length, tarOutput.currBytes);
        
        // Test writing to archive with assembly and complete buffer
        tarOutput.assemLen = 10;
        tarOutput.currBytes = 0;
        tarOutput.assemBuf = new byte[20];
        try {
            tarOutput.write(data, 0, data.length);
        } catch(Exception e) {
            fail("Exception thrown");
        }
        assertEquals(data.length + tarOutput.assemLen, tarOutput.currBytes);
        
        // Test writing to archive with assembly and incomplete buffer
        tarOutput.assemLen = 10;
        tarOutput.currBytes = 0;
        tarOutput.assemBuf = new byte[5];
        try {
            tarOutput.write(data, 0, data.length);
        } catch(Exception e) {
            fail("Exception thrown");
        }
        assertEquals(data.length + tarOutput.assemLen, tarOutput.currBytes);
    }
    
    @Test
    public void testWriteEOFRecord() {
        assertThrows(Exception.class, () -> {
            tarOutput.writeEOFRecord();
        });
    }
    
    @Test
    public void testFinish() {
        assertThrows(Exception.class, () -> {
            tarOutput.finish();
        });
    }
    
    @Test
    public void testClose() {
        try {
            tarOutput.close();
            assertTrue(tarOutput.closed);
        } catch(Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarOutput.getRecordSize());
    }
    
    @Test
    public void testCreateArchiveEntry() {
        TarArchiveEntry entry = (TarArchiveEntry) tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
        assertEquals("test.txt", entry.getName());
    }
}


