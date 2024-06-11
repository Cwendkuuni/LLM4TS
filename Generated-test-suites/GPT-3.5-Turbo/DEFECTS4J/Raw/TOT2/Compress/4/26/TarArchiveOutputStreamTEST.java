import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        tarOutputStream.write(expectedHeader);

        assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() {
        try {
            tarOutputStream.finish();
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testClose() {
        try {
            tarOutputStream.close();
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedOutput = outputStream.toByteArray();
        byte[] actualOutput = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        String entryName = "testFile.txt";
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, entryName);

        assertNotNull(archiveEntry);
        assertTrue(archiveEntry instanceof TarArchiveEntry);
    }

}