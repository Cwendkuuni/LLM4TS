import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;
    private File testFile;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        testFile = new File("testFile.txt");
    }

    @Test
    public void testConstructor_Defaults() {
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testConstructor_BlockSize() {
        int blockSize = 2048;
        tarOutputStream = new TarArchiveOutputStream(outputStream, blockSize);
        assertEquals(blockSize, tarOutputStream.getRecordSize());
    }

    @Test
    public void testConstructor_BlockAndRecordSize() {
        int blockSize = 2048;
        int recordSize = 512;
        tarOutputStream = new TarArchiveOutputStream(outputStream, blockSize, recordSize);
        assertEquals(blockSize, tarOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        int longFileMode = TarArchiveOutputStream.LONGFILE_GNU;
        tarOutputStream.setLongFileMode(longFileMode);
        // Assert
        assertEquals(longFileMode, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertEquals(2 * TarBuffer.DEFAULT_RCDSIZE, outputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());
    }

    @Test
    public void testPutArchiveEntry_DirectoryEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "directory/");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        // Assert
        assertEquals(archiveEntry.getName(), tarOutputStream.getCurrName());
        assertEquals(archiveEntry.getSize(), tarOutputStream.getCurrSize());
    }

    @Test
    public void testPutArchiveEntry_FileEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "file.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        // Assert
        assertEquals(archiveEntry.getName(), tarOutputStream.getCurrName());
        assertEquals(archiveEntry.getSize(), tarOutputStream.getCurrSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_LongFileName_ErrorMode() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "verylongfilename.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testPutArchiveEntry_LongFileName_TruncateMode() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "verylongfilename.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        // Assert
        assertEquals(archiveEntry.getName(), tarOutputStream.getCurrName());
        assertEquals(archiveEntry.getSize(), tarOutputStream.getCurrSize());
    }

    @Test
    public void testPutArchiveEntry_LongFileName_GnuMode() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "verylongfilename.txt");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        // Assert
        assertEquals(archiveEntry.getName(), tarOutputStream.getCurrName());
        assertEquals(archiveEntry.getSize(), tarOutputStream.getCurrSize());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_IncompleteData() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry(testFile, "file.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[100]); // Not writing all bytes
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry(testFile, "file.txt"));
        tarOutputStream.write("Hello World".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        // Assert
        assertEquals("Hello World", outputStream.toString());
    }

    @Test
    public void testWrite_EOFRecord() throws IOException {
        tarOutputStream.write(new byte[512]);
        tarOutputStream.finish();
        // Assert
        assertEquals(2 * 512, outputStream.size());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, "file.txt");
        // Assert
        assertEquals(testFile.getName(), archiveEntry.getName());
        assertEquals(testFile.length(), archiveEntry.getSize());
    }

}