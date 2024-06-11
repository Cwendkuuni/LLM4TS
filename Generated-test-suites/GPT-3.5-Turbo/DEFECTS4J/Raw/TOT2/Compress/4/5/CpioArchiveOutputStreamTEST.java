import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructorWithFormat() {
        // Testing constructor with specified format
        // Edge case: Valid format
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW);
        assertNotNull(cpioOut);
    }

    @Test
    public void testConstructorWithoutFormat() {
        // Testing constructor without specified format
        // Edge case: Default format
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(cpioOut);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidFormat() {
        // Testing constructor with invalid format
        // Edge case: Invalid format
        new CpioArchiveOutputStream(new ByteArrayOutputStream(), 5);
    }

    @Test
    public void testPutArchiveEntry() {
        // Testing putArchiveEntry method
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        CpioArchiveEntry cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setSize(5);
        
        // Typical case
        try {
            cpioOut.putArchiveEntry(cpioEntry);
            assertEquals(cpioEntry, cpioOut.getCurrentEntry());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        // Testing write method
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());
        byte[] data = {1, 2, 3, 4, 5};

        // Typical case
        try {
            cpioOut.putArchiveEntry(new CpioArchiveEntry());
            cpioOut.write(data, 0, data.length);
            cpioOut.closeArchiveEntry();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        // Testing finish method
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());

        // Typical case
        try {
            cpioOut.finish();
            assertTrue(cpioOut.isFinished());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        // Testing close method
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(new ByteArrayOutputStream());

        // Typical case
        try {
            cpioOut.close();
            assertTrue(cpioOut.isClosed());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}