
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);

        assertNotNull(tOut);
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tOut.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);

        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tOut.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tOut.createArchiveEntry(inputFile, entryName);
        tOut.putArchiveEntry(archiveEntry);

        assertTrue(tOut.haveUnclosedEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tOut.createArchiveEntry(inputFile, entryName);
        tOut.putArchiveEntry(archiveEntry);
        tOut.closeArchiveEntry();

        assertFalse(tOut.haveUnclosedEntry());
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tOut.createArchiveEntry(inputFile, entryName);
        tOut.putArchiveEntry(archiveEntry);

        byte[] data = "Hello World".getBytes();
        tOut.write(data);

        assertFalse(tOut.haveUnclosedEntry());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tOut.createArchiveEntry(inputFile, entryName);
        tOut.putArchiveEntry(archiveEntry);
        tOut.finish();

        assertFalse(tOut.haveUnclosedEntry());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tOut.createArchiveEntry(inputFile, entryName);
        tOut.putArchiveEntry(archiveEntry);
        tOut.close();

        assertTrue(tOut.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tOut.createArchiveEntry(inputFile, entryName);

        assertNotNull(archiveEntry);
    }
}

