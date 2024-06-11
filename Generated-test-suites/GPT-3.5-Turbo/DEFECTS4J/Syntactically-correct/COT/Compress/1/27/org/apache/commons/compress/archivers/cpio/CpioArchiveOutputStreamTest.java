
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutput;
    private ByteArrayOutputStream byteArrayOutput;

    @Before
    public void setUp() throws Exception {
        byteArrayOutput = new ByteArrayOutputStream();
        cpioOutput = new CpioArchiveOutputStream(byteArrayOutput);
    }

    @After
    public void tearDown() throws Exception {
        cpioOutput.close();
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(0777);
        entry.setFileSize(5);

        cpioOutput.putNextEntry(entry);

        assertEquals(0, byteArrayOutput.size());

        cpioOutput.write("12345".getBytes());
        cpioOutput.closeArchiveEntry();

        assertTrue(byteArrayOutput.size() > 0);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(0777);
        entry.setFileSize(5);

        cpioOutput.putNextEntry(entry);
        cpioOutput.write("12345".getBytes());
        cpioOutput.closeArchiveEntry();

        assertEquals(0, cpioOutput.cpioEntry.getSize());
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(0777);
        entry.setFileSize(5);

        cpioOutput.putNextEntry(entry);
        cpioOutput.write("12345".getBytes());
        cpioOutput.closeArchiveEntry();

        long fileSize = byteArrayOutput.size();

        cpioOutput.finish();

        assertTrue(byteArrayOutput.size() > fileSize);
    }

    @Test
    public void testClose() throws IOException {
        cpioOutput.close();
        assertTrue(cpioOutput.closed);
    }
}