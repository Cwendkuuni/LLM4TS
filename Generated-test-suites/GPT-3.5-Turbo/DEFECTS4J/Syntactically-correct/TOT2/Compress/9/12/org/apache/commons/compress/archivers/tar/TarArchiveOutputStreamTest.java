import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);

        String expectedName = "test.txt";
        String actualName = tarOutputStream.currName;
        assertEquals(expectedName, actualName);

        long expectedSize = 10;
        long actualSize = tarOutputStream.currSize;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("this_is_a_very_long_file_name_that_exceeds_the_limit_of_name_length_in_tar_archive_entry.txt");
        entry.setSize(20);
        tarOutputStream.putArchiveEntry(entry);

        int expectedMode = TarArchiveOutputStream.LONGFILE_GNU;
        int actualMode = tarOutputStream.longFileMode;
        assertEquals(expectedMode, actualMode);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.write(new byte[5]);

        tarOutputStream.closeArchiveEntry();

        assertFalse(tarOutputStream.haveUnclosedEntry);
        assertEquals(5, tarOutputStream.currBytes);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutPutArchiveEntry() throws IOException {
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithWrongSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.write(new byte[15]);

        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.write(new byte[10], 0, 10);

        assertEquals(10, tarOutputStream.currBytes);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();

        assertTrue(tarOutputStream.finished);
    }

    @Test
    public void testGetRecordSize() {
        int expectedRecordSize = 1024;
        int actualRecordSize = tarOutputStream.getRecordSize();
        assertEquals(expectedRecordSize, actualRecordSize);
    }

}