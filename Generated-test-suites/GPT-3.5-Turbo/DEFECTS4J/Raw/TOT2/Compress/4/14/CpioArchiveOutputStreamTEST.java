import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setup() {
        cpioOutputStream = new CpioArchiveOutputStream();
    }

    @After
    public void tearDown() {
        cpioOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(entry);

        assertEquals(entry, cpioOutputStream.getCurrentEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");

        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.closeArchiveEntry();

        assertFalse(cpioOutputStream.isOpen());
    }

    @Test
    public void testWrite() throws Exception {
        String data = "Hello World";
        byte[] bytes = data.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(bytes, 0, bytes.length);
        cpioOutputStream.closeArchiveEntry();

        assertEquals(bytes.length, cpioOutputStream.getCurrentEntry().getSize());
    }

    @Test
    public void testFinish() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);

        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.finish();

        assertTrue(cpioOutputStream.isFinished());
    }
}