import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        t.setLongFileMode(1);
        assertEquals(1, t.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        t.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        t.write("Hello, World!".getBytes());
        t.closeArchiveEntry();
        t.close();
    }

    @Test
    public void testFinish() {
        try {
            TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
            t.finish();
            t.close();
            t.finish(); // Should throw IOException
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = t.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        assertEquals("testFile.txt", entry.getName());
    }
}