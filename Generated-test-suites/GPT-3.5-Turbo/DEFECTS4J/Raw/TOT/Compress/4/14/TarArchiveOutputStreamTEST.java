import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        tarOutputStream = new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode_ValidMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);
    }

    @Test
    public void testSetLongFileMode_InvalidMode() {
        tarOutputStream.setLongFileMode(10);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutputStream.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_TruncateMode() {
        TarArchiveEntry entry = new TarArchiveEntry("this_is_a_very_long_file_name_that_exceeds_the_limit_of_100_bytes_and_should_be_truncated");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        // Truncated file name should be equal to the limit of 100 bytes
        assertEquals(100, entry.getName().length());
    }

    @Test
    public void testPutArchiveEntry_LongFileName_GnuMode() {
        TarArchiveEntry entry = new TarArchiveEntry("this_is_a_very_long_file_name_that_exceeds_the_limit_of_100_bytes_and_should_be_truncated");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        // Long file name should be preserved
        assertEquals("this_is_a_very_long_file_name_that_exceeds_the_limit_of_100_bytes_and_should_be_truncated", entry.getName());
    }

    @Test
    public void testPutArchiveEntry_LongFileName_ErrorMode() {
        TarArchiveEntry entry = new TarArchiveEntry("this_is_a_very_long_file_name_that_exceeds_the_limit_of_100_bytes_and_should_be_truncated");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        try {
            tarOutputStream.putArchiveEntry(entry);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("file name 'this_is_a_very_long_file_name_that_exceeds_the_limit_of_100_bytes_and_should_be_truncated' is too long ( > 100 bytes)", e.getMessage());
        }
    }

    @Test
    public void testPutArchiveEntry_Directory() {
        TarArchiveEntry entry = new TarArchiveEntry("test_directory/");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        
        assertEquals(0, entry.getSize());
    }

    @Test
    public void testCloseArchiveEntry_IncompleteData() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);

        try {
            tarOutputStream.closeArchiveEntry();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("entry 'file.txt' closed at '0' before the '0' bytes specified in the header were written", e.getMessage());
        }
    }

    @Test
    public void testFinish_UnclosedEntries() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        try {
            tarOutputStream.close();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }

    @Test
    public void testFinish_ClosedOutputStream() {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        
        // No exception should be thrown
    }

}