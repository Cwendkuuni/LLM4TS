import org.junit.*;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream archive;

    @Before
    public void setUp() throws Exception {
        archive = new CpioArchiveOutputStream(null);
    }

    @After
    public void tearDown() throws Exception {
        archive.close();
    }

    @Test
    public void testPutNextEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        archive.putNextEntry(entry);
        assertEquals(entry, archive.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithInvalidEntrySize() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(10);
        archive.putNextEntry(entry);
        archive.write(new byte[5]);
        archive.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntryWithValidEntrySize() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(10);
        archive.putNextEntry(entry);
        archive.write(new byte[10]);
        archive.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(10);
        archive.putNextEntry(entry);
        archive.write(new byte[10]);
        archive.closeArchiveEntry();
        assertEquals(10, archive.getBytesWritten());
    }

    @Test
    public void testFinish() throws Exception {
        archive.finish();
        assertTrue(archive.isFinished());
    }

    @Test
    public void testClose() throws Exception {
        archive.close();
        assertTrue(archive.isClosed());
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        archive.putArchiveEntry(entry);
        assertEquals(entry, archive.getNextEntry());
    }

    @Test
    public void testWriteByte() throws Exception {
        archive.write(65);
        assertEquals(1, archive.getBytesWritten());
    }

}