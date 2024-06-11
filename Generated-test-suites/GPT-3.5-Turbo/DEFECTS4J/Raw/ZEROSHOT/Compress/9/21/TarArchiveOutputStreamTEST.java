import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream outputStream;
    private File outputFile;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        outputFile = new File("test.txt");
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
        outputStream.close();
        outputFile.delete();
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(outputFile, "test.txt"));
        tarArchiveOutputStream.closeArchiveEntry();
        assertTrue(outputStream.toString().contains("test.txt"));
    }

    @Test
    public void testWrite() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(outputFile, "test.txt"));
        tarArchiveOutputStream.write("This is a test".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        assertTrue(outputStream.toString().contains("This is a test"));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(outputFile, "test.txt"));
        tarArchiveOutputStream.write("This is a test".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.finished);
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(outputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
    }
}