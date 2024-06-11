import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructorWithFormat() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW);
        assertNotNull(out);
    }

    @Test
    public void testConstructorWithoutFormat() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(out);
    }

    @Test
    public void testPutArchiveEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        try {
            out.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(5);
        try {
            out.putArchiveEntry(entry);
            out.write("12345".getBytes(), 0, 5);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}