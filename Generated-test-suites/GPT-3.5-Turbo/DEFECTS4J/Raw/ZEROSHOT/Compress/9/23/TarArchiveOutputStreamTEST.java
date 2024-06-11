import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import java.io.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    private FileOutputStream fileOutputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        File file = new File("test.tar");
        fileOutputStream = new FileOutputStream(file);
        tarArchiveOutputStream = new TarArchiveOutputStream(fileOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        fileOutputStream.close();
        File file = new File("test.tar");
        file.delete();
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(1);
        assertEquals(1, ((TarArchiveOutputStream) tarArchiveOutputStream).longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(((TarArchiveOutputStream) tarArchiveOutputStream).finished);
    }

    @Test (expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        ((TarArchiveOutputStream) tarArchiveOutputStream).finished = true;
        tarArchiveOutputStream.finish();
    }

    @Test (expected = IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        ((TarArchiveOutputStream) tarArchiveOutputStream).haveUnclosedEntry = true;
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(((TarArchiveOutputStream) tarArchiveOutputStream).closed);
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        assertEquals(tarArchiveEntry, tarArchiveOutputStream.currName);
        assertTrue(((TarArchiveOutputStream) tarArchiveOutputStream).haveUnclosedEntry);
    }

    @Test (expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        ((TarArchiveOutputStream) tarArchiveOutputStream).finished = true;
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ((TarArchiveOutputStream) tarArchiveOutputStream).haveUnclosedEntry = true;
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(((TarArchiveOutputStream) tarArchiveOutputStream).haveUnclosedEntry);
    }

    @Test (expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        ((TarArchiveOutputStream) tarArchiveOutputStream).finished = true;
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test (expected = IOException.class)
    public void testCloseArchiveEntryNoEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = new byte[] { 65, 66, 67 };
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test (expected = IOException.class)
    public void testWriteExceedSize() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveEntry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = new byte[] { 65, 66, 67 };
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test (expected = IOException.class)
    public void testWriteFinished() throws IOException {
        ((TarArchiveOutputStream) tarArchiveOutputStream).finished = true;
        byte[] data = new byte[] { 65, 66, 67 };
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        ((TarArchiveOutputStream) tarArchiveOutputStream).recordBuf = new byte[512];
        tarArchiveOutputStream.writeEOFRecord();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("input.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, "testFile.txt");
        assertTrue(archiveEntry instanceof TarArchiveEntry);
    }
}