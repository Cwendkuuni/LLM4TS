import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setup() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        dumpArchiveInputStream = new DumpArchiveInputStream(input);
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        assertNotNull(summary);
    }

    @Test
    public void testGetNextDumpEntry() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextDumpEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = dumpArchiveInputStream.read(buffer);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        dumpArchiveInputStream.close();
        assertTrue(dumpArchiveInputStream.isClosed);
    }

}