import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testPutArchiveEntryWithoutLongFileName() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertEquals("file.txt", tarArchiveOutputStream.currName);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName_ErrorMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("very_long_file_name_that_exceeds_tar_constants_limit.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
            fail("Expected RuntimeException to be thrown.");
        } catch (RuntimeException e) {
            assertEquals("file name 'very_long_file_name_that_exceeds_tar_constants_limit.txt' is too long ( > 255 bytes)", e.getMessage());
        }
    }

    @Test
    public void testPutArchiveEntryWithLongFileName_TruncateMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("very_long_file_name_that_exceeds_tar_constants_limit.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertEquals("very_long_file_name_that_exceeds_tar_constants_limit.txt", tarArchiveOutputStream.currName);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName_GnuMode() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("very_long_file_name_that_exceeds_tar_constants_limit.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertEquals(TarConstants.GNU_LONGLINK, tarArchiveOutputStream.currName);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(11, tarArchiveOutputStream.currBytes);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_IncompleteEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[8]);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[]{65, 66, 67});
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(3, tarArchiveOutputStream.currBytes);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertEquals(1024, outputStream.toByteArray().length);
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(outputStream.toByteArray().length > 0);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("file.txt");
        TarArchiveEntry entry = (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(inputFile, "entry.txt");
        assertEquals(inputFile, entry.getFile());
        assertEquals("entry.txt", entry.getName());
    }
}