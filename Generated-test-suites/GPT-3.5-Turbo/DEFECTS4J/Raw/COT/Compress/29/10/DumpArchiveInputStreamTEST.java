import static org.junit.Assert.*;
import org.junit.Test;

public class DumpArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        assertNotNull(dis);
    }

    @Test
    public void testGetCount() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        assertEquals(0, dis.getCount());
    }

    @Test
    public void testGetBytesRead() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        assertEquals(0, dis.getBytesRead());
    }

    @Test
    public void testGetSummary() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        assertNull(dis.getSummary());
    }

    @Test
    public void testGetNextDumpEntry() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        assertNull(dis.getNextDumpEntry());
    }

    @Test
    public void testGetNextEntry() {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        assertNull(dis.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3};
        InputStream is = new ByteArrayInputStream(data);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        byte[] buffer = new byte[3];
        assertEquals(3, dis.read(buffer, 0, 3));
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testClose() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream dis = new DumpArchiveInputStream(is);
        dis.close();
        assertTrue(dis.isClosed());
    }

}