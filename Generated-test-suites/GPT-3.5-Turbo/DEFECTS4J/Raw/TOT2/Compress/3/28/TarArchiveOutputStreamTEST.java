import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutput;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        tarOutput = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.getLongFileMode());
    }

    @Test
    public void testFinish() {
        try {
            tarOutput.finish();
            assertEquals(1024, outputStream.size()); // Two EOF records of 512 bytes each
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testClose() {
        try {
            tarOutput.close();
            assertTrue(tarOutput.isClosed());
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryThrowsException() throws IOException {
        tarOutput.putArchiveEntry(new TarArchiveEntry("/testfile", TarArchiveEntry.NORMAL_FILE));
        tarOutput.write(new byte[1024]);
    }

    @Test
    public void testWrite() {
        try {
            tarOutput.putArchiveEntry(new TarArchiveEntry("/testfile", TarArchiveEntry.NORMAL_FILE));
            tarOutput.write(new byte[512]);
            tarOutput.closeArchiveEntry();
            assertEquals(512, tarOutput.getCurrentSize());
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        try {
            ArchiveEntry entry = tarOutput.createArchiveEntry(new File("/test.txt"), "test.txt");
            assertTrue(entry instanceof TarArchiveEntry);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

}