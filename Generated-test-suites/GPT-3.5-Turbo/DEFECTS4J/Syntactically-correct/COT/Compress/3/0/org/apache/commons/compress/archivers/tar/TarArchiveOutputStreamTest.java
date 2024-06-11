
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(testFile, testFile.getName());
        tarStream.putArchiveEntry(entry);

        assertEquals(testFile.getName(), tarStream.currName);
        assertEquals(testFile.length(), tarStream.currSize);

        tarStream.closeArchiveEntry();
        tarStream.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(testFile, testFile.getName());
        tarStream.putArchiveEntry(entry);

        tarStream.closeArchiveEntry();

        assertEquals(testFile.length(), tarStream.currBytes);

        tarStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteWrite() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(testFile, testFile.getName());
        tarStream.putArchiveEntry(entry);

        // write less than the size of the entry
        tarStream.write(new byte[(int) (testFile.length() - 1)], 0, (int) (testFile.length() - 1));

        tarStream.closeArchiveEntry();
        tarStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(testFile, testFile.getName());
        tarStream.putArchiveEntry(entry);

        // write contents of test file to tar stream
        tarStream.write(new byte[(int) testFile.length()], 0, (int) testFile.length());

        assertEquals(testFile.length(), tarStream.currBytes);

        tarStream.closeArchiveEntry();
        tarStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingEntrySize() throws IOException {
        File testFile = new File("test.txt");
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry entry = new TarArchiveEntry(testFile, testFile.getName());
        tarStream.putArchiveEntry(entry);

        // write more than the size of the entry
        tarStream.write(new byte[(int) (testFile.length() + 1)], 0, (int) (testFile.length() + 1));

        tarStream.closeArchiveEntry();
        tarStream.close();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        tarStream.finish();
        // check that two EOF records are written
        assertEquals(2 * tarStream.getRecordSize(), tarStream.currBytes);

        tarStream.close();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarStream.getRecordSize());

        tarStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        int expectedMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;
        tarStream.setLongFileMode(expectedMode);
        assertEquals(expectedMode, tarStream.longFileMode);

        tarStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        File inputFile = new File("input.txt");
        String entryName = "entry.txt";

        ArchiveEntry archiveEntry = tarStream.createArchiveEntry(inputFile, entryName);

        assertEquals(inputFile.getAbsolutePath(), ((TarArchiveEntry) archiveEntry).getFile().getAbsolutePath());
        assertEquals(entryName, ((TarArchiveEntry) archiveEntry).getName());

        tarStream.close();
    }
}
