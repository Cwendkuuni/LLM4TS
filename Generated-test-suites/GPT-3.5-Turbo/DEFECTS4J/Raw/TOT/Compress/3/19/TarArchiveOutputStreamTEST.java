import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        tarArchiveOutputStream = new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode_WithValidMode_Success() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileMode_WithInvalidMode_ThrowsIllegalArgumentException() {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testPutArchiveEntry_WithRegularFileEntry_Success() {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        try {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_WithLongFileNameAndModeError_ThrowsRuntimeException() {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("thisFileNameIsTooLongForTarArchive.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry_WithIncompleteBytes_ThrowsIOException() {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        try {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            tarArchiveOutputStream.write(new byte[10], 0, 15);
            tarArchiveOutputStream.closeArchiveEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("entry 'testFile.txt' closed at '15' before the '0' bytes specified in the header were written", e.getMessage());
        }
    }

    @Test
    public void testWriteByteArray() {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        try {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
            tarArchiveOutputStream.write("This is a test file.".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        try {
            tarArchiveOutputStream.finish();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            tarArchiveOutputStream.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() {
        try {
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
            assertEquals("testFile.txt", archiveEntry.getName());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}