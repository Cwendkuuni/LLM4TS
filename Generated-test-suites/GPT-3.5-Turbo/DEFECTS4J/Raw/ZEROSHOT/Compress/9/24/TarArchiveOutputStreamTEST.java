import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE = "test.tar";

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutput = createTarOutput();
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutput = createTarOutput();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutput.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = createTarOutput();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(100);
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();
        File file = new File(TEST_FILE);
        assertTrue(file.exists());
        assertEquals(1024, file.length());
        file.delete();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = createTarOutput();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(100);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(new byte[50]);
        tarOutput.closeArchiveEntry();
        tarOutput.finish();
        tarOutput.close();
        File file = new File(TEST_FILE);
        assertTrue(file.exists());
        assertEquals(1024, file.length());
        file.delete();
    }

    private TarArchiveOutputStream createTarOutput() {
        try {
            OutputStream os = new FileOutputStream(new File(TEST_FILE));
            return new TarArchiveOutputStream(os);
        } catch (IOException e) {
            return null;
        }
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("file.txt");
        String entryName = "file.txt";
        TarArchiveOutputStream tarOutput = createTarOutput();
        ArchiveEntry entry = tarOutput.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(entryName, entry.getName());
    }
}