
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.ByteArrayOutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Test
    public void testSetLongFileMode() {
        // Create TarArchiveOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        
        // Set long file mode to LONGFILE_ERROR
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());
    }
    
    @Test
    public void testFinish() {
        // Create TarArchiveOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        
        try {
            // Put dummy entry
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(null, "dummy.txt");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            
            // Finish archive
            tarArchiveOutputStream.finish();
            
            // Verify EOF records
            byte[] data = baos.toByteArray();
            int recordSize = tarArchiveOutputStream.getRecordSize();
            
            assertEquals(2 * recordSize, data.length);
            for (int i = 0; i < recordSize; i++) {
                assertEquals(0, data[i]);
                assertEquals(0, data[recordSize + i]);
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testClose() {
        // Create TarArchiveOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        
        try {
            // Put dummy entry
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(null, "dummy.txt");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            
            // Close archive
            tarArchiveOutputStream.close();
            
            // Verify archive is closed
            assertTrue(tarArchiveOutputStream.isClosed());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetRecordSize() {
        // Create TarArchiveOutputStream with custom record size
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int recordSize = 1024;
        tarArchiveOutputStream = new TarArchiveOutputStream(baos, recordSize);
        
        assertEquals(recordSize, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() {
        // Create TarArchiveOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        
        try {
            // Put dummy entry
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(null, "dummy.txt");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            
            // Verify current entry
            assertEquals(archiveEntry.getName(), tarArchiveOutputStream.getCurrentEntry().getName());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        // Create TarArchiveOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        
        try {
            // Put dummy entry
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(null, "dummy.txt");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            
            // Close current entry
            tarArchiveOutputStream.closeArchiveEntry();
            
            // Verify no bytes are left to write
            assertEquals(archiveEntry.getSize(), tarArchiveOutputStream.getBytesWritten());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testWrite() {
        // Create TarArchiveOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        
        try {
            // Put dummy entry
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(null, "dummy.txt");
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            
            // Write some content
            byte[] content = "Hello World".getBytes();
            tarArchiveOutputStream.write(content);
            
            // Close current entry
            tarArchiveOutputStream.closeArchiveEntry();
            
            // Verify bytes are written correctly
            assertEquals(content.length, tarArchiveOutputStream.getBytesWritten());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveEntry() {
        // Create TarArchiveOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(baos);
        
        try {
            // Create archive entry
            File inputFile = new File("dummy.txt");
            String entryName = "dummy.txt";
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
            
            // Verify archive entry properties
            assertEquals(inputFile, archiveEntry.getFile());
            assertEquals(entryName, archiveEntry.getName());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}

