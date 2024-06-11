import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        // Create a TarArchiveOutputStream object before each test case
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
    }

    @Test
    public void testPutArchiveEntry_WithValidEntry_Success() {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("file1.txt");
        
        // Act
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testPutArchiveEntry_WithLongEntryName_TruncateMode_Success() {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("VeryLongFileName123456789.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Act
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_WithLongEntryName_ErrorMode_ThrowsRuntimeException() {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("VeryLongFileName123456789.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        // Act
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite_WithValidData_Success() {
        // Arrange
        byte[] data = "Hello, World!".getBytes();

        // Act
        try {
            tarArchiveOutputStream.write(data);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        // Check if the bytes are successfully written
        assertEquals(data.length, tarArchiveOutputStream.getCount());
    }

    @Test
    public void testWrite_WithExceededDataLength_Success() {
        // Arrange
        byte[] data = new byte[512 * 3];

        // Act
        try {
            tarArchiveOutputStream.write(data);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        // Check if the bytes are successfully written
        assertEquals(data.length, tarArchiveOutputStream.getCount());
    }

    @Test(expected = IOException.class)
    public void testWrite_WithDataExceedingEntrySize_ThrowsIOException() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("file.txt", 1024);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = new byte[2048];

        // Act
        tarArchiveOutputStream.write(data);
    }

    @Test
    public void testCloseArchiveEntry_WithValidEntry_Success() {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Act
        try {
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_WithoutPriorPutArchiveEntry_ThrowsIOException() throws IOException {
        // Act
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish_WithNoUnclosedEntries_Success() {
        // Act
        try {
            tarArchiveOutputStream.finish();
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertTrue(tarArchiveOutputStream.finished);
    }

    @Test(expected = IOException.class)
    public void testFinish_WithUnclosedEntry_ThrowsIOException() throws IOException {
        // Arrange
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Act
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose_WithFinishedArchive_Success() {
        // Arrange
        try {
            tarArchiveOutputStream.finish();
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Act
        try {
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testClose_WithUnfinishedArchive_Success() {
        // Act
        try {
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testCreateArchiveEntry_WithFileAndEntryName_Success() {
        // Arrange
        File file = new File("file.txt");
        String entryName = "file.txt";

        // Act
        try {
            TarArchiveEntry entry = (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(file, entryName);
    
            // Assert
            assertEquals(entry.getName(), entryName);
            assertEquals(entry.getSize(), file.length());
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}