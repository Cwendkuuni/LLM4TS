import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import org.apache.commons.compress.archivers.cpio.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOut;
    private ByteArrayOutputStream byteOut;

    @Before
    public void setUp() {
        byteOut = new ByteArrayOutputStream();
        cpioOut = new CpioArchiveOutputStream(byteOut);
    }

    @Test
    public void testPutNextEntry_WithValidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());

        cpioOut.putNextEntry(entry);
        cpioOut.write(contents.getBytes());
        cpioOut.closeArchiveEntry();

        byte[] output = byteOut.toByteArray();
        // Check if the output contains the expected data
        // ...
    }

    @Test(expected = IOException.class)
    public void testPutNextEntry_WithClosedStream() throws IOException {
        cpioOut.close();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOut.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_WithInvalidEntrySize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);

        cpioOut.putNextEntry(entry);
        cpioOut.write(new byte[10]);
        cpioOut.closeArchiveEntry();
    }

    @Test
    public void testFinish_WithNoEntries() throws IOException {
        cpioOut.finish();

        byte[] output = byteOut.toByteArray();
        // Check if the output contains the expected data
        // ...
    }

    @After
    public void tearDown() throws IOException {
        cpioOut.close();
    }
}