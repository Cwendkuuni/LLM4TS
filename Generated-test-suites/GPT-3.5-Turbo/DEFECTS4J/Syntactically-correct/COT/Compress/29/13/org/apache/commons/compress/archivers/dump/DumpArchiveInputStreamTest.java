import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        InputStream fileInputStream = new FileInputStream("path_to_dump_file");
        inputStream = new DumpArchiveInputStream(fileInputStream);
    }

    @Test
    public void getCount() {
        int count = inputStream.getCount();
        assertEquals(0, count);
    }

    @Test
    public void getBytesRead() {
        long bytesRead = inputStream.getBytesRead();
        assertEquals(0, bytesRead);
    }

    @Test
    public void getSummary() {
        DumpArchiveSummary summary = inputStream.getSummary();
        assertNotNull(summary);
    }

    @Test
    public void getNextDumpEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextDumpEntry();
        assertNull(entry);
    }

    @Test
    public void getNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void read() throws IOException {
        byte[] buf = new byte[1024];
        int bytesRead = inputStream.read(buf, 0, buf.length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void close() throws IOException {
        inputStream.close();
        assertTrue(inputStream.isClosed);
    }
}