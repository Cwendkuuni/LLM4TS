import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOut;
    private ByteArrayOutputStream byteOut;

    @Before
    public void setUpStreams() {
        byteOut = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(byteOut);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry expectedEntry = new TarArchiveEntry(file, "test.txt");

        tarOut.putArchiveEntry(expectedEntry);
        tarOut.closeArchiveEntry();

        TarArchiveEntry actualEntry = new TarArchiveEntry(byteOut.toByteArray(), 0);

        assertEquals(expectedEntry.getName(), actualEntry.getName());
        assertEquals(expectedEntry.getSize(), actualEntry.getSize());
        assertEquals(expectedEntry.isDirectory(), actualEntry.isDirectory());
    }

    @Test(expected=IOException.class)
    public void testPutArchiveEntry_WithUnclosedEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");

        tarOut.putArchiveEntry(entry);
        tarOut.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");

        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();

        assertFalse(tarOut.haveUnclosedEntry);
    }

    @Test(expected=IOException.class)
    public void testCloseArchiveEntry_WithIncompleteEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        entry.setSize(10);

        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[5]);
        tarOut.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        entry.setSize(10);

        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[10]);
        tarOut.closeArchiveEntry();

        assertEquals(10, tarOut.currBytes);
    }

    @Test(expected=IOException.class)
    public void testWrite_ExceedsEntrySize() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        entry.setSize(10);

        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[15]);
    }

    @Test
    public void testFinish() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();

        tarOut.finish();
        tarOut.close();

        assertEquals(1024, byteOut.toByteArray().length);
    }

    @Test(expected=IOException.class)
    public void testFinish_WithUnclosedEntry() throws IOException {
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
        tarOut.putArchiveEntry(entry);

        tarOut.finish();
        tarOut.close();
    }

    @Test
    public void testSetLongFileMode() {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.longFileMode);
    }

    @After
    public void cleanUpStreams() throws IOException {
        tarOut.close();
        tarOut = null;
        byteOut = null;
    }
}