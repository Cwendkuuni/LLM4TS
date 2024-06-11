import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        // Test constructor with default block size and record size
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        assertNotNull(output);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, output.getRecordSize());
    }

    @Test
    public void testConstructor2() {
        // Test constructor with specified block size and default record size
        int blockSize = 1024;
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub(), blockSize);
        assertNotNull(output);
        assertEquals(blockSize, output.getRecordSize());
    }

    @Test
    public void testConstructor3() {
        // Test constructor with specified block size and record size
        int blockSize = 1024;
        int recordSize = 512;
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub(), blockSize, recordSize);
        assertNotNull(output);
        assertEquals(recordSize, output.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        // Test setting long file mode
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        int mode = TarArchiveOutputStream.LONGFILE_GNU;
        output.setLongFileMode(mode);
        assertEquals(mode, output.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putting archive entry with valid name and size
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(1024);
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            output.putArchiveEntry(entry);
            assertTrue(output.isEntryOpen());
            assertEquals("file.txt", output.getCurrentEntry().getName());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testPutArchiveEntry_InvalidName() {
        // Test putting archive entry with name longer than TarConstants.NAMELEN
        String longName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        TarArchiveEntry entry = new TarArchiveEntry(longName);
        entry.setSize(1024);
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            output.putArchiveEntry(entry);
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("file name '" + longName + "' is too long ( > " + TarConstants.NAMELEN + " bytes)", e.getMessage());
        }
    }

    @Test
    public void testPutArchiveEntry_InvalidSize() {
        // Test putting archive entry with size larger than specified in header
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(1024);
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            output.putArchiveEntry(entry);
            output.write(new byte[2048]);
            output.closeArchiveEntry();
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("request to write '2048' bytes exceeds size in header of '1024' bytes for entry 'file.txt'", e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test closing archive entry
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(1024);
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            output.putArchiveEntry(entry);
            output.closeArchiveEntry();
            assertFalse(output.isEntryOpen());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCloseArchiveEntry_IncompleteWrite() {
        // Test closing archive entry without writing complete data
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(1024);
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            output.putArchiveEntry(entry);
            output.write(new byte[512]);
            output.closeArchiveEntry();
            fail("Exception should be thrown");
        } catch (Exception e) {
            assertEquals("entry 'file.txt' closed at '512' before the '1024' bytes specified in the header were written", e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        // Test finishing the tar archive
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            output.finish();
            assertFalse(output.isEntryOpen());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testClose() {
        // Test closing the tar archive
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            output.close();
            assertFalse(output.isEntryOpen());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        // Test creating archive entry for file
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        TarArchiveOutputStream output = new TarArchiveOutputStream(new OutputStreamStub());
        try {
            ArchiveEntry entry = output.createArchiveEntry(inputFile, entryName);
            assertNotNull(entry);
            assertTrue(entry instanceof TarArchiveEntry);
            assertEquals(entryName, entry.getName());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}