import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class TarArchiveOutputStreamTest {

    private static final String TEST_FILENAME = "test.txt";
    private static final String TEST_DIRECTORY = "testDirectory";
  
    @Test
    public void testSetLongFileMode_LongFileError() {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_LongFileTruncate() {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_LongFileGNU() {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarStream.getLongFileMode());
     }

    @Test(expected = IOException.class)
    public void testFinish_AlreadyFinished() throws IOException {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.finish();
        tarStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinish_UnclosedEntry() throws IOException {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(new File(TEST_FILENAME));
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
    }

    @Test(expected = IOException.class)
    public void testClose_AlreadyClosed() throws IOException {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarStream.close();
        tarStream.close();
    }

    @Test(expected = IOException.class)
    public void testWrite_BytesExceedSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry(TEST_FILENAME);
        entry.setSize(10);
        tarStream.putArchiveEntry(entry);
        tarStream.write(new byte[20]);
        tarStream.closeArchiveEntry();
        tarStream.close();
    }

    @Test
    public void testWrite_WritingToFile() throws IOException {
        File tarFile = new File("test.tar");
        TarArchiveOutputStream tarStream =
            new TarArchiveOutputStream(new FileOutputStream(tarFile));
        TarArchiveEntry entry = new TarArchiveEntry(TEST_FILENAME);
        tarStream.putArchiveEntry(entry);
        tarStream.write(new byte[10]);
        tarStream.closeArchiveEntry();
        tarStream.close();

        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(new FileInputStream(tarFile));
        TarArchiveEntry readEntry = tarInputStream.getNextTarEntry();
        assertEquals(TEST_FILENAME, readEntry.getName());
        tarInputStream.close();
    }

    @Test
    public void testCreateArchiveEntry_File() throws IOException {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        File inputFile = new File(TEST_FILENAME);
        ArchiveEntry entry = tarStream.createArchiveEntry(inputFile, null);
        assertEquals(TEST_FILENAME, entry.getName());
    }

    @Test
    public void testCreateArchiveEntry_Directory() throws IOException {
        TarArchiveOutputStream tarStream =
                new TarArchiveOutputStream(new ByteArrayOutputStream());
        File inputDirectory = new File(TEST_DIRECTORY);
        ArchiveEntry entry = tarStream.createArchiveEntry(inputDirectory, null);
        assertEquals(TEST_DIRECTORY, entry.getName());
        assertTrue(entry.isDirectory());
    }
}