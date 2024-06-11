import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

public class TarArchiveOutputStreamTest {
    private ByteArrayOutputStream outputStream;
    private ArchiveOutputStream archiveOutputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() {
        try {
            archiveOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        archiveOutputStream.putArchiveEntry(archiveEntry);
        archiveOutputStream.write("Hello, World!".getBytes());
        archiveOutputStream.closeArchiveEntry();
        
        // Assert
        assertNotNull(archiveEntry);
    }
    
    @Test
    public void testSetLongFileMode() {
        ((TarArchiveOutputStream)archiveOutputStream).setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, ((TarArchiveOutputStream)archiveOutputStream).longFileMode);
    }
    
    @Test
    public void testFinish() throws IOException {
        archiveOutputStream.finish();
        
        // Assert
        assertTrue(((TarArchiveOutputStream)archiveOutputStream).finished);
    }
    
    @Test
    public void testClose() throws IOException {
        archiveOutputStream.close();
        
        // Assert
        assertTrue(((TarArchiveOutputStream)archiveOutputStream).closed);
    }
    
    @Test
    public void testGetRecordSize() {
        int recordSize = ((TarArchiveOutputStream)archiveOutputStream).getRecordSize();
        
        // Assert
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }
}