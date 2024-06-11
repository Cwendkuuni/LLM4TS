import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        tarOutputStream.setLongFileMode(1);
        assertEquals(1, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Test data".getBytes());
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(os);
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Test data".getBytes());
        tarOutputStream.closeArchiveEntry();
    }
}