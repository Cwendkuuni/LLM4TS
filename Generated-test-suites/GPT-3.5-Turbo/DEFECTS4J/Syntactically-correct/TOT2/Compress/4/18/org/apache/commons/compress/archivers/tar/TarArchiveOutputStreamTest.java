import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructorWithBlockSize() {
        // Test constructor with block size
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os, 512);
        assertNotNull(tOut);
    }

    @Test
    public void testSetLongFileMode() {
        // Test setting long file mode
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tOut.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putting archive entry
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        ArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"));
        assertThrows(IOException.class, () -> {
            tOut.putArchiveEntry(entry);
        });
    }

    @Test
    public void testWrite() {
        // Test writing to archive
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        byte[] data = "Test".getBytes();
        assertThrows(IOException.class, () -> {
            tOut.write(data, 0, 5);
        });
    }

    @Test
    public void testCreateArchiveEntry() {
        // Test creating archive entry
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File file = new File("test.txt");
        ArchiveEntry entry = tOut.createArchiveEntry(file, "test.txt");
        assertTrue(entry instanceof TarArchiveEntry);
    }

}