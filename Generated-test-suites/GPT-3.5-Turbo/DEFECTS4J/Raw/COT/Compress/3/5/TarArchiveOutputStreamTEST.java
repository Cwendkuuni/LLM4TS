
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry_LongName_GNU() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.setLongFileMode(2);
        ArchiveEntry archiveEntry = new TarArchiveEntry("this_is_a_very_long_file_name_that_exceeds_the_limit_of_TarConstants_NAMELEN_bytes");
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongName_Error() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("this_is_a_very_long_file_name_that_exceeds_the_limit_of_TarConstants_NAMELEN_bytes");
        outputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_IncompleteWrite() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "This is a test file content".getBytes().length);
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.write("This is a test file content".getBytes(), 0, 10);
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "This is a test file content".getBytes().length);
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.write("This is a test file content".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWrite_ExceedsSize() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt", "This is a test file content".getBytes().length);
        outputStream.putArchiveEntry(archiveEntry);
        outputStream.write("This is a test file content".getBytes(), 0, 50);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.finish();
        outputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(512, outputStream.getRecordSize());
        outputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = outputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        assertEquals("testFile.txt", archiveEntry.getName());
        outputStream.close();
    }
}
