
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setup() {
        cpioOutputStream = new CpioArchiveOutputStream();
    }

    @Test
    public void testPutArchiveEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putArchiveEntry(entry);
        assertEquals(entry, cpioOutputStream.getCurrentEntry());
    }

    @Test
    public void testWrite() {
        byte[] data = "Hello, World!".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(data, 0, data.length);
        assertEquals(data.length, cpioOutputStream.getBytesWritten());
    }

    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.closeArchiveEntry();
        assertTrue(cpioOutputStream.getCurrentEntry() == null);
    }

    @Test
    public void testFinish() {
        cpioOutputStream.finish();
        assertTrue(cpioOutputStream.isFinished());
    }

    @Test
    public void testClose() {
        cpioOutputStream.close();
        assertTrue(cpioOutputStream.isClosed());
    }

}
