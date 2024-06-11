import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
            TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
            tarOs.putArchiveEntry(entry);
            tarOs.write("test data".getBytes());
            tarOs.closeArchiveEntry();
            tarOs.close();
            assertEquals(0, os.size() % 512); // Check if the output stream size is multiple of 512
        } catch (IOException e) {
            fail("Unexpected IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOs.longFileMode);
    }

    @Test
    public void testFinish() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
            tarOs.finish();
            assertEquals(1024, os.size()); // Check if the output stream size is 1024 after finishing
        } catch (IOException e) {
            fail("Unexpected IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
            tarOs.close();
            assertEquals(true, tarOs.closed);
        } catch (IOException e) {
            fail("Unexpected IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
            File file = new File("testFile.txt");
            String entryName = "testFileEntry";
            ArchiveEntry entry = tarOs.createArchiveEntry(file, entryName);
            assertNotNull(entry);
            assertEquals(TarArchiveEntry.class, entry.getClass());
        } catch (IOException e) {
            fail("Unexpected IOException occurred: " + e.getMessage());
        }
    }

}