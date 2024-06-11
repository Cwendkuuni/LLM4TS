import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("This is a test file.".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        assertTrue(outputStream.toByteArray().length > 0);
    }

    @Test
    public void testCloseArchiveEntryWhenNoCurrentEntry() {
        Exception exception = assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.closeArchiveEntry();
        });
        assertEquals("No current entry to close", exception.getMessage());
    }

    @Test
    public void testWriteExceedingEntrySize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("exceeding-size-file.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        Exception exception = assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.write(new byte[2048]);
        });
        assertEquals("request to write '2048' bytes exceeds size in header of '0' bytes for entry 'exceeding-size-file.txt'", exception.getMessage());
    }

    @Test
    public void testFinishTwice() throws IOException {
        tarArchiveOutputStream.finish();
        Exception exception = assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.finish();
        });
        assertEquals("This archive has already been finished", exception.getMessage());
    }

    @Test
    public void testCloseTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        Exception exception = assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.close();
        });
        assertEquals("Stream has already been finished", exception.getMessage());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test-file.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, file.getName());
        assertTrue(archiveEntry instanceof TarArchiveEntry);
    }

}