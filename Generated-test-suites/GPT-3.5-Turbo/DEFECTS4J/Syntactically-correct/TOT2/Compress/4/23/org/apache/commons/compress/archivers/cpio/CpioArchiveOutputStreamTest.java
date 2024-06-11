import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setup() {
        cpioOutputStream = new CpioArchiveOutputStreamMock();
    }

    @Test
    public void testPutArchiveEntry() {
        // Test basic functionality of putArchiveEntry
        // Create a CpioArchiveEntry and put it into the stream
        // Verify that the entry is successfully added
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setMode(CpioConstants.C_ISREG); // regular file
        try {
            cpioOutputStream.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertNotNull(cpioOutputStream.getCurrentEntry());
    }

    @Test
    public void testWrite() {
        // Test writing data to the CPIO stream
        // Write data to the stream and check if the data is correctly written
        byte[] testData = "12345".getBytes();
        try {
            cpioOutputStream.write(testData, 0, testData.length);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals(5, cpioOutputStream.getWritten());
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test closing the current CPIO entry
        // Close the current entry and verify that it is closed
        try {
            cpioOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertNull(cpioOutputStream.getCurrentEntry());
    }

    // Add more test cases for other methods and edge cases

}