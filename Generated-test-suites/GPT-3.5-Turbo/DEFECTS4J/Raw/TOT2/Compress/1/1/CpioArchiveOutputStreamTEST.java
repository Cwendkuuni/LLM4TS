import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntry() {
        // Typical use case
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(5);
        try {
            cpioOutputStream.putNextEntry(entry);
        } catch (IOException e) {
            fail("Exception thrown");
        }
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testPutNextEntryDuplicate() {
        // Edge case: Putting duplicate entry
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        try {
            cpioOutputStream.putNextEntry(entry);
            cpioOutputStream.putNextEntry(entry);
            fail("Duplicate entry exception not thrown");
        } catch (IOException e) {
            assertEquals("duplicate entry: testfile", e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        // Typical use case
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        byte[] testBytes = "12345".getBytes();
        try {
            cpioOutputStream.write(testBytes, 0, testBytes.length);
        } catch (IOException e) {
            fail("Exception thrown");
        }
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testInvalidEntrySize() {
        // Error scenario: Writing more than the specified entry size
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(5);
        try {
            cpioOutputStream.putNextEntry(entry);
            cpioOutputStream.write("123456".getBytes(), 0, 6);
            fail("Invalid entry size exception not thrown");
        } catch (IOException e) {
            assertEquals("invalid entry size (expected 5 but got 6 bytes)", e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        // Typical use case
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        try {
            cpioOutputStream.finish();
        } catch (IOException e) {
            fail("Exception thrown");
        }
        assertNotNull(cpioOutputStream);
    }

    @Test
    public void testCloseArchiveEntry() {
        // Edge case: Closing archive entry without setting the correct size
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        entry.setSize(5);
        try {
            cpioOutputStream.putNextEntry(entry);
            cpioOutputStream.write("12345".getBytes(), 0, 5);
            cpioOutputStream.closeArchiveEntry();
            fail("Invalid entry size exception not thrown");
        } catch (IOException e) {
            assertEquals("invalid entry size (expected 5 but got 0 bytes)", e.getMessage());
        }
    }
}