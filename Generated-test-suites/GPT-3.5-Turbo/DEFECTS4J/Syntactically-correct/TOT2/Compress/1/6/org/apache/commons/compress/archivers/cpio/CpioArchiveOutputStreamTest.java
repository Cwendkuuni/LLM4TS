import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
        cpioOut.closeArchiveEntry();
        cpioOut.close();
        // Check if entry was successfully added
        assertEquals(110, out.size()); // 110 is the expected size for the entry with specified details
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
        byte[] data = "12345".getBytes();
        cpioOut.write(data);
        cpioOut.closeArchiveEntry();
        cpioOut.close();
        // Check if data was correctly written
        assertEquals("12345", out.toString());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        cpioOut.finish();
        cpioOut.close();
        // Check if a trailer entry was added at the end
        assertEquals(110, out.size()); // 110 is the expected size for the trailer entry
    }

    @Test(expected = IOException.class)
    public void testInvalidEntrySize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
        byte[] data = "123456".getBytes(); // 6 bytes instead of specified 5
        cpioOut.write(data);
        cpioOut.closeArchiveEntry();
        cpioOut.close();
    }

}