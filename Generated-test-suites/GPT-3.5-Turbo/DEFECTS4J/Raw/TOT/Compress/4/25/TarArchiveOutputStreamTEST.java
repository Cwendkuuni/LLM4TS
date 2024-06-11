import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test".getBytes());
        tarOut.closeArchiveEntry();
        tarOut.finish();

        // Verify the entry is written correctly
        assertEquals(os.toByteArray()[0], 0);
        assertEquals(os.toByteArray()[1], 0);
        assertEquals(os.toByteArray()[2], 0);
        assertEquals(os.toByteArray()[3], 0);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        String longFileName = generateLongFileName(); // Long file name with length > 100
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        try {
            tarOut.putArchiveEntry(entry);
        } catch (RuntimeException e) {
            assertEquals("file name '" + longFileName + "' is too long ( > 100 bytes)", e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test".getBytes());

        // Verify the entry is not closed yet
        try {
            tarOut.finish();
        } catch (IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }

        tarOut.closeArchiveEntry();
        tarOut.finish();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test".getBytes());
        tarOut.closeArchiveEntry();
        tarOut.finish();

        assertEquals("This is a test", new String(os.toByteArray(), "UTF-8").trim());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);

        // Test finishing an empty archive
        tarOut.finish();
        assertEquals(1024, os.toByteArray().length); // Empty archive should have 2 consecutive EOF records

        // Test finishing archive with entries
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.write("This is a test".getBytes());
        tarOut.closeArchiveEntry();
        tarOut.finish();
        assertEquals("This is a test", new String(os.toByteArray(), "UTF-8").trim());
    }

    private String generateLongFileName() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            longName.append("a");
        }
        return longName.toString();
    }
}