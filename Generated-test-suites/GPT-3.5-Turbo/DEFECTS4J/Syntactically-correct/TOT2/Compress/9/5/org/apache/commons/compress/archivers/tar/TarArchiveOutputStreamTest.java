import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput.longFileMode);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(baos);
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        
        tarOutput.putArchiveEntry(entry);
        tarOutput.write("Test data".getBytes());
        tarOutput.closeArchiveEntry();
        tarOutput.close();
        
        assertNotNull(baos.toByteArray());
        assertEquals(512, baos.toByteArray().length);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(baos);
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        
        assertFalse(tarOutput.haveUnclosedEntry);
        tarOutput.close();
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(baos);
        tarOutput.finish();
        tarOutput.close();
        assertTrue(tarOutput.finished);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = (TarArchiveEntry) tarOutput.createArchiveEntry(file, "test.txt");
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
    }
}