import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructOutputStream() {
        OutputStream os = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(os);
        assertNotNull(cpioOut);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);
        assertTrue(cpioOut.getCurrentEntry() != null);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicate() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);
        cpioOut.putNextEntry(entry); // should throw IOException
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = "test".getBytes();
        cpioOut.write(data, 0, data.length);
        assertEquals(data.length, cpioOut.getBytesWritten());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOut.putNextEntry(entry);
        cpioOut.write("test".getBytes());
        cpioOut.closeArchiveEntry();
        assertNull(cpioOut.getCurrentEntry());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(os);
        cpioOut.finish();
        assertEquals("TRAILER!!!", cpioOut.getCurrentEntry().getName());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(os);
        cpioOut.close();
        assertTrue(cpioOut.isClosed());
    }
}