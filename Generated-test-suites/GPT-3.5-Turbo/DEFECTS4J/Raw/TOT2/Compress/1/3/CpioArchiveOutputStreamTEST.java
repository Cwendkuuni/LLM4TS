import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest{

    @Test
    public void testPutNextEntry() {
        // Test case for putting next entry
        // Arrange
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();

        // Act
        try {
            cpioOut.putNextEntry(entry);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertNotNull(cpioOut);
        assertNotNull(cpioOut.cpioEntry);
        assertEquals(entry, cpioOut.cpioEntry);
    }

    @Test
    public void testWrite() {
        // Test case for writing data to the stream
        // Arrange
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = { 1, 2, 3, 4, 5 };

        // Act
        try {
            cpioOut.putNextEntry(new CpioArchiveEntry());
            cpioOut.write(data, 0, data.length);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertNotNull(cpioOut);
        assertEquals(data.length, cpioOut.written);
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test case for closing the current CPIO entry
        // Arrange
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        try {
            cpioOut.putNextEntry(new CpioArchiveEntry());
            cpioOut.write(new byte[5], 0, 5);
        } catch (Exception e) {
            fail("Exception occurred while setting up test: " + e.getMessage());
        }

        // Act
        try {
            cpioOut.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertNotNull(cpioOut);
        assertNull(cpioOut.cpioEntry);
        assertEquals(0, cpioOut.written);
    }

    // Add more test cases for other methods as needed

}