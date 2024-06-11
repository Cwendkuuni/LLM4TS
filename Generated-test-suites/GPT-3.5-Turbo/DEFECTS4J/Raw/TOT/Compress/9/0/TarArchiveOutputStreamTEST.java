import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() {
        OutputStream outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry_ValidEntry_Success() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(1, tarArchiveOutputStream.getCurrentEntryCount());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_NullEntry_ThrowsIOException() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntry_LongFileNameWithTruncate_Success() throws IOException {
        String longFileName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz.txt";
        File file = new File(longFileName);
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, longFileName);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(1, tarArchiveOutputStream.getCurrentEntryCount());
    }

    @Test
    public void testCloseArchiveEntry_EntryClosed_Success() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_NoCurrentEntry_ThrowsIOException() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite_ValidData_Success() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Test data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(10, tarArchiveOutputStream.getBytesWritten());
    }

    @Test(expected = IOException.class)
    public void testWrite_ExceedsSizeInHeader_ThrowsIOException() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("This data exceeds the size specified in the header".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testFinish_StreamAlreadyFinished_ThrowsIOException() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testFinish_NoUnclosedEntry_Success() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinish_UnclosedEntryPresent_ThrowsIOException() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose_StreamNotFinished_Success() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testClose_StreamAlreadyFinished_Success() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize_DefaultBlockSize_Success() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode_ValidMode_Success() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testCreateArchiveEntry_ValidFileAndEntryName_Success() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");

        assertEquals("test.txt", entry.getName());
        assertEquals(file.length(), entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntry_StreamAlreadyFinished_ThrowsIOException() throws IOException {
        tarArchiveOutputStream.finish();
        File file = new File("test.txt");
        tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
    }

}