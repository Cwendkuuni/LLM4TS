import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream archive;

    @Before
    public void setup() {
        archive = new CpioArchiveOutputStream();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);

        archive.putArchiveEntry(entry);

        // Verify that the entry is correctly set
        assertEquals("testfile", archive.entry.getName());
        assertEquals(10, archive.entry.getFileSize());
        assertEquals(CpioConstants.C_ISREG, archive.entry.getMode());
    }

    @Test
    public void testPutArchiveEntry_duplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1");
        entry1.setFileSize(10);
        entry1.setMode(CpioConstants.C_ISREG);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile1");
        entry2.setFileSize(20);
        entry2.setMode(CpioConstants.C_ISLNK);

        archive.putArchiveEntry(entry1);
        
        try {
            archive.putArchiveEntry(entry2);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Verify that the correct exception is thrown
            assertEquals("duplicate entry: testfile1", e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);

        archive.putArchiveEntry(entry);
        archive.closeArchiveEntry();

        // Verify that the current entry is null after closing
        assertNull(archive.entry);
    }

    @Test
    public void testCloseArchiveEntry_invalidEntrySize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);

        archive.putArchiveEntry(entry);

        try {
            archive.closeArchiveEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Verify that the correct exception is thrown
            assertEquals("invalid entry size (expected 10 but got 0 bytes)", e.getMessage());
        }
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);

        archive.putArchiveEntry(entry);
        archive.write("1234567890".getBytes());

        // Verify that the correct number of bytes is written
        assertEquals(10, archive.written);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);

        archive.putArchiveEntry(entry);
        archive.finish();

        // Verify that the trailer entry is correctly set
        assertEquals(CpioConstants.CPIO_TRAILER, archive.entry.getName());
        assertEquals(1, archive.entry.getNumberOfLinks());
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG);

        archive.putArchiveEntry(entry);
        archive.close();

        // Verify that the archive is closed
        assertTrue(archive.closed);
    }

}