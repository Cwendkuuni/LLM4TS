import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testConstructorWithDefaultBlockSizeAndRecordSize() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        
        assertNotNull(tarArchiveOutputStream);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntryLongFileNameTruncate() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        
        TarArchiveEntry archiveEntry = new TarArchiveEntry("this_is_a_very_long_file_name_that_should_be_truncated.txt");
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        
        tarArchiveOutputStream.closeArchiveEntry();
        
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }
    
    @Test
    public void testPutArchiveEntryLongFileNameGNU() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        
        TarArchiveEntry archiveEntry = new TarArchiveEntry("this_is_a_very_long_file_name_that_should_be_kept.txt");
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        
        tarArchiveOutputStream.closeArchiveEntry();
        
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }
    
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryLongFileNameError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        
        TarArchiveEntry archiveEntry = new TarArchiveEntry("this_is_a_very_long_file_name_that_should_throw_an_exception.txt");
        
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        
        TarArchiveEntry archiveEntry = new TarArchiveEntry("example.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        
        tarArchiveOutputStream.write("hello world".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        
        tarArchiveOutputStream.finish();
        
        tarArchiveOutputStream.close();
        
        assertTrue(outputStream.size() > 0);
    }
}