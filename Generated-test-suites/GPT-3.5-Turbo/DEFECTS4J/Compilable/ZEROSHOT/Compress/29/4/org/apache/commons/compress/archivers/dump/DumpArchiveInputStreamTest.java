package org.apache.commons.compress.archivers.dump;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new DumpArchiveInputStream(null);
    }

    @Test
    public void testGetCount() {
        int count = inputStream.getCount();
        assertEquals(0, count);
    }

    @Test
    public void testGetBytesRead() {
        long bytesRead = inputStream.getBytesRead();
        assertEquals(0, bytesRead);
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary summary = inputStream.getSummary();
        assertNotNull(summary);
    }

    @Test
    public void testGetNextDumpEntry() throws Exception {
        DumpArchiveEntry entry = inputStream.getNextDumpEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws Exception {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws Exception {
        byte[] buf = new byte[10];
        int bytesRead = inputStream.read(buf, 0, 10);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws Exception {
        inputStream.close();
        // check that the stream is closed
    }
}