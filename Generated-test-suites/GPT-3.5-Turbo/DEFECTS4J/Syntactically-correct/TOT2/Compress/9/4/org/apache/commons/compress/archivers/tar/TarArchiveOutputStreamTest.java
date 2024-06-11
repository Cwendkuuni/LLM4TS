import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() {
        // Typical use case
        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception thrown");
        }

        // Edge case: Long file name
        try {
            StringBuilder longName = new StringBuilder();
            for (int i = 0; i < 300; i++) {
                longName.append("a");
            }
            TarArchiveEntry entry = new TarArchiveEntry(longName.toString());
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.close();
            fail("Expected exception for long file name");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        // Typical use case
        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write("Test data".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception thrown");
        }

        // Error scenario: Closing without putting entry
        try {
            tarArchiveOutputStream.closeArchiveEntry();
            fail("Expected exception for closing without putting entry");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testWrite() {
        // Typical use case
        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write("Test data".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();

            assertEquals(outputStream.size(), 1024); // Buffer size
        } catch (IOException e) {
            fail("Exception thrown");
        }

        // Edge case: Writing beyond header specified size
        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write("Test data that exceeds the size specified in the header".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();
            fail("Expected exception for writing beyond size");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testClose() {
        // Typical use case
        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception thrown");
        }

        // Error scenario: Closing twice
        try {
            tarArchiveOutputStream.close();
            fail("Expected exception for closing twice");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testFinish() {
        // Typical use case
        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.finish();
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Exception thrown");
        }

        // Error scenario: Finishing after closing
        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.closeArchiveEntry();
            tarArchiveOutputStream.close();
            tarArchiveOutputStream.finish();
            fail("Expected exception for finishing after closing");
        } catch (IOException e) {
            // Expected exception
        }
    }
}