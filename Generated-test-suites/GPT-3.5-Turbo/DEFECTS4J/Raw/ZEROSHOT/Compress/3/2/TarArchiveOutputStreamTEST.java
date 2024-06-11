import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructor_WithOutputStream() {
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructor_WithOutputStreamAndBlockSize() {
        int blockSize = 512;
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, blockSize);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructor_WithOutputStreamAndBlockSizeAndRecordSize() {
        int blockSize = 512;
        int recordSize = 1024;
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, blockSize, recordSize);
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        int longFileMode = 2;
        tarArchiveOutputStream.setLongFileMode(longFileMode);
        assertEquals(longFileMode, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        int blockSize = 512;
        int recordSize = 1024;
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, blockSize, recordSize);
        assertEquals(recordSize, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        File file = new File("testfile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, file.getName());
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        assertNotNull(tarArchiveOutputStream.getCurrentEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        File file = new File("testfile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, file.getName());
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(0, tarArchiveOutputStream.getCurrentEntry().getSize());
    }

    @Test
    public void testWrite() throws IOException {
        String content = "test content";
        byte[] data = content.getBytes();
        tarArchiveOutputStream.write(data);
        assertEquals(content.length(), tarArchiveOutputStream.getBytesWritten());
    }

    @Test(expected = IOException.class)
    public void testWrite_ExceedEntrySize() throws IOException {
        File file = new File("testfile.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, file.getName(), file.length() - 1);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        String content = "test content";
        byte[] data = content.getBytes();
        tarArchiveOutputStream.write(data);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertEquals(2 * TarBuffer.DEFAULT_BLKSIZE, outputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testfile.txt");
        String entryName = "testfile.txt";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        assertNotNull(archiveEntry);
        assertEquals(entryName, archiveEntry.getName());
    }

}