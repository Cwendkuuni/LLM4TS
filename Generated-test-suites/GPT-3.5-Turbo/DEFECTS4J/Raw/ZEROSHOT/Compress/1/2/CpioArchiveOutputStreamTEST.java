import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);
        assertNotNull(archiveOutputStream);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write(contents.getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        assertNotNull(outputBytes);
        assertTrue(outputBytes.length > 0);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write(contents.getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(contents.length(), outputBytes.length);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write(contents.getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        assertNotNull(outputBytes);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);
        archiveOutputStream.close();
        assertTrue(archiveOutputStream.isClosed());
    }

}