import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DumpArchiveInputStreamTest {

    @Test
    public void testGetCount() {
        DumpArchiveInputStream stream = new DumpArchiveInputStream(null);
        int count = stream.getCount();
        assertEquals(0, count);
    }

    @Test
    public void testGetBytesRead() {
        DumpArchiveInputStream stream = new DumpArchiveInputStream(null);
        long bytesRead = stream.getBytesRead();
        assertEquals(0, bytesRead);
    }

    @Test
    public void testGetSummary() {
        DumpArchiveInputStream stream = new DumpArchiveInputStream(null);
        DumpArchiveSummary summary = stream.getSummary();
        assertTrue(summary instanceof DumpArchiveSummary);
    }

    @Test
    public void testNextDumpEntry() {
        DumpArchiveInputStream stream = new DumpArchiveInputStream(null);
        DumpArchiveEntry entry = stream.getNextDumpEntry();
        assertTrue(entry instanceof DumpArchiveEntry);
    }

    @Test
    public void testNextEntry() {
        DumpArchiveInputStream stream = new DumpArchiveInputStream(null);
        DumpArchiveEntry entry = stream.getNextEntry();
        assertTrue(entry instanceof DumpArchiveEntry);
    }

    @Test
    public void testRead() throws Exception {
        DumpArchiveInputStream stream = new DumpArchiveInputStream(null);
        byte[] buffer = new byte[10];
        int bytesRead = stream.read(buffer, 0, 10);
        assertEquals(10, bytesRead);
    }

    @Test
    public void testClose() throws Exception {
        DumpArchiveInputStream stream = new DumpArchiveInputStream(null);
        stream.close();
        assertFalse(stream.isClosed());
    }
}