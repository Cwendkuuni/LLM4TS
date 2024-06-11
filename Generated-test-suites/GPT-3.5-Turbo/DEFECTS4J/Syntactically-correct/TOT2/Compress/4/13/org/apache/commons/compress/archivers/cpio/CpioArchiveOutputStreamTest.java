import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        try {
            cpioOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception not expected");
        }
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testWrite() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        byte[] data = { 1, 2, 3, 4, 5 };
        try {
            cpioOutputStream.write(data, 0, data.length);
        } catch (IOException e) {
            fail("Exception not expected");
        }
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testCloseArchiveEntry() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        try {
            cpioOutputStream.putArchiveEntry(entry);
            cpioOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception not expected");
        }
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testClose() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Exception not expected");
        }
        assertNotNull(cpioOutputStream);
    }

}