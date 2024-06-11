import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOut;
    private ByteArrayOutputStream byteOut;

    @Before
    public void setUp() {
        byteOut = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(byteOut);
    }

    @After
    public void tearDown() throws IOException {
        tarOut.close();
        byteOut.close();
    }

    @Test
    public void testPutArchiveEntry_WithValidTarEntry_EnsureEntryWritten() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("/testFile.txt");
        entry.setSize(100);
        tarOut.putArchiveEntry(entry);

        // Act
        tarOut.closeArchiveEntry();

        // Assert
        assertEquals(100, tarOut.getCurrBytes());
        assertEquals("/testFile.txt", tarOut.getCurrName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_WithInvalidTarEntry_ThrowIOException() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("/invalidName.txt12345678"); // Long file name
        tarOut.putArchiveEntry(entry);

        // Act
        tarOut.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_WithWrongTarEntryType_ThrowClassCastException() throws IOException {
        // Arrange
        ArchiveEntry entry = new ArchiveEntry(); // ArchiveEntry instead of TarArchiveEntry
        tarOut.putArchiveEntry(entry);
    }

    @Test
    public void testFinish_EnsureEndOfArchiveWritten() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("/testFile.txt");
        tarOut.putArchiveEntry(entry);

        // Act
        tarOut.finish();

        // Assert
        assertEquals(1024, byteOut.toByteArray().length);
    }

    @Test
    public void testClose_EnsureOutputStreamClosed() throws IOException {
        // Act
        tarOut.close();

        // Assert
        assertTrue(tarOut.isClosed());
        assertTrue(byteOut.isClosed());
    }

    @Test
    public void testWrite_WithDataToWrite_EnsureDataWritten() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("/testFile.txt");
        tarOut.putArchiveEntry(entry);
        byte[] data = "This is a test file.".getBytes();

        // Act
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();

        // Assert
        assertEquals(data.length, tarOut.getCurrBytes());
    }

    @Test(expected = IOException.class)
    public void testWrite_WithDataSizeExceedsEntrySize_ThrowIOException() throws IOException {
        // Arrange
        ArchiveEntry entry = new TarArchiveEntry("/testFile.txt");
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        byte[] data = "This is a test file.".getBytes();

        // Act
        tarOut.write(data, 0, data.length);
        tarOut.closeArchiveEntry();
    }

    // ... additional test cases for other methods

}