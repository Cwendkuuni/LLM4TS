import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

    DumpArchiveInputStream dumpArchiveInputStream;

    @Before
    public void setup() throws IOException, ArchiveException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveUtil.fillWithMagicBytes(headerBytes);
        ByteArrayInputStream in = new ByteArrayInputStream(headerBytes);
        dumpArchiveInputStream = new DumpArchiveInputStream(in);
    }

    @Test
    public void testGetCount() {
        assertEquals(0, dumpArchiveInputStream.getCount());
    }

    @Test
    public void testGetBytesRead() {
        assertEquals(0, dumpArchiveInputStream.getBytesRead());
    }

    @Test
    public void testGetSummary() {
        assertNotNull(dumpArchiveInputStream.getSummary());
    }

    @Test
    public void testGetNextDumpEntry() throws IOException {
        assertNotNull(dumpArchiveInputStream.getNextDumpEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNotNull(dumpArchiveInputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(-1, dumpArchiveInputStream.read(buffer, 0, 10));
    }

    @Test
    public void testClose() throws IOException {
        dumpArchiveInputStream.close();
        assertTrue(dumpArchiveInputStream.isClosed);
    }

    @Test
    public void testMatches() {
        byte[] buffer = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveUtil.fillWithMagicBytes(buffer);
        assertTrue(DumpArchiveInputStream.matches(buffer, DumpArchiveConstants.TP_SIZE));
    }
}