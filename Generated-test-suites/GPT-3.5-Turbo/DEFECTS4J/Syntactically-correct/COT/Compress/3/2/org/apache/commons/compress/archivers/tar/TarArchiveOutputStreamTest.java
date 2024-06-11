
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        tarOutputStream = new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutputStream.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test(expected = IOException.class)
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        tarOutputStream.close();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWrite() throws IOException {
        byte[] buffer = new byte[1024];
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarOutputStream.write(buffer, 0, buffer.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.write(buffer, 0, buffer.length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testFile.txt");
        TarArchiveEntry entry = (TarArchiveEntry) tarOutputStream.createArchiveEntry(file, "testFile.txt");
        assertEquals(file, entry.getFile());
        assertEquals("testFile.txt", entry.getName());
    }
}
