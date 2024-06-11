import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.junit.Test;

public class DumpArchiveInputStreamTest {

    @Test
    public void testGetCount() throws ArchiveException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(null);
        int count = inputStream.getCount();
        assertEquals(0, count);
    }

    @Test
    public void testGetBytesRead() throws ArchiveException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(null);
        long bytesRead = inputStream.getBytesRead();
        assertEquals(0, bytesRead);
    }

    @Test
    public void testGetSummary() throws ArchiveException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(null);
        assertNotNull(inputStream.getSummary());
    }

    @Test
    public void testGetNextDumpEntry() throws ArchiveException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(null);
        DumpArchiveEntry entry = inputStream.getNextDumpEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws ArchiveException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(null);
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws ArchiveException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(null);
        byte[] buffer = new byte[10];
        int read = inputStream.read(buffer, 0, 10);
        assertEquals(-1, read);
    }

    @Test
    public void testClose() throws ArchiveException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(null);
        inputStream.close();
        assertTrue(inputStream.isClosed());
    }
}