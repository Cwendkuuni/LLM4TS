import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;

    @Before
    public void setUp() {
        // Initialize CpioArchiveOutputStream with new format
        outputStream = new CpioArchiveOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing
            }
        }, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() {
        // Close the output stream
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a CpioArchiveEntry to be written
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        // Put the entry into the CPIO stream
        outputStream.putArchiveEntry(entry);

        // Verify that the current entry in the output stream is the same as the one we put
        assertEquals(entry, outputStream.getCurrentEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Create a CpioArchiveEntry to be written
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        // Put the entry into the CPIO stream
        outputStream.putArchiveEntry(entry);

        // Close the current entry in the CPIO stream
        outputStream.closeArchiveEntry();

        // Verify that the current entry in the output stream is null
        assertNull(outputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryInvalidSize() throws IOException {
        // Create a CpioArchiveEntry to be written
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        // Put the entry into the CPIO stream
        outputStream.putArchiveEntry(entry);

        // Write less bytes than the entry size
        outputStream.write(new byte[3]);

        // Close the current entry in the CPIO stream
        outputStream.closeArchiveEntry(); // This should throw an IOException
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryDuplicateName() throws IOException {
        // Create a CpioArchiveEntry to be written
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        // Put the entry into the CPIO stream
        outputStream.putArchiveEntry(entry);

        // Put another entry with the same name into the CPIO stream
        outputStream.putArchiveEntry(entry); // This should throw an IOException
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        // Create a CpioArchiveEntry to be written
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        // Put the entry into the CPIO stream
        outputStream.putArchiveEntry(entry);

        // Close the current entry in the CPIO stream
        outputStream.closeArchiveEntry();

        // Try to write to the output stream after closing the entry
        outputStream.write(new byte[5]); // This should throw an IOException
    }

    @Test
    public void testFinish() throws IOException {
        // Finish the CPIO output stream
        outputStream.finish();

        // Verify that the finish method has been called by checking the finished flag
        assertTrue(outputStream.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        // Close the CPIO output stream
        outputStream.close();

        // Verify that the closed flag is set to true
        assertTrue(outputStream.isClosed());
    }
}