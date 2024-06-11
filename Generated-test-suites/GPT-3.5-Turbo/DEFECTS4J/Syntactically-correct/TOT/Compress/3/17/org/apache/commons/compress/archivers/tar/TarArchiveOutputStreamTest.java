import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFileConstructor() {
        // Typical use case: Creating a TarArchiveOutputStream with a file
        File file = new File("test.tar");
        try {
            TarArchiveOutputStream tOut = new TarArchiveOutputStream(file);
            assertNotNull(tOut);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyOutputStreamConstructor() {
        // Typical use case: Creating a TarArchiveOutputStream with an empty OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        assertNotNull(tOut);
    }

    @Test
    public void testOutputStreamBlockSizeConstructor() {
        // Typical use case: Creating a TarArchiveOutputStream with an OutputStream and a block size
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream, 512);
        assertNotNull(tOut);
    }

    @Test
    public void testOutputStreamBlockSizeRecordSizeConstructor() {
        // Typical use case: Creating a TarArchiveOutputStream with an OutputStream, a block size, and a record size
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream, 512, 512);
        assertNotNull(tOut);
        assertEquals(tOut.getRecordSize(), 512);
    }

    @Test
    public void testSetLongFileMode() {
        // Typical use case: Setting the long file mode to LONGFILE_TRUNCATE
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(tOut.getLongFileMode(), TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test
    public void testPutArchiveEntry() {
        // Typical use case: Putting an archive entry and closing it
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testfile.txt");
        entry.setSize(10);
        try {
            tOut.putArchiveEntry(entry);
            tOut.write("1234567890".getBytes());
            tOut.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        // Error scenario: Closing an archive entry without putting one first
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        tOut.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingEntrySize() throws IOException {
        // Error scenario: Writing bytes that exceed the size specified for the current entry
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testfile.txt");
        entry.setSize(10);
        tOut.putArchiveEntry(entry);
        tOut.write("12345678901".getBytes());
    }

    @Test
    public void testFinish() {
        // Typical use case: Finishing the tar archive
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        try {
            tOut.finish();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        // Typical use case: Closing the tar archive
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        try {
            tOut.close();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        // Typical use case: Flushing the tar archive
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(outputStream);
        try {
            tOut.flush();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        // Typical use case: Creating a TarArchiveEntry from a file
        File file = new File("testfile.txt");
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        try {
            ArchiveEntry entry = tOut.createArchiveEntry(file, "testfile.txt");
            assertNotNull(entry);
            assertEquals(entry.getName(), "testfile.txt");
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}