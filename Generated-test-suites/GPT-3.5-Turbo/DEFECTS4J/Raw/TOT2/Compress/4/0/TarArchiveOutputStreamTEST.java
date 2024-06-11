import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tStream = new TarArchiveOutputStream(baos);
        assertNotNull(tStream);
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tStream = new TarArchiveOutputStream(baos);
        tStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tStream.longFileMode);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tStream = new TarArchiveOutputStream(baos);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tStream.putArchiveEntry(entry);
        tStream.write("Hello World".getBytes());
        tStream.closeArchiveEntry();
        tStream.finish();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tStream = new TarArchiveOutputStream(baos);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tStream.putArchiveEntry(entry);
        tStream.write("Hello World".getBytes());
        tStream.closeArchiveEntry();
        tStream.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongName() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tStream = new TarArchiveOutputStream(baos);
        String longName = "this_is_a_very_long_file_name_that_exceeds_the_limit_of_100_characters_to_test_long_file_mode";
        TarArchiveEntry entry = new TarArchiveEntry(longName);
        tStream.putArchiveEntry(entry);
    }

}