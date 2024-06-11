import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setup() {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(System.out);
    }

    @Test
    public void testPutNextEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioArchiveOutputStream.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] content = "12345".getBytes();
        cpioArchiveOutputStream.write(content, 0, content.length);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] content = "12345".getBytes();
        cpioArchiveOutputStream.write(content, 0, content.length);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws Exception {
        cpioArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws Exception {
        cpioArchiveOutputStream.close();
    }
}