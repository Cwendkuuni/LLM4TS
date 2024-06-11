import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream output;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(output);
    }

    @After
    public void tearDown() {
        try {
            cpioOutputStream.close();
        } catch (IOException e) {
            // ignore
        }
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);

        String testContents = "12345";
        cpioOutputStream.write(testContents.getBytes());
        cpioOutputStream.closeArchiveEntry();

        assertEquals(output.size(), entry.getSize() + entry.getHeaderSize());
    }

    @Test
    public void testCloseArchiveEntryWithInvalidSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        cpioOutputStream.putNextEntry(entry);

        try {
            cpioOutputStream.closeArchiveEntry();
            fail("Should throw IOException for invalid entry size");
        } catch (IOException e) {
            // success
        }
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        assertEquals("TRAILER!!!", cpioOutputStream.names.get("TRAILER!!!").getName());
    }
}