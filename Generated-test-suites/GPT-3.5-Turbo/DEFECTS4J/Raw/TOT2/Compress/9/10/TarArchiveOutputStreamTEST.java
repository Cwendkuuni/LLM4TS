import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Mock
    private TarArchiveOutputStream mockOutputStream;
    
    @Test
    public void testPutArchiveEntry() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            
            TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.close();
            
            assertNotNull(outputStream.toByteArray());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testPutArchiveEntryWithLongFileNameTruncateMode() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            
            TarArchiveEntry entry = new TarArchiveEntry("veryLongFileNameThatExceedsTarConstantsNameLen.txt");
            tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.close();
            
            assertNotNull(outputStream.toByteArray());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testPutArchiveEntryWithLongFileNameGNUMode() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            
            TarArchiveEntry entry = new TarArchiveEntry("veryLongFileNameThatExceedsTarConstantsNameLen.txt");
            tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.close();
            
            assertNotNull(outputStream.toByteArray());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testPutArchiveEntryWithLongFileNameErrorMode() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            
            TarArchiveEntry entry = new TarArchiveEntry("veryLongFileNameThatExceedsTarConstantsNameLen.txt");
            tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
            
            try {
                tarOutputStream.putArchiveEntry(entry);
                fail("Expected RuntimeException due to long file name");
            } catch (RuntimeException e) {
                // Pass test
            }
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testWrite() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            
            byte[] data = new byte[10];
            tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
            tarOutputStream.write(data, 0, data.length);
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.close();
            
            assertNotNull(outputStream.toByteArray());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testFinish() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            
            tarOutputStream.finish();
            tarOutputStream.close();
            
            assertNotNull(outputStream.toByteArray());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testFinishTwice() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            
            tarOutputStream.finish();
            try {
                tarOutputStream.finish();
                fail("Expected IOException due to finishing twice");
            } catch (IOException e) {
                // Pass test
            }
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }

}