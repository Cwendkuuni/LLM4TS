
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);

        assertNotNull(tarOS);
    }

    @Test
    public void testConstructor2() {
        OutputStream os = new OutputStream();
        int blockSize = 512;

        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, blockSize);

        assertNotNull(tarOS);
    }
    
    @Test
    public void testConstructor3() {
        OutputStream os = new OutputStream();
        int blockSize = 512;
        int recordSize = 1024;

        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os, blockSize, recordSize);

        assertNotNull(tarOS);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOS.getLongFileMode());
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new ArchiveEntry();
        tarOS.putArchiveEntry(entry);
        
        tarOS.finish();
    }
    
    @Test
    public void testFinish() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        tarOS.finish();
        
        // Assert that no exception is thrown
        
        // Also assert that the output stream is closed (cannot be tested directly since the out field is private)
    }
    
    @Test
    public void testClose() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        tarOS.close();
        
        // Assert that the output stream is closed (cannot be tested directly since the out field is private)
    }
    
    @Test
    public void testGetRecordSize() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        int recordSize = tarOS.getRecordSize();
        
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new ArchiveEntry();
        tarOS.putArchiveEntry(entry);
        
        // Assert that no exception is thrown
        
        tarOS.closeArchiveEntry();
        
        // Assert that no exception is thrown
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncorrectBytesWritten() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new ArchiveEntry();
        tarOS.putArchiveEntry(entry);
        
        tarOS.write(new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE + 1]);
        
        tarOS.closeArchiveEntry();
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new ArchiveEntry();
        tarOS.putArchiveEntry(entry);
        
        tarOS.closeArchiveEntry();
        
        // Assert that no exception is thrown
    }
    
    @Test
    public void testWrite() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new ArchiveEntry();
        tarOS.putArchiveEntry(entry);

        byte[] data = new byte[TarArchiveOutputStream.DEFAULT_RCDSIZE];
        tarOS.write(data);
        
        tarOS.closeArchiveEntry();
        
        // Assert that no exception is thrown
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(os);
        
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOS.createArchiveEntry(inputFile, entryName);
        
        assertNotNull(entry);
    }

}
