import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        // Create CpioArchiveOutputStream object
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(new OutputStream(), CpioArchiveOutputStream.FORMAT_NEW);

        // Create CpioArchiveEntry object
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testEntry");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setFileSize(100L);

        try {
            cpioOutput.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception thrown when putting archive entry");
        }
    }

    @Test
    public void testWrite() {
        // Create CpioArchiveOutputStream object
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(new OutputStream(), CpioArchiveOutputStream.FORMAT_NEW);

        // Create byte array to write
        byte[] data = {0x41, 0x42, 0x43, 0x44};

        try {
            cpioOutput.write(data, 0, data.length);
        } catch (IOException e) {
            fail("Exception thrown when writing data");
        }
    }

    @Test
    public void testFinish() {
        // Create CpioArchiveOutputStream object
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(new OutputStream(), CpioArchiveOutputStream.FORMAT_NEW);

        try {
            cpioOutput.finish();
        } catch (IOException e) {
            fail("Exception thrown when finishing");
        }
    }

    @Test
    public void testClose() {
        // Create CpioArchiveOutputStream object
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(new OutputStream(), CpioArchiveOutputStream.FORMAT_NEW);

        try {
            cpioOutput.close();
        } catch (IOException e) {
            fail("Exception thrown when closing");
        }
    }
}