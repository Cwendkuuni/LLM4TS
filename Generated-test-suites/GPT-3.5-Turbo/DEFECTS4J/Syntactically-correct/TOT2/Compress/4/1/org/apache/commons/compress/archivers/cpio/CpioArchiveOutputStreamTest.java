import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream_Constructor() {
        // Test constructor with specified format
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchive = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertNotNull(cpioArchive);
    }

    @Test
    public void testPutArchiveEntry() {
        // Test putting a new archive entry
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchive = new CpioArchiveOutputStream(out);
        CpioArchiveEntry newEntry = new CpioArchiveEntry();
        newEntry.setName("testEntry");
        try {
            cpioArchive.putArchiveEntry(newEntry);
            assertNotNull(cpioArchive);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWrite() {
        // Test writing data to the current CPIO entry
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchive = new CpioArchiveOutputStream(out);
        CpioArchiveEntry newEntry = new CpioArchiveEntry();
        newEntry.setName("testEntry");
        try {
            cpioArchive.putArchiveEntry(newEntry);
            byte[] data = "Test Data".getBytes();
            cpioArchive.write(data, 0, data.length);
            cpioArchive.closeArchiveEntry();
            cpioArchive.close();
            assertEquals(data.length, newEntry.getSize());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testFinish() {
        // Test finishing writing the contents of the CPIO output stream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchive = new CpioArchiveOutputStream(out);
        try {
            cpioArchive.finish();
            assertTrue(cpioArchive.isFinished());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testClose() {
        // Test closing the CPIO output stream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchive = new CpioArchiveOutputStream(out);
        try {
            cpioArchive.close();
            assertTrue(cpioArchive.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

}