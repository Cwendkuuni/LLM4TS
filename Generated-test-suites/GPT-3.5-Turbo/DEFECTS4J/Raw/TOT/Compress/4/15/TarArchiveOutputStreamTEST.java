import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarStream;
    private ByteArrayOutputStream outputStream;
    private File testFile;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarStream = new TarArchiveOutputStream(outputStream);
        testFile = new File("testFile.txt");
    }

    @After
    public void tearDown() {
        tarStream = null;
        outputStream = null;
        testFile = null;
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();

        assertTrue(tarStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_LongNameError() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "thisIsALongFileNameThatExceedsTheMaximumAllowed");
        tarStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite_WhenEntrySizeIsZero() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        entry.setSize(0);
        tarStream.putArchiveEntry(entry);

        tarStream.write("".getBytes());
        tarStream.closeArchiveEntry();

        assertEquals(0, tarStream.currBytes);
    }

    @Test
    public void testWrite_WhenEntryIsDirectory() throws IOException {
        File directory = new File("testDirectory");
        directory.mkdir();

        TarArchiveEntry entry = new TarArchiveEntry(directory, "testDirectory");
        entry.setSize(0);
        entry.setDirectory(true);
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();

        assertEquals(0, tarStream.currSize);
    }

    @Test(expected = IOException.class)
    public void testWrite_WhenNumToWriteExceedsEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        entry.setSize(10);
        tarStream.putArchiveEntry(entry);

        tarStream.write("This is a long string".getBytes());

        tarStream.closeArchiveEntry();
    }

    @Test
    public void testWrite_WhenNumToWriteIsEqualToRecordSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        entry.setSize(512);
        tarStream.putArchiveEntry(entry);

        tarStream.write("This is a long string".getBytes());

        tarStream.closeArchiveEntry();
        assertEquals(512, tarStream.currBytes);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarStream.putArchiveEntry(entry);

        tarStream.write("This is a test".getBytes());

        tarStream.closeArchiveEntry();
        assertFalse(tarStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_WhenBytesWrittenIsLessThanEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        entry.setSize(10);
        tarStream.putArchiveEntry(entry);

        tarStream.write("This is a long string".getBytes());

        tarStream.closeArchiveEntry();
    }

    @Test
    public void testSetLongFileMode() {
        tarStream.setLongFileMode(1);
        assertEquals(1, tarStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarStream.finish();

        byte[] output = outputStream.toByteArray();
        assertEquals(1024, output.length);
        assertEquals(0, output[0]);
        assertEquals(0, output[511]);
        assertEquals(0, output[512]);
        assertEquals(0, output[1023]);
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarStream.getRecordSize();
        assertEquals(512, recordSize);
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        tarStream.setLongFileMode(1);
        tarStream.putArchiveEntry(new TarArchiveEntry(testFile));
        tarStream.close();
        tarStream.putArchiveEntry(new TarArchiveEntry(testFile));
    }
}