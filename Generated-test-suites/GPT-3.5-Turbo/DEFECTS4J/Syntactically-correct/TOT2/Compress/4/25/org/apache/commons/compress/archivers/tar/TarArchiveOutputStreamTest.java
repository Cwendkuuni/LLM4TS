import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutput.putArchiveEntry(entry);

        assertEquals("testFile.txt", tarOutput.currName);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();

        assertFalse(tarOutput.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        byte[] data = "Hello World".getBytes();
        tarOutput.write(data, 0, data.length);
        tarOutput.closeArchiveEntry();

        assertEquals(data.length, tarOutput.currBytes);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.finish();

        assertThrows(IOException.class, tarOutput::finish);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);
        tarOutput.close();

        assertTrue(tarOutput.closed);
    }
}