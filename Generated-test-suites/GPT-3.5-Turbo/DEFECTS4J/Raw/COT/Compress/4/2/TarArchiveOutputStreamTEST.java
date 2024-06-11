import static org.junit.Assert.*;
import java.io.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        
        // Check if the archive entry was added correctly
        assertEquals(1, tarOutputStream.getTotalEntries());
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        
        // Check if the archive entry was closed correctly
        assertEquals(0, tarOutputStream.getOpenEntries());
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPuttingEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }
    
    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] content = "Hello, World!".getBytes();
        tarOutputStream.write(content);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        
        // Check if the content was written correctly
        assertEquals(content.length, tarOutputStream.getTotalBytesWritten());
    }
    
    @Test
    public void testWriteWithOffsetAndLength() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] content = "Hello, World!".getBytes();
        tarOutputStream.write(content, 7, 5);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        
        // Check if the content was written correctly
        assertEquals(5, tarOutputStream.getTotalBytesWritten());
    }
    
    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        
        // Check if the archive was finished correctly
        assertTrue(tarOutputStream.isFinished());
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithoutClosingArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
    }
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarConstants.LONGFILE_TRUNCATE);
        
        // Check if the long file mode was set correctly
        assertEquals(TarConstants.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        
        // Check if the record size is returned correctly
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "test.txt");
        
        // Check if the archive entry was created correctly
        assertEquals("test.txt", entry.getName());
    }
}