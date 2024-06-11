import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private File tarFile;
    private TarArchiveOutputStream tarOut;
    private ByteArrayOutputStream byteOut;

    @Before
    public void setUp() throws Exception {
        tarFile = new File("test.tar");
        byteOut = new ByteArrayOutputStream();
        FileOutputStream fileOut = new FileOutputStream(tarFile);
        GZIPOutputStream gzipOut = new GZIPOutputStream(fileOut);
        tarOut = new TarArchiveOutputStream(gzipOut);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.close();
        tarFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);
        entry.setMode(0644);
        tarOut.putArchiveEntry(entry);
        tarOut.write("Test data".getBytes());
        tarOut.closeArchiveEntry();

        assertEquals(1, tarOut.getCurrentEntryNum());
        assertEquals(entry, tarOut.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryTwiceWithoutCloseArchiveEntry() throws IOException {
        tarOut.putArchiveEntry(new TarArchiveEntry("file.txt"));
        tarOut.putArchiveEntry(new TarArchiveEntry("file2.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        tarOut.finish();
        tarOut.putArchiveEntry(new TarArchiveEntry("file.txt"));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOut.putArchiveEntry(new TarArchiveEntry("file.txt"));
        tarOut.write("Test data".getBytes());
        tarOut.closeArchiveEntry();

        assertFalse(tarOut.isEntryOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarOut.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        tarOut.finish();
        tarOut.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        tarOut.putArchiveEntry(new TarArchiveEntry("file.txt"));
        tarOut.write("Test data".getBytes());
        tarOut.closeArchiveEntry();

        assertTrue(tarFile.exists());

        // TODO: Verify the expected contents of the tar file
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        tarOut.putArchiveEntry(new TarArchiveEntry("file.txt"));
        tarOut.closeArchiveEntry();
        tarOut.write("Test data".getBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        tarOut.finish();
        tarOut.write("Test data".getBytes());
    }

    @Test
    public void testFinish() throws IOException {
        tarOut.putArchiveEntry(new TarArchiveEntry("file.txt"));
        tarOut.write("Test data".getBytes());
        tarOut.finish();

        assertTrue(tarFile.exists());

        // TODO: Verify the expected contents of the tar file
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarOut.finish();
        tarOut.finish();
    }
}