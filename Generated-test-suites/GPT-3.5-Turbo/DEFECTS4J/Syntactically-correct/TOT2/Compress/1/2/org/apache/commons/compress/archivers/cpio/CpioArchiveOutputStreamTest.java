import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outStream;

    @Before
    public void setUp() {
        outStream = new ByteArrayOutputStream();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);

        cpioOut.closeArchiveEntry();
        cpioOut.finish();

        // Check if the entry is written to the stream correctly
        assertEquals(MAGIC_NEW, outStream.toString().substring(0, 6));
        assertEquals(16, outStream.size()); // Verify the header size
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());

        cpioOut.closeArchiveEntry();
        cpioOut.finish();

        // Check if the content is written to the stream correctly
        assertEquals("12345", outStream.toString().substring(18));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());

        // Close the current entry
        cpioOut.closeArchiveEntry();

        // Check if closing the entry sets the correct size 
        assertEquals(entry.getSize(), 5);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(outStream);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write("12345".getBytes());

        // Finish the CPIO output stream
        cpioOut.finish();

        // Check if the trailer entry is written correctly
        assertEquals("TRAILER!!!", outStream.toString().substring(outStream.size() - 10));
    }
}