import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.compress.archivers.tar.*;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOut;
    
    @Before
    public void setUp() throws IOException {
        tarOut = new TarArchiveOutputStream(new FileOutputStream("test.tar"));
    }

    @Test
    public void testSetLongFileMode() {
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOut.getLongFileMode());

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.getLongFileMode());

        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");

        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test file.".getBytes());
        tarOut.closeArchiveEntry();

        File file = new File("test.tar");
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");

        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test file.".getBytes());
        tarOut.closeArchiveEntry();

        tarOut.finish();
        tarOut.close();

        byte[] fileContent = Files.readAllBytes(Paths.get("test.tar"));
        String content = new String(fileContent);
        assertTrue(content.contains("testFile.txt"));
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        tarOut.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");

        tarOut.putArchiveEntry(entry);
        byte[] data = "This is a test file.".getBytes();
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        tarOut.finish();
        tarOut.close();

        byte[] fileContent = Files.readAllBytes(Paths.get("test.tar"));
        assertArrayEquals(data, fileContent);
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt", 10);

        tarOut.putArchiveEntry(entry);
        byte[] data = "This is a test file.".getBytes();
        tarOut.write(data);
        tarOut.closeArchiveEntry(); 
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry1 = new TarArchiveEntry("file1.txt");
        TarArchiveEntry entry2 = new TarArchiveEntry("file2.txt");

        tarOut.putArchiveEntry(entry1);
        tarOut.write("This is file 1.".getBytes());
        tarOut.closeArchiveEntry();

        tarOut.putArchiveEntry(entry2);
        tarOut.write("This is file 2.".getBytes());
        tarOut.closeArchiveEntry();

        tarOut.finish();
        tarOut.close();

        byte[] fileContent = Files.readAllBytes(Paths.get("test.tar"));
        String content = new String(fileContent);
        assertTrue(content.contains("file1.txt"));
        assertTrue(content.contains("file2.txt"));
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");

        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test file.".getBytes());
        tarOut.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");

        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test file.".getBytes());
        tarOut.closeArchiveEntry();

        tarOut.close();

        File file = new File("test.tar");
        assertFalse(file.exists());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        tarOut.close();
        tarOut.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        ArchiveEntry entry = tarOut.createArchiveEntry(inputFile, "testFile.txt");

        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals("testFile.txt", entry.getName());
        assertEquals(inputFile.length(), entry.getSize());
    }
}