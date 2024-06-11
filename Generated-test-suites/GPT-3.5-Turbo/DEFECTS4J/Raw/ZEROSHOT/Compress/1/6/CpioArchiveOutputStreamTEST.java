import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws Exception {
        cpioOutputStream = new CpioArchiveOutputStream(new OutputStream());
    }

    @After
    public void tearDown() throws Exception {
        cpioOutputStream.close();
    }

    @Test
    public void testPutNextEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        assertEquals(entry, cpioOutputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithoutStream() throws Exception {
        cpioOutputStream.close();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();
        assertNull(cpioOutputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithInvalidSize() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("123456".getBytes());
        cpioOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("1234567890".getBytes());
        cpioOutputStream.closeArchiveEntry();
        assertNull(cpioOutputStream.getCpioEntry());
    }

    @Test(expected = IOException.class)
    public void testWritePastEndOfStoredEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write("123456".getBytes());
    }

    @Test
    public void testFinish() throws Exception {
        cpioOutputStream.finish();
        assertNull(cpioOutputStream.getCpioEntry());
    }

    @Test
    public void testClose() throws Exception {
        cpioOutputStream.close();
        assertTrue(cpioOutputStream.isClosed());
    }

}