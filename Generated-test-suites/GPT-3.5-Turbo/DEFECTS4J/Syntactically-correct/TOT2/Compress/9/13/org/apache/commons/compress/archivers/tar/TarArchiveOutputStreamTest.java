import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream byteOut;

    @Before
    public void setUp() {
        byteOut = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(byteOut);
    }

    @Test
    public void testPutArchiveEntry() {
        try {
            TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
            tarOutput.putArchiveEntry(entry);
            tarOutput.closeArchiveEntry();
            assertTrue(tarOutput.getRecordSize() > 0);
        } catch (IOException e) {
            fail("Exception thrown while testing putArchiveEntry: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        try {
            TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
            tarOutput.putArchiveEntry(entry);
            tarOutput.closeArchiveEntry();
            assertFalse(tarOutput.haveUnclosedEntry);
        } catch (IOException e) {
            fail("Exception thrown while testing closeArchiveEntry: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        try {
            TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
            tarOutput.putArchiveEntry(entry);
            String data = "This is a test data.";
            tarOutput.write(data.getBytes());
            tarOutput.closeArchiveEntry();
            assertEquals(data.length(), tarOutput.getCurrBytes());
        } catch (IOException e) {
            fail("Exception thrown while testing write: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        try {
            TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
            tarOutput.putArchiveEntry(entry);
            tarOutput.closeArchiveEntry();
            tarOutput.finish();
            // Check if finish method was successful
            assertTrue(tarOutput.isFinished());
        } catch (IOException e) {
            fail("Exception thrown while testing finish: " + e.getMessage());
        }
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());
    }

}