import org.junit.*;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.closeArchiveEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("testfile", out.getName());
    }

    @Test
    public void testWrite() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.write("12345".getBytes());
            out.closeArchiveEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(5, out.getSize());
    }

    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.write("12345".getBytes());
            out.closeArchiveEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("testfile", out.getResource().getName());
    }

    @Test
    public void testFinish() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.write("12345".getBytes());
            out.closeArchiveEntry();
            out.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(5, out.getCrc());
    }

    @Test
    public void testClose() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.write("12345".getBytes());
            out.closeArchiveEntry();
            out.finish();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(out.isClosed());
    }

}