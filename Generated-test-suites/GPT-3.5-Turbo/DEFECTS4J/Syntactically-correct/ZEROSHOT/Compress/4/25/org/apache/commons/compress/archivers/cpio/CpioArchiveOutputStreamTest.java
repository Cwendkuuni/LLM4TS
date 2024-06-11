import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.closeArchiveEntry();
        assertEquals(110, byteArrayOutputStream.size());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(new byte[]{1, 2, 3});
        cpioOutputStream.closeArchiveEntry();
        assertEquals(122, byteArrayOutputStream.size());
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(new byte[]{1, 2, 3});
        cpioOutputStream.closeArchiveEntry();
        assertEquals(122, byteArrayOutputStream.size());
    }

    @Test
    public void testFinish() throws IOException {
        cpioOutputStream.finish();
        assertEquals(119, byteArrayOutputStream.size());
    }

    @Test
    public void testClose() throws IOException {
        cpioOutputStream.close();
        assertTrue(byteArrayOutputStream.size() > 0);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = cpioOutputStream.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
    }

}