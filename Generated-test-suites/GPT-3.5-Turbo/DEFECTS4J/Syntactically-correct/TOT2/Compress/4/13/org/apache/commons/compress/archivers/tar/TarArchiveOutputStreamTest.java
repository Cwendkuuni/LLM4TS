import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Test
    public void testPutArchiveEntry() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        assertEquals(512, outputStream.size()); // Check if entry header is 512 bytes
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt", TarArchiveEntry.NORMAL_FILE);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[512]);
        tarArchiveOutputStream.write(new byte[512]);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        assertEquals(1024, outputStream.size()); // Check if entry content is written correctly
    }

    @Test
    public void testSetLongFileMode() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    // Add more test cases for other methods

}