import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DumpArchiveInputStreamTest {
    private DumpArchiveInputStream stream;

    @Before
    public void setUp() throws ArchiveException {
        stream = new DumpArchiveInputStream(null);
    }

    @Test
    public void testGetCount() {
        assertEquals(0, stream.getCount());
    }

    @Test
    public void testGetBytesRead() {
        assertEquals(0, stream.getBytesRead());
    }

    @Test
    public void testGetSummary() {
        assertNotNull(stream.getSummary());
    }

    @Test
    public void testGetNextDumpEntry() throws IOException {
        assertNull(stream.getNextDumpEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNull(stream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buf = new byte[10];
        assertEquals(-1, stream.read(buf, 0, 10));
    }

    @Test
    public void testClose() throws IOException {
        stream.close();
        assertTrue(stream.isClosed());
    }
}