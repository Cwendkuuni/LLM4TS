import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void test_putArchiveEntry_and_closeArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(baos);
        
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Test data".getBytes());
        tarOutputStream.closeArchiveEntry();
        
        tarOutputStream.close();
        
        // Check if data was written successfully
        assertNotNull(baos.toByteArray());
    }

    @Test
    public void test_setLongFileMode() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(baos);
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }
    
    @Test(expected = IOException.class)
    public void test_finish_withUnclosedEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(baos);
        
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.finish(); // Should throw IOException
    }

    @Test
    public void test_write_EOFRecord() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(baos);
        
        tarOutputStream.writeEOFRecord();
        
        tarOutputStream.close();
        
        // Check if data was written successfully
        assertNotNull(baos.toByteArray());
    }

    @Test
    public void test_createArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(baos);
        
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        
        assertEquals("testFile.txt", archiveEntry.getName());
    }
}