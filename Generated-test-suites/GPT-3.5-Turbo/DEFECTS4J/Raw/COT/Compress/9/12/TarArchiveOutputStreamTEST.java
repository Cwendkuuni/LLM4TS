import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    // Method to generate a TarArchiveOutputStream for testing
    private TarArchiveOutputStream createTarArchiveOutputStream() {
        OutputStream out = new ByteArrayOutputStream();
        return new TarArchiveOutputStream(out);
    }

    // Method to generate an ArchiveEntry for testing
    private ArchiveEntry createArchiveEntry(String entryName, long size, boolean isDirectory) {
        TarArchiveEntry entry = new TarArchiveEntry(entryName);
        entry.setSize(size);
        entry.setDirectory(isDirectory);
        return entry;
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tOut = createTarArchiveOutputStream();
        ArchiveEntry entry = createArchiveEntry("file1.txt", 100, false);
        tOut.putArchiveEntry(entry);
        tOut.closeArchiveEntry();
        tOut.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tOut = createTarArchiveOutputStream();
        ArchiveEntry entry = createArchiveEntry("directory1", 0, true);
        tOut.putArchiveEntry(entry);
        tOut.closeArchiveEntry();
        tOut.close();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tOut = createTarArchiveOutputStream();
        ArchiveEntry entry = createArchiveEntry("file1.txt", 100, false);
        tOut.putArchiveEntry(entry);
        byte[] data = new byte[50];
        tOut.write(data, 0, 50);
        tOut.closeArchiveEntry();
        tOut.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithoutClosingEntry() throws IOException {
        TarArchiveOutputStream tOut = createTarArchiveOutputStream();
        ArchiveEntry entry = createArchiveEntry("file1.txt", 100, false);
        tOut.putArchiveEntry(entry);
        tOut.finish();
        tOut.close();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutCurrentEntry() throws IOException {
        TarArchiveOutputStream tOut = createTarArchiveOutputStream();
        tOut.closeArchiveEntry();
        tOut.close();
    }

    @Test(expected = IOException.class)
    public void testWriteExceedsSizeInHeader() throws IOException {
        TarArchiveOutputStream tOut = createTarArchiveOutputStream();
        ArchiveEntry entry = createArchiveEntry("file1.txt", 100, false);
        tOut.putArchiveEntry(entry);
        byte[] data = new byte[150];
        tOut.write(data, 0, 150);
        tOut.closeArchiveEntry();
        tOut.close();
    }

    @Test(expected = IOException.class)
    public void testArchiveAlreadyFinished() throws IOException {
        TarArchiveOutputStream tOut = createTarArchiveOutputStream();
        tOut.finish();
        ArchiveEntry entry = createArchiveEntry("file1.txt", 100, false);
        tOut.putArchiveEntry(entry);
        tOut.close();
    }
}
