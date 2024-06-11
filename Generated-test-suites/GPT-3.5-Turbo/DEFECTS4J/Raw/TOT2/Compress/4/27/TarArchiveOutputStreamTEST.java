import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        tarOut.close();
        // Assert that the entry was successfully added to the archive
        assertTrue(out.size() > 0);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        // Assert that the entry was closed successfully
        assertFalse(tarOut.haveUnclosedEntry);
        tarOut.close();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        byte[] data = "Test data".getBytes();
        tarOut.write(data, 0, data.length);
        // Assert that the data was written successfully
        assertTrue(out.size() == data.length);
        tarOut.close();
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        // Assert that the long file mode was set successfully
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.longFileMode);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongNameError() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        // Create an entry with a long name to trigger an error
        TarArchiveEntry entry = new TarArchiveEntry("this_is_a_very_long_file_name_that_exceeds_the_limit_of_name_length_in_tar_archives.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        tarOut.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutFinishing() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOut.close();
    }
}