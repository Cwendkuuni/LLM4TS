import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithClosedTarArchiveOutputStream() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new ByteArrayOutputStream());
        t.close();
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        t.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        // Create a directory and put a file inside it
        File directory = new File("testDir");
        directory.mkdir();
        File file = new File(directory, "testFile");
        file.createNewFile();

        // Create a TarArchiveOutputStream and put the directory as archive entry
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry(directory, "testDir");
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();
        tarStream.finish();

        // Verify that the archive entry is correct
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        TarArchiveEntry extractedEntry = tarInputStream.getNextTarEntry();
        assertEquals(entry.getName(), extractedEntry.getName());
        assertTrue(extractedEntry.isDirectory());

        // Clean up
        tarInputStream.close();
        inputStream.close();
        tarStream.close();
        outputStream.close();
        file.delete();
        directory.delete();
    }

    @Test
    public void testCloseArchiveEntryBeforeAllBytesAreWritten() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarStream.putArchiveEntry(entry);
        tarStream.write("this is a test".getBytes());
        tarStream.closeArchiveEntry();
        tarStream.finish();

        // Verify that the entry is written correctly and is complete
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        TarArchiveEntry extractedEntry = tarInputStream.getNextTarEntry();
        assertEquals(entry.getName(), extractedEntry.getName());
        int size = (int) extractedEntry.getSize();
        byte[] actualBytes = new byte[size];
        tarInputStream.read(actualBytes);
        String actualString = new String(actualBytes);
        assertEquals("this is a test", actualString);

        // Clean up
        tarInputStream.close();
        inputStream.close();
        tarStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithMissingBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile", "this is a test".getBytes().length);
        tarStream.putArchiveEntry(entry);
        tarStream.write("this is a test".getBytes());
        tarStream.closeArchiveEntry();
        tarStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile", "this is a test".getBytes().length);
        tarStream.putArchiveEntry(entry);
        tarStream.write("this is a test".getBytes());
        tarStream.finish();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("thisIsAVeryLongFileNameThisIsAVeryLongFileNameThisIsAVeryLongFileNameThisIsAVeryLongFileName");
        
        // Test LONGFILE_ERROR 
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        try {
            tarStream.putArchiveEntry(entry);
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("file name '" + entry.getName() + "' is too long ( > " + TarConstants.NAMELEN + " bytes)", e.getMessage());
        }

        // Test LONGFILE_TRUNCATE
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();
        tarStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(inputStream);
        TarArchiveEntry extractedEntry = tarInputStream.getNextTarEntry();
        assertEquals(entry.getName().substring(0, TarConstants.NAMELEN), extractedEntry.getName());
        tarInputStream.close();
        inputStream.close();

        // Test LONGFILE_GNU
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();
        tarStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        tarInputStream = new TarArchiveInputStream(inputStream);
        extractedEntry = tarInputStream.getNextTarEntry();

        // Verify that a LONGNAME archive entry is created
        assertEquals(TarConstants.GNU_LONGLINK, extractedEntry.getName());
        assertEquals(TarConstants.LF_GNUTYPE_LONGNAME, extractedEntry.getEntryType());

        // Verify that the name of the extracted entry is the original long name
        int size = (int) extractedEntry.getSize();
        byte[] nameBytes = new byte[size - 1]; // Exclude the NUL terminator
        tarInputStream.read(nameBytes);
        String actualName = new String(nameBytes);
        assertEquals(entry.getName(), actualName);

        tarInputStream.close();
        inputStream.close();

        // Clean up
        tarStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveEntryWithFile() throws IOException {
        File testFile = new File("testFile");
        testFile.createNewFile();

        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = tarStream.createArchiveEntry(testFile, "testFileEntry");
        assertEquals("testFileEntry", entry.getName());

        testFile.delete();
        tarStream.close();
    }
}