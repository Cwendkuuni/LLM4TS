import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);

        try {
            cpioOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(entry, cpioOutputStream.getEntry());

        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);

        try {
            cpioOutputStream.putArchiveEntry(entry);
            cpioOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNull(cpioOutputStream.getEntry());

        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setSize(10);

        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

        try {
            cpioOutputStream.putArchiveEntry(entry);
            cpioOutputStream.write(data);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNull(cpioOutputStream.getEntry());
    }

    @Test
    public void testFinish() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null);

        try {
            cpioOutputStream.finish();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertNotNull(cpioOutputStream.getEntry());
        assertEquals(CPIO_TRAILER, cpioOutputStream.getEntry().getName());

        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null);

        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue(cpioOutputStream.isClosed());
    }
}