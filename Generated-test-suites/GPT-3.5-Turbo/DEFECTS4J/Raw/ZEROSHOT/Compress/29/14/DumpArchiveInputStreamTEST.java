import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DumpArchiveInputStreamTest {

    DumpArchiveInputStream archiveInputStream;

    @Before
    public void setUp() throws Exception {
        archiveInputStream = new DumpArchiveInputStream(null);
    }

    @Test
    public void testGetBytesRead() {
        assertEquals(0, archiveInputStream.getBytesRead());
    }

    @Test
    public void testGetSummary() {
        assertNotNull(archiveInputStream.getSummary());
    }

    @Test
    public void testGetNextDumpEntry() throws Exception {
        assertNull(archiveInputStream.getNextDumpEntry());
    }

    @Test
    public void testGetNextEntry() throws Exception {
        assertNull(archiveInputStream.getNextEntry());
    }

    @Test
    public void testReadByteArrayIntInt() throws Exception {
        byte[] buffer = new byte[1];
        assertEquals(-1, archiveInputStream.read(buffer, 0, 1));
    }

    @Test
    public void testClose() throws Exception {
        archiveInputStream.close();
        assertTrue(archiveInputStream.isClosed);
    }

    @Test
    public void testMatches() {
        byte[] buffer = new byte[32];
        assertFalse(DumpArchiveInputStream.matches(buffer, 31));
    }

}