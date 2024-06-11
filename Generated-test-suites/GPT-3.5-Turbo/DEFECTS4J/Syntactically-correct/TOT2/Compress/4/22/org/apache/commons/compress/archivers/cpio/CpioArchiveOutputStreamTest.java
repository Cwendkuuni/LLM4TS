import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setSize(5L);

        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();

        // Ensure the entry was written correctly
        assertEquals(72, outputStream.size());
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG);
        entry.setSize(5L);

        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("12345".getBytes());
        cpioOutputStream.closeArchiveEntry();

        // Ensure the data was written correctly
        assertEquals("12345", outputStream.toString());
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();

        // Ensure finish was called without any exception
        assertTrue(true);
    }

    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();

        // Ensure close was called without any exception
        assertTrue(true);
    }
}