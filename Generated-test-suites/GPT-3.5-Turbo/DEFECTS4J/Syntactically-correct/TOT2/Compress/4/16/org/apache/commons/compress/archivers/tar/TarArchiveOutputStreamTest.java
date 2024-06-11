import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        // Create TarArchiveOutputStream instance
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);

        // Create TarArchiveEntry instance to put into archive
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");

        try {
            tarOutput.putArchiveEntry(entry);

            // Check if the entry name matches
            assertEquals("testEntry.txt", tarOutput.currName);
            tarOutput.closeArchiveEntry(); // Close the entry
            tarOutput.close();
        } catch (IOException e) {
            fail("Exception thrown while testing putArchiveEntry: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        // Create TarArchiveOutputStream instance
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(outputStream);

        // Create an entry and put it in the archive
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");

        try {
            tarOutput.putArchiveEntry(entry);
            tarOutput.write("Test data".getBytes());
            tarOutput.closeArchiveEntry(); // Close the entry
            tarOutput.close();

            // Check if the data was written correctly
            String result = new String(outputStream.toByteArray());
            assertEquals("Test data", result.trim());
        } catch (IOException e) {
            fail("Exception thrown while testing write: " + e.getMessage());
        }
    }

    // Add more test cases for other methods in TarArchiveOutputStream

}